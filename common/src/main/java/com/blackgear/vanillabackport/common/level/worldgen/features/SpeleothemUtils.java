package com.blackgear.vanillabackport.common.level.worldgen.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderSet;
import net.minecraft.util.Mth;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class SpeleothemUtils {
    protected static double getSpeleothemHeight(double xzDistanceFromCenter, double speleothemRadius, double scale, double bluntness) {
        if (xzDistanceFromCenter < bluntness) {
            xzDistanceFromCenter = bluntness;
        }
        
        double r = xzDistanceFromCenter / speleothemRadius * 0.384;
        double part1 = 0.75 * Math.pow(r, 1.3333333333333333);
        double part2 = Math.pow(r, 0.6666666666666666);
        double part3 = 0.3333333333333333 * Math.log(r);
        double heightRelativeToMaxRadius = scale * (part1 - part2 - part3);
        heightRelativeToMaxRadius = Math.max(heightRelativeToMaxRadius, 0.0);
        return heightRelativeToMaxRadius / 0.384 * speleothemRadius;
    }
    
    protected static boolean isCircleMostlyEmbeddedInStone(WorldGenLevel level, BlockPos center, int xzRadius) {
        if (isEmptyOrWaterOrLava(level, center)) {
            return false;
        } else {
            float angleIncrement = 6.0F / xzRadius;
            
            for (float angle = 0.0F; angle < Mth.TWO_PI; angle += angleIncrement) {
                int dx = (int)(Mth.cos(angle) * xzRadius);
                int dz = (int)(Mth.sin(angle) * xzRadius);
                if (isEmptyOrWaterOrLava(level, center.offset(dx, 0, dz))) {
                    return false;
                }
            }
            
            return true;
        }
    }
    
    protected static boolean isEmptyOrWater(LevelAccessor level, BlockPos pos) {
        return level.isStateAtPosition(pos, SpeleothemUtils::isEmptyOrWater);
    }
    
    protected static boolean isEmptyOrWaterOrLava(LevelAccessor level, BlockPos pos) {
        return level.isStateAtPosition(pos, SpeleothemUtils::isEmptyOrWaterOrLava);
    }

    protected static void buildBaseToTipColumn(
        Direction direction,
        int height,
        boolean mergeTip,
        Consumer<BlockState> blockSetter,
        Block pointedBlock
    ) {
        if (height >= 3) {
            blockSetter.accept(createPointedBlock(direction, DripstoneThickness.BASE, pointedBlock));

            for(int i = 0; i < height - 3; ++i) {
                blockSetter.accept(createPointedBlock(direction, DripstoneThickness.MIDDLE, pointedBlock));
            }
        }

        if (height >= 2) {
            blockSetter.accept(createPointedBlock(direction, DripstoneThickness.FRUSTUM, pointedBlock));
        }

        if (height >= 1) {
            blockSetter.accept(createPointedBlock(direction, mergeTip ? DripstoneThickness.TIP_MERGE : DripstoneThickness.TIP, pointedBlock));
        }
    }
    
    protected static void buildBaseToTipColumn(
        Direction direction,
        int totalLength,
        boolean mergedTip,
        Consumer<BlockState> consumer,
        Block pointedBlock,
        BaseBlockTransformer baseBlockTransformer
    ) {
        for (int remainingLength = totalLength; remainingLength > 0; remainingLength--) {
            boolean isColumnBase = remainingLength == totalLength;
            DripstoneThickness thickness;
            if (remainingLength == 2) {
                thickness = DripstoneThickness.FRUSTUM;
            } else if (remainingLength == 1) {
                thickness = mergedTip ? DripstoneThickness.TIP_MERGE : DripstoneThickness.TIP;
            } else if (isColumnBase) {
                thickness = DripstoneThickness.BASE;
            } else {
                thickness = DripstoneThickness.MIDDLE;
            }
            
            BlockState state = createPointedBlock(direction, thickness, pointedBlock);
            consumer.accept(isColumnBase ? baseBlockTransformer.transform(state) : state);
        }
    }
    
    protected static void growSpeleothem(
        LevelAccessor level,
        BlockPos startPos,
        Direction direction,
        int height,
        boolean mergeTip,
        Block baseBlock,
        Block pointedBlock,
        HolderSet<Block> replaceableBlocks
    ) {
        growSpeleothem(level, startPos, direction, height, mergeTip, baseBlock, pointedBlock, replaceableBlocks, BaseBlockTransformer.NONE);
    }
    
    protected static void growSpeleothem(
        LevelAccessor level,
        BlockPos startPos,
        Direction direction,
        int height,
        boolean mergeTip,
        Block baseBlock,
        Block pointedBlock,
        HolderSet<Block> replaceableBlocks,
        BaseBlockTransformer baseBlockTransformer
    ) {
        if (isBase(level.getBlockState(startPos.relative(direction.getOpposite())), baseBlock, replaceableBlocks)) {
            BlockPos.MutableBlockPos pos = startPos.mutable();
            buildBaseToTipColumn(direction, height, mergeTip, state -> {
                if (state.is(pointedBlock)) {
                    state = state.setValue(PointedDripstoneBlock.WATERLOGGED, level.isWaterAt(pos));
                }

                level.setBlock(pos, state, 2);
                pos.move(direction);
            }, pointedBlock, baseBlockTransformer);
        }
    }

    protected static boolean placeBaseBlockIfPossible(
        LevelAccessor level,
        BlockPos pos,
        Block baseBlock,
        HolderSet<Block> replaceableBlocks
    ) {
        BlockState state = level.getBlockState(pos);
        if (state.is(replaceableBlocks)) {
            level.setBlock(pos, baseBlock.defaultBlockState(), 2);
            return true;
        } else {
            return false;
        }
    }

    private static BlockState createPointedBlock(Direction direction, DripstoneThickness thickness, Block pointedBlock) {
        return pointedBlock.defaultBlockState()
            .setValue(PointedDripstoneBlock.TIP_DIRECTION, direction)
            .setValue(PointedDripstoneBlock.THICKNESS, thickness);
    }
    
    public static boolean isBaseOrLava(BlockState state, Block baseBlock, HolderSet<Block> replaceableBlocks) {
        return isBase(state, baseBlock, replaceableBlocks) || state.is(Blocks.LAVA);
    }
    
    public static boolean isBase(BlockState state, Block baseBlock, HolderSet<Block> replaceableBlocks) {
        return state.is(baseBlock) || state.is(replaceableBlocks);
    }

    public static boolean isEmptyOrWater(BlockState state) {
        return state.isAir() || state.is(Blocks.WATER);
    }

    public static boolean isNeitherEmptyNorWater(BlockState state) {
        return !state.isAir() && !state.is(Blocks.WATER);
    }
    
    public static boolean isEmptyOrWaterOrLava(BlockState state) {
        return state.isAir() || state.is(Blocks.WATER) || state.is(Blocks.LAVA);
    }
    
    public enum BaseBlockTransformer implements StringRepresentable {
        NONE("none", UnaryOperator.identity()),
        SET_ATTACHED("set_attached", state -> state.trySetValue(BlockStateProperties.ATTACHED, true));
        
        public static final Codec<BaseBlockTransformer> CODEC = StringRepresentable.fromEnum(BaseBlockTransformer::values);
        private final String name;
        private final UnaryOperator<BlockState> transformer;
        
        BaseBlockTransformer(String name, UnaryOperator<BlockState> transformer) {
            this.name = name;
            this.transformer = transformer;
        }
        
        public BlockState transform(BlockState state) {
            return this.transformer.apply(state);
        }
        
        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}

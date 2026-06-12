package com.blackgear.vanillabackport.common.level.worldgen.features;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.levelgen.feature.DripstoneUtils;

import java.util.function.Consumer;

public class SpeleothemUtils {
    protected static boolean isEmptyOrWater(LevelAccessor level, BlockPos pos) {
        return level.isStateAtPosition(pos, DripstoneUtils::isEmptyOrWater);
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

    protected static void growSpeleothem(
        LevelAccessor level,
        BlockPos startPos,
        Direction direction,
        int height,
        boolean mergeTip,
        Block baseBlock,
        Block pointedBlock,
        TagKey<Block> replaceableBlocks
    ) {
        if (isBase(level.getBlockState(startPos.relative(direction.getOpposite())), baseBlock, replaceableBlocks)) {
            BlockPos.MutableBlockPos pos = startPos.mutable();
            buildBaseToTipColumn(direction, height, mergeTip, state -> {
                if (state.is(pointedBlock)) {
                    state = state.setValue(PointedDripstoneBlock.WATERLOGGED, level.isWaterAt(pos));
                }

                level.setBlock(pos, state, 2);
                pos.move(direction);
            }, pointedBlock);
        }
    }

    protected static boolean placeBaseBlockIfPossible(
        LevelAccessor level,
        BlockPos pos,
        Block baseBlock,
        TagKey<Block> replaceableBlocks
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

    public static boolean isBase(BlockState state, Block baseBlock, TagKey<Block> replaceableBlocks) {
        return state.is(baseBlock) || state.is(replaceableBlocks);
    }

    public static boolean isEmptyOrWater(BlockState state) {
        return state.isAir() || state.is(Blocks.WATER);
    }

    public static boolean isNeitherEmptyNorWater(BlockState state) {
        return !state.isAir() && !state.is(Blocks.WATER);
    }
}

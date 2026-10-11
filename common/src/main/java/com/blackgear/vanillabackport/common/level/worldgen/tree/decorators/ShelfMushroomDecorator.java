package com.blackgear.vanillabackport.common.level.worldgen.tree.decorators;

import com.blackgear.vanillabackport.common.level.blocks.ShelfMushroomBlock;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.common.registries.worldgen.ModTreeDecorators;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecoratorType;

import java.util.List;
import java.util.function.Predicate;

public class ShelfMushroomDecorator extends TreeDecorator {
    public static final Codec<ShelfMushroomDecorator> CODEC = Codec.floatRange(0.0F, 1.0F)
        .fieldOf("probability")
        .xmap(ShelfMushroomDecorator::new, decorator -> decorator.placementProbability).codec();
    private final float placementProbability;
    
    public ShelfMushroomDecorator(float probability) {
        this.placementProbability = probability;
    }
    
    @Override
    protected TreeDecoratorType<?> type() {
        return ModTreeDecorators.SHELF_MUSHROOM.get();
    }
    
    @Override
    public void place(Context context) {
        RandomSource random = context.random();
        if (!(random.nextFloat() >= this.placementProbability)) {
            List<BlockPos> logs = context.logs();
            if (!logs.isEmpty()) {
                if (isFallenLog(logs)) {
                    placeOnFallenLog(context, logs, random);
                } else {
                    placeOnStandingTree(context, logs, random);
                }
            }
        }
    }
    
    private static void placeOnStandingTree(Context context, List<BlockPos> logs, RandomSource random) {
        Direction[] directions = pickTwoPerpendicularDirections(random);
        int treeBaseY = logs.get(0).getY();
        
        for (BlockPos logPos : logs) {
            if (isWithinDecoratableHeight(logPos, treeBaseY)) {
                for (Direction facing : directions) {
                    if (!(random.nextFloat() > 0.25F) && tryPlaceMushroomOnStandingTree(context, logPos, facing, random)) {
                        break;
                    }
                }
            }
        }
    }
    
    private static void placeOnFallenLog(Context context, List<BlockPos> logs, RandomSource random) {
        Direction[] directions = perpendicularToFallenLog(logs);
        
        for (BlockPos logPos : logs) {
            for (Direction facing : directions) {
                if (!(random.nextFloat() > 0.25F)) {
                    tryPlaceMushroomOnFallenTree(context, logPos, facing, random);
                }
            }
        }
    }
    
    private static boolean tryPlaceMushroomOnStandingTree(
        final Context context, BlockPos logPos, Direction facing, RandomSource random
    ) {
        BlockPos mushroomPos = mushroomPosFor(logPos, facing);
        if (!isBlockReplaceableWithShelfMushroom(context, mushroomPos)) {
            return false;
        } else if (hasShelfMushroomAt(context, mushroomPos.below())) {
            return false;
        } else {
            placeMushroom(context, mushroomPos, facing, random);
            return true;
        }
    }
    
    private static void tryPlaceMushroomOnFallenTree(Context context, BlockPos logPos, Direction facing, RandomSource random) {
        BlockPos mushroomPos = mushroomPosFor(logPos, facing);
        if (isBlockReplaceableWithShelfMushroom(context, mushroomPos)) {
            if (!hasHorizontallyAdjacentShelfMushroom(context, mushroomPos) && !hasHorizontallyAdjacentShelfMushroom(context, logPos)) {
                placeMushroom(context, mushroomPos, facing, random);
            }
        }
    }
    
    private static boolean isFallenLog(List<BlockPos> logs) {
        return logs.get(0).getY() == logs.get(logs.size() - 1).getY();
    }
    
    private static Direction[] pickTwoPerpendicularDirections(RandomSource random) {
        Direction first = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        return new Direction[]{first, first.getClockWise()};
    }
    
    private static Direction[] perpendicularToFallenLog(List<BlockPos> logs) {
        BlockPos first = logs.get(0);
        BlockPos last = logs.get(logs.size() - 1);
        Direction.Axis logAxis = first.getX() != last.getX() ? Direction.Axis.X : Direction.Axis.Z;
        return logAxis == Direction.Axis.X ? new Direction[]{Direction.NORTH, Direction.SOUTH} : new Direction[]{Direction.EAST, Direction.WEST};
    }
    
    private static boolean isWithinDecoratableHeight(BlockPos pos, int treeBaseY) {
        int dy = pos.getY() - treeBaseY;
        return dy >= 1 && dy <= 4;
    }
    
    private static BlockPos mushroomPosFor(BlockPos logPos, Direction facing) {
        return logPos.offset(facing.getStepX(), 0, facing.getStepZ());
    }
    
    private static void placeMushroom(Context context, BlockPos pos, Direction facing, RandomSource random) {
        context.setBlock(
            pos, ModBlocks.SHELF_MUSHROOM.get().defaultBlockState().setValue(ShelfMushroomBlock.AGE, random.nextInt(2)).setValue(ShelfMushroomBlock.FACING, facing)
        );
    }
    
    private static boolean hasShelfMushroomAt(Context context, BlockPos pos) {
        return checkBlock(context, pos, state -> state.is(ModBlocks.SHELF_MUSHROOM.get()));
    }
    
    private static boolean hasHorizontallyAdjacentShelfMushroom(Context context, BlockPos pos) {
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            if (hasShelfMushroomAt(context, pos.relative(dir))) {
                return true;
            }
        }
        
        return false;
    }
    
    public static boolean isBlockReplaceableWithShelfMushroom(Context context, BlockPos pos) {
        return isReplaceable(context, pos) && !isWaterOrWaterNearby(context, pos);
    }
    
    private static boolean checkBlock(Context context, BlockPos pos, Predicate<BlockState> predicate) {
        return context.level().isStateAtPosition(pos, predicate);
    }
    
    public static boolean isReplaceable(Context context, BlockPos pos) {
        return checkBlock(context, pos, BlockBehaviour.BlockStateBase::canBeReplaced);
    }
    
    public static boolean isWaterOrWaterNearby(Context context, BlockPos pos) {
        return checkBlock(context, pos, state -> state.is(Blocks.WATER))
            || checkBlock(context, pos.east(), state -> state.is(Blocks.WATER))
            || checkBlock(context, pos.west(), state -> state.is(Blocks.WATER))
            || checkBlock(context, pos.north(), state -> state.is(Blocks.WATER))
            || checkBlock(context, pos.south(), state -> state.is(Blocks.WATER));
    }
}

package com.blackgear.vanillabackport.common.level.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.TreeFeature;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class FallenTreeFeature extends Feature<FallenTreeConfiguration> {

    public FallenTreeFeature(Codec<FallenTreeConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<FallenTreeConfiguration> context) {
        this.placeFallenTree(context.config(), context.origin(), context.level(), context.random());
        return true;
    }

    private void placeFallenTree(FallenTreeConfiguration config, BlockPos pos, WorldGenLevel level, RandomSource random) {
        this.placeStump(config, level, random, pos.mutable());
        Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        int length = config.logLength.sample(random) - 2;
        BlockPos.MutableBlockPos mutable = pos.relative(direction, 2 + random.nextInt(2)).mutable();
        this.setGroundHeightForFallenLogStartPos(level, mutable);

        if (this.canPlaceEntireFallenLog(level, length, mutable, direction)) {
            this.placeFallenLog(config, level, random, length, mutable, direction);
        }
    }

    private void setGroundHeightForFallenLogStartPos(WorldGenLevel level, BlockPos.MutableBlockPos mutable) {
        mutable.move(Direction.UP, 1);

        for (int i = 0; i < 6; i++) {
            if (this.mayPlaceOn(level, mutable)) {
                return;
            }

            mutable.move(Direction.DOWN);
        }
    }

    private void placeStump(
        FallenTreeConfiguration config,
        WorldGenLevel level,
        RandomSource random,
        BlockPos.MutableBlockPos mutable
    ) {
        BlockPos origin = this.placeLogBlock(config, level, random, mutable, Function.identity());
        this.decorateLogs(level, random, Set.of(origin), config.stumpDecorators);
    }

    private boolean canPlaceEntireFallenLog(WorldGenLevel level, int length, BlockPos.MutableBlockPos mutable, Direction direction) {
        int gaps = 0;

        for (int k = 0; k < length; k++) {
            if (!TreeFeature.validTreePos(level, mutable)) {
                return false;
            }

            if (!this.isOverSolidGround(level, mutable)) {
                if (gaps++ > 2) {
                    return false;
                }
            } else {
                gaps = 0;
            }

            mutable.move(direction);
        }

        mutable.move(direction.getOpposite(), length);
        return true;
    }

    private void placeFallenLog(
        FallenTreeConfiguration config,
        WorldGenLevel level,
        RandomSource random,
        int length,
        BlockPos.MutableBlockPos mutable,
        Direction direction
    ) {
        Set<BlockPos> positions = new HashSet<>();

        for (int i = 0; i < length; i++) {
            positions.add(this.placeLogBlock(config, level, random, mutable, getSidewaysStateModifier(direction)));
            mutable.move(direction);
        }

        this.decorateLogs(level, random, positions, config.logDecorators);
    }

    private boolean mayPlaceOn(LevelAccessor level, BlockPos pos) {
        return TreeFeature.validTreePos(level, pos) && this.isOverSolidGround(level, pos);
    }

    private boolean isOverSolidGround(LevelAccessor level, BlockPos pos) {
        return level.getBlockState(pos.below()).isFaceSturdy(level, pos, Direction.UP);
    }

    private BlockPos placeLogBlock(
        FallenTreeConfiguration config,
        WorldGenLevel level,
        RandomSource random,
        BlockPos.MutableBlockPos mutable,
        Function<BlockState, BlockState> factory
    ) {
        level.setBlock(mutable, factory.apply(config.trunkProvider.getState(random, mutable)), 3);
        this.markAboveForPostProcessing(level, mutable);
        return mutable.immutable();
    }

    private void decorateLogs(WorldGenLevel level, RandomSource random, Set<BlockPos> positions, List<TreeDecorator> decorators) {
        if (!decorators.isEmpty()) {
            TreeDecorator.Context context = new TreeDecorator.Context(level, this.getDecorationSetter(level), random, positions, Set.of(), Set.of());
            decorators.forEach(decorator -> decorator.place(context));
        }
    }

    private BiConsumer<BlockPos, BlockState> getDecorationSetter(WorldGenLevel level) {
        return (pos, state) -> level.setBlock(pos, state, 19);
    }

    private static Function<BlockState, BlockState> getSidewaysStateModifier(Direction direction) {
        return state -> state.trySetValue(RotatedPillarBlock.AXIS, direction.getAxis());
    }
}
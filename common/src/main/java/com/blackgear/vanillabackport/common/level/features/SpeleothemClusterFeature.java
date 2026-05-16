package com.blackgear.vanillabackport.common.level.features;

import com.mojang.serialization.Codec;

import java.util.Optional;
import java.util.OptionalInt;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Direction.Plane;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.ClampedNormalFloat;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Column;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class SpeleothemClusterFeature extends Feature<SpeleothemClusterConfiguration> {
    public SpeleothemClusterFeature(Codec<SpeleothemClusterConfiguration> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<SpeleothemClusterConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        SpeleothemClusterConfiguration config = context.config();
        RandomSource random = context.random();
        if (!SpeleothemUtils.isEmptyOrWater(level, origin)) {
            return false;
        } else {
            int height = config.height().sample(random);
            float wetness = config.wetness().sample(random);
            float density = config.density().sample(random);
            int xRadius = config.radius().sample(random);
            int zRadius = config.radius().sample(random);

            for (int x = -xRadius; x <= xRadius; ++x) {
                for (int z = -zRadius; z <= zRadius; ++z) {
                    double chanceOfStalagmiteOrStalactite = this.getChanceOfStalagmiteOrStalactite(xRadius, zRadius, x, z, config);
                    BlockPos pos = origin.offset(x, 0, z);
                    this.placeColumn(level, random, pos, x, z, wetness, chanceOfStalagmiteOrStalactite, height, density, config);
                }
            }

            return true;
        }
    }

    private void placeColumn(
        WorldGenLevel level,
        RandomSource random,
        BlockPos pos,
        int x,
        int z,
        float wetness,
        double chanceOfStalagmiteOrStalactite,
        int height,
        float density,
        SpeleothemClusterConfiguration config
    ) {
        Optional<Column> baseColumn = Column.scan(level, pos, config.floorToCeilingSearchRange(), SpeleothemUtils::isEmptyOrWater, SpeleothemUtils::isNeitherEmptyNorWater);
        if (baseColumn.isPresent()) {
            OptionalInt ceiling = baseColumn.get().getCeiling();
            OptionalInt baseFloor = baseColumn.get().getFloor();
            if (ceiling.isPresent() || baseFloor.isPresent()) {
                boolean wantPool = random.nextFloat() < wetness;
                Column column;
                if (wantPool && baseFloor.isPresent() && this.canPlacePool(level, pos.atY(baseFloor.getAsInt()), config)) {
                    int baseFloorY = baseFloor.getAsInt();
                    column = baseColumn.get().withFloor(OptionalInt.of(baseFloorY - 1));
                    level.setBlock(pos.atY(baseFloorY), Blocks.WATER.defaultBlockState(), 2);
                } else {
                    column = baseColumn.get();
                }

                OptionalInt floor = column.getFloor();
                boolean wantStalactite = random.nextDouble() < chanceOfStalagmiteOrStalactite;
                int stalactiteHeight;
                if (ceiling.isPresent() && wantStalactite && !this.isLava(level, pos.atY(ceiling.getAsInt()))) {
                    int ceilingThickness = config.speleothemBlockLayerThickness().sample(random);
                    this.replaceBlocksWithBaseBlocks(level, pos.atY(ceiling.getAsInt()), ceilingThickness, Direction.UP, config);
                    int maxHeightForThisColumn;
                    if (floor.isPresent()) {
                        maxHeightForThisColumn = Math.min(height, ceiling.getAsInt() - floor.getAsInt());
                    } else {
                        maxHeightForThisColumn = height;
                    }

                    stalactiteHeight = this.getSpeleothemHeight(random, x, z, density, maxHeightForThisColumn, config);
                } else {
                    stalactiteHeight = 0;
                }

                boolean wantStalagmite = random.nextDouble() < chanceOfStalagmiteOrStalactite;
                int stalagmiteHeight;
                if (floor.isPresent() && wantStalagmite && !this.isLava(level, pos.atY(floor.getAsInt()))) {
                    stalagmiteHeight = config.speleothemBlockLayerThickness().sample(random);
                    this.replaceBlocksWithBaseBlocks(level, pos.atY(floor.getAsInt()), stalagmiteHeight, Direction.DOWN, config);
                    if (ceiling.isPresent()) {
                        stalagmiteHeight = Math.max(0, stalactiteHeight + Mth.randomBetweenInclusive(random, -config.maxStalagmiteStalactiteHeightDiff(), config.maxStalagmiteStalactiteHeightDiff()));
                    } else {
                        stalagmiteHeight = this.getSpeleothemHeight(random, x, z, density, height, config);
                    }
                } else {
                    stalagmiteHeight = 0;
                }

                int actualStalactiteHeight;
                int actualStalagmiteHeight;
                if (ceiling.isPresent() && floor.isPresent() && ceiling.getAsInt() - stalactiteHeight <= floor.getAsInt() + stalagmiteHeight) {
                    int floorY = floor.getAsInt();
                    int ceilingY = ceiling.getAsInt();
                    int lowestStalactiteBottom = Math.max(ceilingY - stalactiteHeight, floorY + 1);
                    int highestStalagmiteTop = Math.min(floorY + stalagmiteHeight, ceilingY - 1);
                    int actualStalactiteBottom = Mth.randomBetweenInclusive(random, lowestStalactiteBottom, highestStalagmiteTop + 1);
                    int actualStalagmiteTop = actualStalactiteBottom - 1;
                    actualStalactiteHeight = ceilingY - actualStalactiteBottom;
                    actualStalagmiteHeight = actualStalagmiteTop - floorY;
                } else {
                    actualStalactiteHeight = stalactiteHeight;
                    actualStalagmiteHeight = stalagmiteHeight;
                }

                boolean mergeTips = random.nextBoolean()
                    && actualStalactiteHeight > 0
                    && actualStalagmiteHeight > 0
                    && column.getHeight().isPresent()
                    && actualStalactiteHeight + actualStalagmiteHeight == column.getHeight().getAsInt();
                if (ceiling.isPresent()) {
                    SpeleothemUtils.growSpeleothem(
                        level,
                        pos.atY(ceiling.getAsInt() - 1),
                        Direction.DOWN,
                        actualStalactiteHeight,
                        mergeTips,
                        config.baseBlock().getBlock(),
                        config.pointedBlock().getBlock(),
                        config.replaceableBlocks()
                    );
                }

                if (floor.isPresent()) {
                    SpeleothemUtils.growSpeleothem(
                        level,
                        pos.atY(floor.getAsInt() + 1),
                        Direction.UP,
                        actualStalagmiteHeight,
                        mergeTips,
                        config.baseBlock().getBlock(),
                        config.pointedBlock().getBlock(),
                        config.replaceableBlocks()
                    );
                }
            }
        }
    }

    private boolean isLava(LevelReader level, BlockPos pos) {
        return level.getBlockState(pos).is(Blocks.LAVA);
    }

    private int getSpeleothemHeight(
        RandomSource random,
        int x,
        int z,
        float chance,
        int height,
        SpeleothemClusterConfiguration config
    ) {
        if (random.nextFloat() > chance) {
            return 0;
        } else {
            int distanceFromCenter = Math.abs(x) + Math.abs(z);
            float heightMean = (float) Mth.clampedMap(distanceFromCenter, 0.0, config.maxDistanceFromCenterAffectingHeightBias(), height / 2.0, 0.0);
            return (int) randomBetweenBiased(random, 0.0F, height, heightMean, config.heightDeviation());
        }
    }

    private boolean canPlacePool(WorldGenLevel level, BlockPos pos, SpeleothemClusterConfiguration config) {
        BlockState state = level.getBlockState(pos);
        if (!state.is(Blocks.WATER) && !state.is(config.baseBlock().getBlock()) && !state.is(config.pointedBlock().getBlock())) {
            if (level.getBlockState(pos.above()).getFluidState().is(FluidTags.WATER)) {
                return false;
            } else {
                for (Direction direction : Plane.HORIZONTAL) {
                    if (!this.canBeAdjacentToWater(level, pos.relative(direction))) {
                        return false;
                    }
                }

                return this.canBeAdjacentToWater(level, pos.below());
            }
        } else {
            return false;
        }
    }

    private boolean canBeAdjacentToWater(LevelAccessor level, BlockPos pos) {
        BlockState blockState = level.getBlockState(pos);
        return blockState.is(BlockTags.BASE_STONE_OVERWORLD) || blockState.getFluidState().is(FluidTags.WATER);
    }

    private void replaceBlocksWithBaseBlocks(
        WorldGenLevel level,
        BlockPos origin,
        int maxCount,
        Direction direction,
        SpeleothemClusterConfiguration config
    ) {
        BlockPos.MutableBlockPos pos = origin.mutable();

        for (int i = 0; i < maxCount; ++i) {
            if (!SpeleothemUtils.placeBaseBlockIfPossible(level, pos, config.baseBlock().getBlock(), config.replaceableBlocks())) {
                return;
            }

            pos.move(direction);
        }

    }

    private double getChanceOfStalagmiteOrStalactite(
        int xRadius,
        int zRadius,
        int x,
        int z,
        SpeleothemClusterConfiguration config
    ) {
        int xDistanceFromEdge = xRadius - Math.abs(x);
        int zDistanceFromEdge = zRadius - Math.abs(z);
        int distanceFromEdge = Math.min(xDistanceFromEdge, zDistanceFromEdge);
        return Mth.clampedMap((float) distanceFromEdge, 0.0F, (float) config.maxDistanceFromEdgeAffectingChanceOfSpeleothemColumn(), config.chanceOfSpeleothemColumnAtMaxDistanceFromCenter(), 1.0F);
    }

    private static float randomBetweenBiased(RandomSource random, float min, float max, float mean, float deviation) {
        return ClampedNormalFloat.sample(random, mean, deviation, min, max);
    }
}

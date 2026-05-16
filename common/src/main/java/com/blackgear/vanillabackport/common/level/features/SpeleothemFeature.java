package com.blackgear.vanillabackport.common.level.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

import java.util.Optional;

public class SpeleothemFeature extends Feature<SpeleothemConfiguration> {
    public SpeleothemFeature(Codec<SpeleothemConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<SpeleothemConfiguration> context) {
        LevelAccessor level = context.level();
        BlockPos pos = context.origin();
        RandomSource random = context.random();
        SpeleothemConfiguration config = context.config();
        Optional<Direction> tipDirection = getTipDirection(level, pos, random, config);
        if (tipDirection.isEmpty()) {
            return false;
        } else {
            BlockPos rootPos = pos.relative(tipDirection.get().getOpposite());
            createPatchOfBaseBlocks(level, random, rootPos, config);
            int height = random.nextFloat() < config.chanceOfTallerGeneration() && SpeleothemUtils.isEmptyOrWater(level.getBlockState(pos.relative(tipDirection.get())))
                ? 2
                : 1;
            SpeleothemUtils.growSpeleothem(
                level,
                pos,
                tipDirection.get(),
                height,
                false,
                config.baseBlock().getBlock(),
                config.pointedBlock().getBlock(),
                config.replaceableBlocks()
            );
            return true;
        }
    }

    private static Optional<Direction> getTipDirection(
        LevelAccessor level,
        BlockPos pos,
        RandomSource random,
        SpeleothemConfiguration config
    ) {
        boolean canPlaceAbove = SpeleothemUtils.isBase(level.getBlockState(pos.above()), config.baseBlock().getBlock(), config.replaceableBlocks());
        boolean canPlaceBelow = SpeleothemUtils.isBase(level.getBlockState(pos.below()), config.baseBlock().getBlock(), config.replaceableBlocks());
        if (canPlaceAbove && canPlaceBelow) {
            return Optional.of(random.nextBoolean() ? Direction.DOWN : Direction.UP);
        } else if (canPlaceAbove) {
            return Optional.of(Direction.DOWN);
        } else {
            return canPlaceBelow ? Optional.of(Direction.UP) : Optional.empty();
        }
    }

    private void createPatchOfBaseBlocks(LevelAccessor level, RandomSource random, BlockPos pos, SpeleothemConfiguration config) {
        SpeleothemUtils.placeBaseBlockIfPossible(level, pos, config.baseBlock().getBlock(), config.replaceableBlocks());

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            if (random.nextFloat() <= config.chanceOfDirectionalSpread()) {
                BlockPos pos1 = pos.relative(direction);
                SpeleothemUtils.placeBaseBlockIfPossible(level, pos1, config.baseBlock().getBlock(), config.replaceableBlocks());
                if (random.nextFloat() <= config.chanceOfSpreadRadius2()) {
                    BlockPos pos2 = pos1.relative(Direction.getRandom(random));
                    SpeleothemUtils.placeBaseBlockIfPossible(level, pos2, config.baseBlock().getBlock(), config.replaceableBlocks());
                    if (random.nextFloat() <= config.chanceOfSpreadRadius3()) {
                        BlockPos pos3 = pos2.relative(Direction.getRandom(random));
                        SpeleothemUtils.placeBaseBlockIfPossible(level, pos3, config.baseBlock().getBlock(), config.replaceableBlocks());
                    }
                }
            }
        }
    }
}
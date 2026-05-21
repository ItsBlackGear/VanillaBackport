package com.blackgear.vanillabackport.common.level.features;

import com.mojang.serialization.Codec;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;

public class SulfurRootSystemFeature extends Feature<SulfurRootSystemConfiguration> {
    public SulfurRootSystemFeature(Codec<SulfurRootSystemConfiguration> codec) {
        super(codec);
    }

    public boolean place(FeaturePlaceContext<SulfurRootSystemConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        if (!level.getBlockState(origin).isAir()) {
            return false;
        } else {
            RandomSource random = context.random();
            BlockPos pos = context.origin();
            SulfurRootSystemConfiguration config = context.config();
            BlockPos.MutableBlockPos workingPos = pos.mutable();
            if (placeDirtAndTree(level, context.chunkGenerator(), config, random, workingPos, pos)) {
                placeRoots(level, config, random, pos, workingPos);
            }

            return true;
        }
    }

    private static boolean spaceForTree(WorldGenLevel level, SulfurRootSystemConfiguration config, BlockPos pos) {
        BlockPos.MutableBlockPos columnUpPos = pos.mutable();

        for(int i = 1; i <= config.requiredVerticalSpaceForTree(); i++) {
            columnUpPos.move(Direction.UP);
            BlockState state = level.getBlockState(columnUpPos);
            if (!isAllowedTreeSpace(state, i, config.allowedVerticalWaterForTree())) {
                return false;
            }
        }

        if (config.levelTestDistance() > 0) {
            BlockPos.MutableBlockPos cornerPos = pos.mutable();

            for (int i = 0; i < 4; i++) {
                cornerPos.move(Direction.from2DDataValue(i), config.levelTestDistance());
                BlockState below = level.getBlockState(cornerPos.below(config.maxLevelDeviation()));
                BlockState above = level.getBlockState(cornerPos.above(config.maxLevelDeviation()));
                if (below.isAir() || !above.isAir()) return false;
                cornerPos.set(pos);
            }
        }

        return true;
    }

    private static boolean isAllowedTreeSpace(BlockState state, int blocksAboveOrigin, int allowedVerticalWaterHeight) {
        if (state.isAir()) {
            return true;
        } else {
            int blocksAboveGround = blocksAboveOrigin + 1;
            return blocksAboveGround <= allowedVerticalWaterHeight && state.getFluidState().is(FluidTags.WATER);
        }
    }

    private static boolean placeDirtAndTree(
        WorldGenLevel level,
        ChunkGenerator generator,
        SulfurRootSystemConfiguration config,
        RandomSource random,
        BlockPos.MutableBlockPos workingPos,
        BlockPos pos
    ) {
        for (int y = 0; y < config.rootColumnMaxHeight(); y++) {
            workingPos.move(Direction.UP);
            if (level.getHeight(Heightmap.Types.WORLD_SURFACE, workingPos.getX(), workingPos.getZ()) < workingPos.getY()) {
                return false;
            }

            if (config.allowedTreePosition().test(level, workingPos) && spaceForTree(level, config, workingPos)) {
                BlockPos belowPos = workingPos.below();
                if (level.getFluidState(belowPos).is(FluidTags.LAVA) || !level.getBlockState(belowPos).isSolid()) {
                    return false;
                }

                if (config.feature().value().place(level, generator, random, workingPos)) {
                    placeDirt(pos, pos.getY() + y, level, config, random);
                    return true;
                }
            }
        }

        return false;
    }

    private static void placeDirt(BlockPos origin, int targetHeight, WorldGenLevel level, SulfurRootSystemConfiguration config, RandomSource random) {
        int originX = origin.getX();
        int originZ = origin.getZ();
        BlockPos.MutableBlockPos workingPos = origin.mutable();

        for(int y = origin.getY(); y < targetHeight; y++) {
            placeRootedDirt(level, config, random, originX, originZ, workingPos.set(originX, y, originZ));
        }
    }

    private static void placeRootedDirt(
        WorldGenLevel level,
        SulfurRootSystemConfiguration config,
        RandomSource random,
        int originX,
        int originZ,
        BlockPos.MutableBlockPos workingPos
    ) {
        int rootRadius = config.rootRadius();
        Predicate<BlockState> stateTest = state -> state.is(config.rootReplaceable());

        for (int i = 0; i < config.rootPlacementAttempts(); i++) {
            workingPos.setWithOffset(workingPos, random.nextInt(rootRadius) - random.nextInt(rootRadius), 0, random.nextInt(rootRadius) - random.nextInt(rootRadius));
            if (stateTest.test(level.getBlockState(workingPos))) {
                level.setBlock(workingPos, config.rootStateProvider().getState(random, workingPos), 2);
            }

            workingPos.setX(originX);
            workingPos.setZ(originZ);
        }
    }

    private static void placeRoots(WorldGenLevel level, SulfurRootSystemConfiguration config, RandomSource random, BlockPos pos, BlockPos.MutableBlockPos workingPos) {
        int rootRadius = config.hangingRootRadius();
        int verticalSpan = config.hangingRootsVerticalSpan();

        for (int i = 0; i < config.hangingRootPlacementAttempts(); i++) {
            workingPos.setWithOffset(pos, random.nextInt(rootRadius) - random.nextInt(rootRadius), random.nextInt(verticalSpan) - random.nextInt(verticalSpan), random.nextInt(rootRadius) - random.nextInt(rootRadius));
            if (level.isEmptyBlock(workingPos)) {
                BlockState targetState = config.hangingRootStateProvider().getState(random, workingPos);
                if (targetState.canSurvive(level, workingPos) && level.getBlockState(workingPos.above()).isFaceSturdy(level, workingPos, Direction.DOWN)) {
                    level.setBlock(workingPos, targetState, 2);
                }
            }
        }
    }
}

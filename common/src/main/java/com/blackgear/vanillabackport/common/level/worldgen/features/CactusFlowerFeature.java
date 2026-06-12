package com.blackgear.vanillabackport.common.level.worldgen.features;

import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class CactusFlowerFeature extends Feature<NoneFeatureConfiguration> {
    public CactusFlowerFeature(Codec<NoneFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        RandomSource random = context.random();
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        for (int xRange = 0; xRange < 16; xRange++) {
            if (random.nextFloat() > 0.25) continue;

            for (int zRange = 0; zRange < 16; zRange++) {
                if (random.nextFloat() > 0.25) continue;

                int x = origin.getX() + xRange;
                int z = origin.getZ() + zRange;

                mutable.set(x, level.getHeight(Heightmap.Types.MOTION_BLOCKING, x, z) - 1, z);

                if (level.getBlockState(mutable).is(Blocks.CACTUS)) {
                    mutable.set(x, level.getHeight(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z), z);

                    BlockState state = ModBlocks.CACTUS_FLOWER.get().defaultBlockState();

                    if (
                        !level.getBlockState(mutable).is(ModBlocks.CACTUS_FLOWER.get()) &&
                            level.getBlockState(mutable).isAir() &&
                            state.canSurvive(level, mutable)
                    ) {
                        level.setBlock(mutable, state, 1);
                    }
                }
            }
        }

        return true;
    }
}
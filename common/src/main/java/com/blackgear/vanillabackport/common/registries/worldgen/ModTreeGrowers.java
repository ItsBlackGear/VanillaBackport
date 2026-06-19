package com.blackgear.vanillabackport.common.registries.worldgen;

import com.blackgear.vanillabackport.common.worldgen.features.TheGardenAwakensFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractMegaTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

public class ModTreeGrowers {
    public static final AbstractMegaTreeGrower PALE_OAK_TREE = new AbstractMegaTreeGrower() {
        @Override
        protected @Nullable ResourceKey<ConfiguredFeature<?, ?>> getConfiguredMegaFeature(RandomSource random) {
            return null;
        }
        
        @Override
        protected @Nullable ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasFlowers) {
            return TheGardenAwakensFeatures.PALE_OAK_BONEMEAL;
        }
    };
}
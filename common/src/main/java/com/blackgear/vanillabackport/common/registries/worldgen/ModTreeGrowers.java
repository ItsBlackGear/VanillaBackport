package com.blackgear.vanillabackport.common.registries.worldgen;

import com.blackgear.vanillabackport.common.worldgen.features.TheGardenAwakensFeatures;
import com.blackgear.vanillabackport.common.worldgen.features.WildernessBoundFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.AbstractMegaTreeGrower;
import net.minecraft.world.level.block.grower.AbstractTreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

public class ModTreeGrowers {
    public static final AbstractMegaTreeGrower PALE_OAK = new AbstractMegaTreeGrower() {
        @Override
        protected @Nullable ResourceKey<ConfiguredFeature<?, ?>> getConfiguredMegaFeature(RandomSource random) {
            return TheGardenAwakensFeatures.PALE_OAK_BONEMEAL;
        }
        
        @Override
        protected @Nullable ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasFlowers) {
            return null;
        }
    };
    public static final AbstractTreeGrower POPLAR = new AbstractTreeGrower() {
        @Override
        protected @Nullable ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean hasFlowers) {
            int chance = random.nextInt(3);
            return switch (chance) {
                case 0 -> WildernessBoundFeatures.RED_POPLAR;
                case 1 -> WildernessBoundFeatures.ORANGE_POPLAR;
                default -> WildernessBoundFeatures.YELLOW_POPLAR;
            };
        }
    };
}
package com.blackgear.vanillabackport.common.registries.worldgen;

import com.blackgear.vanillabackport.common.worldgen.features.TheGardenAwakensFeatures;
import com.blackgear.vanillabackport.common.worldgen.features.WildernessBoundFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;

public class ModTreeGrowers {
    public static final Supplier<TreeGrower> PALE_OAK = () -> new TreeGrower("pale_oak",
        Optional.of(TheGardenAwakensFeatures.PALE_OAK_BONEMEAL),
        Optional.empty(),
        Optional.empty());
    public static final Supplier<TreeGrower> POPLAR = () -> new TreeGrower("poplar",Optional.empty(), Optional.empty(), Optional.empty()) {
        @Override
        public @Nullable ResourceKey<ConfiguredFeature<?, ?>> getConfiguredFeature(RandomSource random, boolean flowers) {
            int chance = random.nextInt(3);
            return switch (chance) {
                case 0 -> WildernessBoundFeatures.RED_POPLAR;
                case 1 -> WildernessBoundFeatures.ORANGE_POPLAR;
                default -> WildernessBoundFeatures.YELLOW_POPLAR;
            };
        }
    };
}
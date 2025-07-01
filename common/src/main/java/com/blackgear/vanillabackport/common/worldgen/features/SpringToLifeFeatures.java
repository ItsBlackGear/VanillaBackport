package com.blackgear.vanillabackport.common.worldgen.features;

import com.blackgear.platform.common.worldgen.WorldGenRegistry;
import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class SpringToLifeFeatures {
    public static final WorldGenRegistry<ConfiguredFeature<?, ?>> FEATURES = WorldGenRegistry.of(Registries.CONFIGURED_FEATURE, VanillaBackport.MOD_ID);

    // VEGETATION FEATURES
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_BUSH = FEATURES.create("patch_bush");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_FIREFLY_BUSH = FEATURES.create("patch_firefly_bush");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        HolderGetter<ConfiguredFeature<?, ?>> features = context.lookup(Registries.CONFIGURED_FEATURE);
        HolderGetter<PlacedFeature> placements = context.lookup(Registries.PLACED_FEATURE);

        // VEGETATION FEATURES
        FEATURES.register(
            context,
            PATCH_BUSH,
            Feature.RANDOM_PATCH,
            new RandomPatchConfiguration(
                25,
                5,
                3,
                PlacementUtils.onlyWhenEmpty(
                    Feature.SIMPLE_BLOCK,
                    new SimpleBlockConfiguration(
                        BlockStateProvider.simple(ModBlocks.BUSH.get())
                    )
                )
            )
        );
        FEATURES.register(
            context,
            PATCH_FIREFLY_BUSH,
            Feature.RANDOM_PATCH,
            new RandomPatchConfiguration(
                20,
                4,
                3,
                PlacementUtils.onlyWhenEmpty(
                    Feature.SIMPLE_BLOCK,
                    new SimpleBlockConfiguration(
                        BlockStateProvider.simple(ModBlocks.FIREFLY_BUSH.get())
                    )
                )
            )
        );
    }
}
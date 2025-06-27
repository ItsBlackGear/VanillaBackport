package com.blackgear.vanillabackport.common.worldgen.placements;

import com.blackgear.platform.common.worldgen.WorldGenRegistry;
import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.blackgear.vanillabackport.common.worldgen.features.TheGardenAwakensFeatures;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

public class TheGardenAwakensPlacements {
    public static final WorldGenRegistry<PlacedFeature> FEATURES = WorldGenRegistry.of(Registries.PLACED_FEATURE, VanillaBackport.MOD_ID);

    // TREE PLACEMENTS
    public static final ResourceKey<PlacedFeature> PALE_OAK_CHECKED = FEATURES.create("pale_oak_checked");
    public static final ResourceKey<PlacedFeature> PALE_OAK_CREAKING_CHECKED = FEATURES.create("pale_oak_creaking_checked");

    // VEGETATION PLACEMENTS
    public static final ResourceKey<PlacedFeature> FLOWER_PALE_GARDEN = FEATURES.create("flower_pale_garden");
    public static final ResourceKey<PlacedFeature> PALE_GARDEN_VEGETATION = FEATURES.create("pale_garden_vegetation");
    public static final ResourceKey<PlacedFeature> PALE_GARDEN_FLOWERS = FEATURES.create("pale_garden_flowers");
    public static final ResourceKey<PlacedFeature> PALE_MOSS_PATCH = FEATURES.create("pale_moss_patch");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> features = context.lookup(Registries.CONFIGURED_FEATURE);

        // TREE PLACEMENTS
        FEATURES.register(
            context,
            PALE_OAK_CHECKED,
            features.getOrThrow(TheGardenAwakensFeatures.PALE_OAK),
            PlacementUtils.filteredByBlockSurvival(ModBlocks.PALE_OAK_SAPLING.get())
        );
        FEATURES.register(
            context,
            PALE_OAK_CREAKING_CHECKED,
            features.getOrThrow(TheGardenAwakensFeatures.PALE_OAK_CREAKING),
            PlacementUtils.filteredByBlockSurvival(ModBlocks.PALE_OAK_SAPLING.get())
        );

        // VEGETATION PLACEMENTS
        FEATURES.register(
            context,
            FLOWER_PALE_GARDEN,
            features.getOrThrow(TheGardenAwakensFeatures.FLOWER_PALE_GARDEN),
            RarityFilter.onAverageOnceEvery(32),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP,
            BiomeFilter.biome()
        );
        FEATURES.register(
            context,
            PALE_GARDEN_VEGETATION,
            features.getOrThrow(TheGardenAwakensFeatures.PALE_GARDEN_VEGETATION),
            CountPlacement.of(16),
            InSquarePlacement.spread(),
            SurfaceWaterDepthFilter.forMaxDepth(0),
            PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
            BiomeFilter.biome()
        );
        FEATURES.register(
            context,
            PALE_GARDEN_FLOWERS,
            features.getOrThrow(TheGardenAwakensFeatures.PALE_GARDEN_FLOWERS),
            RarityFilter.onAverageOnceEvery(8),
            InSquarePlacement.spread(),
            HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
            BiomeFilter.biome()
        );
        FEATURES.register(
            context,
            PALE_MOSS_PATCH,
            features.getOrThrow(TheGardenAwakensFeatures.PALE_MOSS_PATCH),
            CountPlacement.of(1),
            InSquarePlacement.spread(),
            HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
            BiomeFilter.biome()
        );
    }
}
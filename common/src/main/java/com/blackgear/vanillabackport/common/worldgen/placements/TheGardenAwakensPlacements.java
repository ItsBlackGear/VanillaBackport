package com.blackgear.vanillabackport.common.worldgen.placements;

import com.blackgear.platform.core.api.registrar.bootstrap.PlacedFeatureRegistrar;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.common.worldgen.features.TheGardenAwakensFeatures;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.placement.*;

public class TheGardenAwakensPlacements {
    public static final PlacedFeatureRegistrar REGISTRIES = PlacedFeatureRegistrar.create(VanillaBackport.NAMESPACE);
    
    // TREE PLACEMENTS
    
    public static final ResourceKey<PlacedFeature> PALE_OAK_CHECKED = REGISTRIES.register("pale_oak_checked",
        TheGardenAwakensFeatures.PALE_OAK,
        PlacementUtils.filteredByBlockSurvival(ModBlocks.PALE_OAK_SAPLING.get()));
    public static final ResourceKey<PlacedFeature> PALE_OAK_CREAKING_CHECKED = REGISTRIES.register("pale_oak_creaking_checked",
        TheGardenAwakensFeatures.PALE_OAK_CREAKING,
        PlacementUtils.filteredByBlockSurvival(ModBlocks.PALE_OAK_SAPLING.get()));
    
    // VEGETATION PLACEMENTS
    
    public static final ResourceKey<PlacedFeature> FLOWER_PALE_GARDEN = REGISTRIES.register("flower_pale_garden",
        TheGardenAwakensFeatures.FLOWER_PALE_GARDEN,
        RarityFilter.onAverageOnceEvery(32),
        InSquarePlacement.spread(),
        PlacementUtils.HEIGHTMAP,
        BiomeFilter.biome());
    public static final ResourceKey<PlacedFeature> PALE_GARDEN_VEGETATION = REGISTRIES.register("pale_garden_vegetation",
        TheGardenAwakensFeatures.PALE_GARDEN_VEGETATION,
        CountPlacement.of(16),
        InSquarePlacement.spread(),
        SurfaceWaterDepthFilter.forMaxDepth(0),
        PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
        BiomeFilter.biome());
    public static final ResourceKey<PlacedFeature> PALE_GARDEN_FLOWERS = REGISTRIES.register("pale_garden_flowers",
        TheGardenAwakensFeatures.PALE_GARDEN_FLOWERS,
        RarityFilter.onAverageOnceEvery(8),
        InSquarePlacement.spread(),
        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
        BiomeFilter.biome());
    public static final ResourceKey<PlacedFeature> PALE_MOSS_PATCH = REGISTRIES.register("pale_moss_patch",
        TheGardenAwakensFeatures.PALE_MOSS_PATCH,
        CountPlacement.of(1),
        InSquarePlacement.spread(),
        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
        BiomeFilter.biome());
}
package com.blackgear.vanillabackport.common.worldgen.placements;

import com.blackgear.platform.core.api.registrar.bootstrap.PlacedFeatureRegistrar;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.common.value_providers.TrapezoidInt;
import com.blackgear.vanillabackport.common.worldgen.features.WildernessBoundFeatures;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.*;

public class WildernessBoundPlacements {
    public static final PlacedFeatureRegistrar REGISTRIES = PlacedFeatureRegistrar.create(VanillaBackport.NAMESPACE);
    
    public static final ResourceKey<PlacedFeature> RED_POPLAR = REGISTRIES.register("red_poplar",
        WildernessBoundFeatures.RED_POPLAR,
        PlacementUtils.filteredByBlockSurvival(ModBlocks.POPLAR_SAPLING.get()));
    public static final ResourceKey<PlacedFeature> ORANGE_POPLAR = REGISTRIES.register("orange_poplar",
        WildernessBoundFeatures.ORANGE_POPLAR,
        PlacementUtils.filteredByBlockSurvival(ModBlocks.POPLAR_SAPLING.get()));
    public static final ResourceKey<PlacedFeature> YELLOW_POPLAR = REGISTRIES.register("yellow_poplar",
        WildernessBoundFeatures.YELLOW_POPLAR,
        PlacementUtils.filteredByBlockSurvival(ModBlocks.POPLAR_SAPLING.get()));
    public static final ResourceKey<PlacedFeature> FALLEN_POPLAR_TREE = REGISTRIES.register("fallen_poplar_tree",
        WildernessBoundFeatures.FALLEN_POPLAR_TREE,
        VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(80), ModBlocks.POPLAR_SAPLING.get()));
    
    public static final ResourceKey<PlacedFeature> PATCH_RED_SHRUB = REGISTRIES.register("patch_red_shrub",
        WildernessBoundFeatures.RED_SHRUB,
        InSquarePlacement.spread(),
        PlacementUtils.HEIGHTMAP_WORLD_SURFACE,
        BiomeFilter.biome(),
        CountPlacement.of(8),
        RandomOffsetPlacement.of(TrapezoidInt.triangle(7), TrapezoidInt.triangle(3)),
        BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE)
    );
    public static final ResourceKey<PlacedFeature> BROWN_MUSHROOM_DAPPLED_FOREST = REGISTRIES.register("brown_mushroom_dappled_forest",
        WildernessBoundFeatures.BROWN_MUSHROOM,
        RarityFilter.onAverageOnceEvery(2),
        InSquarePlacement.spread(),
        PlacementUtils.HEIGHTMAP,
        BiomeFilter.biome(),
        CountPlacement.of(96),
        RandomOffsetPlacement.of(TrapezoidInt.triangle(7), TrapezoidInt.triangle(3)),
        BlockPredicateFilter.forPredicate(BlockPredicate.ONLY_IN_AIR_PREDICATE)
    );
    
    public static final ResourceKey<PlacedFeature> BAMBOO_IN_STRUCTURE = REGISTRIES.register("bamboo_in_structure",
        VegetationFeatures.BAMBOO_NO_PODZOL,
        PlacementUtils.isEmpty());
    
    public static final ResourceKey<PlacedFeature> TREES_DAPPLED_FOREST = REGISTRIES.register("trees_dappled_forest",
        WildernessBoundFeatures.TREES_DAPPLED_FOREST,
        CountPlacement.of(6),
        InSquarePlacement.spread(),
        SurfaceWaterDepthFilter.forMaxDepth(0),
        PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
        BiomeFilter.biome());
}
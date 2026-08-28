package com.blackgear.vanillabackport.common.worldgen.placements;

import com.blackgear.platform.core.api.registrar.bootstrap.PlacedFeatureRegistrar;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.common.worldgen.features.SpringToLifeFeatures;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.BlockPos;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.material.Fluids;

public class SpringToLifePlacements {
    public static final PlacedFeatureRegistrar REGISTRIES = PlacedFeatureRegistrar.create(VanillaBackport.NAMESPACE);
    
    // VEGETATION PLACEMENTS
    
    public static final ResourceKey<PlacedFeature> PATCH_BUSH = REGISTRIES.register("patch_bush",
        SpringToLifeFeatures.PATCH_BUSH,
        RarityFilter.onAverageOnceEvery(4),
        InSquarePlacement.spread(),
        PlacementUtils.HEIGHTMAP,
        BiomeFilter.biome());
    public static final ResourceKey<PlacedFeature> PATCH_FIREFLY_BUSH_NEAR_WATER = REGISTRIES.register("patch_firefly_bush_near_water",
        SpringToLifeFeatures.PATCH_FIREFLY_BUSH,
        CountPlacement.of(2),
        InSquarePlacement.spread(),
        HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
        BiomeFilter.biome(),
        nearWaterPredicate(ModBlocks.FIREFLY_BUSH.get()));
    public static final ResourceKey<PlacedFeature> PATCH_FIREFLY_BUSH_NEAR_WATER_SWAMP = REGISTRIES.register("patch_firefly_bush_near_water_swamp",
        SpringToLifeFeatures.PATCH_FIREFLY_BUSH,
        CountPlacement.of(3),
        InSquarePlacement.spread(),
        PlacementUtils.HEIGHTMAP,
        BiomeFilter.biome(),
        nearWaterPredicate(ModBlocks.FIREFLY_BUSH.get()));
    public static final ResourceKey<PlacedFeature> PATCH_FIREFLY_BUSH_SWAMP = REGISTRIES.register("patch_firefly_bush_swamp",
        SpringToLifeFeatures.PATCH_FIREFLY_BUSH,
        RarityFilter.onAverageOnceEvery(8),
        InSquarePlacement.spread(),
        PlacementUtils.HEIGHTMAP,
        BiomeFilter.biome());
    public static final ResourceKey<PlacedFeature> WILDFLOWERS_BIRCH_FOREST = REGISTRIES.register("wildflowers_birch_forest",
        SpringToLifeFeatures.WILDFLOWERS_BIRCH_FOREST,
        CountPlacement.of(3),
        RarityFilter.onAverageOnceEvery(2),
        InSquarePlacement.spread(),
        PlacementUtils.HEIGHTMAP,
        BiomeFilter.biome());
    public static final ResourceKey<PlacedFeature> WILDFLOWERS_MEADOW = REGISTRIES.register("wildflowers_meadow",
        SpringToLifeFeatures.WILDFLOWERS_MEADOW,
        NoiseThresholdCountPlacement.of(-0.8, 5, 10),
        InSquarePlacement.spread(),
        PlacementUtils.HEIGHTMAP,
        BiomeFilter.biome());
    public static final ResourceKey<PlacedFeature> PATCH_DRY_GRASS_BADLANDS = REGISTRIES.register("patch_dry_grass_badlands",
        SpringToLifeFeatures.PATCH_DRY_GRASS,
        RarityFilter.onAverageOnceEvery(6),
        InSquarePlacement.spread(),
        PlacementUtils.HEIGHTMAP,
        BiomeFilter.biome());
    public static final ResourceKey<PlacedFeature> PATCH_DRY_GRASS_DESERT = REGISTRIES.register("patch_dry_grass_desert",
        SpringToLifeFeatures.PATCH_DRY_GRASS,
        RarityFilter.onAverageOnceEvery(3),
        InSquarePlacement.spread(),
        PlacementUtils.HEIGHTMAP,
        BiomeFilter.biome());
    public static final ResourceKey<PlacedFeature> PATCH_LEAF_LITTER = REGISTRIES.register("patch_leaf_litter",
        SpringToLifeFeatures.PATCH_LEAF_LITTER,
        VegetationPlacements.worldSurfaceSquaredWithCount(2));
    public static final ResourceKey<PlacedFeature> LEAF_LITTER_SPARSE = REGISTRIES.register("leaf_litter_sparse",
        SpringToLifeFeatures.LEAF_LITTER_SPARSE,
        SurfaceWaterDepthFilter.forMaxDepth(0),
        PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
        BiomeFilter.biome());
    public static final ResourceKey<PlacedFeature> LEAF_LITTER_THICK = REGISTRIES.register("leaf_litter_thick",
        SpringToLifeFeatures.LEAF_LITTER_THICK,
        SurfaceWaterDepthFilter.forMaxDepth(0),
        PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
        BiomeFilter.biome());
    public static final ResourceKey<PlacedFeature> CACTUS_FLOWER = REGISTRIES.register("cactus_flower",
        SpringToLifeFeatures.CACTUS_FLOWER,
        SurfaceWaterDepthFilter.forMaxDepth(0),
        PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
        BiomeFilter.biome());
    
    // TREE PLACEMENTS
    
    public static final ResourceKey<PlacedFeature> FALLEN_OAK_TREE = REGISTRIES.register("fallen_oak_tree",
        SpringToLifeFeatures.FALLEN_OAK_TREE,
        PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING));
    public static final ResourceKey<PlacedFeature> FALLEN_BIRCH_TREE = REGISTRIES.register("fallen_birch_tree",
        SpringToLifeFeatures.FALLEN_BIRCH_TREE,
        PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING));
    public static final ResourceKey<PlacedFeature> FALLEN_SUPER_BIRCH_TREE = REGISTRIES.register("fallen_super_birch_tree",
        SpringToLifeFeatures.FALLEN_SUPER_BIRCH_TREE,
        PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING));
    public static final ResourceKey<PlacedFeature> FALLEN_JUNGLE_TREE = REGISTRIES.register("fallen_jungle_tree",
        SpringToLifeFeatures.FALLEN_JUNGLE_TREE,
        PlacementUtils.filteredByBlockSurvival(Blocks.JUNGLE_SAPLING));
    public static final ResourceKey<PlacedFeature> FALLEN_SPRUCE_TREE = REGISTRIES.register("fallen_spruce_tree",
        SpringToLifeFeatures.FALLEN_SPRUCE_TREE,
        PlacementUtils.filteredByBlockSurvival(Blocks.SPRUCE_SAPLING));
    
    public static final ResourceKey<PlacedFeature> PLACED_FALLEN_OAK_TREE = REGISTRIES.register("placed_fallen_oak_tree",
        SpringToLifeFeatures.FALLEN_OAK_TREE,
        VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(80), Blocks.OAK_SAPLING));
    
    public static final ResourceKey<PlacedFeature> PLACED_RARE_FALLEN_BIRCH_TREE = REGISTRIES.register("placed_rare_fallen_birch_tree",
        SpringToLifeFeatures.FALLEN_BIRCH_TREE,
        VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(400), Blocks.BIRCH_SAPLING));
    public static final ResourceKey<PlacedFeature> PLACED_FALLEN_BIRCH_TREE = REGISTRIES.register("placed_fallen_birch_tree",
        SpringToLifeFeatures.FALLEN_BIRCH_TREE,
        VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(80), Blocks.BIRCH_SAPLING));
    
    public static final ResourceKey<PlacedFeature> PLACED_FALLEN_SUPER_BIRCH_TREE = REGISTRIES.register("placed_fallen_super_birch_tree",
        SpringToLifeFeatures.FALLEN_SUPER_BIRCH_TREE,
        VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(160), Blocks.BIRCH_SAPLING));
    
    public static final ResourceKey<PlacedFeature> PLACED_FALLEN_JUNGLE_TREE = REGISTRIES.register("placed_fallen_jungle_tree",
        SpringToLifeFeatures.FALLEN_JUNGLE_TREE,
        VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(80), Blocks.JUNGLE_SAPLING));
    
    public static final ResourceKey<PlacedFeature> PLACED_FALLEN_SPRUCE_TREE = REGISTRIES.register("placed_fallen_spruce_tree",
        SpringToLifeFeatures.FALLEN_SPRUCE_TREE,
        VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(80), Blocks.SPRUCE_SAPLING));
    public static final ResourceKey<PlacedFeature> PLACED_RARE_FALLEN_SPRUCE_TREE = REGISTRIES.register("placed_rare_fallen_spruce_tree",
        SpringToLifeFeatures.FALLEN_SPRUCE_TREE,
        VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(120), Blocks.SPRUCE_SAPLING));
    
    public static BlockPredicateFilter nearWaterPredicate(Block block) {
        return BlockPredicateFilter.forPredicate(
            BlockPredicate.allOf(
                BlockPredicate.ONLY_IN_AIR_PREDICATE,
                BlockPredicate.wouldSurvive(block.defaultBlockState(), BlockPos.ZERO),
                BlockPredicate.anyOf(
                    BlockPredicate.matchesFluids(new BlockPos(1, -1, 0), Fluids.WATER, Fluids.FLOWING_WATER),
                    BlockPredicate.matchesFluids(new BlockPos(-1, -1, 0), Fluids.WATER, Fluids.FLOWING_WATER),
                    BlockPredicate.matchesFluids(new BlockPos(0, -1, 1), Fluids.WATER, Fluids.FLOWING_WATER),
                    BlockPredicate.matchesFluids(new BlockPos(0, -1, -1), Fluids.WATER, Fluids.FLOWING_WATER)
                )
            )
        );
    }
}
package com.blackgear.vanillabackport.common.worldgen.placements;

import com.blackgear.platform.common.worldgen.WorldGenRegistry;
import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.blackgear.vanillabackport.common.worldgen.features.SpringToLifeFeatures;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.material.Fluids;

public class SpringToLifePlacements {
    public static final WorldGenRegistry<PlacedFeature> FEATURES = WorldGenRegistry.of(Registries.PLACED_FEATURE, VanillaBackport.NAMESPACE);

    // VEGETATION PLACEMENTS
    public static final ResourceKey<PlacedFeature> PATCH_BUSH = FEATURES.create("patch_bush");
    public static final ResourceKey<PlacedFeature> PATCH_FIREFLY_BUSH_NEAR_WATER = FEATURES.create("patch_firefly_bush_near_water");
    public static final ResourceKey<PlacedFeature> PATCH_FIREFLY_BUSH_NEAR_WATER_SWAMP = FEATURES.create("patch_firefly_bush_near_water_swamp");
    public static final ResourceKey<PlacedFeature> PATCH_FIREFLY_BUSH_SWAMP = FEATURES.create("patch_firefly_bush_swamp");
    public static final ResourceKey<PlacedFeature> WILDFLOWERS_BIRCH_FOREST = FEATURES.create("wildflowers_birch_forest");
    public static final ResourceKey<PlacedFeature> WILDFLOWERS_MEADOW = FEATURES.create("wildflowers_meadow");
    public static final ResourceKey<PlacedFeature> PATCH_DRY_GRASS_BADLANDS = FEATURES.create("patch_dry_grass_badlands");
    public static final ResourceKey<PlacedFeature> PATCH_DRY_GRASS_DESERT = FEATURES.create("patch_dry_grass_desert");
    public static final ResourceKey<PlacedFeature> PATCH_LEAF_LITTER = FEATURES.create("patch_leaf_litter");

    // TREE PLACEMENTS
    public static final ResourceKey<PlacedFeature> FALLEN_OAK_TREE = FEATURES.create("fallen_oak_tree");
    public static final ResourceKey<PlacedFeature> FALLEN_BIRCH_TREE = FEATURES.create("fallen_birch_tree");
    public static final ResourceKey<PlacedFeature> FALLEN_SUPER_BIRCH_TREE = FEATURES.create("fallen_super_birch_tree");
    public static final ResourceKey<PlacedFeature> FALLEN_JUNGLE_TREE = FEATURES.create("fallen_jungle_tree");
    public static final ResourceKey<PlacedFeature> FALLEN_SPRUCE_TREE = FEATURES.create("fallen_spruce_tree");

    public static final ResourceKey<PlacedFeature> OAK_BEES_0002_LEAF_LITTER = FEATURES.create("oak_bees_0002_leaf_litter");
    public static final ResourceKey<PlacedFeature> BIRCH_BEES_0002_LEAF_LITTER = FEATURES.create("birch_bees_0002_leaf_litter");
    public static final ResourceKey<PlacedFeature> FANCY_OAK_BEES_0002_LEAF_LITTER = FEATURES.create("fancy_oak_bees_0002_leaf_litter");

    public static final ResourceKey<PlacedFeature> OAK_LEAF_LITTER = FEATURES.create("oak_leaf_litter");
    public static final ResourceKey<PlacedFeature> DARK_OAK_LEAF_LITTER = FEATURES.create("dark_oak_leaf_litter");
    public static final ResourceKey<PlacedFeature> BIRCH_LEAF_LITTER = FEATURES.create("birch_leaf_litter");
    public static final ResourceKey<PlacedFeature> FANCY_OAK_LEAF_LITTER = FEATURES.create("fancy_oak_leaf_litter");

    public static final ResourceKey<PlacedFeature> TREES_DARK_FOREST_LEAF_LITTER = FEATURES.create("trees_dark_forest_leaf_litter");
    public static final ResourceKey<PlacedFeature> TREES_BADLANDS_LEAF_LITTER = FEATURES.create("trees_badlands_leaf_litter");
    public static final ResourceKey<PlacedFeature> TREES_BIRCH_AND_OAK_LEAF_LITTER = FEATURES.create("trees_birch_and_oak_leaf_litter");

    public static final ResourceKey<PlacedFeature> PLACED_FALLEN_OAK_TREE = FEATURES.create("placed_fallen_oak_tree");

    public static final ResourceKey<PlacedFeature> PLACED_RARE_FALLEN_BIRCH_TREE = FEATURES.create("placed_rare_fallen_birch_tree");
    public static final ResourceKey<PlacedFeature> PLACED_FALLEN_BIRCH_TREE = FEATURES.create("placed_fallen_birch_tree");

    public static final ResourceKey<PlacedFeature> PLACED_FALLEN_SUPER_BIRCH_TREE = FEATURES.create("placed_fallen_super_birch_tree");

    public static final ResourceKey<PlacedFeature> PLACED_FALLEN_JUNGLE_TREE = FEATURES.create("placed_fallen_jungle_tree");

    public static final ResourceKey<PlacedFeature> PLACED_FALLEN_SPRUCE_TREE = FEATURES.create("placed_fallen_spruce_tree");
    public static final ResourceKey<PlacedFeature> PLACED_RARE_FALLEN_SPRUCE_TREE = FEATURES.create("placed_rare_fallen_spruce_tree");

    public static void bootstrap(BootstapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> features = context.lookup(Registries.CONFIGURED_FEATURE);

        Holder<ConfiguredFeature<?, ?>> patch = features.getOrThrow(SpringToLifeFeatures.PATCH_FIREFLY_BUSH);

        FEATURES.register(
            context,
            PATCH_BUSH,
            features.getOrThrow(SpringToLifeFeatures.PATCH_BUSH),
            RarityFilter.onAverageOnceEvery(4),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP,
            BiomeFilter.biome()
        );
        FEATURES.register(
            context,
            PATCH_FIREFLY_BUSH_NEAR_WATER,
            patch,
            CountPlacement.of(2),
            InSquarePlacement.spread(),
            HeightmapPlacement.onHeightmap(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES),
            BiomeFilter.biome(),
            nearWaterPredicate(ModBlocks.FIREFLY_BUSH.get())
        );
        FEATURES.register(
            context,
            PATCH_FIREFLY_BUSH_NEAR_WATER_SWAMP,
            patch,
            CountPlacement.of(3),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP,
            BiomeFilter.biome(),
            nearWaterPredicate(ModBlocks.FIREFLY_BUSH.get())
        );
        FEATURES.register(
            context,
            PATCH_FIREFLY_BUSH_SWAMP,
            patch,
            RarityFilter.onAverageOnceEvery(8),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP,
            BiomeFilter.biome()
        );
        FEATURES.register(
            context,
            WILDFLOWERS_BIRCH_FOREST,
            features.getOrThrow(SpringToLifeFeatures.WILDFLOWERS_BIRCH_FOREST),
            CountPlacement.of(3),
            RarityFilter.onAverageOnceEvery(2),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP,
            BiomeFilter.biome()
        );
        FEATURES.register(
            context,
            WILDFLOWERS_MEADOW,
            features.getOrThrow(SpringToLifeFeatures.WILDFLOWERS_MEADOW),
            NoiseThresholdCountPlacement.of(-0.8, 5, 10),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP,
            BiomeFilter.biome()
        );
        FEATURES.register(
            context,
            PATCH_DRY_GRASS_BADLANDS,
            features.getOrThrow(SpringToLifeFeatures.PATCH_DRY_GRASS),
            RarityFilter.onAverageOnceEvery(6),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP,
            BiomeFilter.biome()
        );
        FEATURES.register(
            context,
            PATCH_DRY_GRASS_DESERT,
            features.getOrThrow(SpringToLifeFeatures.PATCH_DRY_GRASS),
            RarityFilter.onAverageOnceEvery(3),
            InSquarePlacement.spread(),
            PlacementUtils.HEIGHTMAP,
            BiomeFilter.biome()
        );
        FEATURES.register(
            context,
            PATCH_LEAF_LITTER,
            features.getOrThrow(SpringToLifeFeatures.PATCH_LEAF_LITTER),
            VegetationPlacements.worldSurfaceSquaredWithCount(2)
        );
        FEATURES.register(
            context,
            FALLEN_OAK_TREE,
            features.getOrThrow(SpringToLifeFeatures.FALLEN_OAK_TREE),
            PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING)
        );
        FEATURES.register(
            context,
            FALLEN_BIRCH_TREE,
            features.getOrThrow(SpringToLifeFeatures.FALLEN_BIRCH_TREE),
            PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING)
        );
        FEATURES.register(
            context,
            FALLEN_SUPER_BIRCH_TREE,
            features.getOrThrow(SpringToLifeFeatures.FALLEN_SUPER_BIRCH_TREE),
            PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING)
        );
        FEATURES.register(
            context,
            FALLEN_SPRUCE_TREE,
            features.getOrThrow(SpringToLifeFeatures.FALLEN_SPRUCE_TREE),
            PlacementUtils.filteredByBlockSurvival(Blocks.SPRUCE_SAPLING)
        );
        FEATURES.register(
            context,
            FALLEN_JUNGLE_TREE,
            features.getOrThrow(SpringToLifeFeatures.FALLEN_JUNGLE_TREE),
            PlacementUtils.filteredByBlockSurvival(Blocks.JUNGLE_SAPLING)
        );

        FEATURES.register(
            context,
            OAK_BEES_0002_LEAF_LITTER,
            features.getOrThrow(SpringToLifeFeatures.OAK_BEES_0002_LEAF_LITTER),
            PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING)
        );
        FEATURES.register(
            context,
            BIRCH_BEES_0002_LEAF_LITTER,
            features.getOrThrow(SpringToLifeFeatures.BIRCH_BEES_0002_LEAF_LITTER),
            PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING)
        );
        FEATURES.register(
            context,
            FANCY_OAK_BEES_0002_LEAF_LITTER,
            features.getOrThrow(SpringToLifeFeatures.FANCY_OAK_BEES_0002_LEAF_LITTER),
            PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING)
        );

        FEATURES.register(
            context,
            OAK_LEAF_LITTER,
            features.getOrThrow(SpringToLifeFeatures.OAK_LEAF_LITTER),
            PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING)
        );
        FEATURES.register(
            context,
            DARK_OAK_LEAF_LITTER,
            features.getOrThrow(SpringToLifeFeatures.DARK_OAK_LEAF_LITTER),
            PlacementUtils.filteredByBlockSurvival(Blocks.DARK_OAK_SAPLING)
        );
        FEATURES.register(
            context,
            BIRCH_LEAF_LITTER,
            features.getOrThrow(SpringToLifeFeatures.BIRCH_LEAF_LITTER),
            PlacementUtils.filteredByBlockSurvival(Blocks.BIRCH_SAPLING)
        );
        FEATURES.register(
            context,
            FANCY_OAK_LEAF_LITTER,
            features.getOrThrow(SpringToLifeFeatures.FANCY_OAK_LEAF_LITTER),
            PlacementUtils.filteredByBlockSurvival(Blocks.OAK_SAPLING)
        );

        FEATURES.register(
            context,
            TREES_DARK_FOREST_LEAF_LITTER,
            features.getOrThrow(SpringToLifeFeatures.TREES_DARK_FOREST_LEAF_LITTER),
            CountPlacement.of(16),
            InSquarePlacement.spread(),
            SurfaceWaterDepthFilter.forMaxDepth(0),
            PlacementUtils.HEIGHTMAP_OCEAN_FLOOR,
            BiomeFilter.biome()
        );
        FEATURES.register(
            context,
            TREES_BADLANDS_LEAF_LITTER,
            features.getOrThrow(SpringToLifeFeatures.OAK_LEAF_LITTER),
            VegetationPlacements.treePlacement(CountPlacement.of(3), Blocks.OAK_SAPLING)
        );
        FEATURES.register(
            context,
            TREES_BIRCH_AND_OAK_LEAF_LITTER,
            features.getOrThrow(SpringToLifeFeatures.TREES_BIRCH_AND_OAK_LEAF_LITTER),
            VegetationPlacements.treePlacement(PlacementUtils.countExtra(10, 0.1F, 1))
        );

        FEATURES.register(
            context,
            PLACED_FALLEN_OAK_TREE,
            features.getOrThrow(SpringToLifeFeatures.FALLEN_OAK_TREE),
            VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(80), Blocks.OAK_SAPLING)
        );

        FEATURES.register(
            context,
            PLACED_RARE_FALLEN_BIRCH_TREE,
            features.getOrThrow(SpringToLifeFeatures.FALLEN_BIRCH_TREE),
            VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(400), Blocks.BIRCH_SAPLING)
        );
        FEATURES.register(
            context,
            PLACED_FALLEN_BIRCH_TREE,
            features.getOrThrow(SpringToLifeFeatures.FALLEN_BIRCH_TREE),
            VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(80), Blocks.BIRCH_SAPLING)
        );

        FEATURES.register(
            context,
            PLACED_FALLEN_SUPER_BIRCH_TREE,
            features.getOrThrow(SpringToLifeFeatures.FALLEN_SUPER_BIRCH_TREE),
            VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(160), Blocks.BIRCH_SAPLING)
        );

        FEATURES.register(
            context,
            PLACED_FALLEN_JUNGLE_TREE,
            features.getOrThrow(SpringToLifeFeatures.FALLEN_JUNGLE_TREE),
            VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(80), Blocks.JUNGLE_SAPLING)
        );

        FEATURES.register(
            context,
            PLACED_FALLEN_SPRUCE_TREE,
            features.getOrThrow(SpringToLifeFeatures.FALLEN_SPRUCE_TREE),
            VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(80), Blocks.SPRUCE_SAPLING)
        );
        FEATURES.register(
            context,
            PLACED_RARE_FALLEN_SPRUCE_TREE,
            features.getOrThrow(SpringToLifeFeatures.FALLEN_SPRUCE_TREE),
            VegetationPlacements.treePlacement(RarityFilter.onAverageOnceEvery(120), Blocks.SPRUCE_SAPLING)
        );
    }

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
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
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.material.Fluids;

public class SpringToLifePlacements {
    public static final WorldGenRegistry<PlacedFeature> FEATURES = WorldGenRegistry.of(Registries.PLACED_FEATURE, VanillaBackport.MOD_ID);

    // VEGETATION PLACEMENTS
    public static final ResourceKey<PlacedFeature> PATCH_BUSH = FEATURES.create("patch_bush");

    public static final ResourceKey<PlacedFeature> PATCH_FIREFLY_BUSH_NEAR_WATER = FEATURES.create("patch_firefly_bush_near_water");
    public static final ResourceKey<PlacedFeature> PATCH_FIREFLY_BUSH_NEAR_WATER_SWAMP = FEATURES.create("patch_firefly_bush_near_water_swamp");
    public static final ResourceKey<PlacedFeature> PATCH_FIREFLY_BUSH_SWAMP = FEATURES.create("patch_firefly_bush_swamp");

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
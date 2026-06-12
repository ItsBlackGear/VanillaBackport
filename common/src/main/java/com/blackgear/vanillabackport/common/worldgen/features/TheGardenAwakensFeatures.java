package com.blackgear.vanillabackport.common.worldgen.features;

import com.blackgear.platform.core.api.registrar.bootstrap.ConfiguredFeatureRegistrar;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.common.worldgen.placements.TheGardenAwakensPlacements;
import com.blackgear.vanillabackport.common.level.worldgen.tree_decorators.CreakingHeartDecorator;
import com.blackgear.vanillabackport.common.level.worldgen.tree_decorators.PaleMossDecorator;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.google.common.collect.ImmutableList;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.ThreeLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.DarkOakFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.DarkOakTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.CaveSurface;

import java.util.List;
import java.util.OptionalInt;

public class TheGardenAwakensFeatures {
    public static final ConfiguredFeatureRegistrar REGISTRIES = ConfiguredFeatureRegistrar.create(VanillaBackport.NAMESPACE);
    
    // TREE FEATURES
    
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_OAK = REGISTRIES.register("pale_oak",
        Feature.TREE,
        (features, placements) ->
            new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.PALE_OAK_LOG.get()),
                new DarkOakTrunkPlacer(6, 2, 1),
                BlockStateProvider.simple(ModBlocks.PALE_OAK_LEAVES.get()),
                new DarkOakFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty())
            )
            .decorators(ImmutableList.of(new PaleMossDecorator(0.15F, 0.4F, 0.8F)))
            .ignoreVines()
            .build());
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_OAK_BONEMEAL = REGISTRIES.register("pale_oak_bonemeal",
        Feature.TREE,
        (features, placements) ->
            new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.PALE_OAK_LOG.get()),
                new DarkOakTrunkPlacer(6, 2, 1),
                BlockStateProvider.simple(ModBlocks.PALE_OAK_LEAVES.get()),
                new DarkOakFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty())
            )
            .ignoreVines()
            .build());
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_OAK_CREAKING = REGISTRIES.register("pale_oak_creaking",
        Feature.TREE,
        (features, placements) ->
            new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.PALE_OAK_LOG.get()),
                new DarkOakTrunkPlacer(6, 2, 1),
                BlockStateProvider.simple(ModBlocks.PALE_OAK_LEAVES.get()),
                new DarkOakFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty())
            )
            .decorators(
                ImmutableList.of(
                    new PaleMossDecorator(0.15F, 0.4F, 0.8F),
                    new CreakingHeartDecorator(1.0F)
                )
            )
            .ignoreVines()
            .build());
    
    // VEGETATION FEATURES
    
    public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWER_PALE_GARDEN = REGISTRIES.register("flower_pale_garden",
        Feature.FLOWER,
        (features, placements) ->
            new RandomPatchConfiguration(
                1,
                0,
                0,
                PlacementUtils.onlyWhenEmpty(
                    Feature.SIMPLE_BLOCK,
                    new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.CLOSED_EYEBLOSSOM.get()))
                )
            ));
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_GARDEN_FLOWERS = REGISTRIES.register("pale_garden_flowers",
        Feature.RANDOM_PATCH,
        (features, placements) ->
            FeatureUtils.simplePatchConfiguration(
                Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.CLOSED_EYEBLOSSOM.get()))
            ));
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_GARDEN_VEGETATION = REGISTRIES.register("pale_garden_vegetation",
        Feature.RANDOM_SELECTOR,
        (features, placements) ->
            new RandomFeatureConfiguration(
                List.of(
                    new WeightedPlacedFeature(placements.getOrThrow(TheGardenAwakensPlacements.PALE_OAK_CREAKING_CHECKED), 0.1F),
                    new WeightedPlacedFeature(placements.getOrThrow(TheGardenAwakensPlacements.PALE_OAK_CHECKED), 0.9F)
                ),
                placements.getOrThrow(TheGardenAwakensPlacements.PALE_OAK_CHECKED)
            ));
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_MOSS_VEGETATION = REGISTRIES.register("pale_moss_vegetation",
        Feature.SIMPLE_BLOCK,
        (features, placements) ->
            new SimpleBlockConfiguration(
                new WeightedStateProvider(
                    SimpleWeightedRandomList.<BlockState>builder()
                        .add(ModBlocks.PALE_MOSS_CARPET.get().defaultBlockState(), 25)
                        .add(Blocks.SHORT_GRASS.defaultBlockState(), 25)
                        .add(Blocks.TALL_GRASS.defaultBlockState(), 10)
                )
            ));
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_MOSS_PATCH = REGISTRIES.register("pale_moss_patch",
        Feature.VEGETATION_PATCH,
        (features, placements) ->
            new VegetationPatchConfiguration(
                BlockTags.MOSS_REPLACEABLE,
                BlockStateProvider.simple(ModBlocks.PALE_MOSS_BLOCK.get()),
                PlacementUtils.inlinePlaced(features.getOrThrow(PALE_MOSS_VEGETATION)),
                CaveSurface.FLOOR,
                ConstantInt.of(1),
                0.0F,
                5,
                0.3F,
                UniformInt.of(2, 4),
                0.75F
            ));
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_MOSS_PATCH_BONEMEAL = REGISTRIES.register("pale_moss_patch_bonemeal",
        Feature.VEGETATION_PATCH,
        (features, placements) ->
            new VegetationPatchConfiguration(
                BlockTags.MOSS_REPLACEABLE,
                BlockStateProvider.simple(ModBlocks.PALE_MOSS_BLOCK.get()),
                PlacementUtils.inlinePlaced(features.getOrThrow(PALE_MOSS_VEGETATION)),
                CaveSurface.FLOOR,
                ConstantInt.of(1),
                0.0F,
                5,
                0.6F,
                UniformInt.of(1, 2),
                0.75F
            ));
}
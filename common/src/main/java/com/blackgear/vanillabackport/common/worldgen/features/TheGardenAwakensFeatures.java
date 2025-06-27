package com.blackgear.vanillabackport.common.worldgen.features;

import com.blackgear.platform.common.worldgen.WorldGenRegistry;
import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.blackgear.vanillabackport.common.worldgen.placements.TheGardenAwakensPlacements;
import com.blackgear.vanillabackport.common.worldgen.treedecorators.CreakingHeartDecorator;
import com.blackgear.vanillabackport.common.worldgen.treedecorators.PaleMossDecorator;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
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
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;
import java.util.OptionalInt;

public class TheGardenAwakensFeatures {
    public static final WorldGenRegistry<ConfiguredFeature<?, ?>> FEATURES = WorldGenRegistry.of(Registries.CONFIGURED_FEATURE, VanillaBackport.NAMESPACE);

    // TREE FEATURES
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_OAK = FEATURES.create("pale_oak");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_OAK_BONEMEAL = FEATURES.create("pale_oak_bonemeal");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_OAK_CREAKING = FEATURES.create("pale_oak_creaking");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_OAK_CREAKING_BONEMEAL = FEATURES.create("pale_oak_creaking_bonemeal");

    // VEGETATION FEATURES
    public static final ResourceKey<ConfiguredFeature<?, ?>> FLOWER_PALE_GARDEN = FEATURES.create("flower_pale_garden");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_GARDEN_FLOWERS = FEATURES.create("pale_garden_flowers");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_GARDEN_VEGETATION = FEATURES.create("pale_garden_vegetation");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_MOSS_VEGETATION = FEATURES.create("pale_moss_vegetation");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_MOSS_PATCH = FEATURES.create("pale_moss_patch");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PALE_MOSS_PATCH_BONEMEAL = FEATURES.create("pale_moss_patch_bonemeal");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        HolderGetter<ConfiguredFeature<?, ?>> features = context.lookup(Registries.CONFIGURED_FEATURE);
        HolderGetter<PlacedFeature> placements = context.lookup(Registries.PLACED_FEATURE);

        // TREE FEATURES
        FEATURES.register(
            context,
            PALE_OAK,
            Feature.TREE,
            new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.PALE_OAK_LOG.get()),
                new DarkOakTrunkPlacer(6, 2, 1),
                BlockStateProvider.simple(ModBlocks.PALE_OAK_LEAVES.get()),
                new DarkOakFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty())
            )
            .decorators(ImmutableList.of(new PaleMossDecorator(0.15F, 0.4F, 0.8F)))
            .ignoreVines()
            .build()
        );
        FEATURES.register(
            context,
            PALE_OAK_BONEMEAL,
            Feature.TREE,
            new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.PALE_OAK_LOG.get()),
                new DarkOakTrunkPlacer(6, 2, 1),
                BlockStateProvider.simple(ModBlocks.PALE_OAK_LEAVES.get()),
                new DarkOakFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty())
            )
            .ignoreVines()
            .build()
        );
        FEATURES.register(
            context,
            PALE_OAK_CREAKING,
            Feature.TREE,
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
            .build()
        );
        FEATURES.register(
            context,
            PALE_OAK_CREAKING_BONEMEAL,
            Feature.TREE,
            new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(ModBlocks.PALE_OAK_LOG.get()),
                new DarkOakTrunkPlacer(6, 2, 1),
                BlockStateProvider.simple(ModBlocks.PALE_OAK_LEAVES.get()),
                new DarkOakFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
                new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty())
            )
            .decorators(ImmutableList.of(new CreakingHeartDecorator(1.0F)))
            .ignoreVines()
            .build()
        );

        // VEGETATION FEATURES
        FEATURES.register(
            context,
            FLOWER_PALE_GARDEN,
            Feature.FLOWER,
            new RandomPatchConfiguration(
                1,
                0,
                0,
                PlacementUtils.onlyWhenEmpty(
                    Feature.SIMPLE_BLOCK,
                    new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.CLOSED_EYEBLOSSOM.get()))
                )
            )
        );
        FEATURES.register(
            context,
            PALE_GARDEN_FLOWERS,
            Feature.RANDOM_PATCH,
            FeatureUtils.simplePatchConfiguration(
                Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.CLOSED_EYEBLOSSOM.get()))
            )
        );
        FEATURES.register(
            context,
            PALE_GARDEN_VEGETATION,
            Feature.RANDOM_SELECTOR,
            new RandomFeatureConfiguration(
                List.of(
                    new WeightedPlacedFeature(placements.getOrThrow(TheGardenAwakensPlacements.PALE_OAK_CREAKING_CHECKED), 0.1F),
                    new WeightedPlacedFeature(placements.getOrThrow(TheGardenAwakensPlacements.PALE_OAK_CHECKED), 0.9F)
                ),
                placements.getOrThrow(TheGardenAwakensPlacements.PALE_OAK_CHECKED)
            )
        );
        FEATURES.register(
            context,
            PALE_MOSS_VEGETATION,
            Feature.SIMPLE_BLOCK,
            new SimpleBlockConfiguration(
                new WeightedStateProvider(
                    SimpleWeightedRandomList.<BlockState>builder()
                        .add(ModBlocks.PALE_MOSS_CARPET.get().defaultBlockState(), 25)
                        .add(Blocks.SHORT_GRASS.defaultBlockState(), 25)
                        .add(Blocks.TALL_GRASS.defaultBlockState(), 10)
                )
            )
        );
        FEATURES.register(
            context,
            PALE_MOSS_PATCH,
            Feature.VEGETATION_PATCH,
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
            )
        );
        FEATURES.register(
            context,
            PALE_MOSS_PATCH_BONEMEAL,
            Feature.VEGETATION_PATCH,
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
            )
        );
    }
}
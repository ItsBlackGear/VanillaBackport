package com.blackgear.vanillabackport.common.worldgen.features;

import com.blackgear.platform.common.worldgen.WorldGenRegistry;
import com.blackgear.vanillabackport.common.level.features.FallenTreeConfiguration;
import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.blackgear.vanillabackport.common.registries.ModFeatures;
import com.blackgear.vanillabackport.common.worldgen.treedecorators.AttachedToLogsDecorator;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TrunkVineDecorator;

import java.util.List;

public class SpringToLifeFeatures {
    public static final WorldGenRegistry<ConfiguredFeature<?, ?>> FEATURES = WorldGenRegistry.of(Registries.CONFIGURED_FEATURE, VanillaBackport.NAMESPACE);

    // VEGETATION FEATURES
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_BUSH = FEATURES.create("patch_bush");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_FIREFLY_BUSH = FEATURES.create("patch_firefly_bush");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WILDFLOWERS_BIRCH_FOREST = FEATURES.create("wildflowers_birch_forest");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WILDFLOWERS_MEADOW = FEATURES.create("wildflowers_meadow");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_DRY_GRASS = FEATURES.create("patch_dry_grass");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_LEAF_LITTER = FEATURES.create("patch_leaf_litter");
    public static final ResourceKey<ConfiguredFeature<?, ?>> LEAF_LITTER = FEATURES.create("leaf_litter");
    public static final ResourceKey<ConfiguredFeature<?, ?>> CACTUS_FLOWER = FEATURES.create("cactus_flower");

    // TREE FEATURES
    public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_OAK_TREE = FEATURES.create("fallen_oak_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_BIRCH_TREE = FEATURES.create("fallen_birch_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_SUPER_BIRCH_TREE = FEATURES.create("fallen_super_birch_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_JUNGLE_TREE = FEATURES.create("fallen_jungle_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_SPRUCE_TREE = FEATURES.create("fallen_spruce_tree");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
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
        FEATURES.register(
            context,
            WILDFLOWERS_BIRCH_FOREST,
            Feature.FLOWER,
            new RandomPatchConfiguration(
                64,
                6,
                2,
                PlacementUtils.onlyWhenEmpty(
                    Feature.SIMPLE_BLOCK,
                    new SimpleBlockConfiguration(
                        new WeightedStateProvider(flowerBedPatchBuilder(ModBlocks.WILDFLOWERS.get()))
                    )
                )
            )
        );
        FEATURES.register(
            context,
            WILDFLOWERS_MEADOW,
            Feature.FLOWER,
            new RandomPatchConfiguration(
                8,
                6,
                2,
                PlacementUtils.onlyWhenEmpty(
                    Feature.SIMPLE_BLOCK,
                    new SimpleBlockConfiguration(
                        new WeightedStateProvider(flowerBedPatchBuilder(ModBlocks.WILDFLOWERS.get()))
                    )
                )
            )
        );
        FEATURES.register(
            context,
            PATCH_DRY_GRASS,
            Feature.RANDOM_PATCH,
            grassPatch(
                new WeightedStateProvider(
                    SimpleWeightedRandomList.<BlockState>builder()
                        .add(ModBlocks.SHORT_DRY_GRASS.get().defaultBlockState(), 1)
                        .add(ModBlocks.TALL_DRY_GRASS.get().defaultBlockState(), 1)
                        .build()
                ),
                64
            )
        );
        FEATURES.register(
            context,
            PATCH_LEAF_LITTER,
            Feature.RANDOM_PATCH,
            FeatureUtils.simpleRandomPatchConfiguration(
                32,
                PlacementUtils.filtered(
                    Feature.SIMPLE_BLOCK,
                    new SimpleBlockConfiguration(new WeightedStateProvider(leafLitterPatchBuilder(1, 3))),
                    BlockPredicate.allOf(
                        BlockPredicate.ONLY_IN_AIR_PREDICATE,
                        BlockPredicate.matchesBlocks(Direction.DOWN.getNormal(), Blocks.GRASS_BLOCK)
                    )
                )
            )
        );
        FEATURES.register(
            context,
            FALLEN_OAK_TREE,
            ModFeatures.FALLEN_TREE.get(),
            createFallenOak().build()
        );
        FEATURES.register(
            context,
            FALLEN_BIRCH_TREE,
            ModFeatures.FALLEN_TREE.get(),
            createFallenBirch(8).build()
        );
        FEATURES.register(
            context,
            FALLEN_SUPER_BIRCH_TREE,
            ModFeatures.FALLEN_TREE.get(),
            createFallenBirch(15).build()
        );
        FEATURES.register(
            context,
            FALLEN_JUNGLE_TREE,
            ModFeatures.FALLEN_TREE.get(),
            createFallenJungle().build()
        );
        FEATURES.register(
            context,
            FALLEN_SPRUCE_TREE,
            ModFeatures.FALLEN_TREE.get(),
            createFallenSpruce().build()
        );

        FEATURES.register(
            context,
            LEAF_LITTER,
            ModFeatures.LEAF_LITTER.get(),
            FeatureConfiguration.NONE
        );

        FEATURES.register(
            context,
            CACTUS_FLOWER,
            ModFeatures.CACTUS_FLOWER.get(),
            FeatureConfiguration.NONE
        );
    }

    private static RandomPatchConfiguration grassPatch(BlockStateProvider provider, int tries) {
        return FeatureUtils.simpleRandomPatchConfiguration(tries, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(provider)));
    }

    private static SimpleWeightedRandomList.Builder<BlockState> flowerBedPatchBuilder(Block block) {
        return segmentedBlockPatchBuilder(block, 1, 4, PinkPetalsBlock.AMOUNT, PinkPetalsBlock.FACING);
    }

    private static SimpleWeightedRandomList.Builder<BlockState> leafLitterPatchBuilder(int min, int max) {
        return segmentedBlockPatchBuilder(ModBlocks.LEAF_LITTER.get(), min, max, PinkPetalsBlock.AMOUNT, PinkPetalsBlock.FACING);
    }

    private static SimpleWeightedRandomList.Builder<BlockState> segmentedBlockPatchBuilder(
        Block block,
        int min,
        int max,
        IntegerProperty amount,
        EnumProperty<Direction> facing
    ) {
        SimpleWeightedRandomList.Builder<BlockState> builder = SimpleWeightedRandomList.builder();

        for (int i = min; i <= max; i++) {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                builder.add(block.defaultBlockState().setValue(amount, i).setValue(facing, direction), 1);
            }
        }

        return builder;
    }

    private static FallenTreeConfiguration.FallenTreeConfigurationBuilder createFallenOak() {
        return createFallenTrees(Blocks.OAK_LOG, 4, 7).stumpDecorators(ImmutableList.of(TrunkVineDecorator.INSTANCE));
    }

    private static FallenTreeConfiguration.FallenTreeConfigurationBuilder createFallenBirch(int i) {
        return createFallenTrees(Blocks.BIRCH_LOG, 5, i);
    }

    private static FallenTreeConfiguration.FallenTreeConfigurationBuilder createFallenJungle() {
        return createFallenTrees(Blocks.JUNGLE_LOG, 4, 11).stumpDecorators(ImmutableList.of(TrunkVineDecorator.INSTANCE));
    }

    private static FallenTreeConfiguration.FallenTreeConfigurationBuilder createFallenSpruce() {
        return createFallenTrees(Blocks.SPRUCE_LOG, 6, 10);
    }

    private static FallenTreeConfiguration.FallenTreeConfigurationBuilder createFallenTrees(Block block, int minLength, int maxLength) {
        return new FallenTreeConfiguration.FallenTreeConfigurationBuilder(BlockStateProvider.simple(block), UniformInt.of(minLength, maxLength))
            .logDecorators(
                ImmutableList.of(
                    new AttachedToLogsDecorator(
                        0.1F,
                        new WeightedStateProvider(
                            SimpleWeightedRandomList.<BlockState>builder()
                                .add(Blocks.RED_MUSHROOM.defaultBlockState(), 2)
                                .add(Blocks.BROWN_MUSHROOM.defaultBlockState(), 1)
                        ),
                        List.of(Direction.UP)
                    )
                )
            );
    }
}
package com.blackgear.vanillabackport.common.worldgen.features;

import com.blackgear.platform.core.api.registrar.bootstrap.ConfiguredFeatureRegistrar;
import com.blackgear.vanillabackport.common.level.block.LeafLitterBlock;
import com.blackgear.vanillabackport.common.level.worldgen.features.FallenTreeConfiguration;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.common.registries.worldgen.ModFeatures;
import com.blackgear.vanillabackport.common.level.worldgen.tree_decorators.AttachedToLogsDecorator;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.Direction;
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
    public static final ConfiguredFeatureRegistrar REGISTRIES = ConfiguredFeatureRegistrar.create(VanillaBackport.NAMESPACE);
    
    // VEGETATION FEATURES
    
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_BUSH = REGISTRIES.register("patch_bush",
        Feature.RANDOM_PATCH,
        (features, placements) ->
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
            ));
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_FIREFLY_BUSH = REGISTRIES.register("patch_firefly_bush",
        Feature.RANDOM_PATCH,
        (features, placements) ->
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
            ));
    public static final ResourceKey<ConfiguredFeature<?, ?>> WILDFLOWERS_BIRCH_FOREST = REGISTRIES.register("wildflowers_birch_forest",
        Feature.FLOWER,
        (features, placements) ->
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
            ));
    public static final ResourceKey<ConfiguredFeature<?, ?>> WILDFLOWERS_MEADOW = REGISTRIES.register("wildflowers_meadow",
        Feature.FLOWER,
        (features, placements) ->
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
            ));
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_DRY_GRASS = REGISTRIES.register("patch_dry_grass",
        Feature.RANDOM_PATCH,
        (features, placements) ->
            grassPatch(
                new WeightedStateProvider(
                    SimpleWeightedRandomList.<BlockState>builder()
                        .add(ModBlocks.SHORT_DRY_GRASS.get().defaultBlockState(), 1)
                        .add(ModBlocks.TALL_DRY_GRASS.get().defaultBlockState(), 1)
                        .build()
                ),
                64
            ));
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_LEAF_LITTER = REGISTRIES.register("patch_leaf_litter",
        Feature.RANDOM_PATCH,
        (features, placements) ->
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
            ));
    public static final ResourceKey<ConfiguredFeature<?, ?>> LEAF_LITTER = REGISTRIES.register("leaf_litter",
        ModFeatures.LEAF_LITTER.get());
    public static final ResourceKey<ConfiguredFeature<?, ?>> CACTUS_FLOWER = REGISTRIES.register("cactus_flower",
        ModFeatures.CACTUS_FLOWER.get());
    
    // TREE FEATURES
    
    public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_OAK_TREE = REGISTRIES.register("fallen_oak_tree",
        ModFeatures.FALLEN_TREE.get(),
        (features, placements) -> createFallenOak().build());
    public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_BIRCH_TREE = REGISTRIES.register("fallen_birch_tree",
        ModFeatures.FALLEN_TREE.get(),
        (features, placements) -> createFallenBirch(8).build());
    public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_SUPER_BIRCH_TREE = REGISTRIES.register("fallen_super_birch_tree",
        ModFeatures.FALLEN_TREE.get(),
        (features, placements) -> createFallenBirch(15).build());
    public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_JUNGLE_TREE = REGISTRIES.register("fallen_jungle_tree",
        ModFeatures.FALLEN_TREE.get(),
        (features, placements) -> createFallenJungle().build());
    public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_SPRUCE_TREE = REGISTRIES.register("fallen_spruce_tree",
        ModFeatures.FALLEN_TREE.get(),
        (features, placements) -> createFallenSpruce().build());
    
    private static RandomPatchConfiguration grassPatch(BlockStateProvider provider, int tries) {
        return FeatureUtils.simpleRandomPatchConfiguration(tries, PlacementUtils.onlyWhenEmpty(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(provider)));
    }
    
    private static SimpleWeightedRandomList.Builder<BlockState> flowerBedPatchBuilder(Block block) {
        return segmentedBlockPatchBuilder(block, 1, 4, PinkPetalsBlock.AMOUNT, PinkPetalsBlock.FACING);
    }
    
    private static SimpleWeightedRandomList.Builder<BlockState> leafLitterPatchBuilder(int min, int max) {
        return segmentedBlockPatchBuilder(ModBlocks.LEAF_LITTER.get(), min, max, LeafLitterBlock.AMOUNT, PinkPetalsBlock.FACING);
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

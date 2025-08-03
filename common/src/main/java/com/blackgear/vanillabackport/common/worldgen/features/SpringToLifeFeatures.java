package com.blackgear.vanillabackport.common.worldgen.features;

import com.blackgear.platform.common.worldgen.WorldGenRegistry;
import com.blackgear.vanillabackport.common.level.features.FallenTreeConfiguration;
import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.blackgear.vanillabackport.common.registries.ModFeatures;
import com.blackgear.vanillabackport.common.worldgen.placements.SpringToLifePlacements;
import com.blackgear.vanillabackport.common.worldgen.treedecorators.AttachedToLogsDecorator;
import com.blackgear.vanillabackport.common.worldgen.treedecorators.PlaceOnGroundDecorator;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.google.common.collect.ImmutableList;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.TreeFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
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
import net.minecraft.world.level.levelgen.feature.WeightedPlacedFeature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.featuresize.ThreeLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.BlobFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.DarkOakFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.stateproviders.WeightedStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.BeehiveDecorator;
import net.minecraft.world.level.levelgen.feature.treedecorators.TrunkVineDecorator;
import net.minecraft.world.level.levelgen.feature.trunkplacers.DarkOakTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.StraightTrunkPlacer;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;
import java.util.OptionalInt;

public class SpringToLifeFeatures {
    public static final WorldGenRegistry<ConfiguredFeature<?, ?>> FEATURES = WorldGenRegistry.of(Registries.CONFIGURED_FEATURE, VanillaBackport.NAMESPACE);

    // VEGETATION FEATURES
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_BUSH = FEATURES.create("patch_bush");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_FIREFLY_BUSH = FEATURES.create("patch_firefly_bush");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WILDFLOWERS_BIRCH_FOREST = FEATURES.create("wildflowers_birch_forest");
    public static final ResourceKey<ConfiguredFeature<?, ?>> WILDFLOWERS_MEADOW = FEATURES.create("wildflowers_meadow");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_DRY_GRASS = FEATURES.create("patch_dry_grass");
    public static final ResourceKey<ConfiguredFeature<?, ?>> PATCH_LEAF_LITTER = FEATURES.create("patch_leaf_litter");

    // TREE FEATURES
    public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_OAK_TREE = FEATURES.create("fallen_oak_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_BIRCH_TREE = FEATURES.create("fallen_birch_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_SUPER_BIRCH_TREE = FEATURES.create("fallen_super_birch_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_JUNGLE_TREE = FEATURES.create("fallen_jungle_tree");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_SPRUCE_TREE = FEATURES.create("fallen_spruce_tree");

    public static final ResourceKey<ConfiguredFeature<?, ?>> OAK_BEES_0002_LEAF_LITTER = FEATURES.create("oak_bees_0002_leaf_litter");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BIRCH_BEES_0002_LEAF_LITTER = FEATURES.create("birch_bees_0002_leaf_litter");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FANCY_OAK_BEES_0002_LEAF_LITTER = FEATURES.create("fancy_oak_bees_0002_leaf_litter");

    public static final ResourceKey<ConfiguredFeature<?, ?>> OAK_LEAF_LITTER = FEATURES.create("oak_leaf_litter");
    public static final ResourceKey<ConfiguredFeature<?, ?>> DARK_OAK_LEAF_LITTER = FEATURES.create("dark_oak_leaf_litter");
    public static final ResourceKey<ConfiguredFeature<?, ?>> BIRCH_LEAF_LITTER = FEATURES.create("birch_leaf_litter");
    public static final ResourceKey<ConfiguredFeature<?, ?>> FANCY_OAK_LEAF_LITTER = FEATURES.create("fancy_oak_leaf_litter");

    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_BIRCH_AND_OAK_LEAF_LITTER = FEATURES.create("trees_birch_and_oak_leaf_litter");
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_DARK_FOREST_LEAF_LITTER = FEATURES.create("trees_dark_forest_leaf_litter");

    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        HolderGetter<ConfiguredFeature<?, ?>> features = context.lookup(Registries.CONFIGURED_FEATURE);
        HolderGetter<PlacedFeature> placements = context.lookup(Registries.PLACED_FEATURE);

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

        BeehiveDecorator beehiveDecorator = new BeehiveDecorator(0.002F);
        PlaceOnGroundDecorator placeOnGroundDecorator = new PlaceOnGroundDecorator(96, 4, 2, new WeightedStateProvider(leafLitterPatchBuilder(1, 3)));
        PlaceOnGroundDecorator placeOnGroundDecorator2 = new PlaceOnGroundDecorator(150, 2, 2, new WeightedStateProvider(leafLitterPatchBuilder(1, 4)));

        FEATURES.register(
            context,
            OAK_BEES_0002_LEAF_LITTER,
            Feature.TREE,
            createOak().decorators(List.of(beehiveDecorator, placeOnGroundDecorator, placeOnGroundDecorator2)).build()
        );
        FEATURES.register(
            context,
            BIRCH_BEES_0002_LEAF_LITTER,
            Feature.TREE,
            createBirch().decorators(List.of(beehiveDecorator, placeOnGroundDecorator, placeOnGroundDecorator2)).build()
        );
        FEATURES.register(
            context,
            FANCY_OAK_BEES_0002_LEAF_LITTER,
            Feature.TREE,
            createFancyOak().decorators(List.of(beehiveDecorator, placeOnGroundDecorator, placeOnGroundDecorator2)).build()
        );
        FEATURES.register(
            context,
            OAK_LEAF_LITTER,
            Feature.TREE,
            createOak().decorators(ImmutableList.of(placeOnGroundDecorator, placeOnGroundDecorator2)).build()
        );
        FEATURES.register(
            context,
            DARK_OAK_LEAF_LITTER,
            Feature.TREE,
            createDarkOak().ignoreVines().decorators(ImmutableList.of(placeOnGroundDecorator, placeOnGroundDecorator2)).build()
        );
        FEATURES.register(
            context,
            BIRCH_LEAF_LITTER,
            Feature.TREE,
            createBirch().decorators(ImmutableList.of(placeOnGroundDecorator, placeOnGroundDecorator2)).build()
        );
        FEATURES.register(
            context,
            FANCY_OAK_LEAF_LITTER,
            Feature.TREE,
            createFancyOak().decorators(List.of(placeOnGroundDecorator, placeOnGroundDecorator2)).build()
        );

        Holder<PlacedFeature> birchLeafLitterBees = placements.getOrThrow(SpringToLifePlacements.BIRCH_BEES_0002_LEAF_LITTER);
        Holder<PlacedFeature> fancyOakLeafLitterBees = placements.getOrThrow(SpringToLifePlacements.FANCY_OAK_BEES_0002_LEAF_LITTER);
        Holder<PlacedFeature> oakLeafLitterBees = placements.getOrThrow(SpringToLifePlacements.OAK_BEES_0002_LEAF_LITTER);
        Holder<PlacedFeature> oakLeafLitter = placements.getOrThrow(SpringToLifePlacements.OAK_LEAF_LITTER);
        Holder<PlacedFeature> darkOakLeafLitter = placements.getOrThrow(SpringToLifePlacements.DARK_OAK_LEAF_LITTER);
        Holder<PlacedFeature> birchLeafLitter = placements.getOrThrow(SpringToLifePlacements.BIRCH_LEAF_LITTER);
        Holder<PlacedFeature> fancyOakLeafLitter = placements.getOrThrow(SpringToLifePlacements.FANCY_OAK_LEAF_LITTER);

        FEATURES.register(
            context,
            TREES_DARK_FOREST_LEAF_LITTER,
            Feature.RANDOM_SELECTOR,
            new RandomFeatureConfiguration(
                List.of(
                    new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TreeFeatures.HUGE_BROWN_MUSHROOM)), 0.025F),
                    new WeightedPlacedFeature(PlacementUtils.inlinePlaced(features.getOrThrow(TreeFeatures.HUGE_RED_MUSHROOM)), 0.05F),
                    new WeightedPlacedFeature(darkOakLeafLitter, 0.6666667F),
                    new WeightedPlacedFeature(birchLeafLitter, 0.2F),
                    new WeightedPlacedFeature(fancyOakLeafLitter, 0.1F)
                ),
                oakLeafLitter
            )
        );

        FEATURES.register(
            context,
            TREES_BIRCH_AND_OAK_LEAF_LITTER,
            Feature.RANDOM_SELECTOR,
            new RandomFeatureConfiguration(
                List.of(
                    new WeightedPlacedFeature(birchLeafLitterBees, 0.2F),
                    new WeightedPlacedFeature(fancyOakLeafLitterBees, 0.1F)
                ),
                oakLeafLitterBees
            )
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

    private static TreeConfiguration.TreeConfigurationBuilder createStraightBlobTree(Block trunk, Block foliage, int baseHeight, int heightRandA, int heightRandB, int radius) {
        return new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(trunk),
            new StraightTrunkPlacer(baseHeight, heightRandA, heightRandB),
            BlockStateProvider.simple(foliage),
            new BlobFoliagePlacer(ConstantInt.of(radius), ConstantInt.of(0), 3),
            new TwoLayersFeatureSize(1, 0, 1)
        );
    }

    private static TreeConfiguration.TreeConfigurationBuilder createOak() {
        return createStraightBlobTree(Blocks.OAK_LOG, Blocks.OAK_LEAVES, 4, 2, 0, 2).ignoreVines();
    }

    private static TreeConfiguration.TreeConfigurationBuilder createBirch() {
        return createStraightBlobTree(Blocks.BIRCH_LOG, Blocks.BIRCH_LEAVES, 5, 2, 0, 2).ignoreVines();
    }

    private static TreeConfiguration.TreeConfigurationBuilder createDarkOak() {
        return new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(Blocks.DARK_OAK_LOG),
            new DarkOakTrunkPlacer(6, 2, 1),
            BlockStateProvider.simple(Blocks.DARK_OAK_LEAVES),
            new DarkOakFoliagePlacer(ConstantInt.of(0), ConstantInt.of(0)),
            new ThreeLayersFeatureSize(1, 1, 0, 1, 2, OptionalInt.empty())
        );
    }

    private static TreeConfiguration.TreeConfigurationBuilder createFancyOak() {
        return new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(Blocks.OAK_LOG),
            new FancyTrunkPlacer(3, 11, 0),
            BlockStateProvider.simple(Blocks.OAK_LEAVES),
            new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4),
            new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))
        )
            .ignoreVines();
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
package com.blackgear.vanillabackport.common.worldgen.features;

import com.blackgear.platform.core.api.registrar.bootstrap.ConfiguredFeatureRegistrar;
import com.blackgear.vanillabackport.common.level.worldgen.features.FallenTreeConfiguration;
import com.blackgear.vanillabackport.common.level.worldgen.features.WeightedRandomFeatureConfiguration;
import com.blackgear.vanillabackport.common.level.worldgen.tree.decorators.AttachedToLogsDecorator;
import com.blackgear.vanillabackport.common.level.worldgen.tree.decorators.ShelfMushroomDecorator;
import com.blackgear.vanillabackport.common.level.worldgen.tree.foliage.PoplarFoliagePlacer;
import com.blackgear.vanillabackport.common.level.worldgen.tree.trunk.PoplarTrunkPlacer;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.common.registries.worldgen.ModFeatures;
import com.blackgear.vanillabackport.common.worldgen.placements.WildernessBoundPlacements;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.placement.TreePlacements;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.util.valueproviders.WeightedListInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

public class WildernessBoundFeatures {
    public static final ConfiguredFeatureRegistrar REGISTRIES = ConfiguredFeatureRegistrar.create(VanillaBackport.NAMESPACE);
    
    public static final ResourceKey<ConfiguredFeature<?, ?>> RED_POPLAR = REGISTRIES.register("red_poplar",
        Feature.TREE,
        (features, placements) -> createPoplar(ModBlocks.RED_POPLAR_LEAVES.get()).decorators(List.of(new ShelfMushroomDecorator(0.4F))).build());
    public static final ResourceKey<ConfiguredFeature<?, ?>> ORANGE_POPLAR = REGISTRIES.register("orange_poplar",
        Feature.TREE,
        (features, placements) -> createPoplar(ModBlocks.ORANGE_POPLAR_LEAVES.get()).decorators(List.of(new ShelfMushroomDecorator(0.4F))).build());
    public static final ResourceKey<ConfiguredFeature<?, ?>> YELLOW_POPLAR = REGISTRIES.register("yellow_poplar",
        Feature.TREE,
        (features, placements) -> createPoplar(ModBlocks.YELLOW_POPLAR_LEAVES.get()).decorators(List.of(new ShelfMushroomDecorator(0.4F))).build());
    
    public static final ResourceKey<ConfiguredFeature<?, ?>> FALLEN_POPLAR_TREE = REGISTRIES.register("fallen_poplar_tree",
        ModFeatures.FALLEN_TREE.get(),
        (features, placements) -> createFallenPoplar().build());
    
    public static final ResourceKey<ConfiguredFeature<?, ?>> RED_SHRUB = REGISTRIES.register("red_shrub",
        Feature.SIMPLE_BLOCK,
        (features, placements) -> new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.RED_SHRUB.get())));
    public static final ResourceKey<ConfiguredFeature<?, ?>> BROWN_MUSHROOM = REGISTRIES.register("brown_mushroom",
        Feature.SIMPLE_BLOCK,
        (features, placements) -> new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.BROWN_MUSHROOM)));
    
    public static final ResourceKey<ConfiguredFeature<?, ?>> TREES_DAPPLED_FOREST = REGISTRIES.register("trees_dappled_forest",
        ModFeatures.WEIGHTED_RANDOM_SELECTOR.get(),
        (features, placements) -> new WeightedRandomFeatureConfiguration(
            SimpleWeightedRandomList.<Holder<PlacedFeature>>builder()
                .add(placements.getOrThrow(WildernessBoundPlacements.RED_POPLAR), 200)
                .add(placements.getOrThrow(WildernessBoundPlacements.ORANGE_POPLAR), 240)
                .add(placements.getOrThrow(WildernessBoundPlacements.YELLOW_POPLAR), 90)
                .add(placements.getOrThrow(TreePlacements.SPRUCE_CHECKED), 27)
                .build()
        ));
    
    public static FallenTreeConfiguration.FallenTreeConfigurationBuilder createFallenPoplar() {
        return new FallenTreeConfiguration.FallenTreeConfigurationBuilder(BlockStateProvider.simple(ModBlocks.POPLAR_LOG.get()), UniformInt.of(4, 7))
            .logDecorator(new AttachedToLogsDecorator(0.1F, BlockStateProvider.simple(Blocks.BROWN_MUSHROOM), List.of(Direction.UP)))
            .logDecorator(new ShelfMushroomDecorator(0.8F));
    }
    
    private static TreeConfiguration.TreeConfigurationBuilder createPoplar(Block leafBlock) {
        return new TreeConfiguration.TreeConfigurationBuilder(
            BlockStateProvider.simple(ModBlocks.POPLAR_LOG.get()),
            new PoplarTrunkPlacer(7, 4, 0, ConstantInt.of(4), UniformInt.of(1, 4)),
            BlockStateProvider.simple(leafBlock),
            new PoplarFoliagePlacer(
                new WeightedListInt(SimpleWeightedRandomList.<IntProvider>builder()
                    .add(ConstantInt.of(5), 5)
                    .add(ConstantInt.of(6), 5)
                    .add(ConstantInt.of(7), 1)
                    .add(ConstantInt.of(8), 1)
                    .build()),
                ConstantInt.of(0),
                UniformInt.of(5, 6),
                0.15F
            ),
            new TwoLayersFeatureSize(1, 0, 2)
        ).ignoreVines();
    }
}
package com.blackgear.vanillabackport.common.worldgen.features;

import com.blackgear.platform.core.api.registrar.bootstrap.ConfiguredFeatureRegistrar;
import com.blackgear.vanillabackport.common.level.block.PotentSulfurBlock;
import com.blackgear.vanillabackport.common.level.block.states.PotentSulfurState;
import com.blackgear.vanillabackport.common.level.worldgen.features.*;
import com.blackgear.vanillabackport.common.level.worldgen.features.TemplateFeatureConfiguration.TemplateEntry;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.common.registries.worldgen.ModFeatures;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import com.blackgear.vanillabackport.common.value_providers.TrapezoidInt;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantFloat;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformFloat;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.LakeFeature;
import net.minecraft.world.level.levelgen.feature.configurations.*;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.material.Fluids;

import java.util.function.BiFunction;
import java.util.function.Function;

public class ChaosCubedFeatures {
    public static final ConfiguredFeatureRegistrar REGISTRIES = ConfiguredFeatureRegistrar.create(VanillaBackport.NAMESPACE);
    
    public static final ResourceKey<ConfiguredFeature<?, ?>> SULFUR_POOL = REGISTRIES.register("sulfur_pool",
        ModFeatures.SEQUENCE.get(),
        (features, placements) ->
            new SimpleRandomFeatureConfiguration(
                HolderSet.direct(
                    PlacementUtils.inlinePlaced(
                        Feature.LAKE,
                        new LakeFeature.Configuration(
                            BlockStateProvider.simple(Blocks.WATER.defaultBlockState()),
                            BlockStateProvider.simple(ModBlocks.SULFUR.get().defaultBlockState())
                        )
                    ),
                    PlacementUtils.inlinePlaced(
                        Feature.SIMPLE_BLOCK,
                        new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.POTENT_SULFUR.get().defaultBlockState().setValue(PotentSulfurBlock.STATE, PotentSulfurState.WET))),
                        EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.allOf(BlockPredicate.solid(), BlockPredicate.matchesFluids(Direction.UP.getNormal(), Fluids.WATER)), 4)
                    )
                )
            ));
    public static final ResourceKey<ConfiguredFeature<?, ?>> SULFUR_SPRING = REGISTRIES.register("sulfur_spring",
        ModFeatures.WEIGHTED_RANDOM_SELECTOR.get(),
        (features, placements) -> {
            BiFunction<Integer, Integer, Holder<PlacedFeature>> tuffCover = (count, spread) -> PlacementUtils.inlinePlaced(
                Feature.SIMPLE_BLOCK,
                new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.TUFF)),
                CountPlacement.of(count),
                RandomOffsetPlacement.of(TrapezoidInt.triangle(spread), TrapezoidInt.triangle(3)),
                EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), 4),
                BlockPredicateFilter.forPredicate(BlockPredicate.solid())
            );
            Function<SimpleWeightedRandomList<TemplateEntry>, Holder<PlacedFeature>> sulfurSprings = entries -> PlacementUtils.inlinePlaced(
                ModFeatures.TEMPLATE.get(), new TemplateFeatureConfiguration(entries), RandomOffsetPlacement.vertical(ConstantInt.of(-7))
            );
            
            return new WeightedRandomFeatureConfiguration(
                SimpleWeightedRandomList.<Holder<PlacedFeature>>builder()
                    .add(
                        PlacementUtils.inlinePlaced(
                            ModFeatures.SEQUENCE.get(),
                            new SimpleRandomFeatureConfiguration(
                                HolderSet.direct(
                                    tuffCover.apply(64, 7),
                                    sulfurSprings.apply(
                                        SimpleWeightedRandomList.<TemplateEntry>builder()
                                            .add(TemplateEntry.of(new ResourceLocation("spring/sulfur_spring_small_1")), 1)
                                            .add(TemplateEntry.of(new ResourceLocation("spring/sulfur_spring_small_2")), 1)
                                            .add(TemplateEntry.of(new ResourceLocation("spring/sulfur_spring_small_3")), 1)
                                            .add(TemplateEntry.of(new ResourceLocation("spring/sulfur_spring_small_4")), 1)
                                            .build()
                                    )
                                )
                            )
                        ),
                        200
                    )
                    .add(
                        PlacementUtils.inlinePlaced(
                            ModFeatures.SEQUENCE.get(),
                            new SimpleRandomFeatureConfiguration(
                                HolderSet.direct(
                                    tuffCover.apply(80, 8),
                                    sulfurSprings.apply(
                                        SimpleWeightedRandomList.<TemplateEntry>builder()
                                            .add(TemplateEntry.of(new ResourceLocation("spring/sulfur_spring_medium_1")), 1)
                                            .add(TemplateEntry.of(new ResourceLocation("spring/sulfur_spring_medium_2")), 1)
                                            .add(TemplateEntry.of(new ResourceLocation("spring/sulfur_spring_medium_3")), 1)
                                            .build()
                                    )
                                )
                            )
                        ),
                        90
                    )
                    .add(
                        PlacementUtils.inlinePlaced(
                            ModFeatures.SEQUENCE.get(),
                            new SimpleRandomFeatureConfiguration(
                                HolderSet.direct(
                                    tuffCover.apply(96, 9),
                                    sulfurSprings.apply(
                                        SimpleWeightedRandomList.<TemplateEntry>builder()
                                            .add(TemplateEntry.of(new ResourceLocation("spring/sulfur_spring_large_1")), 1)
                                            .add(TemplateEntry.of(new ResourceLocation("spring/sulfur_spring_large_2")), 1)
                                            .build()
                                    )
                                )
                            )
                        ),
                        20
                    )
                    .add(
                        PlacementUtils.inlinePlaced(
                            ModFeatures.SEQUENCE.get(),
                            new SimpleRandomFeatureConfiguration(
                                HolderSet.direct(
                                    tuffCover.apply(128, 10),
                                    sulfurSprings.apply(
                                        SimpleWeightedRandomList.single(TemplateEntry.of(new ResourceLocation("spring/sulfur_spring_extra_large_1")))
                                    )
                                )
                            )
                        ),
                        5
                    )
                    .build());
        });
    public static final ResourceKey<ConfiguredFeature<?, ?>> ROOTED_SULFUR_SPRING = REGISTRIES.register("rooted_sulfur_spring",
        ModFeatures.SULFUR_ROOT_SYSTEM.get(),
        (features, placements) ->
            new SulfurRootSystemConfiguration(
                PlacementUtils.inlinePlaced(features.getOrThrow(SULFUR_SPRING)),
                5,
                8,
                2,
                3,
                BlockTags.AZALEA_ROOT_REPLACEABLE,
                BlockStateProvider.simple(ModBlocks.SULFUR.get()),
                20,
                184,
                1,
                1,
                BlockStateProvider.simple(ModBlocks.SULFUR.get()),
                1,
                1,
                BlockPredicate.ONLY_IN_AIR_PREDICATE
            ));
    public static final ResourceKey<ConfiguredFeature<?, ?>> SULFUR_SPIKE_CLUSTER = REGISTRIES.register("sulfur_spike_cluster",
        ModFeatures.SPELEOTHEM_CLUSTER.get(),
        (features, placements) ->
            new SpeleothemClusterConfiguration(
                ModBlocks.SULFUR.get().defaultBlockState(),
                ModBlocks.SULFUR_SPIKE.get().defaultBlockState(),
                ModBlockTags.SULFUR_SPIKE_REPLACEABLE,
                12,
                UniformInt.of(1, 4),
                UniformInt.of(2, 8),
                1,
                3,
                UniformInt.of(2, 4),
                UniformFloat.of(0.3F, 0.7F),
                ConstantFloat.ZERO,
                0.1F,
                3,
                8
            ));
    public static final ResourceKey<ConfiguredFeature<?, ?>> SULFUR_SPIKE = REGISTRIES.register("sulfur_spike",
        Feature.SIMPLE_RANDOM_SELECTOR,
        (features, placements) ->
            new SimpleRandomFeatureConfiguration(
                HolderSet.direct(
                    PlacementUtils.inlinePlaced(
                        ModFeatures.SPELEOTHEM.get(),
                        new SpeleothemConfiguration(
                            ModBlocks.SULFUR.get().defaultBlockState(),
                            ModBlocks.SULFUR_SPIKE.get().defaultBlockState(),
                            ModBlockTags.SULFUR_SPIKE_REPLACEABLE,
                            0.2F,
                            0.7F,
                            0.5F,
                            0.5F
                        ),
                        EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, 12),
                        RandomOffsetPlacement.vertical(ConstantInt.of(1))
                    ),
                    PlacementUtils.inlinePlaced(
                        ModFeatures.SPELEOTHEM.get(),
                        new SpeleothemConfiguration(
                            ModBlocks.SULFUR.get().defaultBlockState(),
                            ModBlocks.SULFUR_SPIKE.get().defaultBlockState(),
                            ModBlockTags.SULFUR_SPIKE_REPLACEABLE,
                            0.2F,
                            0.7F,
                            0.5F,
                            0.5F
                        ),
                        EnvironmentScanPlacement.scanningFor(Direction.UP, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_OR_WATER_PREDICATE, 12),
                        RandomOffsetPlacement.vertical(ConstantInt.of(-1))
                    )
                )
            ));
}
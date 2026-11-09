package com.blackgear.vanillabackport.common.worldgen.features;

import com.blackgear.platform.core.api.registrar.bootstrap.ConfiguredFeatureRegistrar;
import com.blackgear.vanillabackport.common.level.worldgen.features.*;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.common.registries.worldgen.ModFeatures;
import com.blackgear.vanillabackport.common.worldgen.ruletest.AllOfRuleTest;
import com.blackgear.vanillabackport.common.worldgen.ruletest.AnyOfRuleTest;
import com.blackgear.vanillabackport.common.worldgen.ruletest.HeightMatchTest;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockMatchTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class MiscellaneousFeatures {
    public static final ConfiguredFeatureRegistrar REGISTRIES = ConfiguredFeatureRegistrar.create(VanillaBackport.NAMESPACE);
    
    private static final AnyOfRuleTest NATURAL_STONE_OR_ICE = AnyOfRuleTest.of(new TagMatchTest(BlockTags.BASE_STONE_OVERWORLD), new TagMatchTest(ModBlockTags.ICE_CAVE_ORE_REPLACEABLES));
    private static final List<OreConfiguration.TargetBlockState> ICE_CAVES_TARGETS = iceCavesOreTargetList(Blocks.STONE, Blocks.DEEPSLATE);
    
    // Ores
    
    public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_CAVE_ORE_COAL = REGISTRIES.register("ice_cave_ore_coal",
        ModFeatures.SEQUENCE.get(), (features, placements) -> hostedOre(
            ICE_CAVES_TARGETS,
            new OreConfiguration(
                oreTargetList(Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE),
                17
            )));
    public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_CAVE_ORE_COAL_BURIED = REGISTRIES.register("ice_cave_ore_coal_buried",
        ModFeatures.SEQUENCE.get(), (features, placements) -> hostedOre(
            ICE_CAVES_TARGETS,
            new OreConfiguration(
                oreTargetList(Blocks.COAL_ORE, Blocks.DEEPSLATE_COAL_ORE),
                17,
                0.5F
            )));
    public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_CAVE_ORE_COPPER = REGISTRIES.register("ice_cave_ore_copper",
        ModFeatures.SEQUENCE.get(), (features, placements) -> hostedOre(
            ICE_CAVES_TARGETS,
            new OreConfiguration(
                oreTargetList(Blocks.COPPER_ORE, Blocks.DEEPSLATE_COPPER_ORE),
                10
            )));
    public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_CAVE_ORE_DIAMOND_SMALL = REGISTRIES.register("ice_cave_ore_diamond_small",
        ModFeatures.SEQUENCE.get(), (features, placements) -> hostedOre(
            ICE_CAVES_TARGETS,
            new OreConfiguration(
                oreTargetList(Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE),
                4,
                0.5F
            )));
    public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_CAVE_ORE_DIAMOND_MEDIUM = REGISTRIES.register("ice_cave_ore_diamond_medium",
        ModFeatures.SEQUENCE.get(), (features, placements) -> hostedOre(
            ICE_CAVES_TARGETS,
            new OreConfiguration(
                oreTargetList(Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE),
                8,
                0.5F
            )));
    public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_CAVE_ORE_DIAMOND_LARGE = REGISTRIES.register("ice_cave_ore_diamond_large",
        ModFeatures.SEQUENCE.get(), (features, placements) -> hostedOre(
            ICE_CAVES_TARGETS,
            new OreConfiguration(
                oreTargetList(Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE),
                12,
                0.7F
            )));
    public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_CAVE_ORE_DIAMOND_BURIED = REGISTRIES.register("ice_cave_ore_diamond_buried",
        ModFeatures.SEQUENCE.get(), (features, placements) -> hostedOre(
            ICE_CAVES_TARGETS,
            new OreConfiguration(
                oreTargetList(Blocks.DIAMOND_ORE, Blocks.DEEPSLATE_DIAMOND_ORE),
                8,
                1.0F
            )));
    public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_CAVE_ORE_IRON = REGISTRIES.register("ice_cave_ore_iron",
        ModFeatures.SEQUENCE.get(), (features, placements) -> hostedOre(
            ICE_CAVES_TARGETS,
            new OreConfiguration(
                oreTargetList(Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE),
                9
            )));
    public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_CAVE_ORE_IRON_SMALL = REGISTRIES.register("ice_cave_ore_iron_small",
        ModFeatures.SEQUENCE.get(), (features, placements) -> hostedOre(
            ICE_CAVES_TARGETS,
            new OreConfiguration(
                oreTargetList(Blocks.IRON_ORE, Blocks.DEEPSLATE_IRON_ORE),
                4
            )));
    public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_CAVE_ORE_GOLD_BURIED = REGISTRIES.register("ice_cave_ore_gold_buried",
        ModFeatures.SEQUENCE.get(), (features, placements) -> hostedOre(
            ICE_CAVES_TARGETS,
            new OreConfiguration(
                oreTargetList(Blocks.GOLD_ORE, Blocks.DEEPSLATE_GOLD_ORE),
                9,
                0.5F
            )));
    public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_CAVE_ORE_LAPIS = REGISTRIES.register("ice_cave_ore_lapis",
        ModFeatures.SEQUENCE.get(), (features, placements) -> hostedOre(
            ICE_CAVES_TARGETS,
            new OreConfiguration(
                oreTargetList(Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE),
                7
            )));
    public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_CAVE_ORE_LAPIS_BURIED = REGISTRIES.register("ice_cave_ore_lapis_buried",
        ModFeatures.SEQUENCE.get(), (features, placements) -> hostedOre(
            ICE_CAVES_TARGETS,
            new OreConfiguration(
                oreTargetList(Blocks.LAPIS_ORE, Blocks.DEEPSLATE_LAPIS_ORE),
                7,
                1.0F
            )));
    public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_CAVE_ORE_REDSTONE = REGISTRIES.register("ice_cave_ore_redstone",
        ModFeatures.SEQUENCE.get(), (features, placements) -> hostedOre(
            ICE_CAVES_TARGETS,
            new OreConfiguration(
                oreTargetList(Blocks.REDSTONE_ORE, Blocks.DEEPSLATE_REDSTONE_ORE),
                8
            )));
    
    // Underground Variety
    
    public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_CAVE_ORE_GRAVEL = REGISTRIES.register("ice_cave_ore_gravel",
        Feature.ORE, (features, placements) -> new OreConfiguration(
            NATURAL_STONE_OR_ICE,
            Blocks.GRAVEL.defaultBlockState(),
            33
        ));
    
    public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_CAVE_ORE_DIORITE = REGISTRIES.register("ice_cave_ore_diorite",
        Feature.ORE, (features, placements) -> new OreConfiguration(
            NATURAL_STONE_OR_ICE,
            Blocks.DIORITE.defaultBlockState(),
            64
        ));
    
    public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_CAVE_ORE_ANDESITE = REGISTRIES.register("ice_cave_ore_andesite",
        Feature.ORE, (features, placements) -> new OreConfiguration(
            NATURAL_STONE_OR_ICE,
            Blocks.ANDESITE.defaultBlockState(),
            64
        ));
    
    // Features
    
    public static final ResourceKey<ConfiguredFeature<?, ?>> SNOW_LAYERS = REGISTRIES.register("snow_layers",
        Feature.SIMPLE_BLOCK, (features, placements) -> new SimpleBlockConfiguration(BlockStateProvider.simple(Blocks.SNOW)));
    
    public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_CRYSTAL = REGISTRIES.register("ice_crystal",
        Feature.SIMPLE_BLOCK, (features, placements) -> new SimpleBlockConfiguration(BlockStateProvider.simple(ModBlocks.ICE_CRYSTAL.get())));
    
    public static final ResourceKey<ConfiguredFeature<?, ?>> LARGE_ICICLE = REGISTRIES.register("large_icicle",
        context -> new ConfiguredFeature<>(
            ModFeatures.LARGE_SPELEOTHEM.get(),
            new LargeSpeleothemConfiguration(
                Blocks.PACKED_ICE.defaultBlockState(),
                context.lookup(Registries.BLOCK).getOrThrow(ModBlockTags.LARGE_ICICLE_REPLACEABLE),
                30,
                ClampedInt.of(UniformInt.of(3, 19), 3, 16),
                UniformFloat.of(0.4F, 2.0F),
                0.33F,
                UniformFloat.of(0.3F, 0.9F),
                UniformFloat.of(0.4F, 1.0F),
                UniformFloat.of(0.0F, 0.3F),
                4,
                0.6F
            )
        )
    );
    
    public static final ResourceKey<ConfiguredFeature<?, ?>> ICICLE_CLUSTER = REGISTRIES.register("icicle_cluster",
        context -> new ConfiguredFeature<>(
            ModFeatures.SPELEOTHEM_CLUSTER.get(),
            new SpeleothemClusterConfiguration(
                Blocks.PACKED_ICE.defaultBlockState(),
                ModBlocks.ICICLE.get().defaultBlockState(),
                HolderSet.empty(),
                32,
                UniformInt.of(3, 5),
                ConstantInt.of(6),
                1,
                3,
                UniformInt.of(2, 3),
                UniformFloat.of(0.3F, 0.7F),
                ConstantFloat.ZERO,
                0.1F,
                3,
                8,
                new SpeleothemClusterFeature.PlacementOptions(SpeleothemClusterFeature.PlacementMode.CEILING_ONLY, SpeleothemUtils.BaseBlockTransformer.SET_ATTACHED, false)
            )
        )
    );
    
    public static final ResourceKey<ConfiguredFeature<?, ?>> ICE_CREVASSE = REGISTRIES.register("ice_crevasse",
        ModFeatures.CREVASSE.get(), (features, placements) ->
            new CrevasseConfiguration(
                4,
                16,
                10,
                8,
                8,
                4.0F,
                0.6F,
                0.05F,
                UniformInt.of(6, 10),
                UniformInt.of(4, 6),
                BlockStateProvider.simple(ModBlocks.BRITTLE_ICE.get()),
                BlockStateProvider.simple(Blocks.WATER),
                BlockPredicate.matchesTag(ModBlockTags.ICE_CREVASSE_REPLACEABLES),
                BlockPredicate.solid()
            )
    );
    
    // Helper
    
    private static List<OreConfiguration.TargetBlockState> oreTargetList(Block stone, Block deepslate) {
        return List.of(
            OreConfiguration.target(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), stone.defaultBlockState()),
            OreConfiguration.target(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), deepslate.defaultBlockState())
        );
    }
    
    private static List<OreConfiguration.TargetBlockState> oreTargetList(Block stone, Block deepslate, RuleTest heightSpecificReplaceables) {
        return List.of(
            OreConfiguration.target(new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES), stone.defaultBlockState()),
            OreConfiguration.target(new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES), deepslate.defaultBlockState()),
            OreConfiguration.target(AllOfRuleTest.of(HeightMatchTest.min(0), heightSpecificReplaceables), stone.defaultBlockState()),
            OreConfiguration.target(AllOfRuleTest.of(HeightMatchTest.max(0), heightSpecificReplaceables), deepslate.defaultBlockState())
        );
    }
    
    private static List<OreConfiguration.TargetBlockState> iceCavesOreTargetList(Block stone, Block deepslate) {
        return oreTargetList(stone, deepslate, AnyOfRuleTest.of(new BlockMatchTest(Blocks.TUFF), new TagMatchTest(ModBlockTags.ICE_CAVE_ORE_REPLACEABLES)));
    }
    
    private static SimpleRandomFeatureConfiguration hostedOre(List<OreConfiguration.TargetBlockState> targets, OreConfiguration ore) {
        return new SimpleRandomFeatureConfiguration(HolderSet.direct(PlacementUtils.inlinePlaced(Feature.ORE, new OreConfiguration(targets, 20)), PlacementUtils.inlinePlaced(Feature.ORE, ore)));
    }
}
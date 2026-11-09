package com.blackgear.vanillabackport.common.worldgen.placements;

import com.blackgear.platform.core.api.registrar.bootstrap.PlacedFeatureRegistrar;
import com.blackgear.vanillabackport.common.value_providers.TrapezoidInt;
import com.blackgear.vanillabackport.common.worldgen.features.MiscellaneousFeatures;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import net.minecraft.core.Direction;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class MiscellaneousPlacements {
    public static final PlacedFeatureRegistrar REGISTRIES = PlacedFeatureRegistrar.create(VanillaBackport.NAMESPACE);
    
    public static final List<PlacementModifier> DEPOSIT_UPPER_MODIFIERS = rareOrePlacement(6, HeightRangePlacement.uniform(VerticalAnchor.absolute(64), VerticalAnchor.absolute(128)));
    public static final List<PlacementModifier> DEPOSIT_LOWER_MODIFIERS = commonOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(60)));
    
    // Ores
    
    public static final ResourceKey<PlacedFeature> ICE_CAVE_ORE_COAL_UPPER = REGISTRIES.register("ice_cave_ore_copper_upper",
        MiscellaneousFeatures.ICE_CAVE_ORE_COAL,
        commonOrePlacement(30, HeightRangePlacement.uniform(VerticalAnchor.absolute(136), VerticalAnchor.top()))
    );
    
    public static final ResourceKey<PlacedFeature> ICE_CAVE_ORE_COAL_LOWER = REGISTRIES.register("ice_cave_ore_coal_lower",
        MiscellaneousFeatures.ICE_CAVE_ORE_COAL_BURIED,
        commonOrePlacement(20, HeightRangePlacement.triangle(VerticalAnchor.absolute(0), VerticalAnchor.absolute(192)))
    );
    
    public static final ResourceKey<PlacedFeature> ICE_CAVE_ORE_COPPER = REGISTRIES.register("ice_cave_ore_copper",
        MiscellaneousFeatures.ICE_CAVE_ORE_COPPER,
        commonOrePlacement(16, HeightRangePlacement.triangle(VerticalAnchor.absolute(-16), VerticalAnchor.absolute(112)))
    );
    
    public static final ResourceKey<PlacedFeature> ICE_CAVE_ORE_DIAMOND = REGISTRIES.register("ice_cave_ore_diamond",
        MiscellaneousFeatures.ICE_CAVE_ORE_DIAMOND_SMALL,
        commonOrePlacement(7, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)))
    );
    
    public static final ResourceKey<PlacedFeature> ICE_CAVE_ORE_DIAMOND_MEDIUM = REGISTRIES.register("ice_cave_ore_diamond_medium",
        MiscellaneousFeatures.ICE_CAVE_ORE_DIAMOND_MEDIUM,
        commonOrePlacement(2, HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-4)))
    );
    
    public static final ResourceKey<PlacedFeature> ICE_CAVE_ORE_DIAMOND_LARGE = REGISTRIES.register("ice_cave_ore_diamond_large",
        MiscellaneousFeatures.ICE_CAVE_ORE_DIAMOND_LARGE,
        rareOrePlacement(9, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)))
    );
    
    public static final ResourceKey<PlacedFeature> ICE_CAVE_ORE_DIAMOND_BURIED = REGISTRIES.register("ice_cave_ore_diamond_buried",
        MiscellaneousFeatures.ICE_CAVE_ORE_DIAMOND_BURIED,
        commonOrePlacement(4, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)))
    );
    
    public static final ResourceKey<PlacedFeature> ICE_CAVE_ORE_IRON_UPPER = REGISTRIES.register("ice_cave_ore_iron_upper",
        MiscellaneousFeatures.ICE_CAVE_ORE_IRON,
        commonOrePlacement(90, HeightRangePlacement.triangle(VerticalAnchor.absolute(80), VerticalAnchor.absolute(384)))
    );
    
    public static final ResourceKey<PlacedFeature> ICE_CAVE_ORE_IRON_MIDDLE = REGISTRIES.register("ice_cave_ore_iron_middle",
        MiscellaneousFeatures.ICE_CAVE_ORE_IRON,
        commonOrePlacement(10, HeightRangePlacement.triangle(VerticalAnchor.absolute(-24), VerticalAnchor.absolute(56)))
    );
    
    public static final ResourceKey<PlacedFeature> ICE_CAVE_ORE_IRON_SMALL = REGISTRIES.register("ice_cave_ore_iron_small",
        MiscellaneousFeatures.ICE_CAVE_ORE_IRON_SMALL,
        commonOrePlacement(10, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(72)))
    );
    
    public static final ResourceKey<PlacedFeature> ICE_CAVE_ORE_GOLD = REGISTRIES.register("ice_cave_ore_gold",
        MiscellaneousFeatures.ICE_CAVE_ORE_GOLD_BURIED,
        commonOrePlacement(4, HeightRangePlacement.triangle(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(32)))
    );
    
    public static final ResourceKey<PlacedFeature> ICE_CAVE_ORE_GOLD_LOWER = REGISTRIES.register("ice_cave_ore_gold_lower",
        MiscellaneousFeatures.ICE_CAVE_ORE_GOLD_BURIED,
        orePlacement(CountPlacement.of(UniformInt.of(0, 1)), HeightRangePlacement.uniform(VerticalAnchor.absolute(-64), VerticalAnchor.absolute(-48)))
    );
    
    public static final ResourceKey<PlacedFeature> ICE_CAVE_ORE_LAPIS = REGISTRIES.register("ice_cave_ore_lapis",
        MiscellaneousFeatures.ICE_CAVE_ORE_LAPIS,
        commonOrePlacement(2, HeightRangePlacement.triangle(VerticalAnchor.absolute(-32), VerticalAnchor.absolute(32)))
    );
    
    public static final ResourceKey<PlacedFeature> ICE_CAVE_ORE_LAPIS_BURIED = REGISTRIES.register("ice_cave_ore_lapis_buried",
        MiscellaneousFeatures.ICE_CAVE_ORE_LAPIS_BURIED,
        commonOrePlacement(4, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(64)))
    );
    
    public static final ResourceKey<PlacedFeature> ICE_CAVE_ORE_REDSTONE = REGISTRIES.register("ice_cave_ore_redstone",
        MiscellaneousFeatures.ICE_CAVE_ORE_REDSTONE,
        commonOrePlacement(4, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(15)))
    );
    
    public static final ResourceKey<PlacedFeature> ICE_CAVE_ORE_REDSTONE_LOWER = REGISTRIES.register("ice_cave_ore_redstone_lower",
        MiscellaneousFeatures.ICE_CAVE_ORE_REDSTONE,
        commonOrePlacement(8, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-32), VerticalAnchor.aboveBottom(32)))
    );
    
    // Underground Variety
    
    public static final ResourceKey<PlacedFeature> ICE_CAVE_ORE_GRAVEL = REGISTRIES.register("ice_cave_ore_gravel",
        MiscellaneousFeatures.ICE_CAVE_ORE_GRAVEL,
        commonOrePlacement(14, HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.top())));
    
    public static final ResourceKey<PlacedFeature> ICE_CAVE_ORE_DIORITE_UPPER = REGISTRIES.register("ice_cave_ore_diorite_upper",
        MiscellaneousFeatures.ICE_CAVE_ORE_DIORITE,
        DEPOSIT_UPPER_MODIFIERS);
    
    public static final ResourceKey<PlacedFeature> ICE_CAVE_ORE_DIORITE_LOWER = REGISTRIES.register("ice_cave_ore_diorite_lower",
        MiscellaneousFeatures.ICE_CAVE_ORE_DIORITE,
        DEPOSIT_LOWER_MODIFIERS);
    
    public static final ResourceKey<PlacedFeature> ICE_CAVE_ORE_ANDESITE_UPPER = REGISTRIES.register("ice_cave_ore_andesite_upper",
        MiscellaneousFeatures.ICE_CAVE_ORE_ANDESITE,
        DEPOSIT_UPPER_MODIFIERS);
    
    public static final ResourceKey<PlacedFeature> ICE_CAVE_ORE_ANDESITE_LOWER = REGISTRIES.register("ice_cave_ore_andesite_lower",
        MiscellaneousFeatures.ICE_CAVE_ORE_ANDESITE,
        DEPOSIT_LOWER_MODIFIERS);
    
    // Features
    
    public static final ResourceKey<PlacedFeature> SNOW_LAYERS = REGISTRIES.register("snow_layers",
        MiscellaneousFeatures.SNOW_LAYERS,
        CountPlacement.of(UniformInt.of(16, 24)),
        InSquarePlacement.spread(),
        PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
        CountPlacement.of(32),
        RandomOffsetPlacement.of(TrapezoidInt.triangle(4), TrapezoidInt.triangle(0)),
        EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.ONLY_IN_AIR_PREDICATE, 32),
        RandomOffsetPlacement.vertical(ConstantInt.of(1)),
        BiomeFilter.biome());
    
    public static final ResourceKey<PlacedFeature> ICE_CRYSTALS = REGISTRIES.register("ice_crystals",
        MiscellaneousFeatures.ICE_CRYSTAL,
        CountPlacement.of(UniformInt.of(48, 64)),
        InSquarePlacement.spread(),
        PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
        CountPlacement.of(2),
        RandomOffsetPlacement.of(TrapezoidInt.triangle(2), TrapezoidInt.triangle(0)),
        EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.matchesBlocks(Blocks.PACKED_ICE), BlockPredicate.ONLY_IN_AIR_PREDICATE, 32),
        RandomOffsetPlacement.vertical(ConstantInt.of(1)),
        BiomeFilter.biome());
    
    public static final ResourceKey<PlacedFeature> LARGE_ICICLE = REGISTRIES.register("large_icicle",
        MiscellaneousFeatures.LARGE_ICICLE,
        CountPlacement.of(UniformInt.of(48, 64)),
        InSquarePlacement.spread(),
        PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
        BiomeFilter.biome());
    
    public static final ResourceKey<PlacedFeature> ICICLE_CLUSTER = REGISTRIES.register("icicle_cluster",
        MiscellaneousFeatures.ICICLE_CLUSTER,
        CountPlacement.of(UniformInt.of(24, 64)),
        InSquarePlacement.spread(),
        PlacementUtils.RANGE_BOTTOM_TO_MAX_TERRAIN_HEIGHT,
        BiomeFilter.biome());
    
    public static final ResourceKey<PlacedFeature> ICE_CREVASSE = REGISTRIES.register("ice_crevasse",
        MiscellaneousFeatures.ICE_CREVASSE,
        CountPlacement.of(ConstantInt.of(16)),
        InSquarePlacement.spread(),
        HeightRangePlacement.uniform(VerticalAnchor.aboveBottom(24), VerticalAnchor.belowTop(64)),
        EnvironmentScanPlacement.scanningFor(Direction.DOWN, BlockPredicate.matchesTag(ModBlockTags.ICE_CREVASSE_REPLACEABLES), BlockPredicate.ONLY_IN_AIR_PREDICATE, 8),
        BiomeFilter.biome());
    
    // Helpers
    
    private static List<PlacementModifier> orePlacement(PlacementModifier frequencyModifier, PlacementModifier heightRange) {
        return List.of(frequencyModifier, InSquarePlacement.spread(), heightRange, BiomeFilter.biome());
    }
    
    private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier heightRange) {
        return orePlacement(CountPlacement.of(count), heightRange);
    }
    
    private static List<PlacementModifier> rareOrePlacement(int rarity, PlacementModifier heightRange) {
        return orePlacement(RarityFilter.onAverageOnceEvery(rarity), heightRange);
    }
}
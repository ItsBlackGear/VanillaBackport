package com.blackgear.vanillabackport.common.level.worldgen.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record SpeleothemClusterConfiguration(
    BlockState baseBlock,
    BlockState pointedBlock,
    HolderSet<Block> replaceableBlocks,
    int floorToCeilingSearchRange,
    IntProvider height,
    IntProvider radius,
    int maxStalagmiteStalactiteHeightDiff,
    int heightDeviation,
    IntProvider speleothemBlockLayerThickness,
    FloatProvider density,
    FloatProvider wetness,
    float chanceOfSpeleothemColumnAtMaxDistanceFromCenter,
    int maxDistanceFromEdgeAffectingChanceOfSpeleothemColumn,
    int maxDistanceFromCenterAffectingHeightBias,
    SpeleothemClusterFeature.PlacementOptions placementOptions
) implements FeatureConfiguration {
    public static final Codec<SpeleothemClusterConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        BlockState.CODEC.fieldOf("base_block").forGetter(SpeleothemClusterConfiguration::baseBlock),
        BlockState.CODEC.fieldOf("pointed_block").forGetter(SpeleothemClusterConfiguration::pointedBlock),
        RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("replaceable_blocks").forGetter(SpeleothemClusterConfiguration::replaceableBlocks),
        Codec.intRange(1, 512).fieldOf("floor_to_ceiling_search_range").forGetter(SpeleothemClusterConfiguration::floorToCeilingSearchRange),
        IntProvider.codec(1, 128).fieldOf("height").forGetter(SpeleothemClusterConfiguration::height),
        IntProvider.codec(1, 128).fieldOf("radius").forGetter(SpeleothemClusterConfiguration::radius),
        Codec.intRange(0, 64).fieldOf("max_stalagmite_stalactite_height_diff").forGetter(SpeleothemClusterConfiguration::maxStalagmiteStalactiteHeightDiff),
        Codec.intRange(1, 64).fieldOf("height_deviation").forGetter(SpeleothemClusterConfiguration::heightDeviation),
        IntProvider.codec(0, 128).fieldOf("speleothem_block_layer_thickness").forGetter(SpeleothemClusterConfiguration::speleothemBlockLayerThickness),
        FloatProvider.codec(0.0F, 2.0F).fieldOf("density").forGetter(SpeleothemClusterConfiguration::density), 
        FloatProvider.codec(0.0F, 2.0F).fieldOf("wetness").forGetter(SpeleothemClusterConfiguration::wetness),
        Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_speleothem_column_at_max_distance_from_center").forGetter(SpeleothemClusterConfiguration::chanceOfSpeleothemColumnAtMaxDistanceFromCenter),
        Codec.intRange(1, 64).fieldOf("max_distance_from_edge_affecting_chance_of_speleothem_column").forGetter(SpeleothemClusterConfiguration::maxDistanceFromEdgeAffectingChanceOfSpeleothemColumn),
        Codec.intRange(1, 64).fieldOf("max_distance_from_center_affecting_height_bias").forGetter(SpeleothemClusterConfiguration::maxDistanceFromCenterAffectingHeightBias),
        SpeleothemClusterFeature.PlacementOptions.CODEC.optionalFieldOf("placement_options", SpeleothemClusterFeature.PlacementOptions.DEFAULT).forGetter(SpeleothemClusterConfiguration::placementOptions)
    ).apply(instance, SpeleothemClusterConfiguration::new));
    
    public SpeleothemClusterConfiguration(
        BlockState baseBlock,
        BlockState pointedBlock,
        HolderSet<Block> replaceableBlocks,
        int floorToCeilingSearchRange,
        IntProvider height,
        IntProvider radius,
        int maxStalagmiteStalactiteHeightDiff,
        int heightDeviation,
        IntProvider speleothemBlockLayerThickness,
        FloatProvider density,
        FloatProvider wetness,
        float chanceOfSpeleothemAtMaxDistanceFromCenter,
        int maxDistanceFromEdgeAffectingChanceOfSpeleothem,
        int maxDistanceFromCenterAffectingHeightBias
    ) {
        this(
            baseBlock,
            pointedBlock,
            replaceableBlocks,
            floorToCeilingSearchRange,
            height,
            radius,
            maxStalagmiteStalactiteHeightDiff,
            heightDeviation,
            speleothemBlockLayerThickness,
            density,
            wetness,
            chanceOfSpeleothemAtMaxDistanceFromCenter,
            maxDistanceFromEdgeAffectingChanceOfSpeleothem,
            maxDistanceFromCenterAffectingHeightBias,
            SpeleothemClusterFeature.PlacementOptions.DEFAULT
        );
    }
}

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

public record LargeSpeleothemConfiguration(
	BlockState baseBlock,
	HolderSet<Block> replaceableBlocks,
	int floorToCeilingSearchRange,
	IntProvider columnRadius,
	FloatProvider heightScale,
	float maxColumnRadiusToCaveHeightRatio,
	FloatProvider stalactiteBluntness,
	FloatProvider stalagmiteBluntness,
	FloatProvider windSpeed,
	int minRadiusForWind,
	float minBluntnessForWind
) implements FeatureConfiguration {
	public static final Codec<LargeSpeleothemConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
		BlockState.CODEC.fieldOf("base_block").forGetter(LargeSpeleothemConfiguration::baseBlock),
		RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("replaceable_blocks").forGetter(LargeSpeleothemConfiguration::replaceableBlocks),
		Codec.intRange(1, 512).fieldOf("floor_to_ceiling_search_range").orElse(30).forGetter(LargeSpeleothemConfiguration::floorToCeilingSearchRange),
		IntProvider.codec(1, 60).fieldOf("column_radius").forGetter(LargeSpeleothemConfiguration::columnRadius),
		FloatProvider.codec(0.0F, 20.0F).fieldOf("height_scale").forGetter(LargeSpeleothemConfiguration::heightScale),
		Codec.floatRange(0.1F, 1.0F).fieldOf("max_column_radius_to_cave_height_ratio").forGetter(LargeSpeleothemConfiguration::maxColumnRadiusToCaveHeightRatio),
		FloatProvider.codec(0.1F, 10.0F).fieldOf("stalactite_bluntness").forGetter(LargeSpeleothemConfiguration::stalactiteBluntness),
		FloatProvider.codec(0.1F, 10.0F).fieldOf("stalagmite_bluntness").forGetter(LargeSpeleothemConfiguration::stalagmiteBluntness),
		FloatProvider.codec(0.0F, 2.0F).fieldOf("wind_speed").forGetter(LargeSpeleothemConfiguration::windSpeed),
		Codec.intRange(0, 100).fieldOf("min_radius_for_wind").forGetter(LargeSpeleothemConfiguration::minRadiusForWind),
		Codec.floatRange(0.0F, 5.0F).fieldOf("min_bluntness_for_wind").forGetter(LargeSpeleothemConfiguration::minBluntnessForWind)
	).apply(instance, LargeSpeleothemConfiguration::new));
}

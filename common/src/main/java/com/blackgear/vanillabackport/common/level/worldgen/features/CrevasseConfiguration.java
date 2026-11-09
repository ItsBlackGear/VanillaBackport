package com.blackgear.vanillabackport.common.level.worldgen.features;

import com.blackgear.vanillabackport.core.util.codec.AdditionalCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record CrevasseConfiguration(
    int maxWidth,
    int maxLength,
    int minFootprint,
    int cursorSurfaceSearchRange,
    int columnSurfaceSearchRange,
    float twistiness,
    float tapering,
    float turnFactor,
    IntProvider carveDepth,
    IntProvider fillDepth,
    BlockStateProvider surfaceBlock,
    BlockStateProvider fillBlock,
    BlockPredicate carvableBlocks,
    BlockPredicate acceptableBarrier
) implements FeatureConfiguration {
    public static final Codec<CrevasseConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.intRange(2, 8).fieldOf("max_width").forGetter(CrevasseConfiguration::maxWidth),
        ExtraCodecs.intRange(4, 16).fieldOf("max_length").forGetter(CrevasseConfiguration::maxLength),
        ExtraCodecs.intRange(1, 32).fieldOf("min_footprint").forGetter(CrevasseConfiguration::minFootprint),
        ExtraCodecs.POSITIVE_INT.fieldOf("cursor_surface_search_range").forGetter(CrevasseConfiguration::cursorSurfaceSearchRange),
        ExtraCodecs.POSITIVE_INT.fieldOf("column_surface_search_range").forGetter(CrevasseConfiguration::columnSurfaceSearchRange),
        AdditionalCodecs.floatRange(0.0F, 16.0F).fieldOf("twistiness").forGetter(CrevasseConfiguration::twistiness),
        AdditionalCodecs.floatRange(0.0F, 1.0F).fieldOf("tapering").forGetter(CrevasseConfiguration::tapering),
        AdditionalCodecs.floatRange(0.0F, 1.0F).fieldOf("turn_factor").forGetter(CrevasseConfiguration::turnFactor),
        IntProvider.POSITIVE_CODEC.fieldOf("carve_depth").forGetter(CrevasseConfiguration::carveDepth),
        IntProvider.POSITIVE_CODEC.fieldOf("fill_depth").forGetter(CrevasseConfiguration::fillDepth),
        BlockStateProvider.CODEC.fieldOf("surface_block").forGetter(CrevasseConfiguration::surfaceBlock),
        BlockStateProvider.CODEC.fieldOf("fill_block").forGetter(CrevasseConfiguration::fillBlock),
        BlockPredicate.CODEC.fieldOf("carvable_blocks").forGetter(CrevasseConfiguration::carvableBlocks),
        BlockPredicate.CODEC.fieldOf("acceptable_barrier").forGetter(CrevasseConfiguration::acceptableBarrier)
    ).apply(instance, CrevasseConfiguration::new));
}
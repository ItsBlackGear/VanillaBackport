package com.blackgear.vanillabackport.common.level.worldgen.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public record LeafLitterConfiguration(int tries, int radius, int height, BlockStateProvider blockStateProvider) implements FeatureConfiguration {
    public static final Codec<LeafLitterConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.POSITIVE_INT.fieldOf("tries").orElse(128).forGetter(decorator -> decorator.tries),
        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("radius").orElse(2).forGetter(decorator -> decorator.radius),
        ExtraCodecs.NON_NEGATIVE_INT.fieldOf("height").orElse(1).forGetter(decorator -> decorator.height),
        BlockStateProvider.CODEC.fieldOf("block_state_provider").forGetter(decorator -> decorator.blockStateProvider)
    ).apply(instance, LeafLitterConfiguration::new));
}
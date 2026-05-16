package com.blackgear.vanillabackport.common.level.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.stream.Stream;

public record WeightedRandomFeatureConfiguration(SimpleWeightedRandomList<Holder<PlacedFeature>> features) implements FeatureConfiguration {
    public static final Codec<WeightedRandomFeatureConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        SimpleWeightedRandomList.wrappedCodecAllowingEmpty(PlacedFeature.CODEC).fieldOf("features").forGetter(WeightedRandomFeatureConfiguration::features)
    ).apply(instance, WeightedRandomFeatureConfiguration::new));

    @Override
    public Stream<ConfiguredFeature<?, ?>> getFeatures() {
        return this.features.unwrap().stream().flatMap(weighted -> weighted.getData().value().getFeatures());
    }
}
package com.blackgear.vanillabackport.common.level.worldgen.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class SequenceFeature extends Feature<SimpleRandomFeatureConfiguration> {
    public SequenceFeature(Codec<SimpleRandomFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<SimpleRandomFeatureConfiguration> context) {
        for (Holder<PlacedFeature> feature : context.config().features) {
            if (!feature.value().place(context.level(), context.chunkGenerator(), context.random(), context.origin())) {
                return false;
            }
        }

        return true;
    }
}

package com.blackgear.vanillabackport.common.level.worldgen.features;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.Optional;

public class WeightedRandomSelectorFeature extends Feature<WeightedRandomFeatureConfiguration> {
    public WeightedRandomSelectorFeature(Codec<WeightedRandomFeatureConfiguration> codec) {
        super(codec);
    }

    @Override
    public boolean place(FeaturePlaceContext<WeightedRandomFeatureConfiguration> context) {
        WeightedRandomFeatureConfiguration config = context.config();
        RandomSource random = context.random();
        WorldGenLevel level = context.level();
        ChunkGenerator generator = context.chunkGenerator();
        BlockPos origin = context.origin();
        Optional<Holder<PlacedFeature>> feature = config.features().getRandomValue(random);
        return feature.map(holder -> holder.value().place(level, generator, random, origin)).orElse(false);
    }
}

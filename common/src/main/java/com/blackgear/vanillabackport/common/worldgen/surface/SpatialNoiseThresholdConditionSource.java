package com.blackgear.vanillabackport.common.worldgen.surface;

import com.blackgear.vanillabackport.common.api.worldgen.ContextNoiseSampler;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.synth.NormalNoise.NoiseParameters;

import java.util.function.DoubleSupplier;

public class SpatialNoiseThresholdConditionSource implements SurfaceRules.ConditionSource {
    private static final MapCodec<SpatialNoiseThresholdConditionSource> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ResourceKey.codec(Registries.NOISE).fieldOf("noise").forGetter(condition -> condition.noise),
        Codec.DOUBLE.fieldOf("min_threshold").forGetter(condition -> condition.minThreshold),
        Codec.DOUBLE.fieldOf("max_threshold").forGetter(condition -> condition.maxThreshold)
    ).apply(instance, SpatialNoiseThresholdConditionSource::new));
    public static final KeyDispatchDataCodec<SpatialNoiseThresholdConditionSource> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    private final ResourceKey<NoiseParameters> noise;
    private final double minThreshold;
    private final double maxThreshold;


    public SpatialNoiseThresholdConditionSource(ResourceKey<NoiseParameters> noise, double minThreshold, double maxThreshold) {
        this.noise = noise;
        this.minThreshold = minThreshold;
        this.maxThreshold = maxThreshold;
    }

    @Override
    public KeyDispatchDataCodec<? extends SurfaceRules.ConditionSource> codec() {
        return CODEC;
    }

    @Override
    public SurfaceRules.Condition apply(SurfaceRules.Context context) {
        DoubleSupplier noise = ((ContextNoiseSampler) (Object) context).getNoiseSampler3D(this.noise);
        return new NoiseThresholdCondition(this, noise);
    }

    private record NoiseThresholdCondition(SpatialNoiseThresholdConditionSource source, DoubleSupplier noise) implements SurfaceRules.Condition {
        @Override
        public boolean test() {
            double value = noise.getAsDouble();
            return value >= source.minThreshold && value <= source.maxThreshold;
        }
    }
}
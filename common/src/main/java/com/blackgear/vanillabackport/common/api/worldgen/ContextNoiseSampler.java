package com.blackgear.vanillabackport.common.api.worldgen;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

import java.util.function.DoubleSupplier;

public interface ContextNoiseSampler {
    DoubleSupplier getNoiseSampler3D(ResourceKey<NormalNoise.NoiseParameters> noise);
}
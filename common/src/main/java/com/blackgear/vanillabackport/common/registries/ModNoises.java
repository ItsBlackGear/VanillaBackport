package com.blackgear.vanillabackport.common.registries;

import com.blackgear.platform.core.api.registrar.bootstrap.NoiseRegistrar;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.synth.NormalNoise.NoiseParameters;

public class ModNoises {
    public static final NoiseRegistrar NOISES = NoiseRegistrar.create(VanillaBackport.NAMESPACE);

    public static final ResourceKey<NoiseParameters> SULFUR_CAVE_GRADIENT = NOISES.register("sulfur_cave_gradient", -5, 1.0, 0.0, 1.0);
}
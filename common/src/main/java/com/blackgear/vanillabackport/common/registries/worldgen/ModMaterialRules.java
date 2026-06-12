package com.blackgear.vanillabackport.common.registries.worldgen;

import com.blackgear.platform.core.api.registrar.Registrar;
import com.blackgear.vanillabackport.common.level.worldgen.surface.NoiseGradientRuleSource;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.SurfaceRules;

public class ModMaterialRules {
    public static final Registrar<MapCodec<? extends SurfaceRules.RuleSource>> REGISTRIES = Registrar.create(Registries.MATERIAL_RULE, VanillaBackport.NAMESPACE);

    public static final MapCodec<NoiseGradientRuleSource> NOISE_GRADIENT = REGISTRIES.register("noise_gradient", NoiseGradientRuleSource.CODEC.codec());
}
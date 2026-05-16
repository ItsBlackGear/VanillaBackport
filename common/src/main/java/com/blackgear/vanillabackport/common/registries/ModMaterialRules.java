package com.blackgear.vanillabackport.common.registries;

import com.blackgear.platform.core.api.registrar.Registrar;
import com.blackgear.vanillabackport.common.worldgen.surface.NoiseGradientRuleSource;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.SurfaceRules;

public class ModMaterialRules {
    public static final Registrar<Codec<? extends SurfaceRules.RuleSource>> MATERIALS = Registrar.create(Registries.MATERIAL_RULE, VanillaBackport.NAMESPACE);

    public static final Codec<NoiseGradientRuleSource> NOISE_GRADIENT = MATERIALS.register("noise_gradient", NoiseGradientRuleSource.CODEC.codec());
}
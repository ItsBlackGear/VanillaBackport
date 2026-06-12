package com.blackgear.vanillabackport.common.registries.entities;

import com.blackgear.platform.core.api.registrar.bootstrap.BootstrapRegistrar;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageType;

public class ModDamageTypes {
    public static final BootstrapRegistrar<DamageType> REGISTRIES = BootstrapRegistrar.create(Registries.DAMAGE_TYPE, VanillaBackport.NAMESPACE);

    public static final ResourceKey<DamageType> SULFUR_CUBE_HOT = REGISTRIES.register("sulfur_cube_hot",
        context -> new DamageType("sulfurCubeHot", 0.1F, DamageEffects.BURNING)
    );
}
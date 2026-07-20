package com.blackgear.vanillabackport.common.registries.entities;

import com.blackgear.platform.core.api.registrar.Registrar;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.mixin.common.access.MobEffectAccessor;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class ModMobEffects {
    public static final Registrar<MobEffect> REGISTRIES = Registrar.create(Registries.MOB_EFFECT, VanillaBackport.NAMESPACE);
    
    public static final MobEffect BREATH_OF_THE_NAUTILUS = REGISTRIES.register("breath_of_the_nautilus",
        MobEffectAccessor.createMobEffect(MobEffectCategory.BENEFICIAL, 65518)
    );
}
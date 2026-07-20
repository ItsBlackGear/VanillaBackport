package com.blackgear.vanillabackport.common.registries.entities;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.mixin.common.access.MobEffectAccessor;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class ModMobEffects {
    public static final CoreRegistry<MobEffect> REGISTRIES = CoreRegistry.create(Registries.MOB_EFFECT, VanillaBackport.NAMESPACE);
    
    public static final Holder<MobEffect> BREATH_OF_THE_NAUTILUS = REGISTRIES.holder("breath_of_the_nautilus",
        () -> MobEffectAccessor.createMobEffect(MobEffectCategory.BENEFICIAL, 65518));
}
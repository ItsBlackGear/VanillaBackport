package com.blackgear.vanillabackport.common.registries.entities;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.level.effect.FreezingMobEffect;
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
    
    public static final Holder<MobEffect> FREEZING = REGISTRIES.holder("freezing",
        () -> new FreezingMobEffect(MobEffectCategory.HARMFUL, 10415854).withSoundOnAdded(ModSoundEvents.APPLY_EFFECT_FREEZING.get()));
}
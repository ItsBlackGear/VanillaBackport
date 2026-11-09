package com.blackgear.vanillabackport.common.registries.items;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.common.registries.entities.ModMobEffects;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;

public class ModPotions {
    public static final CoreRegistry<Potion> REGISTRIES = CoreRegistry.create(Registries.POTION, ResourceLocation.DEFAULT_NAMESPACE);
    
    public static final Holder<Potion> FREEZING = REGISTRIES.holder("freezing",
        () -> new Potion("freezing", new MobEffectInstance(ModMobEffects.FREEZING, 900)));
    public static final Holder<Potion> LONG_FREEZING = REGISTRIES.holder("long_freezing",
        () -> new Potion("freezing", new MobEffectInstance(ModMobEffects.FREEZING, 1800)));
}
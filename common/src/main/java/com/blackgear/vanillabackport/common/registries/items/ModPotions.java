package com.blackgear.vanillabackport.common.registries.items;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.common.registries.entities.ModMobEffects;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;

import java.util.function.Supplier;

public class ModPotions {
    public static final CoreRegistry<Potion> REGISTRIES = CoreRegistry.create(Registries.POTION, ResourceLocation.DEFAULT_NAMESPACE);
    
    public static final Supplier<Potion> FREEZING = REGISTRIES.register("freezing",
        () -> new Potion("freezing", new MobEffectInstance(ModMobEffects.FREEZING, 900)));
    public static final Supplier<Potion> LONG_FREEZING = REGISTRIES.register("long_freezing",
        () -> new Potion("freezing", new MobEffectInstance(ModMobEffects.FREEZING, 1800)));
}
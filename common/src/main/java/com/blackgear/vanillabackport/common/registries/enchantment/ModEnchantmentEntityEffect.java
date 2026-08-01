package com.blackgear.vanillabackport.common.registries.enchantment;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.common.level.item.enchantment.effects.ApplyEntityImpulse;
import com.blackgear.vanillabackport.common.level.item.enchantment.effects.ApplyExhaustion;
import com.blackgear.vanillabackport.common.level.item.enchantment.effects.PlaySoundEffect;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;

public class ModEnchantmentEntityEffect {
    public static final CoreRegistry<MapCodec<? extends EnchantmentEntityEffect>> REGISTRIES = CoreRegistry.create(Registries.ENCHANTMENT_ENTITY_EFFECT_TYPE, VanillaBackport.MOD_ID);
    
    static {
        REGISTRIES.register("apply_entity_impulse", () -> ApplyEntityImpulse.CODEC);
        REGISTRIES.register("apply_exhaustion", () -> ApplyExhaustion.CODEC);
        REGISTRIES.register("play_sound", () -> PlaySoundEffect.CODEC);
    }
}
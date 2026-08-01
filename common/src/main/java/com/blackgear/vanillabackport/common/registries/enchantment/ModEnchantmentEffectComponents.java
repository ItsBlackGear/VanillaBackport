package com.blackgear.vanillabackport.common.registries.enchantment;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.effects.EnchantmentEntityEffect;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

public class ModEnchantmentEffectComponents {
    public static final CoreRegistry<DataComponentType<?>> REGISTRIES = CoreRegistry.create(BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE, VanillaBackport.NAMESPACE);
    
    public static final Supplier<DataComponentType<List<ConditionalEffect<EnchantmentEntityEffect>>>> POST_PIERCING_ATTACK = register("post_piercing_attack",
        builder -> builder.persistent(ConditionalEffect.codec(EnchantmentEntityEffect.CODEC, LootContextParamSets.ENCHANTED_DAMAGE).listOf()));
    
    public static <T> Supplier<DataComponentType<T>> register(String key, UnaryOperator<DataComponentType.Builder<T>> operator) {
        return REGISTRIES.register(key, () -> operator.apply(DataComponentType.builder()).build());
    }
}
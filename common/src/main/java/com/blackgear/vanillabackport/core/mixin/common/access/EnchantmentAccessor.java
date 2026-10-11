package com.blackgear.vanillabackport.core.mixin.common.access;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.enchantment.ConditionalEffect;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;
import java.util.function.Consumer;

@Mixin(Enchantment.class)
public interface EnchantmentAccessor {
    @Invoker
    static <T> void callApplyEffects(List<ConditionalEffect<T>> effects, LootContext context, Consumer<T> applier) {
        throw new UnsupportedOperationException();
    }
    
    @Invoker
    static LootContext callEntityContext(ServerLevel level, int enchantmentLevel, Entity entity, Vec3 origin) {
        throw new UnsupportedOperationException();
    }
}

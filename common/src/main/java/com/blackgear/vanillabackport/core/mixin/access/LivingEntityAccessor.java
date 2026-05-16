package com.blackgear.vanillabackport.core.mixin.access;

import net.minecraft.core.NonNullList;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(LivingEntity.class)
public interface LivingEntityAccessor {
    @Accessor("jumping")
    boolean isJumping();

    @Accessor("lastArmorItemStacks")
    NonNullList<ItemStack> getLastArmorItemStacks();

    @Invoker
    SoundEvent callGetFallDamageSound(int height);
}
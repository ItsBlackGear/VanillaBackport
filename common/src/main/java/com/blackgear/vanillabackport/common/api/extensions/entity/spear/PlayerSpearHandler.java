package com.blackgear.vanillabackport.common.api.extensions.entity.spear;

import net.minecraft.world.item.ItemStack;

public interface PlayerSpearHandler {
    default boolean vb$cannotAttackWithItem(ItemStack stack, int tolerance) {
        return false;
    }
    
    default float vb$getItemSwapScale(float scale) {
        return 0.0F;
    }
}
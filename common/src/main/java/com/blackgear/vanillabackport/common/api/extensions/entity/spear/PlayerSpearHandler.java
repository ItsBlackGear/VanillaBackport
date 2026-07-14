package com.blackgear.vanillabackport.common.api.extensions.entity.spear;

import net.minecraft.world.item.ItemStack;

public interface PlayerSpearHandler {
    default boolean cannotAttackWithItem(ItemStack stack, int tolerance) {
        return false;
    }
    
    default float getItemSwapScale(float scale) {
        return 0.0F;
    }
}
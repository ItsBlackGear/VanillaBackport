package com.blackgear.vanillabackport.common.api.extensions.entity.spear;

import net.minecraft.world.item.ItemStack;

public interface PlayerSpearHandler {
    boolean vb$cannotAttackWithItem(ItemStack stack, int tolerance);

    float vb$getItemSwapScale(float scale);
}
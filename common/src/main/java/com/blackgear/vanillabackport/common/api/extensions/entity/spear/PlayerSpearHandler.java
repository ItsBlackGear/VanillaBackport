package com.blackgear.vanillabackport.common.api.extensions.entity.spear;

import net.minecraft.world.item.ItemStack;

public interface PlayerSpearHandler {
    boolean cannotAttackWithItem(ItemStack stack, int tolerance);

    float getItemSwapScale(float scale);
}
package com.blackgear.vanillabackport.common.level.items.spear;

import net.minecraft.world.item.ItemStack;

public record UseEffects(
    boolean canSprint,
    boolean interactVibrations,
    float speedMultiplier
) {
    public static final UseEffects DEFAULT = new UseEffects(false, true, 0.2F);
    
    public static UseEffects getUseEffects(ItemStack stack) {
        return stack.getItem() instanceof SpearItem spear ? spear.getUseEffects() : DEFAULT;
    }
}
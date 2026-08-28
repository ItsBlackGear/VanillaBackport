package com.blackgear.vanillabackport.common.level.items.spear;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public record SwingAnimation(SwingAnimationType type, int duration) {
    public static final SwingAnimation DEFAULT = new SwingAnimation(SwingAnimationType.WHACK, 6);
    
    public static @Nullable SwingAnimation getSwingAnimation(ItemStack stack) {
        return stack.getItem() instanceof SpearItem spear ? spear.getSwingAnimation() : null;
    }
}
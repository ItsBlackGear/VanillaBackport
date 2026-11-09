package com.blackgear.vanillabackport.common.api.extensions.enchantment;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;

public interface FallBasedEnchantment {
    default float getFallBasedDamageBonus(int level, ItemStack item, Entity entity) {
        return 0.0F;
    }
    
    default float getReduceArmorEffectivenessAmount(int level, ItemStack item, Entity entity) {
        return 0.0F;
    }
}
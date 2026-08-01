package com.blackgear.vanillabackport.common.level.item.enchantment;

import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class LungeEnchantment extends Enchantment {
    public LungeEnchantment(Rarity rarity, EquipmentSlot... slots) {
        super(rarity, EnchantmentCategory.WEAPON, slots);
    }
    
    @Override
    public int getMinCost(int level) {
        return 5 + (level - 1) * 8;
    }
    
    @Override
    public int getMaxCost(int level) {
        return this.getMinCost(level) + 25;
    }
    
    @Override
    public int getMaxLevel() {
        return 3;
    }
    
    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.is(ModItemTags.LUNGE_ENCHANTABLE);
    }
}
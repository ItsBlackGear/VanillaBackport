package com.blackgear.vanillabackport.common.level.items.enchantment;

import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class WindBurstEnchantment extends Enchantment {
    public WindBurstEnchantment(Rarity rarity, EquipmentSlot... slots) {
        super(rarity, EnchantmentCategory.WEAPON, slots);
    }
    
    @Override
    public int getMinCost(int level) {
        return 15 + ((level - 1) * 9);
    }
    
    @Override
    public int getMaxCost(int level) {
        return 65 + ((level - 1) * 9);
    }
    
    @Override
    public int getMaxLevel() {
        return 3;
    }
    
    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.is(ModItemTags.MACE_ENCHANTABLE);
    }
    
    @Override
    public boolean isTreasureOnly() {
        return true;
    }
}
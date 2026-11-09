package com.blackgear.vanillabackport.common.level.items.enchantment;

import com.blackgear.vanillabackport.common.api.extensions.enchantment.FallBasedEnchantment;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class DensityEnchantment extends Enchantment implements FallBasedEnchantment {
    public DensityEnchantment(Rarity rarity, EquipmentSlot... slots) {
        super(rarity, EnchantmentCategory.WEAPON, slots);
    }
    
    @Override
    public int getMinCost(int level) {
        return 5 + ((level - 1) * 8);
    }
    
    @Override
    public int getMaxCost(int level) {
        return 25 + ((level - 1) * 8);
    }
    
    @Override
    public int getMaxLevel() {
        return 5;
    }
    
    @Override
    public boolean canEnchant(ItemStack stack) {
        return stack.is(ModItemTags.MACE_ENCHANTABLE);
    }
    
    @Override
    protected boolean checkCompatibility(Enchantment other) {
        return !(other instanceof DamageEnchantment || other instanceof BreachEnchantment) && super.checkCompatibility(other);
    }
    
    @Override
    public float getFallBasedDamageBonus(int level, ItemStack item, Entity entity) {
        return level * 0.5F;
    }
}
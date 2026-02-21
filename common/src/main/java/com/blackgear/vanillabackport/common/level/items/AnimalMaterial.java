package com.blackgear.vanillabackport.common.level.items;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.crafting.Ingredient;

public interface AnimalMaterial {
    int getDurability();

    int getDefense();

    int getEnchantmentValue();

    SoundEvent getEquipSound();

    Ingredient getRepairIngredient();

    String getName();

    ResourceLocation getLocation();

    float getToughness();

    float getKnockbackResistance();
}

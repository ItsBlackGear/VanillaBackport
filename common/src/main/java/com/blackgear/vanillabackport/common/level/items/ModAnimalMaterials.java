package com.blackgear.vanillabackport.common.level.items;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.Objects;
import java.util.function.Supplier;

public enum ModAnimalMaterials implements AnimalMaterial {
    ARMADILLO_SCUTE(ModArmorMaterials.ARMADILLO_SCUTE, "armadillo_scute", 11);

    private final String name;
    private final ResourceLocation location;
    private final int durability;
    private final int defense;
    private final int enchantmentValue;
    private final SoundEvent sound;
    private final float toughness;
    private final float knockbackResistance;
    private final Supplier<Ingredient> ingredient;

    private ModAnimalMaterials(String name, int durabilityMultiplier, int defense, int enchantmentValue, SoundEvent sound, float toughness, float knockbackResistance, Supplier ingredient) {
        this.name = name;
        this.location = new ResourceLocation(name);
        this.durability = durabilityMultiplier * 16;
        this.defense = defense;
        this.enchantmentValue = enchantmentValue;
        this.sound = sound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.ingredient = ingredient;
    }

    private ModAnimalMaterials(ArmorMaterial material, String name, int defense) {
        this.name = name;
        this.location = new ResourceLocation(name);
        this.durability = material.getDurabilityForType(ArmorItem.Type.CHESTPLATE);
        this.defense = defense;
        this.enchantmentValue = material.getEnchantmentValue();
        this.sound = material.getEquipSound();
        this.toughness = material.getToughness();
        this.knockbackResistance = material.getKnockbackResistance();
        Objects.requireNonNull(material);
        this.ingredient = material::getRepairIngredient;
    }

    public int getDurability() {
        return this.durability;
    }

    public int getDefense() {
        return this.defense;
    }

    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }

    public SoundEvent getEquipSound() {
        return this.sound;
    }

    public Ingredient getRepairIngredient() {
        return (Ingredient)this.ingredient.get();
    }

    public String getName() {
        return this.name;
    }

    public ResourceLocation getLocation() {
        return this.location;
    }

    public float getToughness() {
        return this.toughness;
    }

    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}

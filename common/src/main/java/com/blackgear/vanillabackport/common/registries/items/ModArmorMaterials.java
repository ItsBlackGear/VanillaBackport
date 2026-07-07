package com.blackgear.vanillabackport.common.registries.items;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.LazyLoadedValue;
import net.minecraft.world.item.ArmorItem.Type;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.function.Supplier;

public enum ModArmorMaterials implements ArmorMaterial {
    COPPER("copper", 11, Util.make(new EnumMap<>(Type.class), map -> {
        map.put(Type.BOOTS, 1);
        map.put(Type.LEGGINGS, 3);
        map.put(Type.CHESTPLATE, 4);
        map.put(Type.HELMET, 2);
    }), 8, ModSoundEvents.ARMOR_EQUIP_COPPER.get(), 0.0F, 0.0F, () -> Ingredient.of(Items.COPPER_INGOT));
    
    private final String name;
    private final int durabilityMultiplier;
    private final EnumMap<Type, Integer> protectionAmounts;
    private final int enchantmentValue;
    private final SoundEvent equipSound;
    private final float toughness;
    private final float knockbackResistance;
    private final LazyLoadedValue<Ingredient> repairIngredient;
    
    private static final EnumMap<Type, Integer> HEALTH_FUNCTION_FOR_TYPE = Util.make(new EnumMap<>(Type.class), enumMap -> {
        enumMap.put(Type.BOOTS, 13);
        enumMap.put(Type.LEGGINGS, 15);
        enumMap.put(Type.CHESTPLATE, 16);
        enumMap.put(Type.HELMET, 11);
    });
    
    ModArmorMaterials(
        String name,
        int durabilityMultiplier,
        EnumMap<Type, Integer> protectionAmounts,
        int enchantmentValue,
        SoundEvent equipSound,
        float toughness,
        float knockbackResistance,
        Supplier<Ingredient> repairIngredient
    ) {
        this.name = name;
        this.durabilityMultiplier = durabilityMultiplier;
        this.protectionAmounts = protectionAmounts;
        this.enchantmentValue = enchantmentValue;
        this.equipSound = equipSound;
        this.toughness = toughness;
        this.knockbackResistance = knockbackResistance;
        this.repairIngredient = new LazyLoadedValue<>(repairIngredient);
    }
    
    @Override
    public int getDurabilityForType(Type type) {
        return HEALTH_FUNCTION_FOR_TYPE.get(type) * this.durabilityMultiplier;
    }
    
    @Override
    public int getDefenseForType(Type type) {
        return this.protectionAmounts.get(type);
    }
    
    @Override
    public int getEnchantmentValue() {
        return this.enchantmentValue;
    }
    
    @Override
    public SoundEvent getEquipSound() {
        return this.equipSound;
    }
    
    @Override
    public Ingredient getRepairIngredient() {
        return this.repairIngredient.get();
    }
    
    @Override
    public String getName() {
        return this.name;
    }
    
    @Override
    public float getToughness() {
        return this.toughness;
    }
    
    @Override
    public float getKnockbackResistance() {
        return this.knockbackResistance;
    }
}
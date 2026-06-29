package com.blackgear.vanillabackport.common.registries.items;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.EnumMap;
import java.util.List;
import java.util.function.Supplier;

public class ModArmorMaterials {
    public static final CoreRegistry<ArmorMaterial> REGISTRIES = CoreRegistry.create(BuiltInRegistries.ARMOR_MATERIAL, VanillaBackport.NAMESPACE);
    
    public static final Holder<ArmorMaterial> COPPER = register("copper",
        Util.make(new EnumMap<>(ArmorItem.Type.class), attributes -> {
            attributes.put(ArmorItem.Type.BOOTS, 1);
            attributes.put(ArmorItem.Type.LEGGINGS, 3);
            attributes.put(ArmorItem.Type.CHESTPLATE, 4);
            attributes.put(ArmorItem.Type.HELMET, 2);
            attributes.put(ArmorItem.Type.BODY, 4);
        }),
        8,
        ModSoundEvents.ARMOR_EQUIP_COPPER,
        0.0F,
        0.0F,
        () -> Ingredient.of(Items.COPPER_INGOT)
    );
    
    public static Holder<ArmorMaterial> register(
        String name,
        EnumMap<ArmorItem.Type, Integer> defense,
        int enchantmentValue,
        Holder<SoundEvent> equipSound,
        float toughness,
        float knockbackResistance,
        Supplier<Ingredient> repairIngredient
    ) {
        List<ArmorMaterial.Layer> layers = List.of(new ArmorMaterial.Layer(ResourceLocation.withDefaultNamespace(name)));
        return register(name, defense, enchantmentValue, equipSound, toughness, knockbackResistance, repairIngredient, layers);
    }
    
    private static Holder<ArmorMaterial> register(
        String name,
        EnumMap<ArmorItem.Type, Integer> defense,
        int enchantmentValue,
        Holder<SoundEvent> equipSound,
        float toughness,
        float knockbackResistance,
        Supplier<Ingredient> repairIngredient,
        List<ArmorMaterial.Layer> layers
    ) {
        return REGISTRIES.holder(name,
            () -> new ArmorMaterial(defense, enchantmentValue, equipSound, repairIngredient, layers, toughness, knockbackResistance));
    }
}
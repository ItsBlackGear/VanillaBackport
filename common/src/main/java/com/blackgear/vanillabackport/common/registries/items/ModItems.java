package com.blackgear.vanillabackport.common.registries.items;

import com.blackgear.platform.core.helper.ItemRegistry;
import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.chicken.ChickenVariants;
import com.blackgear.vanillabackport.common.level.item.*;
import com.blackgear.vanillabackport.common.level.item.spear.SpearItem;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.registries.experimental.FeatureHolder;
import com.blackgear.vanillabackport.core.registries.experimental.handlers.VanillaItemRegistry;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.Item.Properties;

import java.util.UUID;
import java.util.function.Supplier;

public class ModItems {
    public static final ItemRegistry REGISTRIES = ItemRegistry.create(VanillaBackport.NAMESPACE);
    public static final VanillaItemRegistry HOLDERS = VanillaItemRegistry.create();
    
    // Armored Paws
    
    public static final Supplier<Item> ARMADILLO_SCUTE = REGISTRIES.register("armadillo_scute");
    public static final Supplier<Item> ARMADILLO_SPAWN_EGG = REGISTRIES.register("armadillo_spawn_egg",
        () -> ItemRegistry.createSpawnEgg(ModEntityTypes.ARMADILLO, 11366765, 8538184, new Properties()));
    public static final Supplier<Item> WOLF_ARMOR = REGISTRIES.register("wolf_armor", WolfArmorItem::new, new Properties().durability(64));
    
    // Bundles of Bravery
    
    public static final Supplier<Item> WHITE_BUNDLE = REGISTRIES.register("white_bundle",
        BundleItem::new, new Properties().stacksTo(1));
    public static final Supplier<Item> ORANGE_BUNDLE = REGISTRIES.register("orange_bundle",
        BundleItem::new, new Properties().stacksTo(1));
    public static final Supplier<Item> MAGENTA_BUNDLE = REGISTRIES.register("magenta_bundle",
        BundleItem::new, new Properties().stacksTo(1));
    public static final Supplier<Item> LIGHT_BLUE_BUNDLE = REGISTRIES.register("light_blue_bundle",
        BundleItem::new, new Properties().stacksTo(1));
    public static final Supplier<Item> YELLOW_BUNDLE = REGISTRIES.register("yellow_bundle",
        BundleItem::new, new Properties().stacksTo(1));
    public static final Supplier<Item> LIME_BUNDLE = REGISTRIES.register("lime_bundle",
        BundleItem::new, new Properties().stacksTo(1));
    public static final Supplier<Item> PINK_BUNDLE = REGISTRIES.register("pink_bundle",
        BundleItem::new, new Properties().stacksTo(1));
    public static final Supplier<Item> GRAY_BUNDLE = REGISTRIES.register("gray_bundle",
        BundleItem::new, new Properties().stacksTo(1));
    public static final Supplier<Item> LIGHT_GRAY_BUNDLE = REGISTRIES.register("light_gray_bundle",
        BundleItem::new, new Properties().stacksTo(1));
    public static final Supplier<Item> CYAN_BUNDLE = REGISTRIES.register("cyan_bundle",
        BundleItem::new, new Properties().stacksTo(1));
    public static final Supplier<Item> PURPLE_BUNDLE = REGISTRIES.register("purple_bundle",
        BundleItem::new, new Properties().stacksTo(1));
    public static final Supplier<Item> BLUE_BUNDLE = REGISTRIES.register("blue_bundle",
        BundleItem::new, new Properties().stacksTo(1));
    public static final Supplier<Item> BROWN_BUNDLE = REGISTRIES.register("brown_bundle",
        BundleItem::new, new Properties().stacksTo(1));
    public static final Supplier<Item> GREEN_BUNDLE = REGISTRIES.register("green_bundle",
        BundleItem::new, new Properties().stacksTo(1));
    public static final Supplier<Item> RED_BUNDLE = REGISTRIES.register("red_bundle",
        BundleItem::new, new Properties().stacksTo(1));
    public static final Supplier<Item> BLACK_BUNDLE = REGISTRIES.register("black_bundle",
        BundleItem::new, new Properties().stacksTo(1));
    
    // The Garden Awakens
    
    public static final Supplier<Item> PALE_OAK_BOAT = REGISTRIES.register("pale_oak_boat",
        () -> new PaleOakBoatItem(false, new Properties().stacksTo(1)));
    public static final Supplier<Item> PALE_OAK_CHEST_BOAT = REGISTRIES.register("pale_oak_chest_boat",
        () -> new PaleOakBoatItem(true, new Properties().stacksTo(1)));
    
    public static final Supplier<Item> CREAKING_SPAWN_EGG = REGISTRIES.register("creaking_spawn_egg",
        () -> ItemRegistry.createSpawnEgg(ModEntityTypes.CREAKING, 6250335, 16545810, new Properties()));
    
    public static final Supplier<Item> RESIN_BRICK = REGISTRIES.register("resin_brick");
    
    // Spring to Life
    
    public static final Supplier<Item> BLUE_EGG = REGISTRIES.register("blue_egg",
        properties -> new VariantEggItem(ChickenVariants.COLD, properties),
        new Properties().stacksTo(16)
    );
    public static final Supplier<Item> BROWN_EGG = REGISTRIES.register("brown_egg",
        properties -> new VariantEggItem(ChickenVariants.WARM, properties),
        new Properties().stacksTo(16)
    );
    
    // Chase the Skies
    
    public static final Supplier<Item> WHITE_HARNESS = REGISTRIES.register("white_harness",
        new Properties().stacksTo(1));
    public static final Supplier<Item> ORANGE_HARNESS = REGISTRIES.register("orange_harness",
        new Properties().stacksTo(1));
    public static final Supplier<Item> MAGENTA_HARNESS = REGISTRIES.register("magenta_harness",
        new Properties().stacksTo(1));
    public static final Supplier<Item> LIGHT_BLUE_HARNESS = REGISTRIES.register("light_blue_harness",
        new Properties().stacksTo(1));
    public static final Supplier<Item> YELLOW_HARNESS = REGISTRIES.register("yellow_harness",
        new Properties().stacksTo(1));
    public static final Supplier<Item> LIME_HARNESS = REGISTRIES.register("lime_harness",
        new Properties().stacksTo(1));
    public static final Supplier<Item> PINK_HARNESS = REGISTRIES.register("pink_harness",
        new Properties().stacksTo(1));
    public static final Supplier<Item> GRAY_HARNESS = REGISTRIES.register("gray_harness",
        new Properties().stacksTo(1));
    public static final Supplier<Item> LIGHT_GRAY_HARNESS = REGISTRIES.register("light_gray_harness",
        new Properties().stacksTo(1));
    public static final Supplier<Item> CYAN_HARNESS = REGISTRIES.register("cyan_harness",
        new Properties().stacksTo(1));
    public static final Supplier<Item> PURPLE_HARNESS = REGISTRIES.register("purple_harness",
        new Properties().stacksTo(1));
    public static final Supplier<Item> BLUE_HARNESS = REGISTRIES.register("blue_harness",
        new Properties().stacksTo(1));
    public static final Supplier<Item> BROWN_HARNESS = REGISTRIES.register("brown_harness",
        new Properties().stacksTo(1));
    public static final Supplier<Item> GREEN_HARNESS = REGISTRIES.register("green_harness",
        new Properties().stacksTo(1));
    public static final Supplier<Item> RED_HARNESS = REGISTRIES.register("red_harness",
        new Properties().stacksTo(1));
    public static final Supplier<Item> BLACK_HARNESS = REGISTRIES.register("black_harness",
        new Properties().stacksTo(1));
    
    public static final Supplier<Item> MUSIC_DISC_TEARS = REGISTRIES.register("music_disc_tears",
        properties -> new RecordItem(10, ModSoundEvents.MUSIC_DISC_TEARS.get(), properties, 175),
        new Properties()
            .stacksTo(1)
            .rarity(Rarity.UNCOMMON));
    
    public static final Supplier<Item> HAPPY_GHAST_SPAWN_EGG = REGISTRIES.register("happy_ghast_spawn_egg",
        () -> ItemRegistry.createSpawnEgg(ModEntityTypes.HAPPY_GHAST, 16382457, 12369084, new Properties()));
    
    // Hot as Lava
    
    public static final Supplier<Item> MUSIC_DISC_LAVA_CHICKEN = REGISTRIES.register("music_disc_lava_chicken",
        properties -> new RecordItem(9, ModSoundEvents.MUSIC_DISC_LAVA_CHICKEN.get(), properties, 134),
        new Properties()
            .stacksTo(1)
            .rarity(Rarity.RARE));
    
    // Copper Age
    
    public static final FeatureHolder<Item> COPPER_SWORD = HOLDERS.register("copper_sword",
        properties -> new SwordItem(ModToolMaterials.COPPER, 3, -2.4F, properties));
    public static final FeatureHolder<Item> COPPER_SHOVEL = HOLDERS.register("copper_shovel",
        properties -> new ShovelItem(ModToolMaterials.COPPER, 1.5F, -3.0F, properties));
    public static final FeatureHolder<Item> COPPER_PICKAXE = HOLDERS.register("copper_pickaxe",
        properties -> new PickaxeItem(ModToolMaterials.COPPER, 1, -2.8F, properties));
    public static final FeatureHolder<Item> COPPER_AXE = HOLDERS.register("copper_axe",
        properties -> new AxeItem(ModToolMaterials.COPPER, 1.0F, -3.2F, properties));
    public static final FeatureHolder<Item> COPPER_HOE = HOLDERS.register("copper_hoe",
        properties -> new HoeItem(ModToolMaterials.COPPER, 1, -2.0F, properties));
    
    public static final FeatureHolder<Item> COPPER_HELMET = HOLDERS.register("copper_helmet",
        properties -> new ArmorItem(ModArmorMaterials.COPPER, ArmorItem.Type.HELMET, properties));
    public static final FeatureHolder<Item> COPPER_CHESTPLATE = HOLDERS.register("copper_chestplate",
        properties -> new ArmorItem(ModArmorMaterials.COPPER, ArmorItem.Type.CHESTPLATE, properties));
    public static final FeatureHolder<Item> COPPER_LEGGINGS = HOLDERS.register("copper_leggings",
        properties -> new ArmorItem(ModArmorMaterials.COPPER, ArmorItem.Type.LEGGINGS, properties));
    public static final FeatureHolder<Item> COPPER_BOOTS = HOLDERS.register("copper_boots",
        properties -> new ArmorItem(ModArmorMaterials.COPPER, ArmorItem.Type.BOOTS, properties));
    
    public static final FeatureHolder<Item> COPPER_NUGGET = HOLDERS.register("copper_nugget");
    public static final FeatureHolder<Item> COPPER_HORSE_ARMOR = HOLDERS.register("copper_horse_armor",
        properties -> new HorseArmorItem(4, "copper", properties),
        new Properties().stacksTo(1));
    
    public static final Supplier<Item> COPPER_GOLEM_SPAWN_EGG = REGISTRIES.register("copper_golem_spawn_egg",
        () -> ItemRegistry.createSpawnEgg(ModEntityTypes.COPPER_GOLEM, 14052680, 8403233, new Properties()));
    
    // Mounts of Mayhem
    
    public static final Supplier<Item> WOODEN_SPEAR = REGISTRIES.register("wooden_spear",
        properties -> new SpearItem(
            Tiers.WOOD,
            0.65F,
            0.7F,
            0.75F,
            5.0F,
            14.0F,
            10.0F,
            5.1F,
            15.0F,
            4.6F,
            properties));
    public static final Supplier<Item> STONE_SPEAR = REGISTRIES.register("stone_spear",
        properties -> new SpearItem(
            Tiers.STONE,
            0.75F,
            0.82F,
            0.7F,
            4.5F,
            10.0F,
            9.0F,
            5.1F,
            13.75F,
            4.6F,
            properties));
    public static final Supplier<Item> COPPER_SPEAR = REGISTRIES.register("copper_spear",
        properties -> new SpearItem(
            ModToolMaterials.COPPER,
            0.85F,
            0.82F,
            0.65F,
            4.0F,
            9.0F,
            8.25F,
            5.1F,
            12.5F,
            4.6F,
            properties));
    public static final Supplier<Item> IRON_SPEAR = REGISTRIES.register("iron_spear",
        properties -> new SpearItem(
            Tiers.IRON,
            0.95F,
            0.95F,
            0.6F,
            2.5F,
            8.0F,
            6.75F,
            5.1F,
            11.25F,
            4.6F,
            properties));
    public static final Supplier<Item> GOLDEN_SPEAR = REGISTRIES.register("golden_spear",
        properties -> new SpearItem(
            Tiers.GOLD,
            0.95F,
            0.7F,
            0.7F,
            3.5F,
            10.0F,
            8.5F,
            5.1F,
            13.75F,
            4.6F,
            properties));
    public static final Supplier<Item> DIAMOND_SPEAR = REGISTRIES.register("diamond_spear",
        properties -> new SpearItem(
            Tiers.DIAMOND,
            1.05F,
            1.075F,
            0.5F,
            3.0F,
            7.5F,
            6.5F,
            5.1F,
            10.0F,
            4.6F,
            properties));
    public static final Supplier<Item> NETHERITE_SPEAR = REGISTRIES.register("netherite_spear",
        properties -> new SpearItem(
            Tiers.NETHERITE,
            1.15F,
            1.2F,
            0.4F,
            2.5F,
            7.0F,
            5.5F,
            5.1F,
            8.75F,
            4.6F,
            properties.fireResistant()));
    
    public static final Supplier<Item> NETHERITE_HORSE_ARMOR = REGISTRIES.register("netherite_horse_armor",
        properties -> new HorseArmorItem(19, "netherite", properties) {
            @Override
            public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
                UUID uuid = UUID.fromString("9F3D476D-C118-4544-8365-64846904B48E");
                ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
                builder.put(Attributes.ARMOR_TOUGHNESS, new AttributeModifier(uuid, "Armor toughness", ArmorMaterials.NETHERITE.getToughness(), AttributeModifier.Operation.ADDITION));
                builder.put(Attributes.KNOCKBACK_RESISTANCE, new AttributeModifier(uuid, "Armor knockback resistance", ArmorMaterials.NETHERITE.getKnockbackResistance(), AttributeModifier.Operation.ADDITION));
                return slot == EquipmentSlot.CHEST ? builder.build() : super.getDefaultAttributeModifiers(slot);
            }
        },
        new Item.Properties().stacksTo(1).fireResistant());
    
    public static final Supplier<Item> PARCHED_SPAWN_EGG = REGISTRIES.register("parched_spawn_egg",
        () -> ItemRegistry.createSpawnEgg(ModEntityTypes.PARCHED, 7630438, 14533518, new Properties()));
    public static final Supplier<Item> CAMEL_HUSK_SPAWN_EGG = REGISTRIES.register("camel_husk_spawn_egg",
        () -> ItemRegistry.createSpawnEgg(ModEntityTypes.CAMEL_HUSK, 7630438, 14533518, new Properties()));
    
    // Chaos Cubed
    
    public static final Supplier<Item> SULFUR_CUBE_BUCKET = REGISTRIES.register("sulfur_cube_bucket",
        properties -> new SulfurCubeBucketItem<>(ModEntityTypes.SULFUR_CUBE, ModSoundEvents.BUCKET_EMPTY_SULFUR_CUBE.get(), properties),
        new Properties().stacksTo(1));
    
    public static final Supplier<Item> MUSIC_DISC_BOUNCE = REGISTRIES.register("music_disc_bounce",
        properties -> new RecordItem(8, ModSoundEvents.MUSIC_DISC_BOUNCE.get(), properties, 234),
        new Properties()
            .stacksTo(1)
            .rarity(Rarity.UNCOMMON));
    public static final Supplier<Item> SULFUR_CUBE_SPAWN_EGG = REGISTRIES.register("sulfur_cube_spawn_egg",
        () -> ItemRegistry.createSpawnEgg(ModEntityTypes.SULFUR_CUBE, 16777215, 8421504, new Properties()));
    
    // Miscellaneous
    
    public static final Supplier<Item> WHITE_CUSHION = REGISTRIES.register("white_cushion",
        properties -> new CushionItem(properties.stacksTo(16), DyeColor.WHITE));
    public static final Supplier<Item> ORANGE_CUSHION = REGISTRIES.register("orange_cushion",
        properties -> new CushionItem(properties.stacksTo(16), DyeColor.ORANGE));
    public static final Supplier<Item> MAGENTA_CUSHION = REGISTRIES.register("magenta_cushion",
        properties -> new CushionItem(properties.stacksTo(16), DyeColor.MAGENTA));
    public static final Supplier<Item> LIGHT_BLUE_CUSHION = REGISTRIES.register("light_blue_cushion",
        properties -> new CushionItem(properties.stacksTo(16), DyeColor.LIGHT_BLUE));
    public static final Supplier<Item> YELLOW_CUSHION = REGISTRIES.register("yellow_cushion",
        properties -> new CushionItem(properties.stacksTo(16), DyeColor.YELLOW));
    public static final Supplier<Item> LIME_CUSHION = REGISTRIES.register("lime_cushion",
        properties -> new CushionItem(properties.stacksTo(16), DyeColor.LIME));
    public static final Supplier<Item> PINK_CUSHION = REGISTRIES.register("pink_cushion",
        properties -> new CushionItem(properties.stacksTo(16), DyeColor.PINK));
    public static final Supplier<Item> GRAY_CUSHION = REGISTRIES.register("gray_cushion",
        properties -> new CushionItem(properties.stacksTo(16), DyeColor.GRAY));
    public static final Supplier<Item> LIGHT_GRAY_CUSHION = REGISTRIES.register("light_gray_cushion",
        properties -> new CushionItem(properties.stacksTo(16), DyeColor.LIGHT_GRAY));
    public static final Supplier<Item> CYAN_CUSHION = REGISTRIES.register("cyan_cushion",
        properties -> new CushionItem(properties.stacksTo(16), DyeColor.CYAN));
    public static final Supplier<Item> PURPLE_CUSHION = REGISTRIES.register("purple_cushion",
        properties -> new CushionItem(properties.stacksTo(16), DyeColor.PURPLE));
    public static final Supplier<Item> BLUE_CUSHION = REGISTRIES.register("blue_cushion",
        properties -> new CushionItem(properties.stacksTo(16), DyeColor.BLUE));
    public static final Supplier<Item> BROWN_CUSHION = REGISTRIES.register("brown_cushion",
        properties -> new CushionItem(properties.stacksTo(16), DyeColor.BROWN));
    public static final Supplier<Item> GREEN_CUSHION = REGISTRIES.register("green_cushion",
        properties -> new CushionItem(properties.stacksTo(16), DyeColor.GREEN));
    public static final Supplier<Item> RED_CUSHION = REGISTRIES.register("red_cushion",
        properties -> new CushionItem(properties.stacksTo(16), DyeColor.RED));
    public static final Supplier<Item> BLACK_CUSHION = REGISTRIES.register("black_cushion",
        properties -> new CushionItem(properties.stacksTo(16), DyeColor.BLACK));
}
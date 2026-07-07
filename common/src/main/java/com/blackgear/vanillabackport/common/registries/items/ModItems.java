package com.blackgear.vanillabackport.common.registries.items;

import com.blackgear.platform.core.helper.ItemRegistry;
import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.chicken.ChickenVariants;
import com.blackgear.vanillabackport.common.level.item.PaleOakBoatItem;
import com.blackgear.vanillabackport.common.level.item.SulfurCubeBucketItem;
import com.blackgear.vanillabackport.common.level.item.VariantEggItem;
import com.blackgear.vanillabackport.common.level.item.WolfArmorItem;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.registries.experimental.FeatureHolder;
import com.blackgear.vanillabackport.core.registries.experimental.handlers.VanillaItemRegistry;
import net.minecraft.world.item.*;
import net.minecraft.world.item.Item.Properties;

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
}
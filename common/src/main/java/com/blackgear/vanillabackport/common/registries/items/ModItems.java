package com.blackgear.vanillabackport.common.registries.items;

import com.blackgear.platform.core.helper.ItemRegistry;
import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.chicken.ChickenVariants;
import com.blackgear.vanillabackport.common.level.items.PaleOakBoatItem;
import com.blackgear.vanillabackport.common.level.items.SulfurCubeBucketItem;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.registries.experimental.FeatureHolder;
import com.blackgear.vanillabackport.core.registries.experimental.handlers.VanillaItemRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.*;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.component.BundleContents;

import java.util.function.Supplier;

public class ModItems {
    public static final ItemRegistry REGISTRIES = ItemRegistry.create(VanillaBackport.NAMESPACE);
    public static final VanillaItemRegistry ITEMS = VanillaItemRegistry.create();

    // Bundles of Bravery
    
    public static final Supplier<Item> WHITE_BUNDLE = REGISTRIES.register("white_bundle",
        BundleItem::new,
        new Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
    public static final Supplier<Item> ORANGE_BUNDLE = REGISTRIES.register("orange_bundle",
        BundleItem::new,
        new Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
    public static final Supplier<Item> MAGENTA_BUNDLE = REGISTRIES.register("magenta_bundle",
        BundleItem::new,
        new Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
    public static final Supplier<Item> LIGHT_BLUE_BUNDLE = REGISTRIES.register("light_blue_bundle",
        BundleItem::new,
        new Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
    public static final Supplier<Item> YELLOW_BUNDLE = REGISTRIES.register("yellow_bundle",
        BundleItem::new,
        new Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
    public static final Supplier<Item> LIME_BUNDLE = REGISTRIES.register("lime_bundle",
        BundleItem::new,
        new Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
    public static final Supplier<Item> PINK_BUNDLE = REGISTRIES.register("pink_bundle",
        BundleItem::new,
        new Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
    public static final Supplier<Item> GRAY_BUNDLE = REGISTRIES.register("gray_bundle",
        BundleItem::new,
        new Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
    public static final Supplier<Item> LIGHT_GRAY_BUNDLE = REGISTRIES.register("light_gray_bundle",
        BundleItem::new,
        new Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
    public static final Supplier<Item> CYAN_BUNDLE = REGISTRIES.register("cyan_bundle",
        BundleItem::new,
        new Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
    public static final Supplier<Item> PURPLE_BUNDLE = REGISTRIES.register("purple_bundle",
        BundleItem::new,
        new Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
    public static final Supplier<Item> BLUE_BUNDLE = REGISTRIES.register("blue_bundle",
        BundleItem::new,
        new Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
    public static final Supplier<Item> BROWN_BUNDLE = REGISTRIES.register("brown_bundle",
        BundleItem::new,
        new Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
    public static final Supplier<Item> GREEN_BUNDLE = REGISTRIES.register("green_bundle",
        BundleItem::new,
        new Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
    public static final Supplier<Item> RED_BUNDLE = REGISTRIES.register("red_bundle",
        BundleItem::new,
        new Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
    public static final Supplier<Item> BLACK_BUNDLE = REGISTRIES.register("black_bundle",
        BundleItem::new,
        new Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
    
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
        () -> new EggItem(new Properties()
            .stacksTo(16)
            .component(ModDataComponents.CHICKEN_VARIANT.get(), ChickenVariants.COLD)));
    public static final Supplier<Item> BROWN_EGG = REGISTRIES.register("brown_egg",
        () -> new EggItem(new Properties()
            .stacksTo(16)
            .component(ModDataComponents.CHICKEN_VARIANT.get(), ChickenVariants.WARM)));

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
        new Properties()
            .stacksTo(1)
            .rarity(Rarity.UNCOMMON)
            .jukeboxPlayable(ModJukeboxSongs.TEARS));
    
    public static final Supplier<Item> HAPPY_GHAST_SPAWN_EGG = REGISTRIES.register("happy_ghast_spawn_egg",
        () -> ItemRegistry.createSpawnEgg(ModEntityTypes.HAPPY_GHAST, 16382457, 12369084, new Properties()));
    
    // Hot as Lava
    
    public static final Supplier<Item> MUSIC_DISC_LAVA_CHICKEN = REGISTRIES.register("music_disc_lava_chicken",
        new Properties()
            .stacksTo(1)
            .rarity(Rarity.RARE)
            .jukeboxPlayable(ModJukeboxSongs.LAVA_CHICKEN));

    // Copper Age
    
    public static final FeatureHolder<Item> COPPER_NUGGET = ITEMS.register("copper_nugget");
    
    public static final FeatureHolder<Item> COPPER_SWORD = ITEMS.register("copper_sword",
        properties -> new SwordItem(ModToolMaterials.COPPER, properties),
        new Item.Properties().attributes(SwordItem.createAttributes(ModToolMaterials.COPPER, 3, -2.4F)));
    public static final FeatureHolder<Item> COPPER_SHOVEL = ITEMS.register("copper_shovel",
        properties -> new ShovelItem(ModToolMaterials.COPPER, properties),
        new Item.Properties().attributes(ShovelItem.createAttributes(ModToolMaterials.COPPER, 1.5F, -3.0F)));
    public static final FeatureHolder<Item> COPPER_PICKAXE = ITEMS.register("copper_pickaxe",
        properties -> new PickaxeItem(ModToolMaterials.COPPER, properties),
        new Item.Properties().attributes(PickaxeItem.createAttributes(ModToolMaterials.COPPER, 1.0F, -2.8F)));
    public static final FeatureHolder<Item> COPPER_AXE = ITEMS.register("copper_axe",
        properties -> new AxeItem(ModToolMaterials.COPPER, properties),
        new Item.Properties().attributes(AxeItem.createAttributes(ModToolMaterials.COPPER, 7.0F, -3.2F)));
    public static final FeatureHolder<Item> COPPER_HOE = ITEMS.register("copper_hoe",
        properties -> new HoeItem(ModToolMaterials.COPPER, properties),
        new Item.Properties().attributes(HoeItem.createAttributes(ModToolMaterials.COPPER, -1.0F, -2.0F)));
    
    public static final FeatureHolder<Item> COPPER_HELMET = ITEMS.register("copper_helmet",
        properties -> new ArmorItem(ModArmorMaterials.COPPER, ArmorItem.Type.HELMET, properties),
        new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(5)));
    public static final FeatureHolder<Item> COPPER_CHESTPLATE = ITEMS.register("copper_chestplate",
        properties -> new ArmorItem(ModArmorMaterials.COPPER, ArmorItem.Type.CHESTPLATE, properties),
        new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(5)));
    public static final FeatureHolder<Item> COPPER_LEGGINGS = ITEMS.register("copper_leggings",
        properties -> new ArmorItem(ModArmorMaterials.COPPER, ArmorItem.Type.LEGGINGS, properties),
        new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(5)));
    public static final FeatureHolder<Item> COPPER_BOOTS = ITEMS.register("copper_boots",
        properties -> new ArmorItem(ModArmorMaterials.COPPER, ArmorItem.Type.BOOTS, properties),
        new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(5)));
    
    // Chaos Cubed
    
    public static final Supplier<Item> SULFUR_CUBE_BUCKET = REGISTRIES.register("sulfur_cube_bucket",
        properties -> new SulfurCubeBucketItem<>(ModEntityTypes.SULFUR_CUBE, ModSoundEvents.BUCKET_EMPTY_SULFUR_CUBE.get(), properties),
        new Properties().stacksTo(1));
    
    public static final Supplier<Item> MUSIC_DISC_BOUNCE = REGISTRIES.register("music_disc_bounce",
        new Properties()
            .stacksTo(1)
            .rarity(Rarity.UNCOMMON)
            .jukeboxPlayable(ModJukeboxSongs.BOUNCE));
    
    public static final Supplier<Item> SULFUR_CUBE_SPAWN_EGG = REGISTRIES.register("sulfur_cube_spawn_egg",
        () -> ItemRegistry.createSpawnEgg(ModEntityTypes.SULFUR_CUBE, 16777215, 8421504, new Properties()));
}
package com.blackgear.vanillabackport.common.registries;

import com.blackgear.platform.core.helper.ItemRegistry;
import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.level.entities.animal.ChickenVariants;
import com.blackgear.vanillabackport.common.level.items.PaleOakBoatItem;
import com.blackgear.vanillabackport.common.level.items.SulfurCubeBucketItem;
import com.blackgear.vanillabackport.common.level.items.VariantEggItem;
import com.blackgear.vanillabackport.common.level.items.WolfArmorItem;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.world.item.*;
import net.minecraft.world.item.Item.Properties;

import java.util.function.Supplier;

import static com.blackgear.platform.core.helper.ItemRegistry.createSpawnEgg;

public class ModItems {
    public static final ItemRegistry ITEMS = ItemRegistry.create(VanillaBackport.NAMESPACE);
    
    // Armored Paws
    
    public static final Supplier<Item> ARMADILLO_SCUTE = ITEMS.register("armadillo_scute");
    public static final Supplier<Item> ARMADILLO_SPAWN_EGG = ITEMS.register("armadillo_spawn_egg",
        () -> createSpawnEgg(
            () -> ModEntityTypes.ARMADILLO,
            11366765,
            8538184,
            new Properties())
    );
    public static final Supplier<Item> WOLF_ARMOR = ITEMS.register("wolf_armor", WolfArmorItem::new, new Properties().durability(64));
    
    // Bundles of Bravery
    
    public static final Supplier<Item> WHITE_BUNDLE = ITEMS.register("white_bundle", BundleItem::new, new Properties().stacksTo(1));
    public static final Supplier<Item> ORANGE_BUNDLE = ITEMS.register("orange_bundle", BundleItem::new, new Properties().stacksTo(1));
    public static final Supplier<Item> MAGENTA_BUNDLE = ITEMS.register("magenta_bundle", BundleItem::new, new Properties().stacksTo(1));
    public static final Supplier<Item> LIGHT_BLUE_BUNDLE = ITEMS.register("light_blue_bundle", BundleItem::new, new Properties().stacksTo(1));
    public static final Supplier<Item> YELLOW_BUNDLE = ITEMS.register("yellow_bundle", BundleItem::new, new Properties().stacksTo(1));
    public static final Supplier<Item> LIME_BUNDLE = ITEMS.register("lime_bundle", BundleItem::new, new Properties().stacksTo(1));
    public static final Supplier<Item> PINK_BUNDLE = ITEMS.register("pink_bundle", BundleItem::new, new Properties().stacksTo(1));
    public static final Supplier<Item> GRAY_BUNDLE = ITEMS.register("gray_bundle", BundleItem::new, new Properties().stacksTo(1));
    public static final Supplier<Item> LIGHT_GRAY_BUNDLE = ITEMS.register("light_gray_bundle", BundleItem::new, new Properties().stacksTo(1));
    public static final Supplier<Item> CYAN_BUNDLE = ITEMS.register("cyan_bundle", BundleItem::new, new Properties().stacksTo(1));
    public static final Supplier<Item> PURPLE_BUNDLE = ITEMS.register("purple_bundle", BundleItem::new, new Properties().stacksTo(1));
    public static final Supplier<Item> BLUE_BUNDLE = ITEMS.register("blue_bundle", BundleItem::new, new Properties().stacksTo(1));
    public static final Supplier<Item> BROWN_BUNDLE = ITEMS.register("brown_bundle", BundleItem::new, new Properties().stacksTo(1));
    public static final Supplier<Item> GREEN_BUNDLE = ITEMS.register("green_bundle", BundleItem::new, new Properties().stacksTo(1));
    public static final Supplier<Item> RED_BUNDLE = ITEMS.register("red_bundle", BundleItem::new, new Properties().stacksTo(1));
    public static final Supplier<Item> BLACK_BUNDLE = ITEMS.register("black_bundle", BundleItem::new, new Properties().stacksTo(1));
    
    // The Garden Awakens
    
    public static final Supplier<Item> PALE_OAK_BOAT = ITEMS.register("pale_oak_boat", () -> new PaleOakBoatItem(false, new Properties().stacksTo(1)));
    public static final Supplier<Item> PALE_OAK_CHEST_BOAT = ITEMS.register("pale_oak_chest_boat", () -> new PaleOakBoatItem(true, new Properties().stacksTo(1)));
    public static final Supplier<Item> CREAKING_SPAWN_EGG = ITEMS.register("creaking_spawn_egg",
        () -> createSpawnEgg(
            () -> ModEntityTypes.CREAKING,
            6250335,
            16545810,
            new Properties())
    );
    public static final Supplier<Item> RESIN_BRICK = ITEMS.register("resin_brick");
    
    // Spring to Life
    
    public static final Supplier<Item> BLUE_EGG = ITEMS.register("blue_egg",
        properties -> new VariantEggItem(ChickenVariants.COLD, properties),
        new Properties().stacksTo(16)
    );
    public static final Supplier<Item> BROWN_EGG = ITEMS.register("brown_egg",
        properties -> new VariantEggItem(ChickenVariants.WARM, properties),
        new Properties().stacksTo(16)
    );
    
    // Chase the Skies
    
    public static final Supplier<Item> WHITE_HARNESS = ITEMS.register("white_harness", new Properties().stacksTo(1));
    public static final Supplier<Item> ORANGE_HARNESS = ITEMS.register("orange_harness", new Properties().stacksTo(1));
    public static final Supplier<Item> MAGENTA_HARNESS = ITEMS.register("magenta_harness", new Properties().stacksTo(1));
    public static final Supplier<Item> LIGHT_BLUE_HARNESS = ITEMS.register("light_blue_harness", new Properties().stacksTo(1));
    public static final Supplier<Item> YELLOW_HARNESS = ITEMS.register("yellow_harness", new Properties().stacksTo(1));
    public static final Supplier<Item> LIME_HARNESS = ITEMS.register("lime_harness", new Properties().stacksTo(1));
    public static final Supplier<Item> PINK_HARNESS = ITEMS.register("pink_harness", new Properties().stacksTo(1));
    public static final Supplier<Item> GRAY_HARNESS = ITEMS.register("gray_harness", new Properties().stacksTo(1));
    public static final Supplier<Item> LIGHT_GRAY_HARNESS = ITEMS.register("light_gray_harness", new Properties().stacksTo(1));
    public static final Supplier<Item> CYAN_HARNESS = ITEMS.register("cyan_harness", new Properties().stacksTo(1));
    public static final Supplier<Item> PURPLE_HARNESS = ITEMS.register("purple_harness", new Properties().stacksTo(1));
    public static final Supplier<Item> BLUE_HARNESS = ITEMS.register("blue_harness", new Properties().stacksTo(1));
    public static final Supplier<Item> BROWN_HARNESS = ITEMS.register("brown_harness", new Properties().stacksTo(1));
    public static final Supplier<Item> GREEN_HARNESS = ITEMS.register("green_harness", new Properties().stacksTo(1));
    public static final Supplier<Item> RED_HARNESS = ITEMS.register("red_harness", new Properties().stacksTo(1));
    public static final Supplier<Item> BLACK_HARNESS = ITEMS.register("black_harness", new Properties().stacksTo(1));
    public static final Supplier<Item> MUSIC_DISC_TEARS = ITEMS.register("music_disc_tears",
        properties -> new RecordItem(10, ModSoundEvents.MUSIC_DISC_TEARS.get(), properties, 175),
        new Properties()
            .stacksTo(1)
            .rarity(Rarity.UNCOMMON)
    );
    public static final Supplier<Item> HAPPY_GHAST_SPAWN_EGG = ITEMS.register("happy_ghast_spawn_egg",
        () -> createSpawnEgg(
            () -> ModEntityTypes.HAPPY_GHAST,
            16382457,
            12369084,
            new Properties())
    );
    
    // Hot as Lava
    
    public static final Supplier<Item> MUSIC_DISC_LAVA_CHICKEN = ITEMS.register("music_disc_lava_chicken",
        properties -> new RecordItem(9, ModSoundEvents.MUSIC_DISC_LAVA_CHICKEN.get(), properties, 134),
        new Properties()
            .stacksTo(1)
            .rarity(Rarity.RARE)
    );
    
    // Chaos Cubed
    
    public static final Supplier<Item> SULFUR_CUBE_BUCKET = ITEMS.register("sulfur_cube_bucket",
        properties -> new SulfurCubeBucketItem(ModEntityTypes.SULFUR_CUBE, ModSoundEvents.BUCKET_EMPTY_SULFUR_CUBE.get(), properties),
        new Properties().stacksTo(1)
    );
    public static final Supplier<Item> MUSIC_DISC_BOUNCE = ITEMS.register("music_disc_bounce",
        properties -> new RecordItem(8, ModSoundEvents.MUSIC_DISC_BOUNCE.get(), properties, 234),
        new Properties()
            .stacksTo(1)
            .rarity(Rarity.UNCOMMON)
    );
    public static final Supplier<Item> SULFUR_CUBE_SPAWN_EGG = ITEMS.register("sulfur_cube_spawn_egg",
        () -> createSpawnEgg(
            () -> ModEntityTypes.SULFUR_CUBE,
            16777215,
            8421504,
            new Properties())
    );
}
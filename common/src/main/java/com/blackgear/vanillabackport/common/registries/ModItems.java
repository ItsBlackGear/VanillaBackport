package com.blackgear.vanillabackport.common.registries;

import com.blackgear.platform.core.helper.ItemRegistry;
import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.level.entities.animal.VariantKeys;
import com.blackgear.vanillabackport.common.level.items.PaleOakBoatItem;
import com.blackgear.vanillabackport.common.level.items.HarnessItem;
import com.blackgear.vanillabackport.common.level.items.VariantEggItem;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.world.item.*;

import java.util.function.Supplier;

public class ModItems {
    public static final ItemRegistry ITEMS = ItemRegistry.create(VanillaBackport.NAMESPACE);

    public static final Supplier<Item> RESIN_BRICK = ITEMS.register("resin_brick");
    public static final Supplier<Item> PALE_OAK_BOAT = ITEMS.register("pale_oak_boat", () -> new PaleOakBoatItem(false, new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> PALE_OAK_CHEST_BOAT = ITEMS.register("pale_oak_chest_boat", () -> new PaleOakBoatItem(true, new Item.Properties().stacksTo(1)));
    public static final Supplier<Item> CREAKING_SPAWN_EGG = ITEMS.spawnEgg("creaking_spawn_egg", ModEntities.CREAKING, 6250335, 16545810, new Item.Properties());
    public static final Supplier<Item> HAPPY_GHAST_SPAWN_EGG = ITEMS.spawnEgg("happy_ghast_spawn_egg", ModEntities.HAPPY_GHAST, 16382457, 12369084, new Item.Properties());

    public static final Supplier<Item> WHITE_HARNESS = ITEMS.register(
        "white_harness",
        HarnessItem::new,
        new Item.Properties().stacksTo(1)
    );
    public static final Supplier<Item> ORANGE_HARNESS = ITEMS.register(
        "orange_harness",
        HarnessItem::new,
        new Item.Properties().stacksTo(1)
    );
    public static final Supplier<Item> MAGENTA_HARNESS = ITEMS.register(
        "magenta_harness",
        HarnessItem::new,
        new Item.Properties().stacksTo(1)
    );
    public static final Supplier<Item> LIGHT_BLUE_HARNESS = ITEMS.register(
        "light_blue_harness",
        HarnessItem::new,
        new Item.Properties().stacksTo(1)
    );
    public static final Supplier<Item> YELLOW_HARNESS = ITEMS.register(
        "yellow_harness",
        HarnessItem::new,
        new Item.Properties().stacksTo(1)
    );
    public static final Supplier<Item> LIME_HARNESS = ITEMS.register(
        "lime_harness",
        HarnessItem::new,
        new Item.Properties().stacksTo(1)
    );
    public static final Supplier<Item> PINK_HARNESS = ITEMS.register(
        "pink_harness",
        HarnessItem::new,
        new Item.Properties().stacksTo(1)
    );
    public static final Supplier<Item> GRAY_HARNESS = ITEMS.register(
        "gray_harness",
        HarnessItem::new,
        new Item.Properties().stacksTo(1)
    );
    public static final Supplier<Item> LIGHT_GRAY_HARNESS = ITEMS.register(
        "light_gray_harness",
        HarnessItem::new,
        new Item.Properties().stacksTo(1)
    );
    public static final Supplier<Item> CYAN_HARNESS = ITEMS.register(
        "cyan_harness",
        HarnessItem::new,
        new Item.Properties().stacksTo(1)
    );
    public static final Supplier<Item> PURPLE_HARNESS = ITEMS.register(
        "purple_harness",
        HarnessItem::new,
        new Item.Properties().stacksTo(1)
    );
    public static final Supplier<Item> BLUE_HARNESS = ITEMS.register(
        "blue_harness",
        HarnessItem::new,
        new Item.Properties().stacksTo(1)
    );
    public static final Supplier<Item> BROWN_HARNESS = ITEMS.register(
        "brown_harness",
        HarnessItem::new,
        new Item.Properties().stacksTo(1)
    );
    public static final Supplier<Item> GREEN_HARNESS = ITEMS.register(
        "green_harness",
        HarnessItem::new,
        new Item.Properties().stacksTo(1)
    );
    public static final Supplier<Item> RED_HARNESS = ITEMS.register(
        "red_harness",
        HarnessItem::new,
        new Item.Properties().stacksTo(1)
    );
    public static final Supplier<Item> BLACK_HARNESS = ITEMS.register(
        "black_harness",
        HarnessItem::new,
        new Item.Properties().stacksTo(1)
    );

    public static final Supplier<Item> MUSIC_DISC_TEARS = ITEMS.register(
        "music_disc_tears",
        properties -> new RecordItem(10, ModSoundEvents.MUSIC_DISC_TEARS.get(), properties, 175),
        new Item.Properties()
            .stacksTo(1)
            .rarity(Rarity.UNCOMMON)
    );

    public static final Supplier<Item> MUSIC_DISC_LAVA_CHICKEN = ITEMS.register(
        "music_disc_lava_chicken",
        properties -> new RecordItem(9, ModSoundEvents.MUSIC_DISC_LAVA_CHICKEN.get(), properties, 134),
        new Item.Properties()
            .stacksTo(1)
            .rarity(Rarity.RARE)
    );

    public static final Supplier<Item> WHITE_BUNDLE = ITEMS.register(
        "white_bundle",
        BundleItem::new, new Item.Properties().stacksTo(1)
    );
    public static final Supplier<Item> ORANGE_BUNDLE = ITEMS.register(
        "orange_bundle",
        BundleItem::new, new Item.Properties().stacksTo(1)
    );
    public static final Supplier<Item> MAGENTA_BUNDLE = ITEMS.register(
        "magenta_bundle",
        BundleItem::new, new Item.Properties().stacksTo(1)
    );
    public static final Supplier<Item> LIGHT_BLUE_BUNDLE = ITEMS.register(
        "light_blue_bundle",
        BundleItem::new, new Item.Properties().stacksTo(1)
    );
    public static final Supplier<Item> YELLOW_BUNDLE = ITEMS.register(
        "yellow_bundle",
        BundleItem::new, new Item.Properties().stacksTo(1));
    public static final Supplier<Item> LIME_BUNDLE = ITEMS.register(
        "lime_bundle",
        BundleItem::new, new Item.Properties().stacksTo(1));
    public static final Supplier<Item> PINK_BUNDLE = ITEMS.register(
        "pink_bundle",
        BundleItem::new, new Item.Properties().stacksTo(1));
    public static final Supplier<Item> GRAY_BUNDLE = ITEMS.register(
        "gray_bundle",
        BundleItem::new, new Item.Properties().stacksTo(1));
    public static final Supplier<Item> LIGHT_GRAY_BUNDLE = ITEMS.register(
        "light_gray_bundle",
        BundleItem::new, new Item.Properties().stacksTo(1));
    public static final Supplier<Item> CYAN_BUNDLE = ITEMS.register(
        "cyan_bundle",
        BundleItem::new, new Item.Properties().stacksTo(1));
    public static final Supplier<Item> PURPLE_BUNDLE = ITEMS.register(
        "purple_bundle",
        BundleItem::new, new Item.Properties().stacksTo(1));
    public static final Supplier<Item> BLUE_BUNDLE = ITEMS.register(
        "blue_bundle",
        BundleItem::new, new Item.Properties().stacksTo(1));
    public static final Supplier<Item> BROWN_BUNDLE = ITEMS.register(
        "brown_bundle",
        BundleItem::new, new Item.Properties().stacksTo(1));
    public static final Supplier<Item> GREEN_BUNDLE = ITEMS.register(
        "green_bundle",
        BundleItem::new, new Item.Properties().stacksTo(1));
    public static final Supplier<Item> RED_BUNDLE = ITEMS.register(
        "red_bundle",
        BundleItem::new, new Item.Properties().stacksTo(1));
    public static final Supplier<Item> BLACK_BUNDLE = ITEMS.register(
        "black_bundle",
        BundleItem::new, new Item.Properties().stacksTo(1));

    public static final Supplier<Item> BLUE_EGG = ITEMS.register(
        "blue_egg",
        properties -> new VariantEggItem(VariantKeys.COLD_CHICKEN, properties),
        new Item.Properties().stacksTo(16));
    public static final Supplier<Item> BROWN_EGG = ITEMS.register(
        "brown_egg",
        properties -> new VariantEggItem(VariantKeys.WARM_CHICKEN, properties),
        new Item.Properties().stacksTo(16));
}
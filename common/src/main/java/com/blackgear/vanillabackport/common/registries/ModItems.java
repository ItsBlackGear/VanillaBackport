package com.blackgear.vanillabackport.common.registries;

import com.blackgear.platform.core.helper.ItemRegistry;
import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.level.entities.animal.ChickenVariants;
import com.blackgear.vanillabackport.common.level.items.PaleOakBoatItem;
import com.blackgear.vanillabackport.common.level.items.SulfurCubeBucketItem;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.*;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.component.BundleContents;

import java.util.function.Supplier;

public class ModItems {
    public static final ItemRegistry ITEMS = ItemRegistry.create(VanillaBackport.NAMESPACE);

    public static final Supplier<Item> CREAKING_SPAWN_EGG = ITEMS.spawnEgg("creaking_spawn_egg", ModEntities.CREAKING, 6250335, 16545810, new Properties());
    public static final Supplier<Item> HAPPY_GHAST_SPAWN_EGG = ITEMS.spawnEgg("happy_ghast_spawn_egg", ModEntities.HAPPY_GHAST, 16382457, 12369084, new Properties());

    public static final Supplier<Item> RESIN_BRICK = ITEMS.register("resin_brick");
    public static final Supplier<Item> PALE_OAK_BOAT = ITEMS.register("pale_oak_boat", () -> new PaleOakBoatItem(false, new Properties().stacksTo(1)));
    public static final Supplier<Item> PALE_OAK_CHEST_BOAT = ITEMS.register("pale_oak_chest_boat", () -> new PaleOakBoatItem(true, new Properties().stacksTo(1)));

    public static final Supplier<Item> WHITE_HARNESS = ITEMS.register(
        "white_harness",
        new Properties().stacksTo(1)
    );
    public static final Supplier<Item> ORANGE_HARNESS = ITEMS.register(
        "orange_harness",
        new Properties().stacksTo(1)
    );
    public static final Supplier<Item> MAGENTA_HARNESS = ITEMS.register(
        "magenta_harness",
        new Properties().stacksTo(1)
    );
    public static final Supplier<Item> LIGHT_BLUE_HARNESS = ITEMS.register(
        "light_blue_harness",
        new Properties().stacksTo(1)
    );
    public static final Supplier<Item> YELLOW_HARNESS = ITEMS.register(
        "yellow_harness",
        new Properties().stacksTo(1)
    );
    public static final Supplier<Item> LIME_HARNESS = ITEMS.register(
        "lime_harness",
        new Properties().stacksTo(1)
    );
    public static final Supplier<Item> PINK_HARNESS = ITEMS.register(
        "pink_harness",
        new Properties().stacksTo(1)
    );
    public static final Supplier<Item> GRAY_HARNESS = ITEMS.register(
        "gray_harness",
        new Properties().stacksTo(1)
    );
    public static final Supplier<Item> LIGHT_GRAY_HARNESS = ITEMS.register(
        "light_gray_harness",
        new Properties().stacksTo(1)
    );
    public static final Supplier<Item> CYAN_HARNESS = ITEMS.register(
        "cyan_harness",
        new Properties().stacksTo(1)
    );
    public static final Supplier<Item> PURPLE_HARNESS = ITEMS.register(
        "purple_harness",
        new Properties().stacksTo(1)
    );
    public static final Supplier<Item> BLUE_HARNESS = ITEMS.register(
        "blue_harness",
        new Properties().stacksTo(1)
    );
    public static final Supplier<Item> BROWN_HARNESS = ITEMS.register(
        "brown_harness",
        new Properties().stacksTo(1)
    );
    public static final Supplier<Item> GREEN_HARNESS = ITEMS.register(
        "green_harness",
        new Properties().stacksTo(1)
    );
    public static final Supplier<Item> RED_HARNESS = ITEMS.register(
        "red_harness",
        new Properties().stacksTo(1)
    );
    public static final Supplier<Item> BLACK_HARNESS = ITEMS.register(
        "black_harness",
        new Properties().stacksTo(1)
    );

    public static final Supplier<Item> MUSIC_DISC_TEARS = ITEMS.register(
        "music_disc_tears",
        new Properties()
            .stacksTo(1)
            .rarity(Rarity.UNCOMMON)
            .jukeboxPlayable(ModJukeboxSongs.TEARS)
    );
    public static final Supplier<Item> MUSIC_DISC_LAVA_CHICKEN = ITEMS.register(
        "music_disc_lava_chicken",
        new Properties()
            .stacksTo(1)
            .rarity(Rarity.RARE)
            .jukeboxPlayable(ModJukeboxSongs.LAVA_CHICKEN)
    );

    public static final Supplier<Item> WHITE_BUNDLE = ITEMS.register(
        "white_bundle",
        BundleItem::new, new Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
    public static final Supplier<Item> ORANGE_BUNDLE = ITEMS.register(
        "orange_bundle",
        BundleItem::new, new Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
    public static final Supplier<Item> MAGENTA_BUNDLE = ITEMS.register(
        "magenta_bundle",
        BundleItem::new, new Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
    public static final Supplier<Item> LIGHT_BLUE_BUNDLE = ITEMS.register(
        "light_blue_bundle",
        BundleItem::new, new Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
    public static final Supplier<Item> YELLOW_BUNDLE = ITEMS.register(
        "yellow_bundle",
        BundleItem::new, new Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
    public static final Supplier<Item> LIME_BUNDLE = ITEMS.register(
        "lime_bundle",
        BundleItem::new, new Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
    public static final Supplier<Item> PINK_BUNDLE = ITEMS.register(
        "pink_bundle",
        BundleItem::new, new Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
    public static final Supplier<Item> GRAY_BUNDLE = ITEMS.register(
        "gray_bundle",
        BundleItem::new, new Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
    public static final Supplier<Item> LIGHT_GRAY_BUNDLE = ITEMS.register(
        "light_gray_bundle",
        BundleItem::new, new Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
    public static final Supplier<Item> CYAN_BUNDLE = ITEMS.register(
        "cyan_bundle",
        BundleItem::new, new Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
    public static final Supplier<Item> PURPLE_BUNDLE = ITEMS.register(
        "purple_bundle",
        BundleItem::new, new Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
    public static final Supplier<Item> BLUE_BUNDLE = ITEMS.register(
        "blue_bundle",
        BundleItem::new, new Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
    public static final Supplier<Item> BROWN_BUNDLE = ITEMS.register(
        "brown_bundle",
        BundleItem::new, new Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
    public static final Supplier<Item> GREEN_BUNDLE = ITEMS.register(
        "green_bundle",
        BundleItem::new, new Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
    public static final Supplier<Item> RED_BUNDLE = ITEMS.register(
        "red_bundle",
        BundleItem::new, new Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));
    public static final Supplier<Item> BLACK_BUNDLE = ITEMS.register(
        "black_bundle",
        BundleItem::new, new Properties().stacksTo(1).component(DataComponents.BUNDLE_CONTENTS, BundleContents.EMPTY));

    public static final Supplier<Item> BLUE_EGG = ITEMS.register(
        "blue_egg",
        () -> new EggItem(new Properties().stacksTo(16).component(ModDataComponents.CHICKEN_VARIANT.get(), ChickenVariants.COLD))
    );
    public static final Supplier<Item> BROWN_EGG = ITEMS.register(
        "brown_egg",
        () -> new EggItem(new Properties().stacksTo(16).component(ModDataComponents.CHICKEN_VARIANT.get(), ChickenVariants.WARM))
    );

    public static final Supplier<Item> SULFUR_CUBE_BUCKET = ITEMS.register(
        "sulfur_cube_bucket",
        properties -> new SulfurCubeBucketItem(ModEntityTypes.SULFUR_CUBE, ModSoundEvents.BUCKET_EMPTY_SULFUR_CUBE.get(), properties),
        new Properties().stacksTo(1)
    );

    public static final Supplier<Item> SULFUR_CUBE_SPAWN_EGG = ITEMS.spawnEgg("sulfur_cube_spawn_egg", () -> ModEntityTypes.SULFUR_CUBE, 16777215, 8421504, new Properties());

    public static final Supplier<Item> MUSIC_DISC_BOUNCE = ITEMS.register(
        "music_disc_bounce",
        new Properties()
            .stacksTo(1)
            .rarity(Rarity.UNCOMMON)
            .jukeboxPlayable(ModJukeboxSongs.BOUNCE)
    );
}
package com.blackgear.vanillabackport.common.api.bundle;

import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;

/**
 * added for retro-compatibility with Dye the World, highly recommended for the dev to migrate!
 */
@Deprecated(forRemoval = true)
public class BundleFeatures {
    public static void register(DyeColor dyeColor, Item item) {
        com.blackgear.vanillabackport.common.api.modules.bundle_ui.BundleFeatures.register(dyeColor, item);
    }
}
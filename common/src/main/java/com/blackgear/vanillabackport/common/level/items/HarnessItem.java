package com.blackgear.vanillabackport.common.level.items;

import net.minecraft.world.item.Item;

/**
 * added for retro-compatibility with Happy Airships, highly recommended for the dev to migrate!
 */
@Deprecated(forRemoval = true)
public class HarnessItem extends Item {
    public HarnessItem(Properties properties) {
        super(properties);
    }
}
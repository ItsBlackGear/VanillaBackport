package com.blackgear.vanillabackport.core.data.tags.create;

import com.blackgear.platform.common.data.TagRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class CreateItemTags {
    public static final TagRegistry<Item> TAGS = TagRegistry.create(Registries.ITEM, "create");

    public static final TagKey<Item> MODDED_STRIPPED_LOGS = TAGS.register("modded_stripped_logs");
    public static final TagKey<Item> MODDED_STRIPPED_WOOD = TAGS.register("modded_stripped_wood");
}
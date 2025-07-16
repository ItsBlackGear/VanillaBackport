package com.blackgear.vanillabackport.core.data.tags;

import com.blackgear.platform.common.data.TagRegistry;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModItemTags {
    public static final TagRegistry<Item> TAGS = TagRegistry.create(Registries.ITEM, VanillaBackport.MOD_ID);

    public static final TagKey<Item> PALE_OAK_LOGS = TAGS.register("pale_oak_logs");
    public static final TagKey<Item> HAPPY_GHAST_TEMPT_ITEMS = TAGS.register("happy_ghast_tempt_items");
    public static final TagKey<Item> HAPPY_GHAST_FOOD = TAGS.register("happy_ghast_food");
    public static final TagKey<Item> HARNESSES = TAGS.register("harnesses");
    public static final TagKey<Item> BUNDLES = TAGS.register("bundles");
}
package com.blackgear.vanillabackport.core.data.tags;

import com.blackgear.platform.common.data.TagRegistry;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ModItemTags {
    public static final TagRegistry<Item> TAGS = TagRegistry.create(Registries.ITEM, VanillaBackport.NAMESPACE);

    public static final TagKey<Item> PALE_OAK_LOGS = TAGS.register("pale_oak_logs");
    public static final TagKey<Item> HAPPY_GHAST_TEMPT_ITEMS = TAGS.register("happy_ghast_tempt_items");
    public static final TagKey<Item> HAPPY_GHAST_FOOD = TAGS.register("happy_ghast_food");
    public static final TagKey<Item> HARNESSES = TAGS.register("harnesses");
    public static final TagKey<Item> BUNDLES = TAGS.register("bundles");
    public static final TagKey<Item> EGGS = TAGS.register("eggs");
    public static final TagKey<Item> ARMADILLO_FOOD = TAGS.register("armadillo_food");

    public static final TagKey<Item> SULFUR_CUBE_FOOD = TAGS.register("sulfur_cube_food");
    public static final TagKey<Item> SULFUR_CUBE_ARCHETYPE_REGULAR = TAGS.register("sulfur_cube_archetype/regular");
    public static final TagKey<Item> SULFUR_CUBE_ARCHETYPE_BOUNCY = TAGS.register("sulfur_cube_archetype/bouncy");
    public static final TagKey<Item> SULFUR_CUBE_ARCHETYPE_SLOW_BOUNCY = TAGS.register("sulfur_cube_archetype/slow_bouncy");
    public static final TagKey<Item> SULFUR_CUBE_ARCHETYPE_SLOW_FLAT = TAGS.register("sulfur_cube_archetype/slow_flat");
    public static final TagKey<Item> SULFUR_CUBE_ARCHETYPE_FAST_FLAT = TAGS.register("sulfur_cube_archetype/fast_flat");
    public static final TagKey<Item> SULFUR_CUBE_ARCHETYPE_LIGHT = TAGS.register("sulfur_cube_archetype/light");
    public static final TagKey<Item> SULFUR_CUBE_ARCHETYPE_FAST_SLIDING = TAGS.register("sulfur_cube_archetype/fast_sliding");
    public static final TagKey<Item> SULFUR_CUBE_ARCHETYPE_SLOW_SLIDING = TAGS.register("sulfur_cube_archetype/slow_sliding");
    public static final TagKey<Item> SULFUR_CUBE_ARCHETYPE_HIGH_RESISTANCE = TAGS.register("sulfur_cube_archetype/high_resistance");
    public static final TagKey<Item> SULFUR_CUBE_ARCHETYPE_STICKY = TAGS.register("sulfur_cube_archetype/sticky");
    public static final TagKey<Item> SULFUR_CUBE_ARCHETYPE_EXPLOSIVE = TAGS.register("sulfur_cube_archetype/explosive");
    public static final TagKey<Item> SULFUR_CUBE_ARCHETYPE_HOT = TAGS.register("sulfur_cube_archetype/hot");
    public static final TagKey<Item> SULFUR_CUBE_SWALLOWABLE = TAGS.register("sulfur_cube_swallowable");
}
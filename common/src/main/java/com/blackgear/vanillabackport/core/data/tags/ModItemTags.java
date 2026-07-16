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
    
    public static final TagKey<Item> COPPER = TAGS.register("copper");
    public static final TagKey<Item> COPPER_GOLEM_STATUES = TAGS.register("copper_golem_statues");
    public static final TagKey<Item> SHEARABLE_FROM_COPPER_GOLEM = TAGS.register("shearable_from_copper_golem");
    
    public static final TagKey<Item> BARS = TAGS.register("bars");
    public static final TagKey<Item> CHAINS = TAGS.register("chains");
    public static final TagKey<Item> LANTERNS = TAGS.register("lanterns");
    public static final TagKey<Item> LIGHTNING_RODS = TAGS.register("lightning_rods");
    public static final TagKey<Item> WOODEN_SHELVES = TAGS.register("wooden_shelves");
    
    public static final TagKey<Item> SPEARS = TAGS.register("spears");
    public static final TagKey<Item> CAMEL_HUSK_FOOD = TAGS.register("camel_husk_food");
    public static final TagKey<Item> ZOMBIE_HORSE_FOOD = TAGS.register("zombie_horse_food");
    
    public static final TagKey<Item> NAUTILUS_BUCKET_FOOD = TAGS.register("nautilus_bucket_food");
    public static final TagKey<Item> NAUTILUS_FOOD = TAGS.register("nautilus_food");
    public static final TagKey<Item> NAUTILUS_TAMING_ITEMS = TAGS.register("nautilus_taming_items");
}
package com.blackgear.vanillabackport.core.data.tags;

import com.blackgear.platform.common.data.TagRegistry;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;

public class ModBiomeTags {
    public static final TagRegistry<Biome> TAGS = TagRegistry.create(Registries.BIOME, VanillaBackport.NAMESPACE);

    public static final TagKey<Biome> SPAWNS_WARM_VARIANT_FARM_ANIMALS = TAGS.register("spawns_warm_variant_farm_animals");
    public static final TagKey<Biome> SPAWNS_COLD_VARIANT_FARM_ANIMALS = TAGS.register("spawns_cold_variant_farm_animals");

    // Worldgen tags
    public static final TagKey<Biome> SPAWNS_BUSHES = TAGS.register("spawns_bushes");
    public static final TagKey<Biome> SPAWNS_FIREFLY_BUSHES = TAGS.register("spawns_firefly_bushes");
    public static final TagKey<Biome> SPAWNS_FIREFLY_BUSHES_SWAMP = TAGS.register("spawns_firefly_bushes_swamp");
}
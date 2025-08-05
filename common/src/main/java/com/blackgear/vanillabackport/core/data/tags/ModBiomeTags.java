package com.blackgear.vanillabackport.core.data.tags;

import com.blackgear.platform.common.data.TagRegistry;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class ModBiomeTags {
    public static final TagRegistry<Biome> TAGS = TagRegistry.create(Registries.BIOME, VanillaBackport.NAMESPACE);

    public static final TagKey<Biome> SPAWNS_WARM_VARIANT_FARM_ANIMALS = TAGS.register("spawns_warm_variant_farm_animals");
    public static final TagKey<Biome> SPAWNS_COLD_VARIANT_FARM_ANIMALS = TAGS.register("spawns_cold_variant_farm_animals");

    // Entity Placement Tags
    public static final TagKey<Biome> SPAWNS_CAMELS = TAGS.register("spawns_camels");

    // Feature Placement Tags
    public static final TagKey<Biome> SPAWNS_BUSHES = TAGS.register("spawns_bushes");
    public static final TagKey<Biome> SPAWNS_FIREFLY_BUSHES = TAGS.register("spawns_firefly_bushes");
    public static final TagKey<Biome> SPAWNS_FIREFLY_BUSHES_SWAMP = TAGS.register("spawns_firefly_bushes_swamp");
    public static final TagKey<Biome> SPAWNS_WILDFLOWERS = TAGS.register("spawns_wildflowers");
    public static final TagKey<Biome> SPAWNS_NOISE_BASED_WILDFLOWERS = TAGS.register("spawns_noise_based_wildflowers");
    public static final TagKey<Biome> SPAWNS_DRY_GRASS = TAGS.register("spawns_dry_grass");
    public static final TagKey<Biome> SPAWNS_DRY_GRASS_RARELY = TAGS.register("spawns_dry_grass_rarely");
    public static final TagKey<Biome> SPAWNS_FALLEN_OAK_TREES = TAGS.register("spawns_fallen_oak_trees");
    public static final TagKey<Biome> SPAWNS_FALLEN_BIRCH_TREES = TAGS.register("spawns_fallen_birch_trees");
    public static final TagKey<Biome> SPAWNS_FALLEN_BIRCH_TREES_RARELY = TAGS.register("spawns_fallen_birch_trees_rarely");
    public static final TagKey<Biome> SPAWNS_FALLEN_SUPER_BIRCH_TREES = TAGS.register("spawns_fallen_super_birch_trees");
    public static final TagKey<Biome> SPAWNS_FALLEN_JUNGLE_TREES = TAGS.register("spawns_fallen_jungle_trees");
    public static final TagKey<Biome> SPAWNS_FALLEN_SPRUCE_TREES = TAGS.register("spawns_fallen_spruce_trees");
    public static final TagKey<Biome> SPAWNS_FALLEN_SPRUCE_TREES_RARELY = TAGS.register("spawns_fallen_spruce_trees_rarely");
    public static final TagKey<Biome> SPAWNS_LEAF_LITTER_PATCHES = TAGS.register("spawns_leaf_litter_patches");
}
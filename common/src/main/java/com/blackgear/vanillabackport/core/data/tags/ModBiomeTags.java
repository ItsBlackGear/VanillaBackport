package com.blackgear.vanillabackport.core.data.tags;

import com.blackgear.platform.common.data.TagRegistry;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public class ModBiomeTags {
    public static final TagRegistry<Biome> TAGS = TagRegistry.create(Registries.BIOME, VanillaBackport.NAMESPACE);
    public static final TagRegistry<Biome> CONVENTIONAL = TagRegistry.create(Registries.BIOME, "c");

    // Entity Placement Tags
    public static final TagKey<Biome> SPAWNS_WARM_VARIANT_FARM_ANIMALS = TAGS.register("spawns_warm_variant_farm_animals");
    public static final TagKey<Biome> SPAWNS_COLD_VARIANT_FARM_ANIMALS = TAGS.register("spawns_cold_variant_farm_animals");
    public static final TagKey<Biome> SPAWNS_CORAL_VARIANT_ZOMBIE_NAUTILUS = TAGS.register("spawns_coral_variant_zombie_nautilus");
    public static final TagKey<Biome> SPAWNS_NAUTILUS = TAGS.register("spawns_nautilus");
    public static final TagKey<Biome> SPAWNS_NAUTILUS_FREQUENTLY = TAGS.register("spawns_nautilus_frequently");

    public static final TagKey<Biome> SPAWNS_CAMELS = TAGS.register("spawns_camels");
    public static final TagKey<Biome> SPAWNS_ARMADILLOS_FREQUENTLY = TAGS.register("spawns_armadillos_frequently");
    public static final TagKey<Biome> SPAWNS_ARMADILLOS = TAGS.register("spawns_armadillos");
    public static final TagKey<Biome> SPAWNS_ZOMBIE_HORSES = TAGS.register("spawns_zombie_horses");
    
    public static final TagKey<Biome> SPAWNS_SPOTTED_WOLVES = CONVENTIONAL.register("has_wolf/spotted");
    public static final TagKey<Biome> SPAWNS_SNOWY_WOLVES = CONVENTIONAL.register("has_wolf/snowy");
    public static final TagKey<Biome> SPAWNS_BLACK_WOLVES = CONVENTIONAL.register("has_wolf/black");
    public static final TagKey<Biome> SPAWNS_ASHEN_WOLVES = CONVENTIONAL.register("has_wolf/ashen");
    public static final TagKey<Biome> SPAWNS_RUSTY_WOLVES = CONVENTIONAL.register("has_wolf/rusty");
    public static final TagKey<Biome> SPAWNS_WOOD_WOLVES = CONVENTIONAL.register("has_wolf/woods");
    public static final TagKey<Biome> SPAWNS_CHESTNUT_WOLVES = CONVENTIONAL.register("has_wolf/chestnut");
    public static final TagKey<Biome> SPAWNS_STRIPED_WOLVES = CONVENTIONAL.register("has_wolf/striped");

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

    public static final TagKey<Biome> SPAWNS_LEAF_LITTER = TAGS.register("spawns_leaf_litter");
    public static final TagKey<Biome> SPAWNS_LEAF_LITTER_PATCHES = TAGS.register("spawns_leaf_litter_patches");

    public static final TagKey<Biome> HAS_DARK_LEAF_LITTER = TAGS.register("has_dark_leaf_litter");
    public static final TagKey<Biome> HAS_PALE_LEAF_LITTER = TAGS.register("has_pale_leaf_litter");
    public static final TagKey<Biome> HAS_RED_LEAF_LITTER = TAGS.register("has_red_leaf_litter");
    
    public static final TagKey<Biome> HAS_ABANDONED_CAMP_BAMBOO_JUNGLE = TAGS.register("has_structure/abandoned_camp_bamboo_jungle");
    public static final TagKey<Biome> HAS_ABANDONED_CAMP_BIRCH_FOREST = TAGS.register("has_structure/abandoned_camp_birch_forest");
    public static final TagKey<Biome> HAS_ABANDONED_CAMP_CHERRY_GROVE = TAGS.register("has_structure/abandoned_camp_cherry_grove");
    public static final TagKey<Biome> HAS_ABANDONED_CAMP_DAPPLED_FOREST = TAGS.register("has_structure/abandoned_camp_dappled_forest");
    public static final TagKey<Biome> HAS_ABANDONED_CAMP_FLOWER_FOREST = TAGS.register("has_structure/abandoned_camp_flower_forest");
    public static final TagKey<Biome> HAS_ABANDONED_CAMP_FOREST = TAGS.register("has_structure/abandoned_camp_forest");
    public static final TagKey<Biome> HAS_ABANDONED_CAMP_MEADOW = TAGS.register("has_structure/abandoned_camp_meadow");
    public static final TagKey<Biome> HAS_ABANDONED_CAMP_OLD_GROWTH_BIRCH_FOREST = TAGS.register("has_structure/abandoned_camp_old_growth_birch_forest");
    public static final TagKey<Biome> HAS_ABANDONED_CAMP_OLD_GROWTH_PINE_TAIGA = TAGS.register("has_structure/abandoned_camp_old_growth_pine_taiga");
    public static final TagKey<Biome> HAS_ABANDONED_CAMP_OLD_GROWTH_SPRUCE_TAIGA = TAGS.register("has_structure/abandoned_camp_old_growth_spruce_taiga");
    public static final TagKey<Biome> HAS_ABANDONED_CAMP_PALE_GARDEN = TAGS.register("has_structure/abandoned_camp_pale_garden");
    public static final TagKey<Biome> HAS_ABANDONED_CAMP_SAVANNA = TAGS.register("has_structure/abandoned_camp_savanna");
    public static final TagKey<Biome> HAS_ABANDONED_CAMP_SNOWY_TAIGA = TAGS.register("has_structure/abandoned_camp_snowy_taiga");
    public static final TagKey<Biome> HAS_ABANDONED_CAMP_SPARSE_JUNGLE = TAGS.register("has_structure/abandoned_camp_sparse_jungle");
    public static final TagKey<Biome> HAS_ABANDONED_CAMP_SWAMP = TAGS.register("has_structure/abandoned_camp_swamp");
    public static final TagKey<Biome> HAS_ABANDONED_CAMP_TAIGA = TAGS.register("has_structure/abandoned_camp_taiga");
    public static final TagKey<Biome> HAS_ABANDONED_CAMP_WINDSWEPT_FOREST = TAGS.register("has_structure/abandoned_camp_windswept_forest");
    public static final TagKey<Biome> HAS_ABANDONED_CAMP_WOODED_BADLANDS = TAGS.register("has_structure/abandoned_camp_wooded_badlands");
}
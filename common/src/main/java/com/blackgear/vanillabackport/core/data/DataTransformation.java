package com.blackgear.vanillabackport.core.data;

import com.blackgear.platform.common.data.DataTransformer;
import com.blackgear.platform.core.Environment;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class DataTransformation {
    public static final List<String> PARTICLE_IDS = List.of(
        "pale_oak_leaves",
        "trail",
        "firefly",
        "tinted_leaves",
        "tinted_needles"
    );
    public static final List<String> BLOCK_IDS = List.of(
        "pale_oak_leaves",
        "pale_oak_planks",
        "pale_oak_stairs",
        "pale_oak_slab",
        "pale_oak_fence",
        "pale_oak_fence_gate",
        "pale_oak_door",
        "pale_oak_wood",
        "pale_oak_log",
        "stripped_pale_oak_wood",
        "stripped_pale_oak_log",
        "pale_moss_block",
        "pale_moss_carpet",
        "pale_hanging_moss",
        "open_eyeblossom",
        "closed_eyeblossom",
        "pale_oak_sapling",
        "potted_open_eyeblossom",
        "potted_closed_eyeblossom",
        "potted_pale_oak_sapling",
        "creaking_heart",
        "pale_oak_sign",
        "pale_oak_wall_sign",
        "pale_oak_hanging_sign",
        "pale_oak_wall_hanging_sign",
        "pale_oak_pressure_plate",
        "pale_oak_trapdoor",
        "resin_clump",
        "resin_block",
        "resin_bricks",
        "resin_brick_stairs",
        "resin_brick_slab",
        "resin_brick_wall",
        "chiseled_resin_bricks",
        "dried_ghast",
        "bush",
        "firefly_bush",
        "wildflowers",
        "leaf_litter",
        "cactus_flower",
        "short_dry_grass",
        "tall_dry_grass"
    );
    public static final List<String> ITEM_IDS = List.of(
        "resin_brick",
        "pale_oak_boat",
        "pale_oak_chest_boat",
        "creaking_spawn_egg",
        "happy_ghast_spawn_egg",
        "white_harness",
        "orange_harness",
        "magenta_harness",
        "light_blue_harness",
        "yellow_harness",
        "lime_harness",
        "pink_harness",
        "gray_harness",
        "light_gray_harness",
        "cyan_harness",
        "purple_harness",
        "blue_harness",
        "brown_harness",
        "black_harness",
        "music_disc_tears",
        "music_disc_lava_chicken",
        "white_bundle",
        "orange_bundle",
        "magenta_bundle",
        "light_blue_bundle",
        "yellow_bundle",
        "lime_bundle",
        "pink_bundle",
        "gray_bundle",
        "light_gray_bundle",
        "cyan_bundle",
        "purple_bundle",
        "blue_bundle",
        "brown_bundle",
        "black_bundle"
    );
    public static final List<String> BLOCK_ENTITY_IDS = List.of(
        "creaking_heart"
    );
    public static final List<String> ENTITY_IDS = List.of(
        "creaking",
        "happy_ghast",
        "pale_oak_boat",
        "pale_oak_chest_boat"
    );
    public static final List<String> SOUND_EVENT_IDS = List.of(
        "block.eyeblossom.open_long",
        "block.eyeblossom.open",
        "block.eyeblossom.close_long",
        "block.eyeblossom.close",
        "block.eyeblossom.idle",
        "block.pale_hanging_moss.idle",
        "block.creaking_heart.break",
        "block.creaking_heart.fall",
        "block.creaking_heart.hit",
        "block.creaking_heart.hurt",
        "block.creaking_heart.place",
        "block.creaking_heart.step",
        "block.creaking_heart.idle",
        "block.creaking_heart.spawn",
        "block.resin.break",
        "block.resin.fall",
        "block.resin.place",
        "block.resin.step",
        "block.resin_bricks.break",
        "block.resin_bricks.fall",
        "block.resin_bricks.hit",
        "block.resin_bricks.place",
        "block.resin_bricks.step",
        "block.dried_ghast.break",
        "block.dried_ghast.step",
        "block.dried_ghast.fall",
        "block.dried_ghast.ambient",
        "block.dried_ghast.ambient_water",
        "block.dried_ghast.place",
        "block.dried_ghast.place_in_water",
        "block.dried_ghast.transition",
        "block.leaf_litter.break",
        "block.leaf_litter.step",
        "block.leaf_litter.place",
        "block.leaf_litter.hit",
        "block.leaf_litter.fall",
        "block.cactus_flower.break",
        "block.cactus_flower.place",
        "block.firefly_bush.idle",
        "block.sand.idle",
        "block.deadbush.idle",
        "block.dry_grass.ambient",
        "item.bundle.insert_fail",
        "entity.creaking.ambient",
        "entity.creaking.activate",
        "entity.creaking.deactivate",
        "entity.creaking.attack",
        "entity.creaking.death",
        "entity.creaking.step",
        "entity.creaking.freeze",
        "entity.creaking.unfreeze",
        "entity.creaking.spawn",
        "entity.creaking.sway",
        "entity.creaking.twitch",
        "entity.ghastling.ambient",
        "entity.ghastling.death",
        "entity.ghastling.hurt",
        "entity.ghastling.spawn",
        "entity.happy_ghast.ambient",
        "entity.happy_ghast.death",
        "entity.happy_ghast.hurt",
        "entity.happy_ghast.riding",
        "entity.happy_ghast.equip",
        "entity.happy_ghast.unequip",
        "entity.happy_ghast.harness_goggles_up",
        "entity.happy_ghast.harness_goggles_down",
        "entity.parrot.imitate.creaking",
        "music_disc.tears",
        "music_disc.lava_chicken"
    );
    public static final List<String> JUKEBOX_SONG_IDS = List.of(
        "tears",
        "lava_chicken"
    );
    public static final List<String> PAINTING_VARIANT_IDS = List.of(
        "dennis"
    );
    public static final List<String> SENSOR_TYPE_IDS = List.of(
        "nearest_adult_any_type",
        "happy_ghast_temptations"
    );
    public static final List<String> BIOME_IDS = List.of(
        "pale_garden"
    );
    public static final List<String> TREE_DECORATOR_IDS = List.of(
        "pale_moss",
        "creaking_heart"
    );
    public static final List<String> TRIM_MATERIAL_IDS = List.of(
        "resin"
    );
    public static final List<String> WOOD_TYPE_IDS = List.of(
        "pale_oak"
    );
    public static final List<String> FEATURE_IDS = List.of(
        "pale_oak",
        "pale_oak_bonemeal",
        "pale_oak_creaking",
        "flower_pale_garden",
        "pale_garden_flowers",
        "pale_garden_vegetation",
        "pale_moss_vegetation",
        "pale_moss_patch",
        "pale_moss_patch_bonemeal",
        "patch_bush",
        "patch_firefly_bush",
        "wildflowers_birch_forest",
        "wildflowers_meadow",
        "patch_dry_grass",
        "patch_leaf_litter",
        "fallen_oak_tree",
        "fallen_birch_tree",
        "fallen_super_birch_tree",
        "fallen_jungle_tree",
        "fallen_spruce_tree",
        "oak_bees_0002_leaf_litter",
        "birch_bees_0002_leaf_litter",
        "fancy_oak_bees_0002_leaf_litter",
        "oak_leaf_litter",
        "dark_oak_leaf_litter",
        "birch_leaf_litter",
        "fancy_oak_leaf_litter",
        "trees_birch_and_oak_leaf_litter",
        "trees_dark_forest_leaf_litter"
    );
    public static final List<String> PLACEMENT_IDS = List.of(
        "pale_oak_checked",
        "pale_oak_creaking_checked",
        "flower_pale_garden",
        "pale_garden_vegetation",
        "pale_garden_flowers",
        "pale_moss_patch",
        "patch_bush",
        "patch_firefly_bush_near_water",
        "patch_firefly_bush_near_water_swamp",
        "patch_firefly_bush_swamp",
        "wildflowers_birch_forest",
        "wildflowers_meadow",
        "patch_dry_grass_badlands",
        "patch_dry_grass_desert",
        "patch_leaf_litter",
        "fallen_oak_tree",
        "fallen_birch_tree",
        "fallen_super_birch_tree",
        "fallen_jungle_tree",
        "fallen_spruce_tree",
        "oak_bees_0002_leaf_litter",
        "birch_bees_0002_leaf_litter",
        "fancy_oak_bees_0002_leaf_litter",
        "oak_leaf_litter",
        "dark_oak_leaf_litter",
        "birch_leaf_litter",
        "fancy_oak_leaf_litter",
        "trees_dark_forest_leaf_litter",
        "trees_badlands_leaf_litter",
        "trees_birch_and_oak_leaf_litter",
        "placed_fallen_oak_tree",
        "placed_fallen_birch_tree",
        "placed_common_fallen_birch_tree",
        "placed_fallen_super_birch_tree",
        "placed_fallen_jungle_tree",
        "placed_fallen_spruce_tree",
        "placed_rare_fallen_spruce_tree"
    );
    public static final List<String> BLOCK_TAG_IDS = List.of(
        "pale_oak_logs",
        "happy_ghast_avoids",
        "triggers_ambient_desert_sand_block_sounds",
        "triggers_ambient_desert_dry_vegetation_block_sounds",
        "triggers_ambient_dried_ghast_block_sounds",
        "spawn_falling_leaves",
        "spawn_falling_needles"
    );
    public static final List<String> ITEM_TAG_IDS = List.of(
        "pale_oak_logs",
        "happy_ghast_tempt_items",
        "happy_ghast_food",
        "harnesses",
        "bundles"
    );
    public static final List<String> ENTITY_TAG_IDS = List.of(
        "followable_friendly_mobs"
    );

    public static void bootstrap() {
        DataTransformer.onDataTransformation(transformer -> {
            String namespace = VanillaBackport.MOD_ID;
            PARTICLE_IDS.forEach(id -> remap(transformer, namespace, id));
            BLOCK_IDS.forEach(id -> remap(transformer, namespace, id));
            ITEM_IDS.forEach(id -> remap(transformer, namespace, id));
            BLOCK_ENTITY_IDS.forEach(id -> remap(transformer, namespace, id));
            ENTITY_IDS.forEach(id -> remap(transformer, namespace, id));
            SOUND_EVENT_IDS.forEach(id -> remap(transformer, namespace, id));
            JUKEBOX_SONG_IDS.forEach(id -> remap(transformer, namespace, id));
            PAINTING_VARIANT_IDS.forEach(id -> remap(transformer, namespace, id));
            SENSOR_TYPE_IDS.forEach(id -> remap(transformer, namespace, id));
            BIOME_IDS.forEach(id -> remap(transformer, namespace, id));
            TREE_DECORATOR_IDS.forEach(id -> remap(transformer, namespace, id));
            TRIM_MATERIAL_IDS.forEach(id -> remap(transformer, namespace, id));
            WOOD_TYPE_IDS.forEach(id -> remap(transformer, namespace, id));
            FEATURE_IDS.forEach(id -> remap(transformer, namespace, id));
            PLACEMENT_IDS.forEach(id -> remap(transformer, namespace, id));
            BLOCK_TAG_IDS.forEach(id -> remap(transformer, namespace, id));
            ITEM_TAG_IDS.forEach(id -> remap(transformer, namespace, id));
            ENTITY_TAG_IDS.forEach(id -> remap(transformer, namespace, id));

            String everycompat = "everycomp";
            if (Environment.hasModLoaded(everycompat)) {
                transformer.add(original -> {
                    String path = original.getPath();
                    if (path.contains(namespace)) {
                        String newPath = path.replaceAll(namespace, VanillaBackport.NAMESPACE);
                        return new ResourceLocation(original.getNamespace(), newPath);
                    }

                    return null;
                });
            }
        });
    }

    private static void remap(DataTransformer.Transformer transformer, String namespace, String id) {
        transformer.remap(new ResourceLocation(namespace, id), VanillaBackport.vanilla(id));
    }
}
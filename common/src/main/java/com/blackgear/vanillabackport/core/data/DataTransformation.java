package com.blackgear.vanillabackport.core.data;

import com.blackgear.platform.common.data.DataTransformer;
import com.blackgear.vanillabackport.core.VanillaBackport;

public class DataTransformation {
    public static void bootstrap() {
        // Particles
        remap("pale_oak_leaves");
        remap("trail");
        remap("firefly");
        remap("tinted_leaves");
        remap("tinted_needles");

        // Blocks
        remap("pale_oak_leaves");
        remap("pale_oak_planks");
        remap("pale_oak_stairs");
        remap("pale_oak_slab");
        remap("pale_oak_fence");
        remap("pale_oak_fence_gate");
        remap("pale_oak_door");
        remap("pale_oak_wood");
        remap("pale_oak_log");
        remap("stripped_pale_oak_wood");
        remap("stripped_pale_oak_log");
        remap("pale_moss_block");
        remap("pale_moss_carpet");
        remap("pale_hanging_moss");
        remap("open_eyeblossom");
        remap("closed_eyeblossom");
        remap("pale_oak_sapling");
        remap("potted_open_eyeblossom");
        remap("potted_closed_eyeblossom");
        remap("potted_pale_oak_sapling");
        remap("creaking_heart");
        remap("pale_oak_sign");
        remap("pale_oak_wall_sign");
        remap("pale_oak_hanging_sign");
        remap("pale_oak_wall_hanging_sign");
        remap("pale_oak_pressure_plate");
        remap("pale_oak_trapdoor");
        remap("resin_clump");
        remap("resin_block");
        remap("resin_bricks");
        remap("resin_brick_stairs");
        remap("resin_brick_slab");
        remap("resin_brick_wall");
        remap("chiseled_resin_bricks");
        remap("dried_ghast");
        remap("bush");
        remap("firefly_bush");
        remap("wildflowers");
        remap("leaf_litter");
        remap("cactus_flower");
        remap("short_dry_grass");
        remap("tall_dry_grass");

        // Items
        remap("resin_brick");
        remap("pale_oak_boat");
        remap("pale_oak_chest_boat");
        remap("creaking_spawn_egg");
        remap("happy_ghast_spawn_egg");
        remap("white_harness");
        remap("orange_harness");
        remap("magenta_harness");
        remap("light_blue_harness");
        remap("yellow_harness");
        remap("lime_harness");
        remap("pink_harness");
        remap("gray_harness");
        remap("light_gray_harness");
        remap("cyan_harness");
        remap("purple_harness");
        remap("blue_harness");
        remap("brown_harness");
        remap("black_harness");
        remap("music_disc_tears");
        remap("music_disc_lava_chicken");
        remap("white_bundle");
        remap("orange_bundle");
        remap("magenta_bundle");
        remap("light_blue_bundle");
        remap("yellow_bundle");
        remap("lime_bundle");
        remap("pink_bundle");
        remap("gray_bundle");
        remap("light_gray_bundle");
        remap("cyan_bundle");
        remap("purple_bundle");
        remap("blue_bundle");
        remap("brown_bundle");
        remap("black_bundle");

        // Block Entities
        remap("creaking_heart");

        // Entities
        remap("creaking");
        remap("happy_ghast");
        remap("pale_oak_boat");
        remap("pale_oak_chest_boat");

        // Sound Events
        remap("block.eyeblossom.open_long");
        remap("block.eyeblossom.open");
        remap("block.eyeblossom.close_long");
        remap("block.eyeblossom.close");
        remap("block.eyeblossom.idle");
        remap("block.pale_hanging_moss.idle");
        remap("block.creaking_heart.break");
        remap("block.creaking_heart.fall");
        remap("block.creaking_heart.hit");
        remap("block.creaking_heart.hurt");
        remap("block.creaking_heart.place");
        remap("block.creaking_heart.step");
        remap("block.creaking_heart.idle");
        remap("block.creaking_heart.spawn");
        remap("block.resin.break");
        remap("block.resin.fall");
        remap("block.resin.place");
        remap("block.resin.step");
        remap("block.resin_bricks.break");
        remap("block.resin_bricks.fall");
        remap("block.resin_bricks.hit");
        remap("block.resin_bricks.place");
        remap("block.resin_bricks.step");
        remap("block.dried_ghast.break");
        remap("block.dried_ghast.step");
        remap("block.dried_ghast.fall");
        remap("block.dried_ghast.ambient");
        remap("block.dried_ghast.ambient_water");
        remap("block.dried_ghast.place");
        remap("block.dried_ghast.place_in_water");
        remap("block.dried_ghast.transition");
        remap("block.leaf_litter.break");
        remap("block.leaf_litter.step");
        remap("block.leaf_litter.place");
        remap("block.leaf_litter.hit");
        remap("block.leaf_litter.fall");
        remap("block.cactus_flower.break");
        remap("block.cactus_flower.place");
        remap("block.firefly_bush.idle");
        remap("block.sand.idle");
        remap("block.deadbush.idle");
        remap("block.dry_grass.ambient");
        remap("item.bundle.insert_fail");
        remap("entity.creaking.ambient");
        remap("entity.creaking.activate");
        remap("entity.creaking.deactivate");
        remap("entity.creaking.attack");
        remap("entity.creaking.death");
        remap("entity.creaking.step");
        remap("entity.creaking.freeze");
        remap("entity.creaking.unfreeze");
        remap("entity.creaking.spawn");
        remap("entity.creaking.sway");
        remap("entity.creaking.twitch");
        remap("entity.ghastling.ambient");
        remap("entity.ghastling.death");
        remap("entity.ghastling.hurt");
        remap("entity.ghastling.spawn");
        remap("entity.happy_ghast.ambient");
        remap("entity.happy_ghast.death");
        remap("entity.happy_ghast.hurt");
        remap("entity.happy_ghast.riding");
        remap("entity.happy_ghast.equip");
        remap("entity.happy_ghast.unequip");
        remap("entity.happy_ghast.harness_goggles_up");
        remap("entity.happy_ghast.harness_goggles_down");
        remap("entity.parrot.imitate.creaking");
        remap("music_disc.tears");
        remap("music_disc.lava_chicken");

        // Jukebox Songs
        remap("tears");
        remap("lava_chicken");

        // Painting Variants
        remap("dennis");

        // Sensor Types
        remap("nearest_adult_any_type");
        remap("happy_ghast_temptations");

        // Biomes
        remap("pale_garden");

        // Tree Decorators
        remap("pale_moss");
        remap("creaking_heart");

        // Trim Materials
        remap("resin");

        // Wood Types
        remap("pale_oak");

        // Features
        remap("pale_oak");
        remap("pale_oak_bonemeal");
        remap("pale_oak_creaking");
        remap("flower_pale_garden");
        remap("pale_garden_flowers");
        remap("pale_garden_vegetation");
        remap("pale_moss_vegetation");
        remap("pale_moss_patch");
        remap("pale_moss_patch_bonemeal");

        remap("patch_bush");
        remap("patch_firefly_bush");
        remap("wildflowers_birch_forest");
        remap("wildflowers_meadow");
        remap("patch_dry_grass");
        remap("patch_leaf_litter");
        remap("fallen_oak_tree");
        remap("fallen_birch_tree");
        remap("fallen_super_birch_tree");
        remap("fallen_jungle_tree");
        remap("fallen_spruce_tree");
        remap("oak_bees_0002_leaf_litter");
        remap("birch_bees_0002_leaf_litter");
        remap("fancy_oak_bees_0002_leaf_litter");
        remap("oak_leaf_litter");
        remap("dark_oak_leaf_litter");
        remap("birch_leaf_litter");
        remap("fancy_oak_leaf_litter");
        remap("trees_birch_and_oak_leaf_litter");
        DataTransformer.apply(VanillaBackport.resource("dark_forest_vegetation"), VanillaBackport.vanilla("trees_dark_forest_leaf_litter"));

        // Placements
        remap("pale_oak_checked");
        remap("pale_oak_creaking_checked");
        remap("flower_pale_garden");
        remap("pale_garden_vegetation");
        remap("pale_garden_flowers");
        remap("pale_moss_patch");

        remap("patch_bush");
        remap("patch_firefly_bush_near_water");
        remap("patch_firefly_bush_near_water_swamp");
        remap("patch_firefly_bush_swamp");
        remap("wildflowers_birch_forest");
        remap("wildflowers_meadow");
        remap("patch_dry_grass_badlands");
        remap("patch_dry_grass_desert");
        remap("patch_leaf_litter");
        remap("fallen_oak_tree");
        remap("fallen_birch_tree");
        remap("fallen_super_birch_tree");
        remap("fallen_jungle_tree");
        remap("fallen_spruce_tree");
        remap("oak_bees_0002_leaf_litter");
        remap("birch_bees_0002_leaf_litter");
        remap("fancy_oak_bees_0002_leaf_litter");
        remap("oak_leaf_litter");
        remap("dark_oak_leaf_litter");
        remap("birch_leaf_litter");
        remap("fancy_oak_leaf_litter");
        DataTransformer.apply(VanillaBackport.resource("dark_forest_vegetation"), VanillaBackport.vanilla("trees_dark_forest_leaf_litter"));
        DataTransformer.apply(VanillaBackport.resource("trees_badlands"), VanillaBackport.vanilla("trees_badlands_leaf_litter"));
        remap("trees_birch_and_oak_leaf_litter");
        remap("placed_fallen_oak_tree");
        remap("placed_fallen_birch_tree");
        remap("placed_common_fallen_birch_tree");
        remap("placed_fallen_super_birch_tree");
        remap("placed_fallen_jungle_tree");
        remap("placed_fallen_spruce_tree");
        remap("placed_rare_fallen_spruce_tree");

        // Block Tags
        remap("pale_oak_logs");
        remap("happy_ghast_avoids");
        remap("triggers_ambient_desert_sand_block_sounds");
        remap("triggers_ambient_desert_dry_vegetation_block_sounds");
        remap("triggers_ambient_dried_ghast_block_sounds");
        remap("spawn_falling_leaves");
        remap("spawn_falling_needles");

        // Item Tags
        remap("pale_oak_logs");
        remap("happy_ghast_tempt_items");
        remap("happy_ghast_food");
        remap("harnesses");
        remap("bundles");

        // Entity Type Tags
        remap("followable_friendly_mobs");
    }

    private static void remap(String mappings) {
        DataTransformer.apply(VanillaBackport.resource(mappings), VanillaBackport.vanilla(mappings));
    }
}
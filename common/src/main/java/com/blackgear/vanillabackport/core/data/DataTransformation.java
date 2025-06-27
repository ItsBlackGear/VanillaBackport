package com.blackgear.vanillabackport.core.data;

import com.blackgear.platform.common.data.DataTransformer;

import static com.blackgear.vanillabackport.core.VanillaBackport.resource;
import static com.blackgear.vanillabackport.core.VanillaBackport.vanilla;

public class DataTransformation {
    public static void bootstrap() {
        // Particles
        DataTransformer.apply(resource("pale_oak_leaves"), vanilla("pale_oak_leaves"));
        DataTransformer.apply(resource("trail"), vanilla("trail"));

        // Blocks
        DataTransformer.apply(resource("pale_oak_leaves"), vanilla("pale_oak_leaves"));
        DataTransformer.apply(resource("pale_oak_planks"), vanilla("pale_oak_planks"));
        DataTransformer.apply(resource("pale_oak_stairs"), vanilla("pale_oak_stairs"));
        DataTransformer.apply(resource("pale_oak_slab"), vanilla("pale_oak_slab"));
        DataTransformer.apply(resource("pale_oak_fence"), vanilla("pale_oak_fence"));
        DataTransformer.apply(resource("pale_oak_fence_gate"), vanilla("pale_oak_fence_gate"));
        DataTransformer.apply(resource("pale_oak_door"), vanilla("pale_oak_door"));
        DataTransformer.apply(resource("pale_oak_wood"), vanilla("pale_oak_wood"));
        DataTransformer.apply(resource("pale_oak_log"), vanilla("pale_oak_log"));
        DataTransformer.apply(resource("stripped_pale_oak_wood"), vanilla("stripped_pale_oak_wood"));
        DataTransformer.apply(resource("stripped_pale_oak_log"), vanilla("stripped_pale_oak_log"));
        DataTransformer.apply(resource("pale_moss_block"), vanilla("pale_moss_block"));
        DataTransformer.apply(resource("pale_moss_carpet"), vanilla("pale_moss_carpet"));
        DataTransformer.apply(resource("pale_hanging_moss"), vanilla("pale_hanging_moss"));
        DataTransformer.apply(resource("open_eyeblossom"), vanilla("open_eyeblossom"));
        DataTransformer.apply(resource("closed_eyeblossom"), vanilla("closed_eyeblossom"));
        DataTransformer.apply(resource("pale_oak_sapling"), vanilla("pale_oak_sapling"));
        DataTransformer.apply(resource("potted_open_eyeblossom"), vanilla("potted_open_eyeblossom"));
        DataTransformer.apply(resource("potted_closed_eyeblossom"), vanilla("potted_closed_eyeblossom"));
        DataTransformer.apply(resource("potted_pale_oak_sapling"), vanilla("potted_pale_oak_sapling"));
        DataTransformer.apply(resource("creaking_heart"), vanilla("creaking_heart"));
        DataTransformer.apply(resource("pale_oak_sign"), vanilla("pale_oak_sign"));
        DataTransformer.apply(resource("pale_oak_wall_sign"), vanilla("pale_oak_wall_sign"));
        DataTransformer.apply(resource("pale_oak_hanging_sign"), vanilla("pale_oak_hanging_sign"));
        DataTransformer.apply(resource("pale_oak_wall_hanging_sign"), vanilla("pale_oak_wall_hanging_sign"));
        DataTransformer.apply(resource("pale_oak_pressure_plate"), vanilla("pale_oak_pressure_plate"));
        DataTransformer.apply(resource("pale_oak_trapdoor"), vanilla("pale_oak_trapdoor"));
        DataTransformer.apply(resource("resin_clump"), vanilla("resin_clump"));
        DataTransformer.apply(resource("resin_block"), vanilla("resin_block"));
        DataTransformer.apply(resource("resin_bricks"), vanilla("resin_bricks"));
        DataTransformer.apply(resource("resin_brick_stairs"), vanilla("resin_brick_stairs"));
        DataTransformer.apply(resource("resin_brick_slab"), vanilla("resin_brick_slab"));
        DataTransformer.apply(resource("resin_brick_wall"), vanilla("resin_brick_wall"));
        DataTransformer.apply(resource("chiseled_resin_bricks"), vanilla("chiseled_resin_bricks"));
        DataTransformer.apply(resource("dried_ghast"), vanilla("dried_ghast"));

        // Items
        DataTransformer.apply(resource("resin_brick"), vanilla("resin_brick"));
        DataTransformer.apply(resource("pale_oak_boat"), vanilla("pale_oak_boat"));
        DataTransformer.apply(resource("pale_oak_chest_boat"), vanilla("pale_oak_chest_boat"));
        DataTransformer.apply(resource("creaking_spawn_egg"), vanilla("creaking_spawn_egg"));
        DataTransformer.apply(resource("happy_ghast_spawn_egg"), vanilla("happy_ghast_spawn_egg"));
        DataTransformer.apply(resource("white_harness"), vanilla("white_harness"));
        DataTransformer.apply(resource("orange_harness"), vanilla("orange_harness"));
        DataTransformer.apply(resource("magenta_harness"), vanilla("magenta_harness"));
        DataTransformer.apply(resource("light_blue_harness"), vanilla("light_blue_harness"));
        DataTransformer.apply(resource("yellow_harness"), vanilla("yellow_harness"));
        DataTransformer.apply(resource("lime_harness"), vanilla("lime_harness"));
        DataTransformer.apply(resource("pink_harness"), vanilla("pink_harness"));
        DataTransformer.apply(resource("gray_harness"), vanilla("gray_harness"));
        DataTransformer.apply(resource("light_gray_harness"), vanilla("light_gray_harness"));
        DataTransformer.apply(resource("cyan_harness"), vanilla("cyan_harness"));
        DataTransformer.apply(resource("purple_harness"), vanilla("purple_harness"));
        DataTransformer.apply(resource("blue_harness"), vanilla("blue_harness"));
        DataTransformer.apply(resource("brown_harness"), vanilla("brown_harness"));
        DataTransformer.apply(resource("black_harness"), vanilla("black_harness"));
        DataTransformer.apply(resource("music_disc_tears"), vanilla("music_disc_tears"));
        DataTransformer.apply(resource("music_disc_lava_chicken"), vanilla("music_disc_lava_chicken"));

        // Block Entities
        DataTransformer.apply(resource("creaking_heart"), vanilla("creaking_heart"));

        // Entities
        DataTransformer.apply(resource("creaking"), vanilla("creaking"));
        DataTransformer.apply(resource("happy_ghast"), vanilla("happy_ghast"));
        DataTransformer.apply(resource("pale_oak_boat"), vanilla("pale_oak_boat"));
        DataTransformer.apply(resource("pale_oak_chest_boat"), vanilla("pale_oak_chest_boat"));

        // Sound Events
        DataTransformer.apply(resource("block.eyeblossom.open_long"), vanilla("block.eyeblossom.open_long"));
        DataTransformer.apply(resource("block.eyeblossom.open"), vanilla("block.eyeblossom.open"));
        DataTransformer.apply(resource("block.eyeblossom.close_long"), vanilla("block.eyeblossom.close_long"));
        DataTransformer.apply(resource("block.eyeblossom.close"), vanilla("block.eyeblossom.close"));
        DataTransformer.apply(resource("block.eyeblossom.idle"), vanilla("block.eyeblossom.idle"));
        DataTransformer.apply(resource("block.pale_hanging_moss.idle"), vanilla("block.pale_hanging_moss.idle"));
        DataTransformer.apply(resource("block.creaking_heart.break"), vanilla("block.creaking_heart.break"));
        DataTransformer.apply(resource("block.creaking_heart.fall"), vanilla("block.creaking_heart.fall"));
        DataTransformer.apply(resource("block.creaking_heart.hit"), vanilla("block.creaking_heart.hit"));
        DataTransformer.apply(resource("block.creaking_heart.hurt"), vanilla("block.creaking_heart.hurt"));
        DataTransformer.apply(resource("block.creaking_heart.place"), vanilla("block.creaking_heart.place"));
        DataTransformer.apply(resource("block.creaking_heart.step"), vanilla("block.creaking_heart.step"));
        DataTransformer.apply(resource("block.creaking_heart.idle"), vanilla("block.creaking_heart.idle"));
        DataTransformer.apply(resource("block.creaking_heart.spawn"), vanilla("block.creaking_heart.spawn"));
        DataTransformer.apply(resource("block.resin.break"), vanilla("block.resin.break"));
        DataTransformer.apply(resource("block.resin.fall"), vanilla("block.resin.fall"));
        DataTransformer.apply(resource("block.resin.place"), vanilla("block.resin.place"));
        DataTransformer.apply(resource("block.resin.step"), vanilla("block.resin.step"));
        DataTransformer.apply(resource("block.resin_bricks.break"), vanilla("block.resin_bricks.break"));
        DataTransformer.apply(resource("block.resin_bricks.fall"), vanilla("block.resin_bricks.fall"));
        DataTransformer.apply(resource("block.resin_bricks.hit"), vanilla("block.resin_bricks.hit"));
        DataTransformer.apply(resource("block.resin_bricks.place"), vanilla("block.resin_bricks.place"));
        DataTransformer.apply(resource("block.resin_bricks.step"), vanilla("block.resin_bricks.step"));
        DataTransformer.apply(resource("block.dried_ghast.break"), vanilla("block.dried_ghast.break"));
        DataTransformer.apply(resource("block.dried_ghast.step"), vanilla("block.dried_ghast.step"));
        DataTransformer.apply(resource("block.dried_ghast.fall"), vanilla("block.dried_ghast.fall"));
        DataTransformer.apply(resource("block.dried_ghast.ambient"), vanilla("block.dried_ghast.ambient"));
        DataTransformer.apply(resource("block.dried_ghast.ambient_water"), vanilla("block.dried_ghast.ambient_water"));
        DataTransformer.apply(resource("block.dried_ghast.place"), vanilla("block.dried_ghast.place"));
        DataTransformer.apply(resource("block.dried_ghast.place_in_water"), vanilla("block.dried_ghast.place_in_water"));
        DataTransformer.apply(resource("block.dried_ghast.transition"), vanilla("block.dried_ghast.transition"));
        DataTransformer.apply(resource("entity.creaking.ambient"), vanilla("entity.creaking.ambient"));
        DataTransformer.apply(resource("entity.creaking.activate"), vanilla("entity.creaking.activate"));
        DataTransformer.apply(resource("entity.creaking.deactivate"), vanilla("entity.creaking.deactivate"));
        DataTransformer.apply(resource("entity.creaking.attack"), vanilla("entity.creaking.attack"));
        DataTransformer.apply(resource("entity.creaking.death"), vanilla("entity.creaking.death"));
        DataTransformer.apply(resource("entity.creaking.step"), vanilla("entity.creaking.step"));
        DataTransformer.apply(resource("entity.creaking.freeze"), vanilla("entity.creaking.freeze"));
        DataTransformer.apply(resource("entity.creaking.unfreeze"), vanilla("entity.creaking.unfreeze"));
        DataTransformer.apply(resource("entity.creaking.spawn"), vanilla("entity.creaking.spawn"));
        DataTransformer.apply(resource("entity.creaking.sway"), vanilla("entity.creaking.sway"));
        DataTransformer.apply(resource("entity.creaking.twitch"), vanilla("entity.creaking.twitch"));
        DataTransformer.apply(resource("entity.ghastling.ambient"), vanilla("entity.ghastling.ambient"));
        DataTransformer.apply(resource("entity.ghastling.death"), vanilla("entity.ghastling.death"));
        DataTransformer.apply(resource("entity.ghastling.hurt"), vanilla("entity.ghastling.hurt"));
        DataTransformer.apply(resource("entity.ghastling.spawn"), vanilla("entity.ghastling.spawn"));
        DataTransformer.apply(resource("entity.happy_ghast.ambient"), vanilla("entity.happy_ghast.ambient"));
        DataTransformer.apply(resource("entity.happy_ghast.death"), vanilla("entity.happy_ghast.death"));
        DataTransformer.apply(resource("entity.happy_ghast.hurt"), vanilla("entity.happy_ghast.hurt"));
        DataTransformer.apply(resource("entity.happy_ghast.riding"), vanilla("entity.happy_ghast.riding"));
        DataTransformer.apply(resource("entity.happy_ghast.equip"), vanilla("entity.happy_ghast.equip"));
        DataTransformer.apply(resource("entity.happy_ghast.unequip"), vanilla("entity.happy_ghast.unequip"));
        DataTransformer.apply(resource("entity.happy_ghast.harness_goggles_up"), vanilla("entity.happy_ghast.harness_goggles_up"));
        DataTransformer.apply(resource("entity.happy_ghast.harness_goggles_down"), vanilla("entity.happy_ghast.harness_goggles_down"));
        DataTransformer.apply(resource("entity.parrot.imitate.creaking"), vanilla("entity.parrot.imitate.creaking"));
        DataTransformer.apply(resource("music_disc.tears"), vanilla("music_disc.tears"));
        DataTransformer.apply(resource("music_disc.lava_chicken"), vanilla("music_disc.lava_chicken"));

        // Creative Tabs
        DataTransformer.apply(resource("the_garden_awakens"), vanilla("the_garden_awakens"));
        DataTransformer.apply(resource("chase_the_skies"), vanilla("chase_the_skies"));

        // Jukebox Songs
        DataTransformer.apply(resource("tears"), vanilla("tears"));
        DataTransformer.apply(resource("lava_chicken"), vanilla("lava_chicken"));

        // Painting Variants
        DataTransformer.apply(resource("dennis"), vanilla("dennis"));

        // Sensor Types
        DataTransformer.apply(resource("nearest_adult_any_type"), vanilla("nearest_adult_any_type"));
        DataTransformer.apply(resource("happy_ghast_temptations"), vanilla("happy_ghast_temptations"));

        // Biomes
        DataTransformer.apply(resource("pale_garden"), vanilla("pale_garden"));

        // Tree Decorators
        DataTransformer.apply(resource("pale_moss"), vanilla("pale_moss"));
        DataTransformer.apply(resource("creaking_heart"), vanilla("creaking_heart"));

        // Trim Materials
        DataTransformer.apply(resource("resin"), vanilla("resin"));

        // Wood Types
        DataTransformer.apply(resource("pale_oak"), vanilla("pale_oak"));

        // Features
        DataTransformer.apply(resource("pale_oak"), vanilla("pale_oak"));
        DataTransformer.apply(resource("pale_oak_bonemeal"), vanilla("pale_oak_bonemeal"));
        DataTransformer.apply(resource("pale_oak_creaking"), vanilla("pale_oak_creaking"));
        DataTransformer.apply(resource("pale_oak_creaking_bonemeal"), vanilla("pale_oak_creaking_bonemeal"));
        DataTransformer.apply(resource("flower_pale_garden"), vanilla("flower_pale_garden"));
        DataTransformer.apply(resource("pale_garden_flowers"), vanilla("pale_garden_flowers"));
        DataTransformer.apply(resource("pale_garden_vegetation"), vanilla("pale_garden_vegetation"));
        DataTransformer.apply(resource("pale_moss_vegetation"), vanilla("pale_moss_vegetation"));
        DataTransformer.apply(resource("pale_moss_patch"), vanilla("pale_moss_patch"));
        DataTransformer.apply(resource("pale_moss_patch_bonemeal"), vanilla("pale_moss_patch_bonemeal"));

        // Placements
        DataTransformer.apply(resource("pale_oak_checked"), vanilla("pale_oak_checked"));
        DataTransformer.apply(resource("pale_oak_creaking_checked"), vanilla("pale_oak_creaking_checked"));
        DataTransformer.apply(resource("flower_pale_garden"), vanilla("flower_pale_garden"));
        DataTransformer.apply(resource("pale_garden_vegetation"), vanilla("pale_garden_vegetation"));
        DataTransformer.apply(resource("pale_garden_flowers"), vanilla("pale_garden_flowers"));
        DataTransformer.apply(resource("pale_moss_patch"), vanilla("pale_moss_patch"));

        // Block Tags
        DataTransformer.apply(resource("pale_oak_logs"), vanilla("pale_oak_logs"));
        DataTransformer.apply(resource("happy_ghast_avoids"), vanilla("happy_ghast_avoids"));

        // Item Tags
        DataTransformer.apply(resource("pale_oak_logs"), vanilla("pale_oak_logs"));
        DataTransformer.apply(resource("happy_ghast_tempt_items"), vanilla("happy_ghast_tempt_items"));
        DataTransformer.apply(resource("happy_ghast_food"), vanilla("happy_ghast_food"));
        DataTransformer.apply(resource("harnesses"), vanilla("harnesses"));

        // Entity Type Tags
        DataTransformer.apply(resource("followable_friendly_mobs"), vanilla("followable_friendly_mobs"));
    }
}
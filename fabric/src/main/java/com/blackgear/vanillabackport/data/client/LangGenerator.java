package com.blackgear.vanillabackport.data.client;

import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.blackgear.vanillabackport.common.registries.ModEntities;
import com.blackgear.vanillabackport.common.registries.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricLanguageProvider;

public class LangGenerator extends FabricLanguageProvider {
    public LangGenerator(FabricDataOutput dataOutput) {
        super(dataOutput);
    }

    @Override
    public void generateTranslations(TranslationBuilder builder) {
        // THE GARDEN AWAKENS

        // Biomes
        builder.add("biome.vanillabackport.pale_garden", "Pale Garden");

        // Blocks
        builder.add(ModBlocks.PALE_HANGING_MOSS.get(), "Pale Hanging Moss");
        builder.add(ModBlocks.PALE_MOSS_BLOCK.get(), "Pale Moss Block");
        builder.add(ModBlocks.PALE_MOSS_CARPET.get(), "Pale Moss Carpet");

        builder.add(ModBlocks.PALE_OAK_BUTTON.get(), "Pale Oak Button");
        builder.add(ModBlocks.PALE_OAK_DOOR.get(), "Pale Oak Door");
        builder.add(ModBlocks.PALE_OAK_FENCE.get(), "Pale Oak Fence");
        builder.add(ModBlocks.PALE_OAK_FENCE_GATE.get(), "Pale Oak Fence Gate");
        builder.add(ModBlocks.PALE_OAK_HANGING_SIGN.getFirst().get(), "Pale Oak Hanging Sign");
        builder.add(ModBlocks.PALE_OAK_LEAVES.get(), "Pale Oak Leaves");
        builder.add(ModBlocks.PALE_OAK_LOG.get(), "Pale Oak Log");
        builder.add(ModBlocks.PALE_OAK_PLANKS.get(), "Pale Oak Planks");
        builder.add(ModBlocks.PALE_OAK_PRESSURE_PLATE.get(), "Pale Oak Pressure Plate");
        builder.add(ModBlocks.PALE_OAK_SAPLING.get(), "Pale Oak Sapling");
        builder.add(ModBlocks.PALE_OAK_SIGN.getFirst().get(), "Pale Oak Sign");
        builder.add(ModBlocks.PALE_OAK_SLAB.get(), "Pale Oak Slab");
        builder.add(ModBlocks.PALE_OAK_STAIRS.get(), "Pale Oak Stairs");
        builder.add(ModBlocks.PALE_OAK_TRAPDOOR.get(), "Pale Oak Trapdoor");
        builder.add("block.vanillabackport.pale_oak_wall_hanging_sign", "Pale Oak Wall Hanging Sign");
        builder.add("block.vanillabackport.pale_oak_wall_sign", "Pale Oak Wall Sign");
        builder.add(ModBlocks.PALE_OAK_WOOD.get(), "Pale Oak Wood");
        builder.add(ModBlocks.STRIPPED_PALE_OAK_LOG.get(), "Stripped Pale Oak Log");
        builder.add(ModBlocks.STRIPPED_PALE_OAK_WOOD.get(), "Stripped Pale Oak Wood");

        builder.add(ModBlocks.OPEN_EYEBLOSSOM.get(), "Open Eyeblossom");
        builder.add(ModBlocks.CLOSED_EYEBLOSSOM.get(), "Closed Eyeblossom");
        builder.add(ModBlocks.POTTED_PALE_OAK_SAPLING.get(), "Potted Pale Oak Sapling");
        builder.add(ModBlocks.POTTED_OPEN_EYEBLOSSOM.get(), "Potted Open Eyeblossom");
        builder.add(ModBlocks.POTTED_CLOSED_EYEBLOSSOM.get(), "Potted Closed Eyeblossom");

        builder.add(ModBlocks.CREAKING_HEART.get(), "Creaking Heart");

        builder.add(ModBlocks.RESIN_CLUMP.get(), "Resin Clump");
        builder.add(ModBlocks.RESIN_BLOCK.get(), "Block of Resin");
        builder.add(ModBlocks.RESIN_BRICKS.get(), "Resin Bricks");
        builder.add(ModBlocks.RESIN_BRICK_STAIRS.get(), "Resin Brick Stairs");
        builder.add(ModBlocks.RESIN_BRICK_SLAB.get(), "Resin Brick Slab");
        builder.add(ModBlocks.RESIN_BRICK_WALL.get(), "Resin Brick Wall");
        builder.add(ModBlocks.CHISELED_RESIN_BRICKS.get(), "Chiseled Resin Bricks");

        // Items
        builder.add(ModItems.PALE_OAK_BOAT.get(), "Pale Oak Boat");
        builder.add(ModItems.PALE_OAK_CHEST_BOAT.get(), "Pale Oak Boat with Chest");
        builder.add(ModItems.RESIN_BRICK.get(), "Resin Brick");
        builder.add(ModItems.CREAKING_SPAWN_EGG.get(), "Creaking Spawn Egg");

        // Entities
        builder.add(ModEntities.CREAKING.get(), "Creaking");
        builder.add(ModEntities.PALE_OAK_BOAT.get(), "Pale Oak Boat");
        builder.add(ModEntities.PALE_OAK_CHEST_BOAT.get(), "Pale Oak Boat with Chest");

        // Trims
        builder.add("trim_material.vanillabackport.resin", "Resin Material");

        // Block Subtitles
        builder.add("subtitles.block.creaking_heart.hurt", "Creaking Heart grumbles");
        builder.add("subtitles.block.creaking_heart.idle", "Eerie noise");
        builder.add("subtitles.block.creaking_heart.spawn", "Creaking Heart awakens");

        builder.add("subtitles.block.pale_hanging_moss.idle", "Eerie noise");

        builder.add("subtitles.block.eyeblossom.close", "Eyeblossom closes");
        builder.add("subtitles.block.eyeblossom.idle", "Eyeblossom whispers");
        builder.add("subtitles.block.eyeblossom.open", "Eyeblossom opens");

        // Entity Subtitles
        builder.add("subtitles.entity.creaking.activate", "Creaking watches");
        builder.add("subtitles.entity.creaking.ambient", "Creaking creaks");
        builder.add("subtitles.entity.creaking.deactivate", "Creaking calms");
        builder.add("subtitles.entity.creaking.attack", "Creaking attacks");
        builder.add("subtitles.entity.creaking.death", "Creaking crumbles");
        builder.add("subtitles.entity.creaking.freeze", "Creaking stop");
        builder.add("subtitles.entity.creaking.spawn", "Creaking manifests");
        builder.add("subtitles.entity.creaking.sway", "Creaking is hit");
        builder.add("subtitles.entity.creaking.twitch", "Creaking twitches");
        builder.add("subtitles.entity.creaking.unfreeze", "Creaking moves");

        builder.add("subtitles.entity.parrot.imitate.creaking", "Parrot creaks");

        // Chase the Skies
        builder.add(ModBlocks.DRIED_GHAST.get(), "Dried Ghast");

        builder.add(ModItems.HAPPY_GHAST_SPAWN_EGG.get(), "Happy Ghast Spawn Egg");

        builder.add(ModItems.WHITE_HARNESS.get(), "White Harness");
        builder.add(ModItems.ORANGE_HARNESS.get(), "Orange Harness");
        builder.add(ModItems.MAGENTA_HARNESS.get(), "Magenta Harness");
        builder.add(ModItems.LIGHT_BLUE_HARNESS.get(), "Light Blue Harness");
        builder.add(ModItems.YELLOW_HARNESS.get(), "Yellow Harness");
        builder.add(ModItems.LIME_HARNESS.get(), "Lime Harness");
        builder.add(ModItems.PINK_HARNESS.get(), "Pink Harness");
        builder.add(ModItems.GRAY_HARNESS.get(), "Gray Harness");
        builder.add(ModItems.LIGHT_GRAY_HARNESS.get(), "Light Gray Harness");
        builder.add(ModItems.CYAN_HARNESS.get(), "Cyan Harness");
        builder.add(ModItems.PURPLE_HARNESS.get(), "Purple Harness");
        builder.add(ModItems.BLUE_HARNESS.get(), "Blue Harness");
        builder.add(ModItems.BROWN_HARNESS.get(), "Brown Harness");
        builder.add(ModItems.GREEN_HARNESS.get(), "Green Harness");
        builder.add(ModItems.RED_HARNESS.get(), "Red Harness");
        builder.add(ModItems.BLACK_HARNESS.get(), "Black Harness");

        builder.add("item.vanillabackport.music_disc_tears", "Music Disc");
        builder.add("item.vanillabackport.music_disc_tears.desc", "Amos Roddy - Tears");

        builder.add("jukebox_song.vanillabackport.tears", "Amos Roddy - Tears");

        builder.add("item.vanillabackport.music_disc_lava_chicken", "Music Disc");
        builder.add("item.vanillabackport.music_disc_lava_chicken.desc", "Hyper Potions - Lava Chicken");

        builder.add("jukebox_song.vanillabackport.lava_chicken", "Hyper Potions - Lava Chicken");

        builder.add("painting.vanillabackport.dennis.author", "Sarah Boeving");
        builder.add("painting.vanillabackport.dennis.title", "Dennis");

        builder.add(ModEntities.HAPPY_GHAST.get(), "Happy Ghast");

        builder.add("subtitles.block.dried_ghast.ambient", "Dried Ghast wheezes");
        builder.add("subtitles.block.dried_ghast.ambient_water", "Dried Ghast rehydrates");
        builder.add("subtitles.block.dried_ghast.place_in_water", "Dried Ghast soaks");
        builder.add("subtitles.block.dried_ghast.transition", "Dried Ghast feels better");

        builder.add("subtitles.entity.ghastling.ambient", "Ghastling coos");
        builder.add("subtitles.entity.ghastling.death", "Ghastling dies");
        builder.add("subtitles.entity.ghastling.hurt", "Ghastling hurts");
        builder.add("subtitles.entity.ghastling.spawn", "Ghastling appears");

        builder.add("subtitles.entity.happy_ghast.ambient", "Happy Ghast croons");
        builder.add("subtitles.entity.happy_ghast.death", "Happy Ghast dies");
        builder.add("subtitles.entity.happy_ghast.hurt", "Happy Ghast hurts");
        builder.add("subtitles.entity.happy_ghast.harness_goggles_down", "Happy Ghast is ready");
        builder.add("subtitles.entity.happy_ghast.harness_goggles_up", "Happy Ghast stops");
        builder.add("subtitles.entity.happy_ghast.unequip", "Harness unequips");
        builder.add("subtitles.entity.happy_ghast.equip", "Harness equips");

        // Spring to Life
        builder.add(ModBlocks.BUSH.get(), "Bush");
        builder.add(ModBlocks.FIREFLY_BUSH.get(), "Firefly Bush");
        builder.add(ModBlocks.WILDFLOWERS.get(), "Wildflowers");
        builder.add(ModBlocks.CACTUS_FLOWER.get(), "Cactus Flower");
        builder.add(ModBlocks.SHORT_DRY_GRASS.get(), "Short Dry Grass");
        builder.add(ModBlocks.TALL_DRY_GRASS.get(), "Tall Dry Grass");
        builder.add(ModBlocks.LEAF_LITTER.get(), "Leaf Litter");

        builder.add("subtitles.block.sand.idle", "Sandy sounds");
        builder.add("subtitles.block.deadbush.idle", "Dry sounds");
        builder.add("subtitles.block.dry_grass.ambient", "Windy sounds");

        // Bundles of Bravery
        builder.add("item.minecraft.bundle.empty", "Empty");
        builder.add("item.minecraft.bundle.empty.description", "Can hold a mixed stack of items");
        builder.add("item.minecraft.bundle.full", "Full");
        
        builder.add("subtitles.item.bundle.insert_fail", "Bundle full");

        builder.add(ModItems.BLACK_BUNDLE.get(), "Black Bundle");
        builder.add(ModItems.WHITE_BUNDLE.get(), "White Bundle");
        builder.add(ModItems.GRAY_BUNDLE.get(), "Gray Bundle");
        builder.add(ModItems.LIGHT_GRAY_BUNDLE.get(), "Light Gray Bundle");
        builder.add(ModItems.LIGHT_BLUE_BUNDLE.get(), "Light Blue Bundle");
        builder.add(ModItems.BLUE_BUNDLE.get(), "Blue Bundle");
        builder.add(ModItems.CYAN_BUNDLE.get(), "Cyan Bundle");
        builder.add(ModItems.YELLOW_BUNDLE.get(), "Yellow Bundle");
        builder.add(ModItems.RED_BUNDLE.get(), "Red Bundle");
        builder.add(ModItems.PURPLE_BUNDLE.get(), "Purple Bundle");
        builder.add(ModItems.MAGENTA_BUNDLE.get(), "Magenta Bundle");
        builder.add(ModItems.PINK_BUNDLE.get(), "Pink Bundle");
        builder.add(ModItems.GREEN_BUNDLE.get(), "Green Bundle");
        builder.add(ModItems.LIME_BUNDLE.get(), "Lime Bundle");
        builder.add(ModItems.BROWN_BUNDLE.get(), "Brown Bundle");
        builder.add(ModItems.ORANGE_BUNDLE.get(), "Orange Bundle");
    }
}

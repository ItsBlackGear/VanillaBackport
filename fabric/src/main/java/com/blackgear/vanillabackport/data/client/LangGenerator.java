package com.blackgear.vanillabackport.data.client;

import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.common.registries.entities.ModAttributes;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import com.blackgear.vanillabackport.common.registries.items.ModItems;
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
        builder.add("biome.minecraft.pale_garden", "Pale Garden");

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
        builder.add("block.minecraft.pale_oak_wall_hanging_sign", "Pale Oak Wall Hanging Sign");
        builder.add("block.minecraft.pale_oak_wall_sign", "Pale Oak Wall Sign");
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
        builder.add(ModEntityTypes.CREAKING.get(), "Creaking");
        builder.add(ModEntityTypes.PALE_OAK_BOAT.get(), "Pale Oak Boat");
        builder.add(ModEntityTypes.PALE_OAK_CHEST_BOAT.get(), "Pale Oak Boat with Chest");

        // Trims
        builder.add("trim_material.minecraft.resin", "Resin Material");

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
        builder.add("subtitles.entity.creaking.freeze", "Creaking stops");
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

        builder.add("item.minecraft.music_disc_tears", "Music Disc");
        builder.add("item.minecraft.music_disc_tears.desc", "Amos Roddy - Tears");

        builder.add("jukebox_song.minecraft.tears", "Amos Roddy - Tears");

        builder.add("item.minecraft.music_disc_lava_chicken", "Music Disc");
        builder.add("item.minecraft.music_disc_lava_chicken.desc", "Hyper Potions - Lava Chicken");

        builder.add("jukebox_song.minecraft.lava_chicken", "Hyper Potions - Lava Chicken");

        builder.add("painting.minecraft.dennis.author", "Sarah Boeving");
        builder.add("painting.minecraft.dennis.title", "Dennis");

        builder.add(ModEntityTypes.HAPPY_GHAST.get(), "Happy Ghast");

        builder.add("subtitles.block.dried_ghast.ambient", "Sounds of dryness");
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

        builder.add(ModItems.BLUE_EGG.get(), "Blue Egg");
        builder.add(ModItems.BROWN_EGG.get(), "Brown Egg");

        builder.add("subtitles.block.firefly_bush.idle", "Fireflies buzz");

        builder.add("subtitles.block.sand.idle", "Sandy sounds");
        builder.add("subtitles.block.deadbush.idle", "Dry sounds");
        builder.add("subtitles.block.dry_grass.ambient", "Windy sounds");

        builder.add("subtitles.entity.wolf.bark", "Wolf barks");
        builder.add("subtitles.entity.wolf.pant", "Wolf pants");
        builder.add("subtitles.entity.wolf.whine", "Wolf whines");

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

        // Armored Paws
        builder.add(ModEntityTypes.ARMADILLO.get(), "Armadillo");

        builder.add(ModItems.WOLF_ARMOR.get(), "Wolf Armor");
        builder.add(ModItems.ARMADILLO_SCUTE.get(), "Armadillo Scute");
        builder.add(ModItems.ARMADILLO_SPAWN_EGG.get(), "Armadillo Spawn Egg");

        builder.add("subtitles.block.decorated_pot.insert","Decorated Pot fills");
        builder.add("subtitles.block.decorated_pot.insert_fail","Decorated Pot wobbles");

        builder.add("subtitles.entity.armadillo.ambient", "Armadillo grunts");
        builder.add("subtitles.entity.armadillo.brush", "Scute is brushed off");
        builder.add("subtitles.entity.armadillo.death", "Armadillo dies");
        builder.add("subtitles.entity.armadillo.eat", "Armadillo eats");
        builder.add("subtitles.entity.armadillo.hurt", "Armadillo hurts");
        builder.add("subtitles.entity.armadillo.hurt_reduced", "Armadillo shields itself");
        builder.add("subtitles.entity.armadillo.land", "Armadillo lands");
        builder.add("subtitles.entity.armadillo.peek", "Armadillo peeks");
        builder.add("subtitles.entity.armadillo.roll", "Armadillo rolls up");
        builder.add("subtitles.entity.armadillo.scute_drop", "Armadillo sheds scute");
        builder.add("subtitles.entity.armadillo.unroll_finish", "Armadillo unrolls");
        builder.add("subtitles.entity.armadillo.unroll_start", "Armadillo peeks");

        builder.add("subtitles.item.armor.equip_wolf", "Wolf Armor is fastened");
        builder.add("subtitles.item.armor.unequip_wolf", "Wolf Armor snips away");

        builder.add("subtitles.item.wolf_armor.break", "Wolf Armor breaks");
        builder.add("subtitles.item.wolf_armor.crack", "Wolf Armor cracks");
        builder.add("subtitles.item.wolf_armor.damage", "Wolf Armor takes damage");
        builder.add("subtitles.item.wolf_armor.repair", "Wolf Armor is repaired");

        // Copper Age
        
        builder.add(ModBlocks.COPPER_CHEST.get(), "Copper Chest");
        builder.add(ModBlocks.EXPOSED_COPPER_CHEST.get(), "Exposed Copper Chest");
        builder.add(ModBlocks.WEATHERED_COPPER_CHEST.get(), "Weathered Copper Chest");
        builder.add(ModBlocks.OXIDIZED_COPPER_CHEST.get(), "Oxidized Copper Chest");
        
        builder.add(ModBlocks.WAXED_COPPER_CHEST.get(), "Waxed Copper Chest");
        builder.add(ModBlocks.WAXED_EXPOSED_COPPER_CHEST.get(), "Waxed Exposed Copper Chest");
        builder.add(ModBlocks.WAXED_WEATHERED_COPPER_CHEST.get(), "Waxed Weathered Copper Chest");
        builder.add(ModBlocks.WAXED_OXIDIZED_COPPER_CHEST.get(), "Waxed Oxidized Copper Chest");
        
        builder.add(ModBlocks.EXPOSED_LIGHTNING_ROD.get(), "Exposed Lightning Rod");
        builder.add(ModBlocks.WEATHERED_LIGHTNING_ROD.get(), "Weathered Lightning Rod");
        builder.add(ModBlocks.OXIDIZED_LIGHTNING_ROD.get(), "Oxidized Lightning Rod");
        builder.add(ModBlocks.WAXED_LIGHTNING_ROD.get(), "Waxed Lightning Rod");
        builder.add(ModBlocks.WAXED_EXPOSED_LIGHTNING_ROD.get(), "Waxed Exposed Lightning Rod");
        builder.add(ModBlocks.WAXED_WEATHERED_LIGHTNING_ROD.get(), "Waxed Weathered Lightning Rod");
        builder.add(ModBlocks.WAXED_OXIDIZED_LIGHTNING_ROD.get(), "Waxed Oxidized Lightning Rod");
        
        builder.add(ModItems.COPPER_SWORD.get(), "Copper Sword");
        builder.add(ModItems.COPPER_AXE.get(), "Copper Axe");
        builder.add(ModItems.COPPER_PICKAXE.get(), "Copper Pickaxe");
        builder.add(ModItems.COPPER_SHOVEL.get(), "Copper Shovel");
        builder.add(ModItems.COPPER_HOE.get(), "Copper Hoe");
        
        builder.add(ModItems.COPPER_HELMET.get(), "Copper Helmet");
        builder.add(ModItems.COPPER_CHESTPLATE.get(), "Copper Chestplate");
        builder.add(ModItems.COPPER_LEGGINGS.get(), "Copper Leggings");
        builder.add(ModItems.COPPER_BOOTS.get(), "Copper Boots");
        
        builder.add(ModItems.COPPER_HORSE_ARMOR.get(), "Copper Horse Armor");
        builder.add(ModItems.COPPER_NUGGET.get(), "Copper Nugget");
        
        builder.add(ModBlocks.COPPER_TORCH.getFirst().get(), "Copper Torch");
        
        builder.add(ModBlocks.COPPER_LANTERN.unaffected().get(), "Copper Lantern");
        builder.add(ModBlocks.COPPER_LANTERN.exposed().get(), "Exposed Copper Lantern");
        builder.add(ModBlocks.COPPER_LANTERN.weathered().get(), "Weathered Copper Lantern");
        builder.add(ModBlocks.COPPER_LANTERN.oxidized().get(), "Oxidized Copper Lantern");
        builder.add(ModBlocks.COPPER_LANTERN.waxed().get(), "Waxed Copper Lantern");
        builder.add(ModBlocks.COPPER_LANTERN.waxedExposed().get(), "Waxed Exposed Copper Lantern");
        builder.add(ModBlocks.COPPER_LANTERN.waxedWeathered().get(), "Waxed Weathered Copper Lantern");
        builder.add(ModBlocks.COPPER_LANTERN.waxedOxidized().get(), "Waxed Oxidized Copper Lantern");
        
        builder.add(ModBlocks.COPPER_CHAIN.unaffected().get(), "Copper Chain");
        builder.add(ModBlocks.COPPER_CHAIN.exposed().get(), "Exposed Copper Chain");
        builder.add(ModBlocks.COPPER_CHAIN.weathered().get(), "Weathered Copper Chain");
        builder.add(ModBlocks.COPPER_CHAIN.oxidized().get(), "Oxidized Copper Chain");
        builder.add(ModBlocks.COPPER_CHAIN.waxed().get(), "Waxed Copper Chain");
        builder.add(ModBlocks.COPPER_CHAIN.waxedExposed().get(), "Waxed Exposed Copper Chain");
        builder.add(ModBlocks.COPPER_CHAIN.waxedWeathered().get(), "Waxed Weathered Copper Chain");
        builder.add(ModBlocks.COPPER_CHAIN.waxedOxidized().get(), "Waxed Oxidized Copper Chain");
        
        builder.add(ModBlocks.COPPER_BARS.unaffected().get(), "Copper Bars");
        builder.add(ModBlocks.COPPER_BARS.exposed().get(), "Exposed Copper Bars");
        builder.add(ModBlocks.COPPER_BARS.weathered().get(), "Weathered Copper Bars");
        builder.add(ModBlocks.COPPER_BARS.oxidized().get(), "Oxidized Copper Bars");
        builder.add(ModBlocks.COPPER_BARS.waxed().get(), "Waxed Copper Bars");
        builder.add(ModBlocks.COPPER_BARS.waxedExposed().get(), "Waxed Exposed Copper Bars");
        builder.add(ModBlocks.COPPER_BARS.waxedWeathered().get(), "Waxed Weathered Copper Bars");
        builder.add(ModBlocks.COPPER_BARS.waxedOxidized().get(), "Waxed Oxidized Copper Bars");
        
        builder.add("subtitles.block.copper_chest.close", "Chest closes");
        builder.add("subtitles.block.copper_chest.open", "Chest opens");
        
        // Chaos Cubed

        builder.add("biome.minecraft.sulfur_caves", "Sulfur Caves");

        builder.add(ModBlocks.CINNABAR.get(), "Cinnabar");
        builder.add(ModBlocks.CINNABAR_SLAB.get(), "Cinnabar Slab");
        builder.add(ModBlocks.CINNABAR_STAIRS.get(), "Cinnabar Stairs");
        builder.add(ModBlocks.CINNABAR_WALL.get(), "Cinnabar Wall");
        builder.add(ModBlocks.POLISHED_CINNABAR.get(), "Polished Cinnabar");
        builder.add(ModBlocks.POLISHED_CINNABAR_SLAB.get(), "Polished Cinnabar Slab");
        builder.add(ModBlocks.POLISHED_CINNABAR_STAIRS.get(), "Polished Cinnabar Stairs");
        builder.add(ModBlocks.POLISHED_CINNABAR_WALL.get(), "Polished Cinnabar Wall");
        builder.add(ModBlocks.CINNABAR_BRICKS.get(), "Cinnabar Bricks");
        builder.add(ModBlocks.CINNABAR_BRICK_SLAB.get(), "Cinnabar Brick Slab");
        builder.add(ModBlocks.CINNABAR_BRICK_STAIRS.get(), "Cinnabar Brick Stairs");
        builder.add(ModBlocks.CINNABAR_BRICK_WALL.get(), "Cinnabar Brick Wall");
        builder.add(ModBlocks.CHISELED_CINNABAR.get(), "Chiseled Cinnabar");
        builder.add(ModBlocks.POTENT_SULFUR.get(), "Potent Sulfur");
        builder.add(ModBlocks.SULFUR_SPIKE.get(), "Sulfur Spike");
        builder.add(ModBlocks.SULFUR.get(), "Sulfur");
        builder.add(ModBlocks.SULFUR_SLAB.get(), "Sulfur Slab");
        builder.add(ModBlocks.SULFUR_STAIRS.get(), "Sulfur Stairs");
        builder.add(ModBlocks.SULFUR_WALL.get(), "Sulfur Wall");
        builder.add(ModBlocks.POLISHED_SULFUR.get(), "Polished Sulfur");
        builder.add(ModBlocks.POLISHED_SULFUR_SLAB.get(), "Polished Sulfur Slab");
        builder.add(ModBlocks.POLISHED_SULFUR_STAIRS.get(), "Polished Sulfur Stairs");
        builder.add(ModBlocks.POLISHED_SULFUR_WALL.get(), "Polished Sulfur Wall");
        builder.add(ModBlocks.SULFUR_BRICKS.get(), "Sulfur Bricks");
        builder.add(ModBlocks.SULFUR_BRICK_SLAB.get(), "Sulfur Brick Slab");
        builder.add(ModBlocks.SULFUR_BRICK_STAIRS.get(), "Sulfur Brick Stairs");
        builder.add(ModBlocks.SULFUR_BRICK_WALL.get(), "Sulfur Brick Wall");
        builder.add(ModBlocks.CHISELED_SULFUR.get(), "Chiseled Sulfur");

        builder.add(ModEntityTypes.SULFUR_CUBE.get(), "Sulfur Cube");
        builder.add("entity.minecraft.sulfur_cube.content", "Contains: %s");
        builder.add("entity.minecraft.sulfur_cube.explosion_disabled", "Sulfur Cube explosions are disabled");

        builder.add(ModItems.SULFUR_CUBE_BUCKET.get(), "Bucket of Sulfur Cube");
        builder.add(ModItems.SULFUR_CUBE_SPAWN_EGG.get(), "Sulfur Cube Spawn Egg");

        builder.add("subtitles.entity.sulfur_cube.absorb", "Sulfur Cube full");
        builder.add("subtitles.entity.sulfur_cube.bounce", "Sulfur Cube bounces");
        builder.add("subtitles.entity.sulfur_cube.death", "Sulfur Cube dies");
        builder.add("subtitles.entity.sulfur_cube.eject", "Block removed");
        builder.add("subtitles.entity.sulfur_cube.hit", "Sulfur Cube hit");
        builder.add("subtitles.entity.sulfur_cube.hurt", "Sulfur Cube hurts");
        builder.add("subtitles.entity.sulfur_cube.push", "Sulfur Cube pushed");
        builder.add("subtitles.entity.sulfur_cube.squish", "Sulfur Cube bounces");

        builder.add("subtitles.entity.small_sulfur_cube.death", "Small Sulfur Cube dies");
        builder.add("subtitles.entity.small_sulfur_cube.eat", "Small Sulfur Cube puts on mass");
        builder.add("subtitles.entity.small_sulfur_cube.hurt", "Small Sulfur Cube hurts");
        builder.add("subtitles.entity.small_sulfur_cube.jump", "Small Sulfur Cube bounces");

        builder.add("subtitles.item.bucket.fill_sulfur_cube", "Sulfur Cube scooped");

        builder.add("subtitles.block.potent_sulfur.geyser_eruption", "Sulfur spring bursts");
        builder.add("subtitles.block.potent_sulfur.noxious_gas", "Noxious gas appears");

        builder.add(ModAttributes.AIR_DRAG_MODIFIER, "Air Drag Modifier");
        builder.add(ModAttributes.BOUNCINESS, "Bounciness");
        builder.add(ModAttributes.FRICTION_MODIFIER, "Friction Modifier");

        builder.add("item.minecraft.music_disc_bounce", "Music Disc");
        builder.add("item.minecraft.music_disc_bounce.desc", "fingerspit - Bounce");

        builder.add("jukebox_song.minecraft.bounce", "fingerspit - Bounce");

        builder.add("death.attack.sulfurCubeHot", "%1$s died because not just the floor is lava");
        builder.add("death.attack.sulfurCubeHot.player", "%2$s showed %1$s that not just the floor is lava");

        // Advancements
        builder.add("advancements.adventure.heart_transplanter.title", "Heart Transplanter");
        builder.add("advancements.adventure.heart_transplanter.description", "Place a Creaking Heart with the correct alignment between two Pale Oak Log blocks");

        builder.add("advancements.husbandry.whole_pack.title", "The Whole Pack");
        builder.add("advancements.husbandry.whole_pack.description", "Tame one of each Wolf variant");

        builder.add("advancements.husbandry.remove_wolf_armor.title", "Shear Brilliance");
        builder.add("advancements.husbandry.remove_wolf_armor.description", "Remove Wolf Armor from a Wolf using Shears");

        builder.add("advancements.husbandry.repair_wolf_armor.title", "Good as New");
        builder.add("advancements.husbandry.repair_wolf_armor.description", "Fully repair damaged Wolf Armor using Armadillo Scutes");

        builder.add("advancements.husbandry.place_dried_ghast_in_water.title", "Stay Hydrated!");
        builder.add("advancements.husbandry.place_dried_ghast_in_water.description", "Place a Dried Ghast block into water");

        builder.add("advancements.husbandry.uh_oh.title", "Uh Oh");
        builder.add("advancements.husbandry.uh_oh.description", "Have a Sulfur Cube absorb a TNT block");

        // Bundled Tabs
        builder.add("bundled_tab.armored_paws.title", "Armored Paws");
        builder.add("bundled_tab.bundles_of_bravery.title", "Bundles of Bravery");
        builder.add("bundled_tab.the_garden_awakens.title", "The Garden Awakens");
        builder.add("bundled_tab.spring_to_life.title", "Spring to Life");
        builder.add("bundled_tab.chase_the_skies.title", "Chase The Skies");
        builder.add("bundled_tab.hot_as_lava.title", "Hot as Lava");
        builder.add("bundled_tab.copper_age.title", "Copper Age");
        builder.add("bundled_tab.chaos_cubed.title", "Chaos Cubed");
        
        // Options
        builder.add("options.music_frequency", "Music Frequency");
        builder.add("options.music_frequency.constant", "Constant");
        builder.add("options.music_frequency.default", "Default");
        builder.add("options.music_frequency.frequent", "Frequent");
        builder.add("options.music_frequency.tooltip", "Changes how frequently music plays while in a game world.");
        
        builder.add("options.musicToast", "Music Toast");
        builder.add("options.musicToast.never", "Never");
        builder.add("options.musicToast.never.tooltip", "No music toast is shown.");
        builder.add("options.musicToast.pauseMenu", "Pause Menu");
        builder.add("options.musicToast.pauseMenu.tooltip", "A music toast is constantly displayed in the in-game pause menu while a song is playing.");
        builder.add("options.musicToast.pauseMenuAndToast", "Pause Menu and Toast");
        builder.add("options.musicToast.pauseMenuAndToast.tooltip", "Displays a toast when a song starts playing. The same toast is constantly displayed in the in-game pause menu while a song is playing.");
        
        // Music
        builder.add("music.game.a_familiar_room", "Aaron Cherof - A Familiar Room");
        builder.add("music.game.an_ordinary_day", "Kumi Tanioka - An Ordinary Day");
        builder.add("music.game.ancestry", "Lena Raine - Ancestry");
        builder.add("music.game.below_and_above", "Amos Roddy - Below and Above");
        builder.add("music.game.broken_clocks", "Amos Roddy - Broken Clocks");
        builder.add("music.game.bromeliad", "Aaron Cherof - Bromeliad");
        builder.add("music.game.calm2", "C418 - Clark");
        builder.add("music.game.comforting_memories", "Kumi Tanioka - Comforting Memories");
        builder.add("music.game.creative.creative4", "C418 - Aria Math");
        builder.add("music.game.creative.creative1", "C418 - Biome Fest");
        builder.add("music.game.creative.creative2", "C418 - Blind Spots");
        builder.add("music.game.creative.creative5", "C418 - Dreiton");
        builder.add("music.game.creative.creative3", "C418 - Haunt Muskie");
        builder.add("music.game.creative.creative6", "C418 - Taswell");
        builder.add("music.game.crescent_dunes", "Aaron Cherof - Crescent Dunes");
        builder.add("music.game.hal4", "C418 - Danny");
        builder.add("music.game.deeper", "Lena Raine - Deeper");
        builder.add("music.game.dry_hands", "C418 - Dry Hands");
        builder.add("music.game.ebb", "fingerspit - Ebb");
        builder.add("music.game.echo_in_the_wind", "Aaron Cherof - Echo in the Wind");
        builder.add("music.game.eld_unknown", "Lena Raine - Eld Unknown");
        builder.add("music.game.end.credits", "C418 - Alpha");
        builder.add("music.game.end.boss", "C418 - Boss");
        builder.add("music.game.end.end", "C418 - The End");
        builder.add("music.game.endless", "Lena Raine - Endless");
        builder.add("music.game.featherfall", "Aaron Cherof - Featherfall");
        builder.add("music.game.fireflies", "Amos Roddy - Fireflies");
        builder.add("music.game.floating_dream", "Kumi Tanioka - Floating Dream");
        builder.add("music.game.hal3", "C418 - Haggstrom");
        builder.add("music.game.home", "fingerspit - Home");
        builder.add("music.game.infinite_amethyst", "Lena Raine - Infinite Amethyst");
        builder.add("music.game.nuance1", "C418 - Key");
        builder.add("music.game.komorebi", "Kumi Tanioka - komorebi");
        builder.add("music.game.left_to_bloom", "Lena Raine - Left to Bloom");
        builder.add("music.game.lilypad", "Amos Roddy - Lilypad");
        builder.add("music.game.hal2", "C418 - Living Mice");
        builder.add("music.game.memories", "fingerspit - Memories");
        builder.add("music.game.piano3", "C418 - Mice on Venus");
        builder.add("music.game.calm1", "C418 - Minecraft");
        builder.add("music.game.nether.nether4", "C418 - Ballad of the Cats");
        builder.add("music.game.nether.nether1", "C418 - Concrete Halls");
        builder.add("music.game.nether.crimson_forest.chrysopoeia", "Lena Raine - Chrysopoeia");
        builder.add("music.game.nether.nether2", "C418 - Dead Voxel");
        builder.add("music.game.nether.nether_wastes.rubedo", "Lena Raine - Rubedo");
        builder.add("music.game.nether.soulsand_valley.so_below", "Lena Raine - So Below");
        builder.add("music.game.nether.nether3", "C418 - Warmth");
        builder.add("music.game.nightly", "fingerspit - Nightly");
        builder.add("music.game.one_more_day", "Lena Raine - One More Day");
        builder.add("music.game.os_piano", "Amos Roddy - O's Piano");
        builder.add("music.game.nuance2", "C418 - Oxygène");
        builder.add("music.game.pokopoko", "Kumi Tanioka - pokopoko");
        builder.add("music.game.puzzlebox", "Aaron Cherof - Puzzlebox");
        builder.add("music.game.shores", "fingerspit - Shores");
        builder.add("music.game.stand_tall", "Lena Raine - Stand Tall");
        builder.add("music.game.hal1", "C418 - Subwoofer Lullaby");
        builder.add("music.game.swamp.aerie", "Lena Raine - Aerie");
        builder.add("music.game.swamp.firebugs", "Lena Raine - Firebugs");
        builder.add("music.game.swamp.labyrinthine", "Lena Raine - Labyrinthine");
        builder.add("music.game.calm3", "C418 - Sweden");
        builder.add("music.game.watcher", "Aaron Cherof - Watcher");
        builder.add("music.game.water.axolotl", "C418 - Axolotl");
        builder.add("music.game.water.dragon_fish", "C418 - Dragon Fish");
        builder.add("music.game.water.shuniji", "C418 - Shuniji");
        builder.add("music.game.wending", "Lena Raine - Wending");
        builder.add("music.game.piano2", "C418 - Wet Hands");
        builder.add("music.game.yakusoku", "Kumi Tanioka - yakusoku");
        
        builder.add("music.menu.menu3", "C418 - Beginning 2");
        builder.add("music.menu.menu4", "C418 - Floating Trees");
        builder.add("music.menu.menu2", "C418 - Moog City 2");
        builder.add("music.menu.menu1", "C418 - Mutation");
    }
}
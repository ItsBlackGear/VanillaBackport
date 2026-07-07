package com.blackgear.vanillabackport.client;

import com.blackgear.platform.common.CreativeTabs;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.common.registries.items.ModItems;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;

import java.util.List;

public interface CreativeTabIntegration {
    CreativeTabs.Modifier BUILDING_BLOCKS = (flag, output, operator) -> {
        output.addAllAfter(Items.CHERRY_BUTTON, List.of(
            ModBlocks.PALE_OAK_LOG.get(),
            ModBlocks.PALE_OAK_WOOD.get(),
            ModBlocks.STRIPPED_PALE_OAK_LOG.get(),
            ModBlocks.STRIPPED_PALE_OAK_WOOD.get(),
            ModBlocks.PALE_OAK_PLANKS.get(),
            ModBlocks.PALE_OAK_STAIRS.get(),
            ModBlocks.PALE_OAK_SLAB.get(),
            ModBlocks.PALE_OAK_FENCE.get(),
            ModBlocks.PALE_OAK_FENCE_GATE.get(),
            ModBlocks.PALE_OAK_DOOR.get(),
            ModBlocks.PALE_OAK_TRAPDOOR.get(),
            ModBlocks.PALE_OAK_PRESSURE_PLATE.get(),
            ModBlocks.PALE_OAK_BUTTON.get()
        ));
        output.addAllAfter(Items.MUD_BRICK_WALL, List.of(
            ModBlocks.RESIN_BRICKS.get(),
            ModBlocks.RESIN_BRICK_STAIRS.get(),
            ModBlocks.RESIN_BRICK_SLAB.get(),
            ModBlocks.RESIN_BRICK_WALL.get(),
            ModBlocks.CHISELED_RESIN_BRICKS.get()
        ));
        output.addAllAfter(Items.CUT_RED_SANDSTONE_SLAB, List.of(
            ModBlocks.CINNABAR.get(),
            ModBlocks.CINNABAR_STAIRS.get(),
            ModBlocks.CINNABAR_SLAB.get(),
            ModBlocks.CINNABAR_WALL.get(),
            ModBlocks.CHISELED_CINNABAR.get(),
            ModBlocks.POLISHED_CINNABAR.get(),
            ModBlocks.POLISHED_CINNABAR_STAIRS.get(),
            ModBlocks.POLISHED_CINNABAR_SLAB.get(),
            ModBlocks.POLISHED_CINNABAR_WALL.get(),
            ModBlocks.CINNABAR_BRICKS.get(),
            ModBlocks.CINNABAR_BRICK_STAIRS.get(),
            ModBlocks.CINNABAR_BRICK_SLAB.get(),
            ModBlocks.CINNABAR_BRICK_WALL.get(),
            ModBlocks.SULFUR.get(),
            ModBlocks.SULFUR_STAIRS.get(),
            ModBlocks.SULFUR_SLAB.get(),
            ModBlocks.SULFUR_WALL.get(),
            ModBlocks.CHISELED_SULFUR.get(),
            ModBlocks.POLISHED_SULFUR.get(),
            ModBlocks.POLISHED_SULFUR_STAIRS.get(),
            ModBlocks.POLISHED_SULFUR_SLAB.get(),
            ModBlocks.POLISHED_SULFUR_WALL.get(),
            ModBlocks.SULFUR_BRICKS.get(),
            ModBlocks.SULFUR_BRICK_STAIRS.get(),
            ModBlocks.SULFUR_BRICK_SLAB.get(),
            ModBlocks.SULFUR_BRICK_WALL.get()
        ));
        output.addAfter(Items.CUT_COPPER_SLAB, ModBlocks.COPPER_BARS.unaffected().get());
        output.addAfter(Items.COPPER_BULB, ModBlocks.COPPER_CHAIN.unaffected().get());
        output.addAfter(Items.WEATHERED_CUT_COPPER_SLAB, ModBlocks.COPPER_BARS.weathered().get());
        output.addAfter(Items.WEATHERED_COPPER_BULB, ModBlocks.COPPER_CHAIN.weathered().get());
        output.addAfter(Items.EXPOSED_CUT_COPPER_SLAB, ModBlocks.COPPER_BARS.exposed().get());
        output.addAfter(Items.EXPOSED_COPPER_BULB, ModBlocks.COPPER_CHAIN.exposed().get());
        output.addAfter(Items.OXIDIZED_CUT_COPPER_SLAB, ModBlocks.COPPER_BARS.oxidized().get());
        output.addAfter(Items.OXIDIZED_COPPER_BULB, ModBlocks.COPPER_CHAIN.oxidized().get());
        output.addAfter(Items.WAXED_CUT_COPPER_SLAB, ModBlocks.COPPER_BARS.waxed().get());
        output.addAfter(Items.WAXED_COPPER_BULB, ModBlocks.COPPER_CHAIN.waxed().get());
        output.addAfter(Items.WAXED_WEATHERED_CUT_COPPER_SLAB, ModBlocks.COPPER_BARS.waxedWeathered().get());
        output.addAfter(Items.WAXED_WEATHERED_COPPER_BULB, ModBlocks.COPPER_CHAIN.waxedWeathered().get());
        output.addAfter(Items.WAXED_EXPOSED_CUT_COPPER_SLAB, ModBlocks.COPPER_BARS.waxedExposed().get());
        output.addAfter(Items.WAXED_EXPOSED_COPPER_BULB, ModBlocks.COPPER_CHAIN.waxedExposed().get());
        output.addAfter(Items.WAXED_OXIDIZED_CUT_COPPER_SLAB, ModBlocks.COPPER_BARS.waxedOxidized().get());
        output.addAfter(Items.WAXED_OXIDIZED_COPPER_BULB, ModBlocks.COPPER_CHAIN.waxedOxidized().get());
    };

    CreativeTabs.Modifier COLORED_BLOCKS = (flag, output, operator) -> {};

    CreativeTabs.Modifier NATURAL_BLOCKS = (flag, output, operator) -> {
        output.addAllAfter(Items.MOSS_CARPET, List.of(
            ModBlocks.PALE_MOSS_BLOCK.get(),
            ModBlocks.PALE_MOSS_CARPET.get(),
            ModBlocks.PALE_HANGING_MOSS.get()
        ));
        output.addAllAfter(Items.PRISMARINE, List.of(
            ModBlocks.CINNABAR.get(),
            ModBlocks.SULFUR.get(),
            ModBlocks.SULFUR_SPIKE.get(),
            ModBlocks.POTENT_SULFUR.get()
        ));
        output.addAfter(Items.CHERRY_LOG, ModBlocks.PALE_OAK_LOG.get());
        output.addAfter(Items.CHERRY_LEAVES, ModBlocks.PALE_OAK_LEAVES.get());
        output.addAfter(Items.CHERRY_SAPLING, ModBlocks.PALE_OAK_SAPLING.get());
        output.addAllAfter(Items.FERN, List.of(
            ModBlocks.SHORT_DRY_GRASS.get(),
            ModBlocks.BUSH.get()
        ));
        output.addAllAfter(Items.TORCHFLOWER, List.of(
            ModBlocks.CACTUS_FLOWER.get(),
            ModBlocks.CLOSED_EYEBLOSSOM.get(),
            ModBlocks.OPEN_EYEBLOSSOM.get()
        ));
        output.addAllAfter(Items.PINK_PETALS, List.of(
            ModBlocks.WILDFLOWERS.get(),
            ModBlocks.LEAF_LITTER.get()
        ));
        output.addAfter(Items.SPORE_BLOSSOM, ModBlocks.FIREFLY_BUSH.get());
        output.addAfter(Items.LARGE_FERN, ModBlocks.TALL_DRY_GRASS.get());
        output.addAfter(Items.SNIFFER_EGG, ModBlocks.DRIED_GHAST.get());
        output.addAfter(Items.HONEY_BLOCK, ModBlocks.RESIN_BLOCK.get());
    };

    CreativeTabs.Modifier FUNCTIONAL_BLOCKS = (flag, output, operator) -> {
        output.addAfter(Items.SOUL_TORCH, ModBlocks.COPPER_TORCH.getFirst().get());
        output.addAllAfter(Items.SOUL_LANTERN, List.of(
            ModBlocks.COPPER_LANTERN.unaffected().get(),
            ModBlocks.COPPER_LANTERN.weathered().get(),
            ModBlocks.COPPER_LANTERN.exposed().get(),
            ModBlocks.COPPER_LANTERN.oxidized().get(),
            ModBlocks.COPPER_LANTERN.waxed().get(),
            ModBlocks.COPPER_LANTERN.waxedWeathered().get(),
            ModBlocks.COPPER_LANTERN.waxedExposed().get(),
            ModBlocks.COPPER_LANTERN.waxedOxidized().get()
        ));
        output.addAllAfter(Items.CHAIN, List.of(
            ModBlocks.COPPER_CHAIN.unaffected().get(),
            ModBlocks.COPPER_CHAIN.weathered().get(),
            ModBlocks.COPPER_CHAIN.exposed().get(),
            ModBlocks.COPPER_CHAIN.oxidized().get(),
            ModBlocks.COPPER_CHAIN.waxed().get(),
            ModBlocks.COPPER_CHAIN.waxedWeathered().get(),
            ModBlocks.COPPER_CHAIN.waxedExposed().get(),
            ModBlocks.COPPER_CHAIN.waxedOxidized().get()
        ));
        output.addAllAfter(Items.LIGHTNING_ROD, List.of(
            ModBlocks.WEATHERED_LIGHTNING_ROD.get(),
            ModBlocks.EXPOSED_LIGHTNING_ROD.get(),
            ModBlocks.OXIDIZED_LIGHTNING_ROD.get(),
            ModBlocks.WAXED_LIGHTNING_ROD.get(),
            ModBlocks.WAXED_WEATHERED_LIGHTNING_ROD.get(),
            ModBlocks.WAXED_EXPOSED_LIGHTNING_ROD.get(),
            ModBlocks.WAXED_OXIDIZED_LIGHTNING_ROD.get()
        ));
        output.addAllAfter(Items.CHISELED_BOOKSHELF, List.of(
            ModBlocks.OAK_SHELF.get(),
            ModBlocks.SPRUCE_SHELF.get(),
            ModBlocks.BIRCH_SHELF.get(),
            ModBlocks.JUNGLE_SHELF.get(),
            ModBlocks.ACACIA_SHELF.get(),
            ModBlocks.DARK_OAK_SHELF.get(),
            ModBlocks.MANGROVE_SHELF.get(),
            ModBlocks.CHERRY_SHELF.get(),
            ModBlocks.PALE_OAK_SHELF.get(),
            ModBlocks.BAMBOO_SHELF.get(),
            ModBlocks.CRIMSON_SHELF.get(),
            ModBlocks.WARPED_SHELF.get()
        ));
        output.addAllAfter(Items.CHERRY_HANGING_SIGN, List.of(
            ModBlocks.PALE_OAK_SIGN.getFirst().get(),
            ModBlocks.PALE_OAK_HANGING_SIGN.getFirst().get()
        ));
        output.addAllAfter(Items.CHEST, List.of(
            ModBlocks.COPPER_CHEST.get(),
            ModBlocks.WEATHERED_COPPER_CHEST.get(),
            ModBlocks.EXPOSED_COPPER_CHEST.get(),
            ModBlocks.OXIDIZED_COPPER_CHEST.get(),
            ModBlocks.WAXED_COPPER_CHEST.get(),
            ModBlocks.WAXED_WEATHERED_COPPER_CHEST.get(),
            ModBlocks.WAXED_EXPOSED_COPPER_CHEST.get(),
            ModBlocks.WAXED_OXIDIZED_COPPER_CHEST.get()
        ));
        output.addAllAfter(Items.ENDER_EYE, List.of(
            ModBlocks.COPPER_GOLEM_STATUE.get(),
            ModBlocks.WEATHERED_COPPER_GOLEM_STATUE.get(),
            ModBlocks.EXPOSED_COPPER_GOLEM_STATUE.get(),
            ModBlocks.OXIDIZED_COPPER_GOLEM_STATUE.get(),
            ModBlocks.WAXED_COPPER_GOLEM_STATUE.get(),
            ModBlocks.WAXED_WEATHERED_COPPER_GOLEM_STATUE.get(),
            ModBlocks.WAXED_EXPOSED_COPPER_GOLEM_STATUE.get(),
            ModBlocks.WAXED_OXIDIZED_COPPER_GOLEM_STATUE.get()
        ));
    };

    CreativeTabs.Modifier REDSTONE_BLOCKS = (flag, output, operator) -> {
        output.addAfter(Items.CHISELED_BOOKSHELF, ModBlocks.OAK_SHELF.get());
    };

    CreativeTabs.Modifier TOOLS_AND_UTILITIES = (flag, output, operator) -> {
        output.addAllAfter(Items.STONE_HOE, List.of(
            ModItems.COPPER_SHOVEL.get(),
            ModItems.COPPER_PICKAXE.get(),
            ModItems.COPPER_AXE.get(),
            ModItems.COPPER_HOE.get()));
        output.addAfter(Items.TADPOLE_BUCKET, ModItems.SULFUR_CUBE_BUCKET.get());
        if (flag.contains(FeatureFlags.BUNDLE)) {
            output.addAllAfter(Items.BUNDLE, List.of(
                ModItems.WHITE_BUNDLE.get(),
                ModItems.LIGHT_GRAY_BUNDLE.get(),
                ModItems.GRAY_BUNDLE.get(),
                ModItems.BLACK_BUNDLE.get(),
                ModItems.BROWN_BUNDLE.get(),
                ModItems.RED_BUNDLE.get(),
                ModItems.ORANGE_BUNDLE.get(),
                ModItems.YELLOW_BUNDLE.get(),
                ModItems.LIME_BUNDLE.get(),
                ModItems.GREEN_BUNDLE.get(),
                ModItems.CYAN_BUNDLE.get(),
                ModItems.LIGHT_BLUE_BUNDLE.get(),
                ModItems.BLUE_BUNDLE.get(),
                ModItems.PURPLE_BUNDLE.get(),
                ModItems.MAGENTA_BUNDLE.get(),
                ModItems.PINK_BUNDLE.get()
            ));
        }
        output.addAllAfter(Items.SADDLE, List.of(
            ModItems.WHITE_HARNESS.get(),
            ModItems.LIGHT_GRAY_HARNESS.get(),
            ModItems.GRAY_HARNESS.get(),
            ModItems.BLACK_HARNESS.get(),
            ModItems.BROWN_HARNESS.get(),
            ModItems.RED_HARNESS.get(),
            ModItems.ORANGE_HARNESS.get(),
            ModItems.YELLOW_HARNESS.get(),
            ModItems.LIME_HARNESS.get(),
            ModItems.GREEN_HARNESS.get(),
            ModItems.CYAN_HARNESS.get(),
            ModItems.LIGHT_BLUE_HARNESS.get(),
            ModItems.BLUE_HARNESS.get(),
            ModItems.PURPLE_HARNESS.get(),
            ModItems.MAGENTA_HARNESS.get(),
            ModItems.PINK_HARNESS.get()
        ));
        output.addAllAfter(Items.CHERRY_CHEST_BOAT, List.of(
            ModItems.PALE_OAK_BOAT.get(),
            ModItems.PALE_OAK_CHEST_BOAT.get()
        ));
        output.addAllAfter(Items.MUSIC_DISC_RELIC, List.of(
            ModItems.MUSIC_DISC_TEARS.get(),
            ModItems.MUSIC_DISC_LAVA_CHICKEN.get(),
            ModItems.MUSIC_DISC_BOUNCE.get()
        ));
    };

    CreativeTabs.Modifier COMBAT = (flag, output, operator) -> {
        output.addAfter(Items.STONE_SWORD, ModItems.COPPER_SWORD.get());
        output.addAfter(Items.STONE_AXE, ModItems.COPPER_AXE.get());
        output.addAllAfter(Items.LEATHER_BOOTS, List.of(
            ModItems.COPPER_HELMET.get(),
            ModItems.COPPER_CHESTPLATE.get(),
            ModItems.COPPER_LEGGINGS.get(),
            ModItems.COPPER_BOOTS.get()));
        output.addAfter(Items.LEATHER_HORSE_ARMOR, ModItems.COPPER_HORSE_ARMOR.get());
        output.addAllAfter(Items.EGG, List.of(
            ModItems.BROWN_EGG.get(),
            ModItems.BLUE_EGG.get()
        ));
    };

    CreativeTabs.Modifier FOOD_AND_DRINKS = (flag, output, operator) -> {};

    CreativeTabs.Modifier INGREDIENTS = (flag, output, operator) -> {
        output.addAllAfter(Items.EGG, List.of(
            ModItems.BROWN_EGG.get(),
            ModItems.BLUE_EGG.get()
        ));
        output.addAfter(Items.HONEYCOMB, ModBlocks.RESIN_CLUMP.get());
        output.addAfter(Items.NETHER_BRICK, ModItems.RESIN_BRICK.get());
    };

    CreativeTabs.Modifier SPAWN_EGGS = (flag, output, operator) -> {
        output.addAfter(Items.SPAWNER, ModBlocks.CREAKING_HEART.get());
        output.addAfter(Items.COD_SPAWN_EGG, ModItems.COPPER_GOLEM_SPAWN_EGG.get());
        output.addAfter(Items.COW_SPAWN_EGG, ModItems.CREAKING_SPAWN_EGG.get());
        output.addAfter(Items.GUARDIAN_SPAWN_EGG, ModItems.HAPPY_GHAST_SPAWN_EGG.get());
        output.addAfter(Items.SNIFFER_SPAWN_EGG, ModItems.SULFUR_CUBE_SPAWN_EGG.get());
    };

    static void bootstrap() {
        CreativeTabs.modify(CreativeModeTabs.BUILDING_BLOCKS, BUILDING_BLOCKS);
        CreativeTabs.modify(CreativeModeTabs.COLORED_BLOCKS, COLORED_BLOCKS);
        CreativeTabs.modify(CreativeModeTabs.NATURAL_BLOCKS, NATURAL_BLOCKS);
        CreativeTabs.modify(CreativeModeTabs.FUNCTIONAL_BLOCKS, FUNCTIONAL_BLOCKS);
        CreativeTabs.modify(CreativeModeTabs.REDSTONE_BLOCKS, REDSTONE_BLOCKS);
        CreativeTabs.modify(CreativeModeTabs.TOOLS_AND_UTILITIES, TOOLS_AND_UTILITIES);
        CreativeTabs.modify(CreativeModeTabs.COMBAT, COMBAT);
        CreativeTabs.modify(CreativeModeTabs.FOOD_AND_DRINKS, FOOD_AND_DRINKS);
        CreativeTabs.modify(CreativeModeTabs.INGREDIENTS, INGREDIENTS);
        CreativeTabs.modify(CreativeModeTabs.SPAWN_EGGS, SPAWN_EGGS);
    }
}
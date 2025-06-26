package com.blackgear.vanillabackport.client;

import com.blackgear.platform.common.CreativeTabs;
import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.blackgear.vanillabackport.common.registries.ModItems;
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
            ModBlocks.PALE_OAK_BUTTON.get()
        ));
        output.addAllAfter(Items.MUD_BRICK_WALL, List.of(
            ModBlocks.RESIN_BRICKS.get(),
            ModBlocks.RESIN_BRICK_STAIRS.get(),
            ModBlocks.RESIN_BRICK_SLAB.get(),
            ModBlocks.RESIN_BRICK_WALL.get(),
            ModBlocks.CHISELED_RESIN_BRICKS.get()
        ));
    };

    CreativeTabs.Modifier COLORED_BLOCKS = (flag, output, operator) -> {};

    CreativeTabs.Modifier NATURAL_BLOCKS = (flag, output, operator) -> {
        output.addAllAfter(Items.MOSS_CARPET, List.of(
            ModBlocks.PALE_MOSS_BLOCK.get(),
            ModBlocks.PALE_MOSS_CARPET.get(),
            ModBlocks.PALE_HANGING_MOSS.get()
        ));
        output.addAfter(Items.CHERRY_LOG, ModBlocks.PALE_OAK_LOG.get());
        output.addAfter(Items.CHERRY_LEAVES, ModBlocks.PALE_OAK_LEAVES.get());
        output.addAfter(Items.CHERRY_SAPLING, ModBlocks.PALE_OAK_SAPLING.get());
        output.addAllAfter(Items.TORCHFLOWER, List.of(
            ModBlocks.CLOSED_EYEBLOSSOM.get(),
            ModBlocks.OPEN_EYEBLOSSOM.get()
        ));
        output.addAfter(Items.SNIFFER_EGG, ModBlocks.DRIED_GHAST.get());
    };

    CreativeTabs.Modifier FUNCTIONAL_BLOCKS = (flag, output, operator) -> {
        output.addAllAfter(Items.CHERRY_HANGING_SIGN, List.of(
            ModBlocks.PALE_OAK_SIGN.getFirst().get(),
            ModBlocks.PALE_OAK_HANGING_SIGN.getFirst().get()
        ));
    };

    CreativeTabs.Modifier REDSTONE_BLOCKS = (flag, output, operator) -> {};

    CreativeTabs.Modifier TOOLS_AND_UTILITIES = (flag, output, operator) -> {
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
        output.addAllAfter(Items.MUSIC_DISC_PIGSTEP, List.of(ModItems.MUSIC_DISC_TEARS.get(), ModItems.MUSIC_DISC_LAVA_CHICKEN.get()));
    };

    CreativeTabs.Modifier COMBAT = (flag, output, operator) -> {};

    CreativeTabs.Modifier FOOD_AND_DRINKS = (flag, output, operator) -> {};

    CreativeTabs.Modifier INGREDIENTS = (flag, output, operator) -> {
        output.addAfter(Items.HONEYCOMB, ModBlocks.RESIN_CLUMP.get());
        output.addAfter(Items.NETHER_BRICK, ModItems.RESIN_BRICK.get());
    };

    CreativeTabs.Modifier SPAWN_EGGS = (flag, output, operator) -> {
        output.addAfter(Items.TRIAL_SPAWNER, ModBlocks.CREAKING_HEART.get());
        output.addAfter(Items.COW_SPAWN_EGG, ModItems.CREAKING_SPAWN_EGG.get());
        output.addAfter(Items.GUARDIAN_SPAWN_EGG, ModItems.HAPPY_GHAST_SPAWN_EGG.get());
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
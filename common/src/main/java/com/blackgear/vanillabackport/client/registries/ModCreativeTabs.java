package com.blackgear.vanillabackport.client.registries;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.blackgear.vanillabackport.common.registries.ModItems;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

public interface ModCreativeTabs {
    CoreRegistry<CreativeModeTab> TABS = CoreRegistry.create(Registries.CREATIVE_MODE_TAB, VanillaBackport.MOD_ID);

    Supplier<CreativeModeTab> THE_GARDEN_AWAKENS = TABS.register(
        "the_garden_awakens",
        () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
            .title(Component.literal("The Garden Awakens"))
            .icon(() -> new ItemStack(ModBlocks.PALE_MOSS_BLOCK.get()))
            .displayItems((parameters, output) -> {
                output.accept(ModBlocks.CREAKING_HEART.get());
                output.accept(ModBlocks.PALE_OAK_LOG.get());
                output.accept(ModBlocks.STRIPPED_PALE_OAK_LOG.get());
                output.accept(ModBlocks.PALE_OAK_WOOD.get());
                output.accept(ModBlocks.STRIPPED_PALE_OAK_WOOD.get());
                output.accept(ModBlocks.PALE_OAK_PLANKS.get());
                output.accept(ModBlocks.PALE_OAK_STAIRS.get());
                output.accept(ModBlocks.PALE_OAK_SLAB.get());
                output.accept(ModBlocks.PALE_OAK_SIGN.getFirst().get());
                output.accept(ModBlocks.PALE_OAK_HANGING_SIGN.getFirst().get());
                output.accept(ModBlocks.PALE_OAK_BUTTON.get());
                output.accept(ModBlocks.PALE_OAK_PRESSURE_PLATE.get());
                output.accept(ModBlocks.PALE_OAK_DOOR.get());
                output.accept(ModBlocks.PALE_OAK_FENCE.get());
                output.accept(ModBlocks.PALE_OAK_FENCE_GATE.get());
                output.accept(ModBlocks.PALE_OAK_TRAPDOOR.get());
                output.accept(ModItems.PALE_OAK_BOAT.get());
                output.accept(ModItems.PALE_OAK_CHEST_BOAT.get());
                output.accept(ModBlocks.PALE_HANGING_MOSS.get());
                output.accept(ModBlocks.PALE_MOSS_BLOCK.get());
                output.accept(ModBlocks.PALE_MOSS_CARPET.get());
                output.accept(ModBlocks.PALE_OAK_LEAVES.get());
                output.accept(ModBlocks.PALE_OAK_SAPLING.get());
                output.accept(ModItems.CREAKING_SPAWN_EGG.get());
                output.accept(ModBlocks.CLOSED_EYEBLOSSOM.get());
                output.accept(ModBlocks.OPEN_EYEBLOSSOM.get());
                output.accept(ModBlocks.RESIN_BLOCK.get());
                output.accept(ModBlocks.RESIN_BRICKS.get());
                output.accept(ModBlocks.CHISELED_RESIN_BRICKS.get());
                output.accept(ModBlocks.RESIN_BRICK_STAIRS.get());
                output.accept(ModBlocks.RESIN_BRICK_SLAB.get());
                output.accept(ModBlocks.RESIN_BRICK_WALL.get());
                output.accept(ModBlocks.RESIN_CLUMP.get());
                output.accept(ModItems.RESIN_BRICK.get());
            })
            .build()
    );

    Supplier<CreativeModeTab> SUMMER_DROP = TABS.register(
        "summer_drop",
        () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 1)
            .title(Component.literal("Summer Drop"))
            .icon(() -> new ItemStack(ModItems.BROWN_HARNESS.get()))
            .displayItems((parameters, output) -> {
                output.accept(ModBlocks.DRIED_GHAST.get());
                output.accept(ModItems.HAPPY_GHAST_SPAWN_EGG.get());
                output.accept(ModItems.WHITE_HARNESS.get());
                output.accept(ModItems.ORANGE_HARNESS.get());
                output.accept(ModItems.MAGENTA_HARNESS.get());
                output.accept(ModItems.LIGHT_BLUE_HARNESS.get());
                output.accept(ModItems.YELLOW_HARNESS.get());
                output.accept(ModItems.LIME_HARNESS.get());
                output.accept(ModItems.PINK_HARNESS.get());
                output.accept(ModItems.GRAY_HARNESS.get());
                output.accept(ModItems.LIGHT_GRAY_HARNESS.get());
                output.accept(ModItems.CYAN_HARNESS.get());
                output.accept(ModItems.PURPLE_HARNESS.get());
                output.accept(ModItems.BLUE_HARNESS.get());
                output.accept(ModItems.BROWN_HARNESS.get());
                output.accept(ModItems.GREEN_HARNESS.get());
                output.accept(ModItems.RED_HARNESS.get());
                output.accept(ModItems.BLACK_HARNESS.get());
            })
            .build()
    );

    Supplier<CreativeModeTab> SPRING_TO_LIFE = TABS.register(
        "spring_to_life",
        () -> CreativeModeTab.builder(CreativeModeTab.Row.TOP, 2)
            .title(Component.literal("Spring to Life"))
            .icon(() -> new ItemStack(ModBlocks.FIREFLY_BUSH.get()))
            .displayItems((parameters, output) -> {
                output.accept(ModBlocks.FIREFLY_BUSH.get());
            })
            .build()
    );
}
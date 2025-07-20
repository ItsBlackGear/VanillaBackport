package com.blackgear.vanillabackport.client.registries;

import com.blackgear.vanillabackport.client.api.selector.BundledContent;
import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.blackgear.vanillabackport.common.registries.ModItems;
import com.blackgear.vanillabackport.common.registries.ModPaintingVariants;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.item.*;

import java.util.ArrayList;
import java.util.List;

public class ModBundledContents {
    private static final List<BundledContent> FILTERS = new ArrayList<>();

    public static final BundledContent BUNDLES_OF_BRAVERY = register(
        BundledContent.builder()
            .title(Component.literal("Bundles of Bravery"))
            .icon(new ItemStack(Items.BUNDLE))
            .displayItems((provider, output) -> {
                output.accept(Items.BUNDLE);
                output.accept(ModItems.WHITE_BUNDLE.get());
                output.accept(ModItems.LIGHT_GRAY_BUNDLE.get());
                output.accept(ModItems.GRAY_BUNDLE.get());
                output.accept(ModItems.BLACK_BUNDLE.get());
                output.accept(ModItems.BROWN_BUNDLE.get());
                output.accept(ModItems.RED_BUNDLE.get());
                output.accept(ModItems.ORANGE_BUNDLE.get());
                output.accept(ModItems.YELLOW_BUNDLE.get());
                output.accept(ModItems.LIME_BUNDLE.get());
                output.accept(ModItems.GREEN_BUNDLE.get());
                output.accept(ModItems.CYAN_BUNDLE.get());
                output.accept(ModItems.LIGHT_BLUE_BUNDLE.get());
                output.accept(ModItems.BLUE_BUNDLE.get());
                output.accept(ModItems.PURPLE_BUNDLE.get());
                output.accept(ModItems.MAGENTA_BUNDLE.get());
                output.accept(ModItems.PINK_BUNDLE.get());
            })
            .build()
    );

    public static final BundledContent THE_GARDEN_AWAKENS = register(
        BundledContent.builder()
            .title(Component.literal("The Garden Awakens"))
            .icon(new ItemStack(ModBlocks.OPEN_EYEBLOSSOM.get()))
            .displayItems((provider, output) -> {
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

    public static final BundledContent SPRING_TO_LIFE = register(
        BundledContent.builder()
            .title(Component.literal("Spring to Life"))
            .icon(new ItemStack(ModBlocks.WILDFLOWERS.get()))
            .displayItems((provider, output) -> {
                output.accept(ModBlocks.BUSH.get());
                output.accept(ModBlocks.FIREFLY_BUSH.get());
                output.accept(ModBlocks.WILDFLOWERS.get());
                output.accept(ModBlocks.LEAF_LITTER.get());
                output.accept(ModBlocks.CACTUS_FLOWER.get());
                output.accept(ModBlocks.SHORT_DRY_GRASS.get());
                output.accept(ModBlocks.TALL_DRY_GRASS.get());
            })
            .build()
    );

    public static final BundledContent CHASE_THE_SKIES = register(
        BundledContent.builder()
            .title(Component.literal("Chase The Skies"))
            .icon(new ItemStack(ModItems.BLUE_HARNESS.get()))
            .displayItems((provider, output) -> {
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
                output.accept(ModItems.MUSIC_DISC_TEARS.get());
            })
            .build()
    );

    public static final BundledContent HOT_AS_LAVA = register(
        BundledContent.builder()
            .title(Component.literal("Hot as Lava"))
            .icon(new ItemStack(ModItems.MUSIC_DISC_LAVA_CHICKEN.get()))
            .displayItems((provider, output) -> {
                output.accept(ModItems.MUSIC_DISC_LAVA_CHICKEN.get());
                provider.lookup(Registries.PAINTING_VARIANT)
                    .ifPresent(variants -> variants.listElements()
                        .filter(holder -> holder.is(ModPaintingVariants.DENNIS))
                        .forEach(reference -> {
                            ItemStack stack = new ItemStack(Items.PAINTING);
                            CompoundTag tag = stack.getOrCreateTagElement("EntityTag");
                            Painting.storeVariant(tag, reference);
                            output.accept(stack);
                        }));
            })
            .build()
    );

    public static BundledContent register(BundledContent builder) {
        FILTERS.add(builder);
        return builder;
    }

    public static List<BundledContent> getFilters() {
        return FILTERS;
    }
}
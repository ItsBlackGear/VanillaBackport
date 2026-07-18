package com.blackgear.vanillabackport.client.registries;

import com.blackgear.vanillabackport.client.api.bundled_tabs.BundledTabs;
import com.blackgear.vanillabackport.common.level.block.CopperGolemStatueBlock;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.common.registries.items.ModItems;
import com.blackgear.vanillabackport.common.registries.items.ModPaintingVariants;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryOps;
import net.minecraft.world.entity.decoration.Painting;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.BlockItemStateProperties;
import net.minecraft.world.item.component.CustomData;

import java.util.ArrayList;
import java.util.List;

public class ModBundledTabs {
    private static final List<BundledTabs> FILTERS = new ArrayList<>();

    public static final BundledTabs BUNDLES_OF_BRAVERY = register(
        BundledTabs.builder()
            .title(Component.translatable("bundled_tab.bundles_of_bravery.title"))
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

    public static final BundledTabs THE_GARDEN_AWAKENS = register(
        BundledTabs.builder()
            .title(Component.translatable("bundled_tab.the_garden_awakens.title"))
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

    public static final BundledTabs SPRING_TO_LIFE = register(
        BundledTabs.builder()
            .title(Component.translatable("bundled_tab.spring_to_life.title"))
            .icon(new ItemStack(ModBlocks.WILDFLOWERS.get()))
            .displayItems((provider, output) -> {
                output.accept(ModBlocks.BUSH.get());
                output.accept(ModBlocks.FIREFLY_BUSH.get());
                output.accept(ModBlocks.WILDFLOWERS.get());
                output.accept(ModBlocks.LEAF_LITTER.get());
                output.accept(ModBlocks.CACTUS_FLOWER.get());
                output.accept(ModBlocks.SHORT_DRY_GRASS.get());
                output.accept(ModBlocks.TALL_DRY_GRASS.get());
                output.accept(ModItems.BROWN_EGG.get());
                output.accept(ModItems.BLUE_EGG.get());
            })
            .build()
    );

    public static final BundledTabs CHASE_THE_SKIES = register(
        BundledTabs.builder()
            .title(Component.translatable("bundled_tab.chase_the_skies.title"))
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

    public static final BundledTabs HOT_AS_LAVA = register(
        BundledTabs.builder()
            .title(Component.translatable("bundled_tab.hot_as_lava.title"))
            .icon(new ItemStack(ModItems.MUSIC_DISC_LAVA_CHICKEN.get()))
            .displayItems((provider, output) -> {
                output.accept(ModItems.MUSIC_DISC_LAVA_CHICKEN.get());
                provider.lookup(Registries.PAINTING_VARIANT)
                    .ifPresent(variants -> variants.listElements()
                        .filter(holder -> holder.is(ModPaintingVariants.DENNIS))
                        .forEach(reference -> {
                            RegistryOps<Tag> ops = provider.createSerializationContext(NbtOps.INSTANCE);
                            CustomData customData = CustomData.EMPTY
                                .update(ops, Painting.VARIANT_MAP_CODEC, reference)
                                .getOrThrow()
                                .update(tag -> tag.putString("id", "minecraft:painting"));
                            ItemStack stack = new ItemStack(Items.PAINTING);
                            stack.set(DataComponents.ENTITY_DATA, customData);
                            output.accept(stack);
                        }));
            })
            .build()
    );
    
    public static final BundledTabs COPPER_AGE = register(
        BundledTabs.builder()
            .title(Component.translatable("bundled_tab.copper_age.title"))
            .icon(() -> {
                ItemStack stack = new ItemStack(ModBlocks.COPPER_GOLEM_STATUE.get());
                stack.set(DataComponents.BLOCK_STATE, BlockItemStateProperties.EMPTY.with(CopperGolemStatueBlock.POSE, CopperGolemStatueBlock.Pose.RUNNING));
                return stack;
            })
            .displayItems((provider, output) -> {
                output.accept(ModBlocks.COPPER_CHEST.get());
                output.accept(ModBlocks.EXPOSED_COPPER_CHEST.get());
                output.accept(ModBlocks.WEATHERED_COPPER_CHEST.get());
                output.accept(ModBlocks.OXIDIZED_COPPER_CHEST.get());
                
                output.accept(ModBlocks.WAXED_COPPER_CHEST.get());
                output.accept(ModBlocks.WAXED_EXPOSED_COPPER_CHEST.get());
                output.accept(ModBlocks.WAXED_WEATHERED_COPPER_CHEST.get());
                output.accept(ModBlocks.WAXED_OXIDIZED_COPPER_CHEST.get());
                
                output.accept(ModBlocks.OAK_SHELF.get());
                output.accept(ModBlocks.BIRCH_SHELF.get());
                output.accept(ModBlocks.SPRUCE_SHELF.get());
                output.accept(ModBlocks.JUNGLE_SHELF.get());
                output.accept(ModBlocks.ACACIA_SHELF.get());
                output.accept(ModBlocks.DARK_OAK_SHELF.get());
                output.accept(ModBlocks.CRIMSON_SHELF.get());
                output.accept(ModBlocks.WARPED_SHELF.get());
                output.accept(ModBlocks.MANGROVE_SHELF.get());
                output.accept(ModBlocks.BAMBOO_SHELF.get());
                output.accept(ModBlocks.CHERRY_SHELF.get());
                output.accept(ModBlocks.PALE_OAK_SHELF.get());
                
                output.accept(ModBlocks.COPPER_GOLEM_STATUE.get());
                output.accept(ModBlocks.EXPOSED_COPPER_GOLEM_STATUE.get());
                output.accept(ModBlocks.WEATHERED_COPPER_GOLEM_STATUE.get());
                output.accept(ModBlocks.OXIDIZED_COPPER_GOLEM_STATUE.get());
                
                output.accept(ModBlocks.WAXED_COPPER_GOLEM_STATUE.get());
                output.accept(ModBlocks.WAXED_EXPOSED_COPPER_GOLEM_STATUE.get());
                output.accept(ModBlocks.WAXED_WEATHERED_COPPER_GOLEM_STATUE.get());
                output.accept(ModBlocks.WAXED_OXIDIZED_COPPER_GOLEM_STATUE.get());
                
                output.accept(ModBlocks.EXPOSED_LIGHTNING_ROD.get());
                output.accept(ModBlocks.WEATHERED_LIGHTNING_ROD.get());
                output.accept(ModBlocks.OXIDIZED_LIGHTNING_ROD.get());
                output.accept(ModBlocks.WAXED_LIGHTNING_ROD.get());
                
                output.accept(ModBlocks.WAXED_EXPOSED_LIGHTNING_ROD.get());
                output.accept(ModBlocks.WAXED_WEATHERED_LIGHTNING_ROD.get());
                output.accept(ModBlocks.WAXED_OXIDIZED_LIGHTNING_ROD.get());
                
                output.accept(ModItems.COPPER_GOLEM_SPAWN_EGG.get());
                
                output.accept(ModItems.COPPER_SWORD.get());
                output.accept(ModItems.COPPER_AXE.get());
                output.accept(ModItems.COPPER_PICKAXE.get());
                output.accept(ModItems.COPPER_SHOVEL.get());
                output.accept(ModItems.COPPER_HOE.get());
                
                output.accept(ModItems.COPPER_HELMET.get());
                output.accept(ModItems.COPPER_CHESTPLATE.get());
                output.accept(ModItems.COPPER_LEGGINGS.get());
                output.accept(ModItems.COPPER_BOOTS.get());
                
                output.accept(ModItems.COPPER_HORSE_ARMOR.get());
                output.accept(ModItems.COPPER_NUGGET.get());
                
                output.accept(ModBlocks.COPPER_TORCH.getFirst().get());
                ModBlocks.COPPER_LANTERN.asList().forEach(block -> output.accept(block.get()));
                ModBlocks.COPPER_BARS.asList().forEach(block -> output.accept(block.get()));
                ModBlocks.COPPER_CHAIN.asList().forEach(block -> output.accept(block.get()));
            })
            .build()
    );
    
    public static final BundledTabs MOUNTS_OF_MAYHEM = register(
        BundledTabs.builder()
            .title(Component.translatable("bundled_tab.mounts_of_mayhem.title"))
            .icon(new ItemStack(ModItems.IRON_SPEAR.get()))
            .displayItems((provider, output) -> {
                output.accept(ModItems.WOODEN_SPEAR.get());
                output.accept(ModItems.STONE_SPEAR.get());
                output.accept(ModItems.COPPER_SPEAR.get());
                output.accept(ModItems.IRON_SPEAR.get());
                output.accept(ModItems.GOLDEN_SPEAR.get());
                output.accept(ModItems.DIAMOND_SPEAR.get());
                output.accept(ModItems.NETHERITE_SPEAR.get());
                output.accept(ModItems.NETHERITE_HORSE_ARMOR.get());
                output.accept(ModItems.PARCHED_SPAWN_EGG.get());
                output.accept(ModItems.CAMEL_HUSK_SPAWN_EGG.get());
            })
            .build()
    );
    
    public static final BundledTabs CHAOS_CUBED = register(
        BundledTabs.builder()
            .title(Component.translatable("bundled_tab.chaos_cubed.title"))
            .icon(new ItemStack(ModItems.SULFUR_CUBE_BUCKET.get()))
            .displayItems((provider, output) -> {
                output.accept(ModBlocks.CINNABAR.get());
                output.accept(ModBlocks.CINNABAR_SLAB.get());
                output.accept(ModBlocks.CINNABAR_STAIRS.get());
                output.accept(ModBlocks.CINNABAR_WALL.get());
                output.accept(ModBlocks.POLISHED_CINNABAR.get());
                output.accept(ModBlocks.POLISHED_CINNABAR_SLAB.get());
                output.accept(ModBlocks.POLISHED_CINNABAR_STAIRS.get());
                output.accept(ModBlocks.POLISHED_CINNABAR_WALL.get());
                output.accept(ModBlocks.CINNABAR_BRICKS.get());
                output.accept(ModBlocks.CINNABAR_BRICK_SLAB.get());
                output.accept(ModBlocks.CINNABAR_BRICK_STAIRS.get());
                output.accept(ModBlocks.CINNABAR_BRICK_WALL.get());
                output.accept(ModBlocks.CHISELED_CINNABAR.get());
                output.accept(ModBlocks.POTENT_SULFUR.get());
                output.accept(ModBlocks.SULFUR.get());
                output.accept(ModBlocks.SULFUR_SLAB.get());
                output.accept(ModBlocks.SULFUR_STAIRS.get());
                output.accept(ModBlocks.SULFUR_WALL.get());
                output.accept(ModBlocks.POLISHED_SULFUR.get());
                output.accept(ModBlocks.POLISHED_SULFUR_SLAB.get());
                output.accept(ModBlocks.POLISHED_SULFUR_STAIRS.get());
                output.accept(ModBlocks.POLISHED_SULFUR_WALL.get());
                output.accept(ModBlocks.SULFUR_BRICKS.get());
                output.accept(ModBlocks.SULFUR_BRICK_SLAB.get());
                output.accept(ModBlocks.SULFUR_BRICK_STAIRS.get());
                output.accept(ModBlocks.SULFUR_BRICK_WALL.get());
                output.accept(ModBlocks.CHISELED_SULFUR.get());
                output.accept(ModBlocks.SULFUR_SPIKE.get());
                output.accept(ModItems.SULFUR_CUBE_BUCKET.get());
                output.accept(ModItems.SULFUR_CUBE_SPAWN_EGG.get());
                output.accept(ModItems.MUSIC_DISC_BOUNCE.get());
            })
            .build()
    );
    
    public static final BundledTabs MISCELLANEOUS = register(
        BundledTabs.builder()
            .title(Component.translatable("bundled_tab.miscellaneous.title"))
            .icon(new ItemStack(Items.NAME_TAG))
            .displayItems((provider, output) -> {
                output.accept(ModBlocks.WHITE_WOOL_STAIRS.get());
                output.accept(ModBlocks.ORANGE_WOOL_STAIRS.get());
                output.accept(ModBlocks.MAGENTA_WOOL_STAIRS.get());
                output.accept(ModBlocks.LIGHT_BLUE_WOOL_STAIRS.get());
                output.accept(ModBlocks.YELLOW_WOOL_STAIRS.get());
                output.accept(ModBlocks.LIME_WOOL_STAIRS.get());
                output.accept(ModBlocks.PINK_WOOL_STAIRS.get());
                output.accept(ModBlocks.GRAY_WOOL_STAIRS.get());
                output.accept(ModBlocks.LIGHT_GRAY_WOOL_STAIRS.get());
                output.accept(ModBlocks.CYAN_WOOL_STAIRS.get());
                output.accept(ModBlocks.PURPLE_WOOL_STAIRS.get());
                output.accept(ModBlocks.BLUE_WOOL_STAIRS.get());
                output.accept(ModBlocks.BROWN_WOOL_STAIRS.get());
                output.accept(ModBlocks.GREEN_WOOL_STAIRS.get());
                output.accept(ModBlocks.RED_WOOL_STAIRS.get());
                output.accept(ModBlocks.BLACK_WOOL_STAIRS.get());
                
                output.accept(ModBlocks.WHITE_WOOL_SLAB.get());
                output.accept(ModBlocks.ORANGE_WOOL_SLAB.get());
                output.accept(ModBlocks.MAGENTA_WOOL_SLAB.get());
                output.accept(ModBlocks.LIGHT_BLUE_WOOL_SLAB.get());
                output.accept(ModBlocks.YELLOW_WOOL_SLAB.get());
                output.accept(ModBlocks.LIME_WOOL_SLAB.get());
                output.accept(ModBlocks.PINK_WOOL_SLAB.get());
                output.accept(ModBlocks.GRAY_WOOL_SLAB.get());
                output.accept(ModBlocks.LIGHT_GRAY_WOOL_SLAB.get());
                output.accept(ModBlocks.CYAN_WOOL_SLAB.get());
                output.accept(ModBlocks.PURPLE_WOOL_SLAB.get());
                output.accept(ModBlocks.BLUE_WOOL_SLAB.get());
                output.accept(ModBlocks.BROWN_WOOL_SLAB.get());
                output.accept(ModBlocks.GREEN_WOOL_SLAB.get());
                output.accept(ModBlocks.RED_WOOL_SLAB.get());
                output.accept(ModBlocks.BLACK_WOOL_SLAB.get());
                
                output.accept(ModItems.WHITE_CUSHION.get());
                output.accept(ModItems.ORANGE_CUSHION.get());
                output.accept(ModItems.MAGENTA_CUSHION.get());
                output.accept(ModItems.LIGHT_BLUE_CUSHION.get());
                output.accept(ModItems.YELLOW_CUSHION.get());
                output.accept(ModItems.LIME_CUSHION.get());
                output.accept(ModItems.PINK_CUSHION.get());
                output.accept(ModItems.GRAY_CUSHION.get());
                output.accept(ModItems.LIGHT_GRAY_CUSHION.get());
                output.accept(ModItems.CYAN_CUSHION.get());
                output.accept(ModItems.PURPLE_CUSHION.get());
                output.accept(ModItems.BLUE_CUSHION.get());
                output.accept(ModItems.BROWN_CUSHION.get());
                output.accept(ModItems.GREEN_CUSHION.get());
                output.accept(ModItems.RED_CUSHION.get());
                output.accept(ModItems.BLACK_CUSHION.get());
            })
            .build()
    );

    public static BundledTabs register(BundledTabs builder) {
        FILTERS.add(builder);
        return builder;
    }

    public static List<BundledTabs> getTabs() {
        return FILTERS;
    }
}
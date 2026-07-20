package com.blackgear.vanillabackport.data.server.tags;

import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import com.blackgear.vanillabackport.core.data.tags.loader.ConventionalBlockTags;
import com.blackgear.vanillabackport.core.data.tags.loader.ConventionalItemTags;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.Collection;
import java.util.function.Supplier;

public abstract class BlockItemTagGenerator {
    protected void addTags() {
        this.handleConventionalTags();

        this.tag(BlockTags.PLANKS, ItemTags.PLANKS)
            .add(ModBlocks.PALE_OAK_PLANKS.get());

        this.tag(BlockTags.WOODEN_BUTTONS, ItemTags.WOODEN_BUTTONS)
            .add(ModBlocks.PALE_OAK_BUTTON.get());

        this.tag(BlockTags.WOODEN_DOORS, ItemTags.WOODEN_DOORS)
            .add(ModBlocks.PALE_OAK_DOOR.get());

        this.tag(BlockTags.WOODEN_STAIRS, ItemTags.WOODEN_STAIRS)
            .add(ModBlocks.PALE_OAK_STAIRS.get());

        this.tag(BlockTags.WOODEN_SLABS, ItemTags.WOODEN_SLABS)
            .add(ModBlocks.PALE_OAK_SLAB.get());

        this.tag(BlockTags.WOODEN_FENCES, ItemTags.WOODEN_FENCES)
            .add(ModBlocks.PALE_OAK_FENCE.get());

        this.tag(BlockTags.FENCE_GATES, ItemTags.FENCE_GATES)
            .add(ModBlocks.PALE_OAK_FENCE_GATE.get());

        this.tag(BlockTags.WOODEN_PRESSURE_PLATES, ItemTags.WOODEN_PRESSURE_PLATES)
            .add(ModBlocks.PALE_OAK_PRESSURE_PLATE.get());

        this.tag(BlockTags.SAPLINGS, ItemTags.SAPLINGS)
            .add(ModBlocks.PALE_OAK_SAPLING.get());

        this.tag(ModBlockTags.PALE_OAK_LOGS, ModItemTags.PALE_OAK_LOGS)
            .add(ModBlocks.PALE_OAK_LOG.get(), ModBlocks.PALE_OAK_WOOD.get(), ModBlocks.STRIPPED_PALE_OAK_LOG.get(), ModBlocks.STRIPPED_PALE_OAK_WOOD.get());

        this.tag(BlockTags.LOGS_THAT_BURN, ItemTags.LOGS_THAT_BURN)
            .addTag(ModBlockTags.PALE_OAK_LOGS);

        this.tag(BlockTags.SLABS, ItemTags.SLABS)
            .add(
                ModBlocks.RESIN_BRICK_SLAB.get(),
                ModBlocks.CINNABAR_SLAB.get(),
                ModBlocks.POLISHED_CINNABAR_SLAB.get(),
                ModBlocks.CINNABAR_BRICK_SLAB.get(),
                ModBlocks.SULFUR_SLAB.get(),
                ModBlocks.POLISHED_SULFUR_SLAB.get(),
                ModBlocks.SULFUR_BRICK_SLAB.get()
            )
            .addTag(ModBlockTags.WOOL_SLABS);

        this.tag(BlockTags.WALLS, ItemTags.WALLS)
            .add(
                ModBlocks.RESIN_BRICK_WALL.get(),
                ModBlocks.CINNABAR_WALL.get(),
                ModBlocks.POLISHED_CINNABAR_WALL.get(),
                ModBlocks.CINNABAR_BRICK_WALL.get(),
                ModBlocks.SULFUR_WALL.get(),
                ModBlocks.POLISHED_SULFUR_WALL.get(),
                ModBlocks.SULFUR_BRICK_WALL.get()
            );

        this.tag(BlockTags.STAIRS, ItemTags.STAIRS)
            .add(
                ModBlocks.RESIN_BRICK_STAIRS.get(),
                ModBlocks.CINNABAR_STAIRS.get(),
                ModBlocks.POLISHED_CINNABAR_STAIRS.get(),
                ModBlocks.CINNABAR_BRICK_STAIRS.get(),
                ModBlocks.SULFUR_STAIRS.get(),
                ModBlocks.POLISHED_SULFUR_STAIRS.get(),
                ModBlocks.SULFUR_BRICK_STAIRS.get()
            )
            .addTag(ModBlockTags.WOOL_STAIRS);

        this.tag(BlockTags.LEAVES, ItemTags.LEAVES)
            .add(ModBlocks.PALE_OAK_LEAVES.get());

        this.tag(BlockTags.WOODEN_TRAPDOORS, ItemTags.WOODEN_TRAPDOORS)
            .add(ModBlocks.PALE_OAK_TRAPDOOR.get());

        this.tag(BlockTags.SMALL_FLOWERS, ItemTags.SMALL_FLOWERS)
            .add(ModBlocks.OPEN_EYEBLOSSOM.get(), ModBlocks.CLOSED_EYEBLOSSOM.get());

        this.tag(BlockTags.FLOWERS, ItemTags.FLOWERS)
            .add(ModBlocks.WILDFLOWERS.get(), ModBlocks.CACTUS_FLOWER.get());

        this.tag(BlockTags.DIRT, ItemTags.DIRT)
            .add(ModBlocks.PALE_MOSS_BLOCK.get());

        this.tag(BlockTags.STANDING_SIGNS, ItemTags.SIGNS)
            .add(ModBlocks.PALE_OAK_SIGN.getFirst().get());

        this.tag(BlockTags.CEILING_HANGING_SIGNS, ItemTags.HANGING_SIGNS)
            .add(ModBlocks.PALE_OAK_HANGING_SIGN.getFirst().get());
        
        this.tag(ModBlockTags.COPPER, ModItemTags.COPPER) //TODO: check for conventional tag
            .add(Blocks.COPPER_BLOCK)
            .add(Blocks.EXPOSED_COPPER)
            .add(Blocks.WEATHERED_COPPER)
            .add(Blocks.OXIDIZED_COPPER)
            .add(Blocks.WAXED_COPPER_BLOCK)
            .add(Blocks.WAXED_EXPOSED_COPPER)
            .add(Blocks.WAXED_WEATHERED_COPPER)
            .add(Blocks.WAXED_OXIDIZED_COPPER);
        
        this.tag(ModBlockTags.COPPER_GOLEM_STATUES, ModItemTags.COPPER_GOLEM_STATUES)
            .add(ModBlocks.COPPER_GOLEM_STATUE.get())
            .add(ModBlocks.EXPOSED_COPPER_GOLEM_STATUE.get())
            .add(ModBlocks.WEATHERED_COPPER_GOLEM_STATUE.get())
            .add(ModBlocks.OXIDIZED_COPPER_GOLEM_STATUE.get())
            .add(ModBlocks.WAXED_COPPER_GOLEM_STATUE.get())
            .add(ModBlocks.WAXED_EXPOSED_COPPER_GOLEM_STATUE.get())
            .add(ModBlocks.WAXED_WEATHERED_COPPER_GOLEM_STATUE.get())
            .add(ModBlocks.WAXED_OXIDIZED_COPPER_GOLEM_STATUE.get());
        
        this.tag(ModBlockTags.LANTERNS, ModItemTags.LANTERNS)
            .add(Blocks.LANTERN, Blocks.SOUL_LANTERN)
            .addAll(ModBlocks.COPPER_LANTERN.asList().stream().map(Supplier::get).toList());
        
        this.tag(ModBlockTags.BARS, ModItemTags.BARS)
            .add(Blocks.IRON_BARS)
            .addAll(ModBlocks.COPPER_BARS.asList().stream().map(Supplier::get).toList());
        
        this.tag(ModBlockTags.CHAINS, ModItemTags.CHAINS)
            .add(Blocks.CHAIN)
            .addAll(ModBlocks.COPPER_CHAIN.asList().stream().map(Supplier::get).toList());
        
        this.tag(ModBlockTags.LIGHTNING_RODS, ModItemTags.LIGHTNING_RODS)
            .add(Blocks.LIGHTNING_ROD)
            .add(ModBlocks.EXPOSED_LIGHTNING_ROD.get())
            .add(ModBlocks.WEATHERED_LIGHTNING_ROD.get())
            .add(ModBlocks.OXIDIZED_LIGHTNING_ROD.get())
            .add(ModBlocks.WAXED_LIGHTNING_ROD.get())
            .add(ModBlocks.WAXED_EXPOSED_LIGHTNING_ROD.get())
            .add(ModBlocks.WAXED_WEATHERED_LIGHTNING_ROD.get())
            .add(ModBlocks.WAXED_OXIDIZED_LIGHTNING_ROD.get());
        
        this.tag(ModBlockTags.WOODEN_SHELVES, ModItemTags.WOODEN_SHELVES)
            .add(ModBlocks.ACACIA_SHELF.get())
            .add(ModBlocks.BAMBOO_SHELF.get())
            .add(ModBlocks.BIRCH_SHELF.get())
            .add(ModBlocks.CHERRY_SHELF.get())
            .add(ModBlocks.CRIMSON_SHELF.get())
            .add(ModBlocks.DARK_OAK_SHELF.get())
            .add(ModBlocks.JUNGLE_SHELF.get())
            .add(ModBlocks.MANGROVE_SHELF.get())
            .add(ModBlocks.OAK_SHELF.get())
            .add(ModBlocks.PALE_OAK_SHELF.get())
            .add(ModBlocks.SPRUCE_SHELF.get())
            .add(ModBlocks.WARPED_SHELF.get());
        
        this.tag(ModBlockTags.WOOL_STAIRS, ModItemTags.WOOL_STAIRS)
            .add(
                ModBlocks.BLACK_WOOL_STAIRS.get(),
                ModBlocks.BLUE_WOOL_STAIRS.get(),
                ModBlocks.BROWN_WOOL_STAIRS.get(),
                ModBlocks.CYAN_WOOL_STAIRS.get(),
                ModBlocks.GRAY_WOOL_STAIRS.get(),
                ModBlocks.GREEN_WOOL_STAIRS.get(),
                ModBlocks.LIGHT_BLUE_WOOL_STAIRS.get(),
                ModBlocks.LIGHT_GRAY_WOOL_STAIRS.get(),
                ModBlocks.LIME_WOOL_STAIRS.get(),
                ModBlocks.MAGENTA_WOOL_STAIRS.get(),
                ModBlocks.ORANGE_WOOL_STAIRS.get(),
                ModBlocks.PINK_WOOL_STAIRS.get(),
                ModBlocks.PURPLE_WOOL_STAIRS.get(),
                ModBlocks.RED_WOOL_STAIRS.get(),
                ModBlocks.YELLOW_WOOL_STAIRS.get(),
                ModBlocks.WHITE_WOOL_STAIRS.get()
            );
        
        this.tag(ModBlockTags.WOOL_SLABS, ModItemTags.WOOL_SLABS)
            .add(
                ModBlocks.BLACK_WOOL_SLAB.get(),
                ModBlocks.BLUE_WOOL_SLAB.get(),
                ModBlocks.BROWN_WOOL_SLAB.get(),
                ModBlocks.CYAN_WOOL_SLAB.get(),
                ModBlocks.GRAY_WOOL_SLAB.get(),
                ModBlocks.GREEN_WOOL_SLAB.get(),
                ModBlocks.LIGHT_BLUE_WOOL_SLAB.get(),
                ModBlocks.LIGHT_GRAY_WOOL_SLAB.get(),
                ModBlocks.LIME_WOOL_SLAB.get(),
                ModBlocks.MAGENTA_WOOL_SLAB.get(),
                ModBlocks.ORANGE_WOOL_SLAB.get(),
                ModBlocks.PINK_WOOL_SLAB.get(),
                ModBlocks.PURPLE_WOOL_SLAB.get(),
                ModBlocks.RED_WOOL_SLAB.get(),
                ModBlocks.YELLOW_WOOL_SLAB.get(),
                ModBlocks.WHITE_WOOL_SLAB.get()
            );
        
        this.tag(BlockTags.DAMPENS_VIBRATIONS, ItemTags.DAMPENS_VIBRATIONS)
            .addTag(ModBlockTags.WOOL_STAIRS)
            .addTag(ModBlockTags.WOOL_SLABS);
    }

    private void handleConventionalTags() {
        this.tag(ConventionalBlockTags.STRIPPED_LOGS, ConventionalItemTags.STRIPPED_LOGS)
            .add(ModBlocks.STRIPPED_PALE_OAK_LOG.get());

        this.tag(ConventionalBlockTags.STRIPPED_WOOD, ConventionalItemTags.STRIPPED_WOOD)
            .add(ModBlocks.STRIPPED_PALE_OAK_WOOD.get());
    }

    protected abstract TagHolder tag(TagKey<Block> block, TagKey<Item> item);

    protected static class TagHolder {
        private final FabricTagProvider<Item>.FabricTagBuilder item;
        private final FabricTagProvider<Block>.FabricTagBuilder block;

        public TagHolder(FabricTagProvider<Item>.FabricTagBuilder item, FabricTagProvider<Block>.FabricTagBuilder block) {
            this.item = item;
            this.block = block;
        }

        public TagHolder add(Block block) {
            if (this.item != null) {
                this.item.add(block.asItem());
            } else {
                this.block.add(block);
            }

            return this;
        }

        public TagHolder add(Block... toAdd) {
            if (this.item != null) {
                for (Block block : toAdd) {
                    this.item.add(block.asItem());
                }
            } else {
                this.block.add(toAdd);
            }

            return this;
        }
        
        public TagHolder addAll(Collection<Block> elements) {
            elements.forEach(this::add);
            return this;
        }

        private static TagKey<Item> blockTagToItemTag(TagKey<Block> tagKey) {
            return TagKey.create(Registries.ITEM, tagKey.location());
        }

        public TagHolder addOptional(ResourceLocation location) {
            if (this.item != null) {
                this.item.addOptional(location);
            } else {
                this.block.addOptional(location);
            }

            return this;
        }

        public TagHolder addTag(TagKey<Block> tag) {
            if (this.item != null) {
                this.item.addTag(blockTagToItemTag(tag));
            } else {
                this.block.addTag(tag);
            }

            return this;
        }

        public TagHolder addOptionalTag(TagKey<Block> tag) {
            if (this.item != null) {
                this.item.addOptionalTag(blockTagToItemTag(tag));
            } else {
                this.block.addOptionalTag(tag);
            }

            return this;
        }
    }
}
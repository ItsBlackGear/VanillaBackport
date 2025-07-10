package com.blackgear.vanillabackport.data.server.tags;

import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

public abstract class BlockItemTagGenerator {
    protected void addTags() {
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
            .add(ModBlocks.RESIN_BRICK_SLAB.get());

        this.tag(BlockTags.WALLS, ItemTags.WALLS)
            .add(ModBlocks.RESIN_BRICK_WALL.get());

        this.tag(BlockTags.STAIRS, ItemTags.STAIRS)
            .add(ModBlocks.RESIN_BRICK_STAIRS.get());

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
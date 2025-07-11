package com.blackgear.vanillabackport.data.server.tags;

import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class BlockTagGenerator extends FabricTagProvider.BlockTagProvider {
    public BlockTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        new BlockItemTagGenerator() {
            @Override
            protected TagHolder tag(TagKey<Block> block, TagKey<Item> item) {
                return new TagHolder(null, BlockTagGenerator.this.getOrCreateTagBuilder(block));
            }
        }.addTags();

        this.getOrCreateTagBuilder(BlockTags.OVERWORLD_NATURAL_LOGS)
            .add(ModBlocks.PALE_OAK_LOG.get());

        this.getOrCreateTagBuilder(BlockTags.ENDERMAN_HOLDABLE)
            .add(ModBlocks.CACTUS_FLOWER.get());

        this.getOrCreateTagBuilder(BlockTags.FLOWER_POTS)
            .add(
                ModBlocks.POTTED_OPEN_EYEBLOSSOM.get(),
                ModBlocks.POTTED_CLOSED_EYEBLOSSOM.get(),
                ModBlocks.POTTED_PALE_OAK_SAPLING.get()
            );

        this.getOrCreateTagBuilder(BlockTags.WALL_SIGNS)
            .add(ModBlocks.PALE_OAK_SIGN.getSecond().get());

        this.getOrCreateTagBuilder(BlockTags.WALL_HANGING_SIGNS)
            .add(ModBlocks.PALE_OAK_HANGING_SIGN.getSecond().get());

        this.getOrCreateTagBuilder(BlockTags.WALL_POST_OVERRIDE)
            .add(ModBlocks.CACTUS_FLOWER.get());

        this.getOrCreateTagBuilder(BlockTags.INSIDE_STEP_SOUND_BLOCKS)
            .add(ModBlocks.WILDFLOWERS.get(), ModBlocks.LEAF_LITTER.get());

        this.getOrCreateTagBuilder(BlockTags.COMBINATION_STEP_SOUND_BLOCKS)
            .add(ModBlocks.PALE_MOSS_CARPET.get(), ModBlocks.RESIN_CLUMP.get());

        this.getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_AXE)
            .add(ModBlocks.CREAKING_HEART.get());

        this.getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_HOE)
            .add(
                ModBlocks.PALE_OAK_LEAVES.get(),
                ModBlocks.PALE_MOSS_BLOCK.get(),
                ModBlocks.PALE_MOSS_CARPET.get()
            );

        this.getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(
                ModBlocks.RESIN_BRICKS.get(),
                ModBlocks.RESIN_BRICK_SLAB.get(),
                ModBlocks.RESIN_BRICK_WALL.get(),
                ModBlocks.RESIN_BRICK_STAIRS.get(),
                ModBlocks.CHISELED_RESIN_BRICKS.get()
            );

        this.getOrCreateTagBuilder(BlockTags.REPLACEABLE_BY_TREES)
            .add(
                ModBlocks.PALE_MOSS_CARPET.get(),
                ModBlocks.BUSH.get(),
                ModBlocks.FIREFLY_BUSH.get(),
                ModBlocks.LEAF_LITTER.get(),
                ModBlocks.SHORT_DRY_GRASS.get(),
                ModBlocks.TALL_DRY_GRASS.get()
            );

        this.getOrCreateTagBuilder(BlockTags.SNIFFER_DIGGABLE_BLOCK)
            .add(ModBlocks.PALE_MOSS_BLOCK.get());

        this.getOrCreateTagBuilder(ModBlockTags.HAPPY_GHAST_AVOIDS)
            .add(
                Blocks.SWEET_BERRY_BUSH,
                Blocks.CACTUS,
                Blocks.WITHER_ROSE,
                Blocks.MAGMA_BLOCK,
                Blocks.FIRE,
                Blocks.POINTED_DRIPSTONE
            );

        this.getOrCreateTagBuilder(ModBlockTags.TRIGGERS_AMBIENT_DESERT_SAND_BLOCK_SOUNDS)
            .add(Blocks.SAND, Blocks.RED_SAND);

        this.getOrCreateTagBuilder(ModBlockTags.TRIGGERS_AMBIENT_DESERT_DRY_VEGETATION_BLOCK_SOUNDS)
            .forceAddTag(BlockTags.TERRACOTTA)
            .add(Blocks.SAND, Blocks.RED_SAND);

        this.getOrCreateTagBuilder(ModBlockTags.TRIGGERS_AMBIENT_DRIED_GHAST_BLOCK_SOUNDS)
            .add(Blocks.SOUL_SAND, Blocks.SOUL_SOIL);

        this.getOrCreateTagBuilder(ModBlockTags.IGNORE_FALLING_LEAVES)
            .add(Blocks.CHERRY_LEAVES, ModBlocks.PALE_OAK_LEAVES.get());

        this.getOrCreateTagBuilder(ModBlockTags.CONIFEROUS_LEAVES)
            .add(Blocks.SPRUCE_LEAVES);
    }
}
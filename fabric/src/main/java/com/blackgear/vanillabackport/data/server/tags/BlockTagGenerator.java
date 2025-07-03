package com.blackgear.vanillabackport.data.server.tags;

import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;

import java.util.concurrent.CompletableFuture;

public class BlockTagGenerator extends FabricTagProvider.BlockTagProvider {
    public BlockTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        // Wood-related block tags
        addWoodRelatedTags();

        // Building blocks
        addBuildingBlockTags();

        // Nature blocks
        addNatureTags();

        // Tool mineable blocks
        addToolTags();

        // Special behavior tags
        addSpecialBehaviorTags();

        this.getOrCreateTagBuilder(BlockTags.WALL_POST_OVERRIDE)
            .add(ModBlocks.CACTUS_FLOWER.get());

        this.getOrCreateTagBuilder(ModBlockTags.TRIGGERS_AMBIENT_DESERT_SAND_BLOCK_SOUNDS)
            .add(Blocks.SAND, Blocks.RED_SAND);
        this.getOrCreateTagBuilder(ModBlockTags.TRIGGERS_AMBIENT_DESERT_DRY_VEGETATION_BLOCK_SOUNDS)
            .forceAddTag(BlockTags.TERRACOTTA)
            .add(Blocks.SAND, Blocks.RED_SAND);
        this.getOrCreateTagBuilder(ModBlockTags.TRIGGERS_AMBIENT_DRIED_GHAST_BLOCK_SOUNDS)
            .add(Blocks.SOUL_SAND, Blocks.SOUL_SOIL);
    }

    private void addWoodRelatedTags() {
        this.getOrCreateTagBuilder(BlockTags.PLANKS)
            .add(ModBlocks.PALE_OAK_PLANKS.get());

        this.getOrCreateTagBuilder(BlockTags.WOODEN_BUTTONS)
            .add(ModBlocks.PALE_OAK_BUTTON.get());

        this.getOrCreateTagBuilder(BlockTags.WOODEN_DOORS)
            .add(ModBlocks.PALE_OAK_DOOR.get());

        this.getOrCreateTagBuilder(BlockTags.WOODEN_STAIRS)
            .add(ModBlocks.PALE_OAK_STAIRS.get());

        this.getOrCreateTagBuilder(BlockTags.WOODEN_SLABS)
            .add(ModBlocks.PALE_OAK_SLAB.get());

        this.getOrCreateTagBuilder(BlockTags.WOODEN_FENCES)
            .add(ModBlocks.PALE_OAK_FENCE.get());

        this.getOrCreateTagBuilder(BlockTags.FENCE_GATES)
            .add(ModBlocks.PALE_OAK_FENCE_GATE.get());

        this.getOrCreateTagBuilder(BlockTags.WOODEN_PRESSURE_PLATES)
            .add(ModBlocks.PALE_OAK_PRESSURE_PLATE.get());

        this.getOrCreateTagBuilder(BlockTags.WOODEN_TRAPDOORS)
            .add(ModBlocks.PALE_OAK_TRAPDOOR.get());

        this.getOrCreateTagBuilder(BlockTags.STANDING_SIGNS)
            .add(ModBlocks.PALE_OAK_SIGN.getFirst().get());

        this.getOrCreateTagBuilder(BlockTags.WALL_SIGNS)
            .add(ModBlocks.PALE_OAK_SIGN.getSecond().get());

        this.getOrCreateTagBuilder(BlockTags.CEILING_HANGING_SIGNS)
            .add(ModBlocks.PALE_OAK_HANGING_SIGN.getFirst().get());

        this.getOrCreateTagBuilder(BlockTags.WALL_HANGING_SIGNS)
            .add(ModBlocks.PALE_OAK_HANGING_SIGN.getSecond().get());

        this.getOrCreateTagBuilder(BlockTags.LOGS_THAT_BURN)
            .addTag(ModBlockTags.PALE_OAK_LOGS);

        this.getOrCreateTagBuilder(BlockTags.OVERWORLD_NATURAL_LOGS)
            .add(ModBlocks.PALE_OAK_LOG.get());

        this.getOrCreateTagBuilder(ModBlockTags.PALE_OAK_LOGS)
            .add(ModBlocks.PALE_OAK_LOG.get())
            .add(ModBlocks.PALE_OAK_WOOD.get())
            .add(ModBlocks.STRIPPED_PALE_OAK_LOG.get())
            .add(ModBlocks.STRIPPED_PALE_OAK_WOOD.get());

        this.getOrCreateTagBuilder(ModBlockTags.HAPPY_GHAST_AVOIDS)
            .add(
                Blocks.SWEET_BERRY_BUSH,
                Blocks.CACTUS,
                Blocks.WITHER_ROSE,
                Blocks.MAGMA_BLOCK,
                Blocks.FIRE,
                Blocks.POINTED_DRIPSTONE
            );
    }

    private void addBuildingBlockTags() {
        this.getOrCreateTagBuilder(BlockTags.STAIRS)
            .add(ModBlocks.RESIN_BRICK_STAIRS.get());

        this.getOrCreateTagBuilder(BlockTags.SLABS)
            .add(ModBlocks.RESIN_BRICK_SLAB.get());

        this.getOrCreateTagBuilder(BlockTags.WALLS)
            .add(ModBlocks.RESIN_BRICK_WALL.get());
    }

    private void addNatureTags() {
        this.getOrCreateTagBuilder(BlockTags.LEAVES)
            .add(ModBlocks.PALE_OAK_LEAVES.get());

        this.getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_HOE)
            .add(ModBlocks.PALE_OAK_LEAVES.get());

        this.getOrCreateTagBuilder(BlockTags.SAPLINGS)
            .add(ModBlocks.PALE_OAK_SAPLING.get());

        this.getOrCreateTagBuilder(BlockTags.DIRT)
            .add(ModBlocks.PALE_MOSS_BLOCK.get());

        this.getOrCreateTagBuilder(BlockTags.FLOWERS)
            .add(ModBlocks.OPEN_EYEBLOSSOM.get())
            .add(ModBlocks.CLOSED_EYEBLOSSOM.get())
            .add(ModBlocks.WILDFLOWERS.get())
            .add(ModBlocks.CACTUS_FLOWER.get());

        this.getOrCreateTagBuilder(BlockTags.FLOWER_POTS)
            .add(ModBlocks.POTTED_OPEN_EYEBLOSSOM.get())
            .add(ModBlocks.POTTED_CLOSED_EYEBLOSSOM.get())
            .add(ModBlocks.POTTED_PALE_OAK_SAPLING.get());

        this.getOrCreateTagBuilder(BlockTags.REPLACEABLE_BY_TREES)
            .add(
                ModBlocks.BUSH.get(),
                ModBlocks.FIREFLY_BUSH.get(),
                ModBlocks.SHORT_DRY_GRASS.get(),
                ModBlocks.TALL_DRY_GRASS.get()
            );
    }

    private void addToolTags() {
        this.getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_AXE)
            .add(ModBlocks.CREAKING_HEART.get());

        this.getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_HOE)
            .add(ModBlocks.PALE_MOSS_BLOCK.get())
            .add(ModBlocks.PALE_MOSS_CARPET.get());

        this.getOrCreateTagBuilder(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(ModBlocks.RESIN_BRICKS.get())
            .add(ModBlocks.RESIN_BRICK_SLAB.get())
            .add(ModBlocks.RESIN_BRICK_WALL.get())
            .add(ModBlocks.RESIN_BRICK_STAIRS.get())
            .add(ModBlocks.CHISELED_RESIN_BRICKS.get());
    }

    private void addSpecialBehaviorTags() {
        this.getOrCreateTagBuilder(BlockTags.REPLACEABLE_BY_TREES)
            .add(ModBlocks.PALE_MOSS_CARPET.get());

        this.getOrCreateTagBuilder(BlockTags.SNIFFER_DIGGABLE_BLOCK)
            .add(ModBlocks.PALE_MOSS_BLOCK.get());

        this.getOrCreateTagBuilder(BlockTags.COMBINATION_STEP_SOUND_BLOCKS)
            .add(ModBlocks.PALE_MOSS_CARPET.get())
            .add(ModBlocks.RESIN_CLUMP.get());
    }
}
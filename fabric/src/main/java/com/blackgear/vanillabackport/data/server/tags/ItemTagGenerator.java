package com.blackgear.vanillabackport.data.server.tags;

import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.blackgear.vanillabackport.common.registries.ModItems;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends FabricTagProvider.ItemTagProvider {
    public ItemTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
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

        this.getOrCreateTagBuilder(ItemTags.TRIM_MATERIALS)
            .add(ModItems.RESIN_BRICK.get());

        this.getOrCreateTagBuilder(ModItemTags.HARNESSES)
            .add(
                ModItems.WHITE_HARNESS.get(),
                ModItems.ORANGE_HARNESS.get(),
                ModItems.MAGENTA_HARNESS.get(),
                ModItems.LIGHT_BLUE_HARNESS.get(),
                ModItems.YELLOW_HARNESS.get(),
                ModItems.LIME_HARNESS.get(),
                ModItems.PINK_HARNESS.get(),
                ModItems.GRAY_HARNESS.get(),
                ModItems.LIGHT_GRAY_HARNESS.get(),
                ModItems.CYAN_HARNESS.get(),
                ModItems.PURPLE_HARNESS.get(),
                ModItems.BLUE_HARNESS.get(),
                ModItems.BROWN_HARNESS.get(),
                ModItems.GREEN_HARNESS.get(),
                ModItems.RED_HARNESS.get(),
                ModItems.BLACK_HARNESS.get()
            );
        this.getOrCreateTagBuilder(ModItemTags.HAPPY_GHAST_FOOD).add(Items.SNOWBALL);
        this.getOrCreateTagBuilder(ModItemTags.HAPPY_GHAST_TEMPT_ITEMS)
            .addTag(ModItemTags.HAPPY_GHAST_FOOD)
            .addTag(ModItemTags.HARNESSES);

        this.getOrCreateTagBuilder(ItemTags.MUSIC_DISCS)
            .add(
                ModItems.MUSIC_DISC_TEARS.get(),
                ModItems.MUSIC_DISC_LAVA_CHICKEN.get()
            );
    }

    private void addWoodRelatedTags() {
        this.getOrCreateTagBuilder(ItemTags.PLANKS)
            .add(ModBlocks.PALE_OAK_PLANKS.get().asItem());

        this.getOrCreateTagBuilder(ItemTags.WOODEN_BUTTONS)
            .add(ModBlocks.PALE_OAK_BUTTON.get().asItem());

        this.getOrCreateTagBuilder(ItemTags.WOODEN_DOORS)
            .add(ModBlocks.PALE_OAK_DOOR.get().asItem());

        this.getOrCreateTagBuilder(ItemTags.WOODEN_STAIRS)
            .add(ModBlocks.PALE_OAK_STAIRS.get().asItem());

        this.getOrCreateTagBuilder(ItemTags.WOODEN_SLABS)
            .add(ModBlocks.PALE_OAK_SLAB.get().asItem());

        this.getOrCreateTagBuilder(ItemTags.WOODEN_FENCES)
            .add(ModBlocks.PALE_OAK_FENCE.get().asItem());

        this.getOrCreateTagBuilder(ItemTags.FENCE_GATES)
            .add(ModBlocks.PALE_OAK_FENCE_GATE.get().asItem());

        this.getOrCreateTagBuilder(ItemTags.WOODEN_PRESSURE_PLATES)
            .add(ModBlocks.PALE_OAK_PRESSURE_PLATE.get().asItem());

        this.getOrCreateTagBuilder(ItemTags.WOODEN_TRAPDOORS)
            .add(ModBlocks.PALE_OAK_TRAPDOOR.get().asItem());

        this.getOrCreateTagBuilder(ItemTags.SIGNS)
            .add(ModBlocks.PALE_OAK_SIGN.getFirst().get().asItem());

        this.getOrCreateTagBuilder(ItemTags.HANGING_SIGNS)
            .add(ModBlocks.PALE_OAK_HANGING_SIGN.getFirst().get().asItem());

        this.getOrCreateTagBuilder(ItemTags.LOGS_THAT_BURN)
            .addTag(ModItemTags.PALE_OAK_LOGS);

        this.getOrCreateTagBuilder(ModItemTags.PALE_OAK_LOGS)
            .add(ModBlocks.PALE_OAK_LOG.get().asItem())
            .add(ModBlocks.PALE_OAK_WOOD.get().asItem())
            .add(ModBlocks.STRIPPED_PALE_OAK_LOG.get().asItem())
            .add(ModBlocks.STRIPPED_PALE_OAK_WOOD.get().asItem());

        this.getOrCreateTagBuilder(ItemTags.BOATS)
            .add(ModItems.PALE_OAK_BOAT.get());

        this.getOrCreateTagBuilder(ItemTags.CHEST_BOATS)
            .add(ModItems.PALE_OAK_CHEST_BOAT.get());
    }

    private void addBuildingBlockTags() {
        this.getOrCreateTagBuilder(ItemTags.STAIRS)
            .add(ModBlocks.RESIN_BRICK_STAIRS.get().asItem());

        this.getOrCreateTagBuilder(ItemTags.SLABS)
            .add(ModBlocks.RESIN_BRICK_SLAB.get().asItem());

        this.getOrCreateTagBuilder(ItemTags.WALLS)
            .add(ModBlocks.RESIN_BRICK_WALL.get().asItem());
    }

    private void addNatureTags() {
        this.getOrCreateTagBuilder(ItemTags.LEAVES)
            .add(ModBlocks.PALE_OAK_LEAVES.get().asItem());

        this.getOrCreateTagBuilder(ItemTags.SAPLINGS)
            .add(ModBlocks.PALE_OAK_SAPLING.get().asItem());

        this.getOrCreateTagBuilder(ItemTags.DIRT)
            .add(ModBlocks.PALE_MOSS_BLOCK.get().asItem());

        this.getOrCreateTagBuilder(ItemTags.FLOWERS)
            .add(ModBlocks.OPEN_EYEBLOSSOM.get().asItem())
            .add(ModBlocks.CLOSED_EYEBLOSSOM.get().asItem());
    }
}
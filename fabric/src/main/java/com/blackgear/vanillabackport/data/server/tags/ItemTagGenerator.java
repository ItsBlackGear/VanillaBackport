package com.blackgear.vanillabackport.data.server.tags;

import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.blackgear.vanillabackport.common.registries.ModItems;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import com.blackgear.vanillabackport.core.data.tags.create.CreateItemTags;
import com.blackgear.vanillabackport.core.data.tags.fabric.FabricItemTags;
import com.blackgear.vanillabackport.core.data.tags.forge.ForgeItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;

import java.util.concurrent.CompletableFuture;

public class ItemTagGenerator extends FabricTagProvider.ItemTagProvider {
    public ItemTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        new BlockItemTagGenerator() {
            @Override
            protected TagHolder tag(TagKey<Block> block, TagKey<Item> item) {
            return new TagHolder(ItemTagGenerator.this.getOrCreateTagBuilder(item), null);
            }
        }.addTags();
        this.handleConventionalTags();

        this.getOrCreateTagBuilder(ItemTags.BOATS)
            .add(ModItems.PALE_OAK_BOAT.get());

        this.getOrCreateTagBuilder(ItemTags.CHEST_BOATS)
            .add(ModItems.PALE_OAK_CHEST_BOAT.get());

        this.getOrCreateTagBuilder(ModItemTags.BUNDLES)
            .add(
                Items.BUNDLE,
                ModItems.BLACK_BUNDLE.get(),
                ModItems.BLUE_BUNDLE.get(),
                ModItems.BROWN_BUNDLE.get(),
                ModItems.CYAN_BUNDLE.get(),
                ModItems.GRAY_BUNDLE.get(),
                ModItems.GREEN_BUNDLE.get(),
                ModItems.LIGHT_BLUE_BUNDLE.get(),
                ModItems.LIGHT_GRAY_BUNDLE.get(),
                ModItems.LIME_BUNDLE.get(),
                ModItems.MAGENTA_BUNDLE.get(),
                ModItems.ORANGE_BUNDLE.get(),
                ModItems.PINK_BUNDLE.get(),
                ModItems.PURPLE_BUNDLE.get(),
                ModItems.RED_BUNDLE.get(),
                ModItems.YELLOW_BUNDLE.get(),
                ModItems.WHITE_BUNDLE.get()
            );

        this.getOrCreateTagBuilder(ItemTags.MUSIC_DISCS)
            .add(ModItems.MUSIC_DISC_TEARS.get(), ModItems.MUSIC_DISC_LAVA_CHICKEN.get());

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

        this.getOrCreateTagBuilder(ModItemTags.HAPPY_GHAST_FOOD)
            .add(Items.SNOWBALL);

        this.getOrCreateTagBuilder(ModItemTags.HAPPY_GHAST_TEMPT_ITEMS)
            .addTag(ModItemTags.HAPPY_GHAST_FOOD)
            .addTag(ModItemTags.HARNESSES);

        this.getOrCreateTagBuilder(ModItemTags.EGGS)
            .add(Items.EGG, ModItems.BLUE_EGG.get(), ModItems.BROWN_EGG.get());
    }

    private void handleConventionalTags() {
        this.getDualTagBuilder(ForgeItemTags.EGGS, FabricItemTags.EGGS)
            .addTag(ModItemTags.EGGS);
        
        this.getDualTagBuilder(ForgeItemTags.DYED, FabricItemTags.DYED)
            .addTag(ModItemTags.HARNESSES)
            .add(
                ModItems.BLACK_BUNDLE.get(),
                ModItems.BLUE_BUNDLE.get(),
                ModItems.BROWN_BUNDLE.get(),
                ModItems.CYAN_BUNDLE.get(),
                ModItems.GRAY_BUNDLE.get(),
                ModItems.GREEN_BUNDLE.get(),
                ModItems.LIGHT_BLUE_BUNDLE.get(),
                ModItems.LIGHT_GRAY_BUNDLE.get(),
                ModItems.LIME_BUNDLE.get(),
                ModItems.MAGENTA_BUNDLE.get(),
                ModItems.ORANGE_BUNDLE.get(),
                ModItems.PINK_BUNDLE.get(),
                ModItems.PURPLE_BUNDLE.get(),
                ModItems.RED_BUNDLE.get(),
                ModItems.YELLOW_BUNDLE.get(),
                ModItems.WHITE_BUNDLE.get()
            );

        this.getDualTagBuilder(ForgeItemTags.DYED_BLACK, FabricItemTags.DYED_BLACK)
            .add(ModItems.BLACK_BUNDLE.get(), ModItems.BLACK_HARNESS.get());

        this.getDualTagBuilder(ForgeItemTags.DYED_BLUE, FabricItemTags.DYED_BLUE)
            .add(ModItems.BLUE_BUNDLE.get(), ModItems.BLUE_HARNESS.get());

        this.getDualTagBuilder(ForgeItemTags.DYED_BROWN, FabricItemTags.DYED_BROWN)
            .add(ModItems.BROWN_BUNDLE.get(), ModItems.BROWN_HARNESS.get());

        this.getDualTagBuilder(ForgeItemTags.DYED_CYAN, FabricItemTags.DYED_CYAN)
            .add(ModItems.CYAN_BUNDLE.get(), ModItems.CYAN_HARNESS.get());

        this.getDualTagBuilder(ForgeItemTags.DYED_GRAY, FabricItemTags.DYED_GRAY)
            .add(ModItems.GRAY_BUNDLE.get(), ModItems.GRAY_HARNESS.get());

        this.getDualTagBuilder(ForgeItemTags.DYED_GREEN, FabricItemTags.DYED_GREEN)
            .add(ModItems.GREEN_BUNDLE.get(), ModItems.GREEN_HARNESS.get());

        this.getDualTagBuilder(ForgeItemTags.DYED_LIGHT_BLUE, FabricItemTags.DYED_LIGHT_BLUE)
            .add(ModItems.LIGHT_BLUE_BUNDLE.get(), ModItems.LIGHT_BLUE_HARNESS.get());

        this.getDualTagBuilder(ForgeItemTags.DYED_LIGHT_GRAY, FabricItemTags.DYED_LIGHT_GRAY)
            .add(ModItems.LIGHT_GRAY_BUNDLE.get(), ModItems.LIGHT_GRAY_HARNESS.get());

        this.getDualTagBuilder(ForgeItemTags.DYED_LIME, FabricItemTags.DYED_LIME)
            .add(ModItems.LIME_BUNDLE.get(), ModItems.LIME_HARNESS.get());

        this.getDualTagBuilder(ForgeItemTags.DYED_MAGENTA, FabricItemTags.DYED_MAGENTA)
            .add(ModItems.MAGENTA_BUNDLE.get(), ModItems.MAGENTA_HARNESS.get());

        this.getDualTagBuilder(ForgeItemTags.DYED_ORANGE, FabricItemTags.DYED_ORANGE)
            .add(ModItems.ORANGE_BUNDLE.get(), ModItems.ORANGE_HARNESS.get());

        this.getDualTagBuilder(ForgeItemTags.DYED_PINK, FabricItemTags.DYED_PINK)
            .add(ModItems.PINK_BUNDLE.get(), ModItems.PINK_HARNESS.get());

        this.getDualTagBuilder(ForgeItemTags.DYED_PURPLE, FabricItemTags.DYED_PURPLE)
            .add(ModItems.PURPLE_BUNDLE.get(), ModItems.PURPLE_HARNESS.get());

        this.getDualTagBuilder(ForgeItemTags.DYED_RED, FabricItemTags.DYED_RED)
            .add(ModItems.RED_BUNDLE.get(), ModItems.RED_HARNESS.get());

        this.getDualTagBuilder(ForgeItemTags.DYED_YELLOW, FabricItemTags.DYED_YELLOW)
            .add(ModItems.YELLOW_BUNDLE.get(), ModItems.YELLOW_HARNESS.get());

        this.getDualTagBuilder(ForgeItemTags.DYED_WHITE, FabricItemTags.DYED_WHITE)
            .add(ModItems.WHITE_BUNDLE.get(), ModItems.WHITE_HARNESS.get());

        this.getDualTagBuilder(ForgeItemTags.MUSIC_DISCS, FabricItemTags.MUSIC_DISCS)
            .add(ModItems.MUSIC_DISC_TEARS.get(), ModItems.MUSIC_DISC_LAVA_CHICKEN.get());

        this.getOrCreateTagBuilder(CreateItemTags.MODDED_STRIPPED_WOOD)
            .add(ModBlocks.STRIPPED_PALE_OAK_WOOD.get().asItem());

        this.getOrCreateTagBuilder(CreateItemTags.MODDED_STRIPPED_LOGS)
            .add(ModBlocks.STRIPPED_PALE_OAK_LOG.get().asItem());
    }

    protected DualTagHolder getDualTagBuilder(TagKey<Item> forge, TagKey<Item> fabric) {
        return new DualTagHolder(this.getOrCreateTagBuilder(fabric), this.getOrCreateTagBuilder(forge));
    }

    protected record DualTagHolder(FabricTagProvider<Item>.FabricTagBuilder forge, FabricTagProvider<Item>.FabricTagBuilder fabric) {
        public DualTagHolder add(ItemLike entry) {
            this.forge.add(entry.asItem());
            this.fabric.add(entry.asItem());
            return this;
        }

        public DualTagHolder add(Item... toAdd) {
            this.forge.add(toAdd);
            this.fabric.add(toAdd);
            return this;
        }

        public DualTagHolder addOptional(ResourceLocation location) {
            this.forge.addOptional(location);
            this.fabric.addOptional(location);
            return this;
        }

        public DualTagHolder addTag(TagKey<Item> tag) {
            this.forge.addTag(tag);
            this.fabric.addTag(tag);
            return this;
        }

        public DualTagHolder addOptionalTag(TagKey<Item> tag) {
            this.forge.addOptionalTag(tag);
            this.fabric.addOptionalTag(tag);
            return this;
        }
    }
}
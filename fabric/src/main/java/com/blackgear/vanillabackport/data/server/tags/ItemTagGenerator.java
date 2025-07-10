package com.blackgear.vanillabackport.data.server.tags;

import com.blackgear.vanillabackport.common.registries.ModItems;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
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

        this.getOrCreateTagBuilder(ItemTags.BOATS)
            .add(ModItems.PALE_OAK_BOAT.get());

        this.getOrCreateTagBuilder(ItemTags.CHEST_BOATS)
            .add(ModItems.PALE_OAK_CHEST_BOAT.get());

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
    }
}
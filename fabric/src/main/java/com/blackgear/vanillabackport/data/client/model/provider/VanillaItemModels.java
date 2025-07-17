package com.blackgear.vanillabackport.data.client.model.provider;

import com.google.gson.JsonElement;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class VanillaItemModels extends ItemModelGenerators {
    public VanillaItemModels(BiConsumer<ResourceLocation, Supplier<JsonElement>> output) {
        super(output);
    }

    public void createFlatItem(Item item) {
        this.generateFlatItem(item, ModelTemplates.FLAT_ITEM);
    }

    public void createFlatItemWithSuffix(Item item, String suffix) {
        ModelTemplates.FLAT_ITEM.create(ModelLocationUtils.getModelLocation(item), TextureMapping.layer0(TextureMapping.getItemTexture(item, suffix)), this.output);
    }

    public void createMusicDisc(Item item) {
        this.generateFlatItem(item, ModelTemplates.MUSIC_DISC);
    }
}
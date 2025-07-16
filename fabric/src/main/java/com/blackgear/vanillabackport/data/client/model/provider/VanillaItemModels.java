package com.blackgear.vanillabackport.data.client.model.provider;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.minecraft.data.models.ItemModelGenerators;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.data.models.model.ModelTemplates;
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

    public void createMusicDisc(Item item) {
        this.generateFlatItem(item, ModelTemplates.MUSIC_DISC);
    }

    public void createBundle(Item bundle) {
        ResourceLocation filled = ModelLocationUtils.getModelLocation(bundle, "_filled");

        ModelBuilder filledModel = new ModelBuilder()
            .parent("minecraft:item/generated")
            .texture("layer0", filled);
        this.output.accept(filled, filledModel::build);

        ModelBuilder baseModel = new ModelBuilder()
            .parent("minecraft:item/generated")
            .texture("layer0", ModelLocationUtils.getModelLocation(bundle))
            .override(filled, "filled", 0.0000001F);
        this.output.accept(ModelLocationUtils.getModelLocation(bundle), baseModel::build);
    }

    private static class ModelBuilder {
        private final JsonObject json = new JsonObject();
        private final JsonArray overrides = new JsonArray();
        private final JsonObject textures = new JsonObject();

        public ModelBuilder() {
            this.json.add("textures", this.textures);
        }

        public ModelBuilder parent(String parent) {
            this.json.addProperty("parent", parent);
            return this;
        }

        public ModelBuilder texture(String key, ResourceLocation texture) {
            this.textures.addProperty(key, texture.toString());
            return this;
        }

        public ModelBuilder override(ResourceLocation model, String predicate, float value) {
            JsonObject override = new JsonObject();
            JsonObject predicateJson = new JsonObject();
            predicateJson.addProperty(predicate, value);
            override.add("predicate", predicateJson);
            override.addProperty("model", model.toString());
            this.overrides.add(override);

            if (!this.json.has("overrides")) {
                this.json.add("overrides", this.overrides);
            }

            return this;
        }

        public JsonElement build() {
            return this.json;
        }
    }
}
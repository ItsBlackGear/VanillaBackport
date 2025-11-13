package com.blackgear.vanillabackport.data.client.colors;

import com.blackgear.vanillabackport.core.VanillaBackport;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class LeafColorGenerator implements DataProvider {
    private final Map<ResourceLocation, Supplier<JsonElement>> leafColors = new HashMap<>();
    private final PackOutput.PathProvider pathProvider;

    public LeafColorGenerator(FabricDataOutput output) {
        this.pathProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "leaf_colors");
    }

    private void addLeafColors() {
        this.add(Blocks.AZALEA_LEAVES, -9399763);
        this.add(Blocks.FLOWERING_AZALEA_LEAVES, -9399763);
    }

    private void add(Block block, int color) {
        ResourceLocation registry = BuiltInRegistries.BLOCK.getKey(block);
        ResourceLocation name = VanillaBackport.vanilla(registry.getPath());
        this.addElement(name, registry.toString(), color);
    }

    private void add(String block, int color) {
        this.addElement(VanillaBackport.vanilla(block), block, color);
    }

    @Override
    public CompletableFuture<?> run(CachedOutput cachedOutput) {
        this.addLeafColors();

        return CompletableFuture.allOf(
            this.leafColors.entrySet().stream()
                .map(entry -> {
                    Path path = this.pathProvider.json(entry.getKey());
                    JsonElement jsonElement = entry.getValue().get();
                    return DataProvider.saveStable(cachedOutput, jsonElement, path);
                })
                .toArray(CompletableFuture[]::new)
        );
    }

    public void addElement(ResourceLocation name, String block, int color) {
        this.addElement(name, block, color, 0);
    }

    public void addElement(ResourceLocation name, String block, int color, int priority) {
        JsonObject json = new JsonObject();
        json.addProperty("block", block);

        JsonObject properties = new JsonObject();
        properties.addProperty("color", color);
        properties.addProperty("priority", priority);
        json.add("properties", properties);

        if (this.leafColors.put(name, () -> json) != null) {
            throw new IllegalStateException("Duplicate leaf color " + name);
        }
    }

    @Override
    public String getName() {
        return "Leaf Color Definitions";
    }
}
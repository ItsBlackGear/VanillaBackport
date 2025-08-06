package com.blackgear.vanillabackport.data.client.model.provider;

import com.blackgear.vanillabackport.data.client.model.VanillaBlockModels;
import com.blackgear.vanillabackport.data.client.model.VanillaItemModels;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.JsonElement;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.models.blockstates.BlockStateGenerator;
import net.minecraft.data.models.model.DelegatedModel;
import net.minecraft.data.models.model.ModelLocationUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class VanillaModelGenerator implements DataProvider {
    private final PackOutput.PathProvider blockStateProvider;
    private final PackOutput.PathProvider modelProvider;
    private final FabricDataOutput output;

    public VanillaModelGenerator(FabricDataOutput output) {
        this.output = output;
        this.blockStateProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "blockstates");
        this.modelProvider = output.createPathProvider(PackOutput.Target.RESOURCE_PACK, "models");
    }

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        Map<Block, BlockStateGenerator> blockStateGenerators = Maps.newHashMap();
        Map<ResourceLocation, Supplier<JsonElement>> modelSuppliers = Maps.newHashMap();
        Set<Item> generatedItems = Sets.newHashSet();

        Consumer<BlockStateGenerator> blockStateOutput = generator -> {
            Block block = generator.getBlock();
            BlockStateGenerator definition = blockStateGenerators.put(block, generator);
            if (definition != null) throw new IllegalStateException("Duplicate blockstate definition for " + block);
        };

        BiConsumer<ResourceLocation, Supplier<JsonElement>> modelOutput = (location, supplier) -> {
            Supplier<JsonElement> definition = modelSuppliers.put(location, supplier);
            if (definition != null) throw new IllegalStateException("Duplicate model definition for " + location);
        };

        Consumer<Item> skippedAutoModelsOutput = generatedItems::add;

        this.generateBlockStateModels(new VanillaBlockModels(blockStateOutput, modelOutput, skippedAutoModelsOutput));
        this.generateItemModels(new VanillaItemModels(modelOutput));

        // Check for missing blockstate definitions
        List<Block> missingBlockstates = BuiltInRegistries.BLOCK.entrySet().stream()
            .filter(entry -> this.output.isStrictValidationEnabled())
            .map(Map.Entry::getValue)
            .filter(block -> !blockStateGenerators.containsKey(block))
            .toList();

        if (!missingBlockstates.isEmpty()) {
            throw new IllegalStateException("Missing blockstate definitions for: " + missingBlockstates);
        }

        // Add delegated models for items that need them
        BuiltInRegistries.BLOCK.forEach(block -> {
            Item item = Item.BY_BLOCK.get(block);
            if (item == null || generatedItems.contains(item)) return;

            ResourceLocation model = ModelLocationUtils.getModelLocation(item);

            if (blockStateGenerators.containsKey(block) && !modelSuppliers.containsKey(model)) {
                modelSuppliers.put(model, new DelegatedModel(ModelLocationUtils.getModelLocation(block)));
            }
        });

        CompletableFuture<?> blockStatesProvider = this.saveCollection(output, blockStateGenerators, block -> this.blockStateProvider.json(block.builtInRegistryHolder().key().location()));
        CompletableFuture<?> modelsProvider = this.saveCollection(output, modelSuppliers, this.modelProvider::json);

        return CompletableFuture.allOf(blockStatesProvider, modelsProvider);
    }

    private <T> CompletableFuture<?> saveCollection(
        CachedOutput output,
        Map<T, ? extends Supplier<JsonElement>> objectToJsonMap,
        Function<T, Path> resolveObjectPath
    ) {
        return CompletableFuture.allOf(
            objectToJsonMap.entrySet().stream()
                .map(entry -> {
                    Path path = resolveObjectPath.apply(entry.getKey());
                    JsonElement jsonElement = entry.getValue().get();
                    return DataProvider.saveStable(output, jsonElement, path);
                })
                .toArray(CompletableFuture[]::new)
        );
    }

    public abstract void generateBlockStateModels(VanillaBlockModels output);

    public abstract void generateItemModels(VanillaItemModels output);

    @Override
    public String getName() {
        return "Vanilla Model Definitions";
    }
}
package com.blackgear.vanillabackport.data.server.loot;

import com.blackgear.vanillabackport.common.registries.ModEntities;
import com.blackgear.vanillabackport.common.registries.ModEntityTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class EntityLootGenerator extends SimpleFabricLootTableProvider {
    public EntityLootGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup, LootContextParamSets.ENTITY);
    }

    @Override
    public void generate(BiConsumer<ResourceKey<LootTable>, LootTable.Builder> output) {
        output.accept(ModEntities.CREAKING.get().getDefaultLootTable(), LootTable.lootTable());
        output.accept(ModEntities.HAPPY_GHAST.get().getDefaultLootTable(), LootTable.lootTable());
        output.accept(ModEntityTypes.SULFUR_CUBE.getDefaultLootTable(), LootTable.lootTable());
    }
}
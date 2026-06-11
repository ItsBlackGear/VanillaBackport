package com.blackgear.vanillabackport.data.server.loot;

import com.blackgear.vanillabackport.common.registries.ModEntityTypes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.function.BiConsumer;

public class EntityLootGenerator extends SimpleFabricLootTableProvider {
    public EntityLootGenerator(FabricDataOutput output) {
        super(output, LootContextParamSets.ENTITY);
    }

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> output) {
        output.accept(ModEntityTypes.CREAKING.getDefaultLootTable(), LootTable.lootTable());
        output.accept(ModEntityTypes.HAPPY_GHAST.getDefaultLootTable(), LootTable.lootTable());
        output.accept(ModEntityTypes.ARMADILLO.getDefaultLootTable(), LootTable.lootTable());
        output.accept(ModEntityTypes.SULFUR_CUBE.getDefaultLootTable(), LootTable.lootTable());
    }
}
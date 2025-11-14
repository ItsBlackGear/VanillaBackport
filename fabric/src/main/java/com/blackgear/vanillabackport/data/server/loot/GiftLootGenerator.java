package com.blackgear.vanillabackport.data.server.loot;

import com.blackgear.vanillabackport.common.registries.ModBuiltInLootTables;
import com.blackgear.vanillabackport.common.registries.ModItems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.SimpleFabricLootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;

import java.util.function.BiConsumer;

public class GiftLootGenerator extends SimpleFabricLootTableProvider {
    public GiftLootGenerator(FabricDataOutput output) {
        super(output, LootContextParamSets.GIFT);
    }

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> output) {
        output.accept(
            ModBuiltInLootTables.ARMADILLO_SHED,
            LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1.0F)).add(LootItem.lootTableItem(ModItems.ARMADILLO_SCUTE.get())))
        );
    }
}
package com.blackgear.vanillabackport.core.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

public class ModBuiltInLootTables {
    public static final ResourceKey<LootTable> CHICKEN_LAY = ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.withDefaultNamespace("gameplay/chicken_lay"));
}
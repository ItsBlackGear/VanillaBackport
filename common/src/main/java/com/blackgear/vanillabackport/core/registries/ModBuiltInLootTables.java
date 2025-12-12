package com.blackgear.vanillabackport.core.registries;

import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.storage.loot.LootTable;

public class ModBuiltInLootTables {
    public static final ResourceKey<LootTable> CHICKEN_LAY = ResourceKey.create(Registries.LOOT_TABLE, VanillaBackport.vanilla("gameplay/chicken_lay"));
}
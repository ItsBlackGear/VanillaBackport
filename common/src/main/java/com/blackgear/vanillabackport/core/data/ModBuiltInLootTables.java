package com.blackgear.vanillabackport.core.data;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

public class ModBuiltInLootTables {
    // ENTITY LOOT
    public static final ResourceKey<LootTable> CHICKEN_LAY = register("gameplay/chicken_lay");
    
    // CHEST LOOT
    public static final ResourceKey<LootTable> ABANDONED_CAMP_BARREL = register("barrels/abandoned_camp_barrel");
    public static final ResourceKey<LootTable> ABANDONED_CAMP_COMMON_CHEST = register("chests/abandoned_camp_common_chest");
    public static final ResourceKey<LootTable> ABANDONED_CAMP_SECRET_CHEST = register("chests/abandoned_camp_secret_chest");
    
    private static ResourceKey<LootTable> register(String location) {
        return ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.withDefaultNamespace(location));
    }
}
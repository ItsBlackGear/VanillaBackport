package com.blackgear.vanillabackport.core.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

public class LootUtils {
    private static boolean dropFromLootTable(
        ServerLevel level,
        ResourceLocation key,
        Function<LootParams.Builder, LootParams> function,
        BiConsumer<ServerLevel, ItemStack> consumer
    ) {
        LootTable lootTable = level.getServer().getLootData().getLootTable(key);
        LootParams lootParams = function.apply(new LootParams.Builder(level));
        List<ItemStack> list = lootTable.getRandomItems(lootParams);
        if (!list.isEmpty()) {
            list.forEach(stack -> consumer.accept(level, stack));
            return true;
        } else {
            return false;
        }
    }
    
    public static boolean dropFromGiftLootTable(
        Entity entity,
        ServerLevel level,
        ResourceLocation key,
        BiConsumer<ServerLevel, ItemStack> consumer
    ) {
        return dropFromLootTable(
            level,
            key,
            builder -> builder.withParameter(LootContextParams.ORIGIN, entity.position())
                .withParameter(LootContextParams.THIS_ENTITY, entity)
                .create(LootContextParamSets.GIFT),
            consumer
        );
    }
}
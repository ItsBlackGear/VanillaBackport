package com.blackgear.vanillabackport.common.api.extensions.block;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

// Special thanks to Echo2craft
public interface RandomizableContainer extends Container {
    String LOOT_TABLE_TAG = "LootTable";
    String LOOT_TABLE_SEED_TAG = "LootTableSeed";

    @Nullable ResourceLocation getLootTable();
    void setLootTable(@Nullable ResourceLocation lootTable);

    long getLootTableSeed();
    void setLootTableSeed(long seed);

    BlockPos getBlockPos();
    @Nullable Level getLevel();

    default boolean tryLoadLootTable(CompoundTag tag) {
        if (tag.contains(LOOT_TABLE_TAG, Tag.TAG_STRING)) {
            this.setLootTable(ResourceLocation.tryParse(tag.getString(LOOT_TABLE_TAG)));
            if (tag.contains(LOOT_TABLE_SEED_TAG, Tag.TAG_LONG)) {
                this.setLootTableSeed(tag.getLong(LOOT_TABLE_SEED_TAG));
            } else {
                this.setLootTableSeed(0L);
            }

            return true;
        } else {
            return false;
        }
    }

    default boolean trySaveLootTable(CompoundTag tag) {
        ResourceLocation lootTable = this.getLootTable();
        if (lootTable == null) {
            return false;
        } else {
            tag.putString(LOOT_TABLE_TAG, lootTable.toString());
            long seed = this.getLootTableSeed();
            if (seed != 0L) tag.putLong(LOOT_TABLE_SEED_TAG, seed);

            return true;
        }
    }

    default void unpackLootTable(@Nullable Player player) {
        Level level = this.getLevel();
        BlockPos origin = this.getBlockPos();
        ResourceLocation lootTable = this.getLootTable();
        if (lootTable != null && level != null && level.getServer() != null) {
            LootTable loot = level.getServer().getLootData().getLootTable(lootTable);
            if (player instanceof ServerPlayer sp) CriteriaTriggers.GENERATE_LOOT.trigger(sp, lootTable);

            this.setLootTable(null);
            LootParams.Builder builder = new LootParams.Builder((ServerLevel) level).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(origin));
            if (player != null) {
                builder.withLuck(player.getLuck()).withParameter(LootContextParams.THIS_ENTITY, player);
            }

            loot.fill(this, builder.create(LootContextParamSets.CHEST), this.getLootTableSeed());
        }
    }
}

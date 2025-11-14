package com.blackgear.vanillabackport.common.api.block;

import com.blackgear.vanillabackport.core.util.TagUtils;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

// 1.21+ interface for container, replace ResourceKey<LootTable> with classic ResourceLocation - Echo2craft.
public interface RandomizableContainer extends Container {
    String LOOT_TABLE_TAG = "LootTable";
    String LOOT_TABLE_SEED_TAG = "LootTableSeed";

    // 1.20.1 does not feature LootTable registry, and we don't need to create one. - Echo2craft.
    /*@Nullable
    ResourceKey<LootTable> getLootTable();*/

    @Nullable
    ResourceLocation getLootTable();

    // 1.20.1 does not feature LootTable registry, and we don't need to create one. - Echo2craft.
    /*void setLootTable(@Nullable ResourceKey<LootTable> pLootTable);*/

    void setLootTable(@Nullable ResourceLocation pLootTable);

    /*default void setLootTable(ResourceKey<LootTable> pLootTable, long pSeed) {
        this.setLootTable(pLootTable);
        this.setLootTableSeed(pSeed);
    }*/

    default void setLootTable(ResourceLocation pLootTable, long pSeed) {
        this.setLootTable(pLootTable);
        this.setLootTableSeed(pSeed);
    }

    long getLootTableSeed();

    void setLootTableSeed(long pSeed);

    BlockPos getBlockPos();

    @Nullable
    Level getLevel();

    /*static void setBlockEntityLootTable(BlockGetter pLevel, RandomSource pRandom, BlockPos pPs, ResourceKey<LootTable> pLootTable) {
        if (pLevel.getBlockEntity(pPs) instanceof RandomizableContainer randomizablecontainer) {
            randomizablecontainer.setLootTable(pLootTable, pRandom.nextLong());
        }
    }*/

    static void setBlockEntityLootTable(BlockGetter pLevel, RandomSource pRandom, BlockPos pPs, ResourceLocation pLootTable) {
        if (pLevel.getBlockEntity(pPs) instanceof RandomizableContainer randomizablecontainer) {
            randomizablecontainer.setLootTable(pLootTable, pRandom.nextLong());
        }
    }

    default boolean tryLoadLootTable(CompoundTag pTag) {
        // ResourceKey<LootTable> resourcekey = TagUtils.read(pTag, "LootTable", LootTable.KEY_CODEC).orElse(null);
        ResourceLocation lootTable = ResourceLocation.tryParse(pTag.getString(LOOT_TABLE_TAG));
        this.setLootTable(lootTable);
        this.setLootTableSeed(TagUtils.getLongOr(pTag,LOOT_TABLE_SEED_TAG, 0L));
        return lootTable != null;
    }

    default boolean trySaveLootTable(CompoundTag pTag) {
        // ResourceKey<LootTable> resourcekey = this.getLootTable();
        ResourceLocation lootTable = this.getLootTable();
        if (lootTable == null) {
            return false;
        } else {
            pTag.putString(LOOT_TABLE_TAG,lootTable.toString());
            // TagUtils.store(pTag,LOOT_TABLE_TAG, LootTable.KEY_CODEC, lootTable);
            long i = this.getLootTableSeed();
            if (i != 0L) {
                pTag.putLong(LOOT_TABLE_SEED_TAG, i);
            }

            return true;
        }
    }

    default void unpackLootTable(@Nullable Player pPlayer) {
        Level level = this.getLevel();
        BlockPos blockpos = this.getBlockPos();
        // ResourceKey<LootTable> resourcekey = this.getLootTable();
        ResourceLocation lootTable = this.getLootTable();
        if (lootTable != null && level != null && level.getServer() != null) {
            LootTable loottable = level.getServer().getLootData().getLootTable(lootTable);
            if (pPlayer instanceof ServerPlayer) {
                CriteriaTriggers.GENERATE_LOOT.trigger((ServerPlayer)pPlayer, lootTable);
            }

            this.setLootTable(null);
            LootParams.Builder lootparams$builder = new LootParams.Builder((ServerLevel)level).withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockpos));
            if (pPlayer != null) {
                lootparams$builder.withLuck(pPlayer.getLuck()).withParameter(LootContextParams.THIS_ENTITY, pPlayer);
            }

            loottable.fill(this, lootparams$builder.create(LootContextParamSets.CHEST), this.getLootTableSeed());
        }
    }
}

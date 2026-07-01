package com.blackgear.vanillabackport.common.api.extensions.access.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;

public interface MobBehaviorAccess {
    default void vb$finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficultyInstance, MobSpawnType reason, SpawnGroupData data, CompoundTag dataTag) { /* NO-OP */ }
    
    default void vb$tick() { /* NO-OP */ }
}
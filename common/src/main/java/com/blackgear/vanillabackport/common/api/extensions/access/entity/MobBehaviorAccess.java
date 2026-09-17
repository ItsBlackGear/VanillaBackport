package com.blackgear.vanillabackport.common.api.extensions.access.entity;

import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.Nullable;

public interface MobBehaviorAccess {
    default SpawnGroupData vb$finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficultyInstance, MobSpawnType reason, @Nullable SpawnGroupData data) {
        return data;
    }
}
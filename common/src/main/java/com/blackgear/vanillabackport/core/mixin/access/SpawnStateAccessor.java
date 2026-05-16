package com.blackgear.vanillabackport.core.mixin.access;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.chunk.ChunkAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(NaturalSpawner.SpawnState.class)
public interface SpawnStateAccessor {
    @Invoker("canSpawnForCategory")
    boolean callCanSpawnForCategory(MobCategory category, ChunkPos pos);

    @Invoker("canSpawn")
    boolean callCanSpawn(EntityType<?> type, BlockPos pos, ChunkAccess chunk);

    @Invoker("afterSpawn")
    void callAfterSpawn(Mob mob, ChunkAccess chunk);
}
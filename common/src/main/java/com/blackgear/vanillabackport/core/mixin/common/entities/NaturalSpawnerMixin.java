package com.blackgear.vanillabackport.core.mixin.common.entities;

import com.blackgear.vanillabackport.core.data.tags.ModEntityTypeTags;
import com.blackgear.vanillabackport.core.mixin.access.SpawnStateAccessor;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NaturalSpawner.class)
public abstract class NaturalSpawnerMixin {
    @Inject(
        method = "spawnForChunk",
        at = @At("RETURN")
    )
    private static void vb$spawnSulfurCubeOnPeaceful(
        ServerLevel level,
        LevelChunk chunk,
        NaturalSpawner.SpawnState spawnState,
        boolean spawnFriendlies,
        boolean spawnMonsters,
        boolean forcedDespawn,
        CallbackInfo ci
    ) {
        SpawnStateAccessor access = ((SpawnStateAccessor) spawnState);
        if (!spawnMonsters && level.getDifficulty() == Difficulty.PEACEFUL && access.callCanSpawnForCategory(MobCategory.MONSTER, chunk.getPos())) {
            NaturalSpawner.spawnCategoryForChunk(
                MobCategory.MONSTER,
                level,
                chunk,
                (type, pos, chunkAccess) -> type.is(ModEntityTypeTags.MONSTERS_THAT_SPAWN_ON_PEACEFUL) && access.callCanSpawn(type, pos, chunkAccess),
                access::callAfterSpawn
            );
        }
    }
}
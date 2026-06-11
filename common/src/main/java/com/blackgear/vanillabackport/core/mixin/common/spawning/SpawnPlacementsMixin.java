package com.blackgear.vanillabackport.core.mixin.common.spawning;

import com.blackgear.vanillabackport.core.data.tags.ModEntityTypeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SpawnPlacements.class)
public class SpawnPlacementsMixin {
//    @Inject(method = "checkSpawnRules", at = @At("HEAD"), cancellable = true)
//    private static <T extends Entity> void vb$handlePeacefulSpawns(
//        EntityType<T> type,
//        ServerLevelAccessor level,
//        MobSpawnType spawnType,
//        BlockPos pos,
//        RandomSource random,
//        CallbackInfoReturnable<Boolean> cir
//    ) {
//        if (!type.is(ModEntityTypeTags.MONSTERS_THAT_SPAWN_ON_PEACEFUL) && level.getDifficulty() == Difficulty.PEACEFUL) {
//            cir.setReturnValue(false);
//        }
//    }
}
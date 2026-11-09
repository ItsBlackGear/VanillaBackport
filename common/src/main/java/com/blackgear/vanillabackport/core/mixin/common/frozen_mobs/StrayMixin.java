package com.blackgear.vanillabackport.core.mixin.common.frozen_mobs;

import com.blackgear.vanillabackport.core.data.tags.ModBiomeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Stray;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Stray.class)
public class StrayMixin {
    @Inject(method = "checkStraySpawnRules", at = @At("HEAD"), cancellable = true)
    private static void vb$checkUndergroundSpawns(EntityType<Stray> stray, ServerLevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random, CallbackInfoReturnable<Boolean> cir) {
        if (level.getBiome(pos).is(ModBiomeTags.SPAWNS_COLD_VARIANTS_UNDERGROUND)) {
            cir.setReturnValue(Monster.checkMonsterSpawnRules(stray, level, spawnType, pos, random));
        }
    }
}
package com.blackgear.vanillabackport.core.mixin.common.entities;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity {
    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
        method = "defineSynchedData",
        at = @At("RETURN")
    )
    protected void vb$defineSynchedData(CallbackInfo ci) {

    }

    @Inject(
        method = "addAdditionalSaveData",
        at = @At("RETURN")
    )
    protected void vb$addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {

    }

    @Inject(
        method = "readAdditionalSaveData",
        at = @At("RETURN")
    )
    protected void vb$readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {

    }

    @Inject(
        method = "finalizeSpawn",
        at = @At("RETURN")
    )
    protected void vb$finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CompoundTag dataTag, CallbackInfoReturnable<SpawnGroupData> cir) {

    }

    @Inject(
        method = "tick",
        at = @At("RETURN")
    )
    protected void vb$tick(CallbackInfo ci) {

    }
}
package com.blackgear.vanillabackport.core.mixin.common.extension.entity;

import com.blackgear.vanillabackport.common.api.extensions.access.entity.EntityDataHolder;
import com.blackgear.vanillabackport.common.api.extensions.access.entity.MobBehaviorAccess;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobMixin extends LivingEntity implements EntityDataHolder, MobBehaviorAccess {
    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "canBeLeashed", at = @At("HEAD"), cancellable = true)
    private void vb$canBeLeashed(Player player, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(!(this instanceof Enemy));
    }

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    protected void vb$onAddAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        this.vb$addAdditionalSaveData(tag);
    }

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    protected void vb$onReadAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        this.vb$readAdditionalSaveData(tag);
    }

    @Inject(method = "finalizeSpawn", at = @At("HEAD"))
    protected void vb$onFinalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, SpawnGroupData spawnData, CompoundTag dataTag, CallbackInfoReturnable<SpawnGroupData> cir) {
        this.vb$finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }
}
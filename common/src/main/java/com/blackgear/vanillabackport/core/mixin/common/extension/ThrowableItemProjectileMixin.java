package com.blackgear.vanillabackport.core.mixin.common.extension;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrowableProjectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrowableItemProjectile.class)
public abstract class ThrowableItemProjectileMixin extends ThrowableProjectile {
    protected ThrowableItemProjectileMixin(EntityType<? extends ThrowableProjectile> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "defineSynchedData", at = @At("RETURN"))
    protected void vb$defineSynchedData(CallbackInfo ci) {}

    @Inject(method = "addAdditionalSaveData", at = @At("RETURN"))
    protected void vb$addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {}

    @Inject(method = "readAdditionalSaveData", at = @At("RETURN"))
    protected void vb$readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {}
}
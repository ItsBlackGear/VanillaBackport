package com.blackgear.vanillabackport.core.mixin.common.extension;

import com.blackgear.vanillabackport.common.api.extensions.access.EntityDataHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ThrowableItemProjectile.class)
public abstract class ThrowableItemProjectileMixin extends Entity implements EntityDataHolder {
    public ThrowableItemProjectileMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }
    
    @Inject(
        method = "defineSynchedData",
        at = @At("RETURN")
    )
    protected void vb$onDefineSynchedData(CallbackInfo ci) {
        this.vb$defineSynchedData();
    }
    
    @Inject(
        method = "addAdditionalSaveData",
        at = @At("RETURN")
    )
    protected void vb$onAddAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        this.vb$addAdditionalSaveData(tag);
    }
    
    @Inject(
        method = "readAdditionalSaveData",
        at = @At("RETURN")
    )
    protected void vb$onReadAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        this.vb$readAdditionalSaveData(tag);
    }
}
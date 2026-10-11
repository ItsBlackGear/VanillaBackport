package com.blackgear.vanillabackport.core.mixin.common.grace_time_weapon;

import com.blackgear.vanillabackport.common.api.extensions.entity.GraceTimeWeaponHolder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements GraceTimeWeaponHolder {
    @Shadow @Final private static Logger LOGGER;
    
    @Unique private int currentImpulseContextResetGraceTime;
    @Unique private Vec3 currentImpulseImpactPos;
    
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }
    
    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void vb$addAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        compound.putInt("current_impulse_context_reset_grace_time", this.currentImpulseContextResetGraceTime);
        
        if (this.currentImpulseImpactPos != null) {
            compound.put("current_explosion_impact_pos", Vec3.CODEC.encodeStart(NbtOps.INSTANCE, this.currentImpulseImpactPos).getOrThrow(false, IllegalStateException::new));
        }
    }
    
    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void vb$readAdditionalSaveData(CompoundTag compound, CallbackInfo ci) {
        if (compound.contains("current_explosion_impact_pos", Tag.TAG_LIST)) {
            Vec3.CODEC
                .parse(NbtOps.INSTANCE, compound.get("current_explosion_impact_pos"))
                .resultOrPartial(LOGGER::error)
                .ifPresent(position -> this.currentImpulseImpactPos = position);
        }
        
        this.currentImpulseContextResetGraceTime = compound.getInt("current_impulse_context_reset_grace_time");
    }
    
    @Override
    public void setIgnoreFallDamageFromCurrentImpulse(boolean ignoreFallDamage, Vec3 newImpulseImpactPos) {
        if (ignoreFallDamage) {
            this.applyPostImpulseGraceTime(40);
            this.currentImpulseImpactPos = newImpulseImpactPos;
        } else {
            this.currentImpulseContextResetGraceTime = 0;
        }
    }
    
    @Override
    public void applyPostImpulseGraceTime(int ticks) {
        this.currentImpulseContextResetGraceTime = Math.max(this.currentImpulseContextResetGraceTime, ticks);
    }
    
    @Override
    public boolean isIgnoringFallDamageFromCurrentImpulse() {
        return this.currentImpulseImpactPos != null;
    }
    
    @Override
    public void tryResetCurrentImpulseContext() {
        if (this.currentImpulseContextResetGraceTime == 0) {
            this.resetCurrentImpulseContext();
        }
    }
    
    @Override
    public boolean isInPostImpulseGraceTime() {
        return this.currentImpulseContextResetGraceTime > 0;
    }
    
    @Override
    public void resetCurrentImpulseContext() {
        this.currentImpulseContextResetGraceTime = 0;
        this.currentImpulseImpactPos = null;
    }
    
    @ModifyVariable(method = "causeFallDamage", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float vb$causeFallDamage(float fallDistance) {
        if (this.isIgnoringFallDamageFromCurrentImpulse()) {
            float effectiveFallDistance = (float) Math.min(fallDistance, this.currentImpulseImpactPos.y - this.getY());
            boolean hasLandedAboveCurrentImpulseImpactPosY = this.fallDistance <= 0.0;
            if (hasLandedAboveCurrentImpulseImpactPosY) {
                this.resetCurrentImpulseContext();
            } else {
                this.tryResetCurrentImpulseContext();
            }
            
            return effectiveFallDistance;
        }
        
        return fallDistance;
    }
}
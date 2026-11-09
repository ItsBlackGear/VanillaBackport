package com.blackgear.vanillabackport.core.mixin.common.mace;

import com.blackgear.vanillabackport.common.api.extensions.entity.GraceTimeWeaponHolder;
import com.blackgear.vanillabackport.common.level.entities.projectile.wind_charge.AffectedByExplosion;
import com.blackgear.vanillabackport.common.level.entities.projectile.wind_charge.SimpleExplosionDamageCalculator;
import com.blackgear.vanillabackport.common.level.entities.projectile.wind_charge.WindBurst;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.ExplosionDamageCalculator;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(Explosion.class)
public abstract class ExplosionMixin {
    @Shadow @Final public ExplosionDamageCalculator damageCalculator;
    @Shadow @Final private @Nullable Entity source;
    @Shadow @Final public Level level;
    
    @Shadow public abstract List<BlockPos> getToBlow();
    
    @ModifyExpressionValue(
        method = "explode",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Explosion;getSeenPercent(Lnet/minecraft/world/phys/Vec3;Lnet/minecraft/world/entity/Entity;)F"))
    private float vb$getSeenPercent(float original, @Local(ordinal = 0) Entity entity) {
        if (this.damageCalculator instanceof SimpleExplosionDamageCalculator calculator) {
            return original * calculator.getKnockbackMultiplier(entity);
        }
        
        return original;
    }
    
    @WrapOperation(
        method = "explode",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private boolean vb$hurtIfAllowed(Entity instance, DamageSource source, float amount, Operation<Boolean> original) {
        if (this.damageCalculator instanceof SimpleExplosionDamageCalculator calculator) {
            return calculator.shouldDamageEntity((Explosion)(Object)this, instance) && original.call(instance, source, amount);
        }
        
        return original.call(instance, source, amount);
    }
    
    @WrapOperation(
        method = "finalizeExplosion",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;playLocalSound(DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FFZ)V"))
    private void vb$muteExplosion(Level instance, double x, double y, double z, SoundEvent sound, SoundSource category, float volume, float pitch, boolean distanceDelay, Operation<Void> original) {
        if (!(this.damageCalculator instanceof SimpleExplosionDamageCalculator)) {
            original.call(instance, x, y, z, sound, category, volume, pitch, distanceDelay);
        }
    }
    
    @WrapOperation(
        method = "explode",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;setDeltaMovement(Lnet/minecraft/world/phys/Vec3;)V"))
    private void vb$onExplosionHitEntity(Entity entity, Vec3 motion, Operation<Void> original) {
        original.call(entity, motion);
        
        if (entity instanceof ServerPlayer player) {
            AffectedByExplosion exploded = AffectedByExplosion.of(player);
            exploded.setCurrentExplosionImpactPos(player.position());
            exploded.setCurrentExplosionCause(this.source);
            
            boolean windCharge = this.source != null && this.source.getType() == ModEntityTypes.WIND_CHARGE;
            boolean windBurst = this.damageCalculator instanceof SimpleExplosionDamageCalculator;
            if (windCharge || windBurst) {
                GraceTimeWeaponHolder.of(player).setIgnoreFallDamageFromCurrentImpulse(true, player.position());
            }
        }
    }
    
    @Inject(method = "finalizeExplosion", at = @At("HEAD"))
    private void vb$onExplosionHitBlock(boolean spawnParticles, CallbackInfo ci) {
        if (!this.level.isClientSide && this.damageCalculator instanceof SimpleExplosionDamageCalculator) {
            for (BlockPos pos : this.getToBlow()) {
                WindBurst.triggerBlocks(this.level, pos, this.level.getBlockState(pos));
            }
        }
    }
}
package com.blackgear.vanillabackport.core.mixin.common.ender_pearl_persistance;

import com.blackgear.vanillabackport.common.api.extensions.access.EntityRemoval;
import com.blackgear.vanillabackport.common.api.modules.ender_pearl_persistance.EnderPearlAccess;
import com.blackgear.vanillabackport.common.api.modules.ender_pearl_persistance.EnderPearlLoaderModule;
import com.blackgear.vanillabackport.common.registries.ModGameRules;
import com.blackgear.vanillabackport.core.mixin.common.access.ProjectileAccessor;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ThrownEnderpearl.class)
public abstract class ThrownEnderpearlMixin extends ThrowableItemProjectile implements EntityRemoval {
    @Unique private long vb$ticketTimer = 0L;
    @Unique private int vb$startChunkX;
    @Unique private int vb$startChunkZ;
    
    public ThrownEnderpearlMixin(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }
    
    @Override
    public void setOwner(@Nullable Entity owner) {
        this.vb$deregisterFromCurrentOwner();
        super.setOwner(owner);
        this.vb$registerToCurrentOwner();
    }
    
    @Override
    public void onRemoval(RemovalReason reason) {
        if (reason != RemovalReason.UNLOADED_WITH_PLAYER) {
            this.vb$deregisterFromCurrentOwner();
        }
    }
    
    @Override
    public @Nullable Entity changeDimension(ServerLevel destination) {
        Entity traveledEntity = super.changeDimension(destination);
        if (traveledEntity instanceof ThrownEnderpearl pearl) {
            EnderPearlLoaderModule.placeEnderPearlTicket(destination, pearl.chunkPosition());
        }
        return traveledEntity;
    }
    
    @Override
    public Entity getOwner() {
        Entity vanillaOwner = super.getOwner();
        if (vanillaOwner != null) {
            return vanillaOwner;
        }
        
        UUID ownerUUID = ((ProjectileAccessor) this).getOwnerUUID();
        if (ownerUUID != null && this.level() instanceof ServerLevel serverLevel) {
            for (ServerLevel world : serverLevel.getServer().getAllLevels()) {
                Entity crossDimensionOwner = world.getEntity(ownerUUID);
                if (crossDimensionOwner != null) {
                    return crossDimensionOwner;
                }
            }
            
            return serverLevel.getServer().getPlayerList().getPlayer(ownerUUID);
        }
        return null;
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void vb$checkForMissingPlayerAndCapture(CallbackInfo ci) {
        if (this.level() instanceof ServerLevel serverLevel) {
            UUID ownerUUID = ((ProjectileAccessor) this).getOwnerUUID();
            
            if (ownerUUID != null) {
                ServerPlayer onlinePlayer = serverLevel.getServer().getPlayerList().getPlayer(ownerUUID);
                
                if (onlinePlayer == null) {
                    this.setRemoved(RemovalReason.UNLOADED_WITH_PLAYER);
                    ci.cancel();
                    return;
                }
            }
            
            this.vb$startChunkX = SectionPos.blockToSectionCoord(this.position().x());
            this.vb$startChunkZ = SectionPos.blockToSectionCoord(this.position().z());
        }
    }
    
    @Inject(method = "tick", at = @At("TAIL"))
    private void vb$updateEnderPearlTicket(CallbackInfo ci) {
        if (this.level() instanceof ServerLevel && this.isAlive()) {
            BlockPos currentPos = BlockPos.containing(this.position());
            
            boolean hasExpired = --this.vb$ticketTimer <= 0L;
            boolean changedChunk = this.vb$startChunkX != SectionPos.blockToSectionCoord(currentPos.getX())
                || this.vb$startChunkZ != SectionPos.blockToSectionCoord(currentPos.getZ());
            
            if ((hasExpired || changedChunk) && this.getOwner() instanceof ServerPlayer player) {
                this.vb$ticketTimer = EnderPearlLoaderModule.registerAndUpdateEnderPearlTicket(player, (ThrownEnderpearl) (Object) this);
            }
        }
    }
    
    @Inject(method = "onHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"))
    private void vb$playTeleportSoundOnHurt(HitResult result, CallbackInfo ci) {
        this.playSound(SoundEvents.ENDERMAN_TELEPORT);
    }
    
    @Inject(method = "onHit", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;resetFallDistance()V", ordinal = 1))
    private void vb$playTeleportSoundOnResetFall(HitResult result, CallbackInfo ci) {
        this.playSound(SoundEvents.ENDERMAN_TELEPORT);
    }
    
    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/projectile/ThrownEnderpearl;discard()V"))
    private void vb$handleEnderPearlDiscardOnPlayerDeath(ThrownEnderpearl instance, Operation<Void> original) {
        if (this.level().getGameRules().getBoolean(ModGameRules.RULE_ENDER_PEARLS_VANISH_ON_DEATH)) {
            original.call(instance);
        }
    }
    
    @Redirect(
        method = "onHit",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerPlayer;level()Lnet/minecraft/world/level/Level;"
        )
    )
    private Level vb$bypassLevelCheck(ServerPlayer player) {
        return this.level();
    }
    
    @Redirect(
        method = "onHit",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/Entity;teleportTo(DDD)V"
        )
    )
    private void vb$redirectTeleportTo(Entity entity, double x, double y, double z) {
        if (entity instanceof ServerPlayer player && player.level() != this.level()) {
            player.teleportTo((ServerLevel) this.level(), x, y, z, player.getYRot(), player.getXRot());
        } else {
            entity.teleportTo(x, y, z);
        }
    }
    
    @Unique
    private void vb$deregisterFromCurrentOwner() {
        if (this.getOwner() instanceof ServerPlayer player) {
            ((EnderPearlAccess) player).deregisterEnderPearl((ThrownEnderpearl) (Object) this);
        }
    }
    
    @Unique
    private void vb$registerToCurrentOwner() {
        if (this.getOwner() instanceof ServerPlayer player) {
            ((EnderPearlAccess) player).registerEnderPearl((ThrownEnderpearl) (Object) this);
        }
    }
}
package com.blackgear.vanillabackport.core.mixin.common.ender_pearl_persistance;

import com.blackgear.vanillabackport.common.api.modules.ender_pearl_persistance.EnderPearlAccess;
import com.blackgear.vanillabackport.common.api.modules.ender_pearl_persistance.EnderPearlLoaderModule;
import com.blackgear.vanillabackport.core.mixin.access.ProjectileAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.portal.DimensionTransition;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(ThrownEnderpearl.class)
public abstract class ThrownEnderpearlMixin extends ThrowableItemProjectile {
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
    public @Nullable Entity changeDimension(DimensionTransition destination) {
        Entity traveledEntity = super.changeDimension(destination);
        if (traveledEntity instanceof ThrownEnderpearl pearl) {
            EnderPearlLoaderModule.placeEnderPearlTicket(destination.newLevel(), pearl.chunkPosition());
        }
        return traveledEntity;
    }
    
    @Override
    public Entity getOwner() {
       Entity vanillaOwner = super.getOwner();
       if (vanillaOwner != null) return vanillaOwner;
       
       UUID ownerUUID = ((ProjectileAccessor) this).getOwnerUUID();
       if (ownerUUID != null && this.level() instanceof ServerLevel serverLevel) {
          Entity crossDimensionOwner = serverLevel.getServer().getLevel(this.level().dimension()).getEntity(ownerUUID);
          if (crossDimensionOwner != null) return crossDimensionOwner;
          
          for (ServerLevel world : serverLevel.getServer().getAllLevels()) {
             Entity owner = world.getEntity(ownerUUID);
             if (owner != null) return owner;
          }
          return serverLevel.getServer().getPlayerList().getPlayer(ownerUUID);
       }
       return null;
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void vb$checkForMissingPlayerOnTick(CallbackInfo ci) {
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

    @Override
    public void setRemoved(RemovalReason reason) {
       super.setRemoved(reason);
       if (reason != RemovalReason.UNLOADED_WITH_PLAYER) {
          this.vb$deregisterFromCurrentOwner();
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
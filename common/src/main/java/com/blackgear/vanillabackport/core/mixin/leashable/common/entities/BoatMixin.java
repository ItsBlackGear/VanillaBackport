package com.blackgear.vanillabackport.core.mixin.leashable.common.entities;

import com.blackgear.vanillabackport.common.api.leash.InterpolationHandler;
import com.blackgear.vanillabackport.common.api.leash.Leashable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundSetEntityLinkPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.decoration.HangingEntity;
import net.minecraft.world.entity.decoration.LeashFenceKnotEntity;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.UUID;

@Mixin(Boat.class)
public abstract class BoatMixin extends Entity implements Leashable {
    @Unique private final InterpolationHandler interpolation = new InterpolationHandler(this, 3);

    @Unique @Nullable Entity leashHolder;
    @Unique private int delayedLeashHolderId;
    @Unique @Nullable CompoundTag leashInfoTag;
    @Unique private double angularMomentum;

    public BoatMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public double angularMomentum() {
        return this.angularMomentum;
    }

    @Override
    public void setAngularMomentum(double angularMomentum) {
        this.angularMomentum = angularMomentum;
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void vb$onTick(CallbackInfo ci) {
        if (!this.level().isClientSide) {
            this.vb$tickLeash();
        }
    }

    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void vb$addAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        if (this.leashHolder != null) {
            CompoundTag data = new CompoundTag();
            if (this.leashHolder instanceof LivingEntity) {
                UUID uuid = this.leashHolder.getUUID();
                data.putUUID("UUID", uuid);
            } else if (this.leashHolder instanceof HangingEntity) {
                BlockPos pos = ((HangingEntity) this.leashHolder).getPos();
                data.putInt("X", pos.getX());
                data.putInt("Y", pos.getY());
                data.putInt("Z", pos.getZ());
            }

            tag.put("Leash", data);
        } else if (this.leashInfoTag != null) {
            tag.put("Leash", this.leashInfoTag.copy());
        }
    }

    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void vb$readAdditionalSaveData(CompoundTag tag, CallbackInfo ci) {
        if (tag.contains("Leash", Tag.TAG_COMPOUND)) {
            this.leashInfoTag = tag.getCompound("Leash");
        }
    }

    @Unique
    private void vb$tickLeash() {
        if (this.leashInfoTag != null) {
            this.vb$restoreLeashFromSave();
        }

        if (this.leashHolder != null) {
            if (!this.isAlive() || !this.leashHolder.isAlive()) {
                this.dropLeash(true, true);
            }
        }

        Leashable.onTickLeash(this);
    }

    @Override
    public void dropLeash(boolean broadcast, boolean dropItem) {
        if (this.leashHolder != null) {
            this.leashHolder = null;
            this.leashInfoTag = null;
            if (!this.level().isClientSide && dropItem) {
                this.spawnAtLocation(Items.LEAD);
            }

            if (!this.level().isClientSide && broadcast && this.level() instanceof ServerLevel server) {
                server.getChunkSource().broadcast(this, new ClientboundSetEntityLinkPacket(this, null));
            }
        }
    }

    @Override
    protected Vec3 getLeashOffset() {
        return new Vec3(0.0, 0.88F * this.getBbHeight(), 0.64F * this.getBbWidth());
    }

    @Override
    public boolean isLeashed() {
        return this.leashHolder != null;
    }

    @Override
    public void remove(RemovalReason reason) {
        if (!this.level().isClientSide && reason.shouldDestroy() && this.isLeashed()) {
            this.dropLeash(true, true);
        }

        super.remove(reason);
    }

    @Override
    public @Nullable Entity getLeashHolder() {
        if (this.leashHolder == null && this.delayedLeashHolderId != 0 && this.level().isClientSide) {
            this.leashHolder = this.level().getEntity(this.delayedLeashHolderId);
        }

        return this.leashHolder;
    }

    @Override
    public void setLeashedTo(Entity entity, boolean sendAttachPacket) {
        this.leashHolder = entity;
        this.leashInfoTag = null;
        if (!this.level().isClientSide && sendAttachPacket && this.level() instanceof ServerLevel server) {
            server.getChunkSource().broadcast(this, new ClientboundSetEntityLinkPacket(this, this.leashHolder));
        }

        if (this.isPassenger()) {
            this.stopRiding();
        }
    }

    @Override
    public void setBoatDelayedLeashHolderId(int leashHolderId) {
        this.delayedLeashHolderId = leashHolderId;
        this.dropLeash(false, false);
    }

    @Unique
    private void vb$restoreLeashFromSave() {
        if (this.leashInfoTag != null && this.level() instanceof ServerLevel server) {
            if (this.leashInfoTag.hasUUID("UUID")) {
                UUID uuid = this.leashInfoTag.getUUID("UUID");
                Entity entity = server.getEntity(uuid);
                if (entity != null) {
                    this.setLeashedTo(entity, true);
                    return;
                }
            } else if (this.leashInfoTag.contains("X", 99) && this.leashInfoTag.contains("Y", 99) && this.leashInfoTag.contains("Z", 99)) {
                BlockPos pos = NbtUtils.readBlockPos(this.leashInfoTag);
                this.setLeashedTo(LeashFenceKnotEntity.getOrCreateKnot(this.level(), pos), true);
                return;
            }

            if (this.tickCount > 100) {
                this.spawnAtLocation(Items.LEAD);
                this.leashInfoTag = null;
            }
        }
    }

    @Override
    protected void removeAfterChangingDimensions() {
        super.removeAfterChangingDimensions();
        this.dropLeash(true, false);
    }

    @Inject(method = "lerpTo", at = @At("HEAD"), cancellable = true)
    private void vb$lerpTo(double x, double y, double z, float yRot, float xRot, int lerpSteps, boolean teleport, CallbackInfo ci) {
        ci.cancel();
        this.interpolation.interpolateTo(new Vec3(x, y, z), yRot, xRot);
    }

    @Inject(method = "tickLerp", at = @At("HEAD"), cancellable = true)
    private void vb$tickLerp(CallbackInfo ci) {
        ci.cancel();

        if (this.isControlledByLocalInstance()) {
            this.interpolation.cancel();
            this.syncPacketPositionCodec(this.getX(), this.getY(), this.getZ());
        }

        this.interpolation.interpolate();
    }
}
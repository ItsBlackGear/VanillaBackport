package com.blackgear.vanillabackport.core.mixin.common.entities;

import com.blackgear.vanillabackport.common.api.leash.Leashable;
import com.blackgear.vanillabackport.common.level.entities.happyghast.HappyGhast;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Stream;

@Mixin(ServerEntity.class)
public abstract class ServerEntityMixin {
    @Shadow private Vec3 ap;
    @Shadow private int yRotp;
    @Shadow private int xRotp;
    @Shadow private int tickCount;
    @Shadow private int yHeadRotp;
    @Shadow private int teleportDelay;
    @Shadow private boolean wasRiding;
    @Shadow private boolean wasOnGround;
    @Shadow @Final private Entity entity;
    @Shadow @Final private int updateInterval;
    @Shadow @Final private boolean trackDelta;
    @Shadow private List<Entity> lastPassengers;
    @Shadow @Final private VecDeltaCodec positionCodec;
    @Shadow @Final private Consumer<Packet<?>> broadcast;
    @Shadow protected abstract void sendDirtyEntityData();
    @Shadow protected abstract void broadcastAndSend(Packet<?> packet);
    @Shadow private static Stream<Entity> removedPassengers(List<Entity> initialPassengers, List<Entity> currentPassengers) { throw new AssertionError(); }

    @Inject(
        method = "sendChanges",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onSendChanges(CallbackInfo ci) {
        if (this.entity instanceof HappyGhast ghast) {
            List<Entity> passengers = this.entity.getPassengers();
            if (!passengers.equals(this.lastPassengers)) {
                this.broadcast.accept(new ClientboundSetPassengersPacket(this.entity));
                removedPassengers(passengers, this.lastPassengers).forEach(entity -> {
                    if (entity instanceof ServerPlayer player) {
                        player.connection.teleport(player.getX(), player.getY(), player.getZ(), player.getYRot(), player.getXRot());
                    }
                });

                this.lastPassengers = passengers;
            }

            if (this.tickCount % this.updateInterval == 0 || this.entity.hasImpulse || this.entity.getEntityData().isDirty()) {
                if (!this.entity.isPassenger()) {
                    this.teleportDelay++;
                    int yRot = Mth.floor(this.entity.getYRot() * 256.0F / 360.0F);
                    int xRot = Mth.floor(this.entity.getXRot() * 256.0F / 360.0F);
                    Vec3 position = this.entity.trackingPosition();
                    boolean positionChanged = this.positionCodec.delta(position).lengthSqr() >= 7.6293945E-6F;

                    boolean shouldUpdatePosition = positionChanged || this.tickCount % 60 == 0;
                    boolean shouldUpdateRotation = Math.abs(yRot - this.yRotp) >= 1 || Math.abs(xRot - this.xRotp) >= 1;

                    Packet<?> packet = null;
                    boolean positionUpdated = false;
                    boolean rotationUpdated = false;

                    if (this.tickCount > 0 || this.entity instanceof AbstractArrow) {
                        long x = this.positionCodec.encodeX(position);
                        long y = this.positionCodec.encodeY(position);
                        long z = this.positionCodec.encodeZ(position);
                        boolean isOutOfRange = x < -32768L || x > 32767L || y < -32768L || y > 32767L || z < -32768L || z > 32767L;
                        if (ghast.getRequiresPrecisePosition() || isOutOfRange || this.teleportDelay > 400 || this.wasRiding || this.wasOnGround != this.entity.onGround()) {
                            this.wasOnGround = this.entity.onGround();
                            this.teleportDelay = 0;
                            packet = new ClientboundTeleportEntityPacket(this.entity);
                            positionUpdated = true;
                            rotationUpdated = true;
                        } else if ((!shouldUpdatePosition || !shouldUpdateRotation) && !(this.entity instanceof AbstractArrow)) {
                            if (shouldUpdatePosition) {
                                packet = new ClientboundMoveEntityPacket.Pos(this.entity.getId(), (short) ((int) x), (short) ((int) y), (short) ((int) z), this.entity.onGround());
                                positionUpdated = true;
                            } else if (shouldUpdateRotation) {
                                packet = new ClientboundMoveEntityPacket.Rot(this.entity.getId(), (byte) yRot, (byte) xRot, this.entity.onGround());
                                rotationUpdated = true;
                            }
                        } else {
                            packet = new ClientboundMoveEntityPacket.PosRot(this.entity.getId(), (short) ((int) x), (short) ((int) y), (short) ((int) z), (byte) yRot, (byte) xRot, this.entity.onGround());
                            positionUpdated = true;
                            rotationUpdated = true;
                            ghast.setRequiresPrecisePosition(false);
                        }
                    }

                    if ((this.trackDelta || this.entity.hasImpulse || this.entity instanceof LivingEntity && ((LivingEntity)this.entity).isFallFlying()) && this.tickCount > 0) {
                        Vec3 movement = this.entity.getDeltaMovement();
                        double distance = movement.distanceToSqr(this.ap);

                        if (distance > 1.0E-7 || distance > 0.0 && movement.lengthSqr() == 0.0) {
                            this.ap = movement;
                            this.broadcast.accept(new ClientboundSetEntityMotionPacket(this.entity.getId(), this.ap));
                        }
                    }

                    if (packet != null) {
                        this.broadcast.accept(packet);
                    }

                    this.sendDirtyEntityData();
                    if (positionUpdated) {
                        this.positionCodec.setBase(position);
                    }

                    if (rotationUpdated) {
                        this.yRotp = yRot;
                        this.xRotp = xRot;
                    }

                    this.wasRiding = false;
                }

                int headYaw = Mth.floor(this.entity.getYHeadRot() * 256.0F / 360.0F);
                if (Math.abs(headYaw - this.yHeadRotp) >= 1) {
                    this.broadcast.accept(new ClientboundRotateHeadPacket(this.entity, (byte) headYaw));
                    this.yHeadRotp = headYaw;
                }

                this.entity.hasImpulse = false;
            }

            this.tickCount++;
            if (this.entity.hurtMarked) {
                this.entity.hurtMarked = false;
                this.broadcastAndSend(new ClientboundSetEntityMotionPacket(this.entity));
            }

            ci.cancel();
        }
    }

    @Inject(method = "sendPairingData", at = @At("TAIL"))
    private void onSendPairingData(ServerPlayer player, Consumer<Packet<ClientGamePacketListener>> consumer, CallbackInfo ci) {
        if (this.entity instanceof Boat boat && boat instanceof Leashable leashable && leashable.isLeashed()) {
            consumer.accept(new ClientboundSetEntityLinkPacket(boat, leashable.getLeashHolder()));
        }
    }
}
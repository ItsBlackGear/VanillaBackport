package com.blackgear.vanillabackport.core.mixin.common.happy_ghast;

import com.blackgear.vanillabackport.common.api.extensions.entity.movement.PositionAwareEntity;
import com.blackgear.vanillabackport.common.api.modules.leash_behavior.Leashable;
import com.blackgear.vanillabackport.core.util.Utilities.*;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
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
        if (this.entity instanceof PositionAwareEntity mob) {
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
                byte yRot = MthUtils.packDegrees(this.entity.getYRot());
                byte xRot = MthUtils.packDegrees(this.entity.getXRot());
                boolean shouldSendRotation = Math.abs(yRot - this.yRotp) >= 1 || Math.abs(xRot - this.xRotp) >= 1;
                if (this.entity.isPassenger()) {
                    if (shouldSendRotation) {
                        this.broadcastAndSend(new ClientboundMoveEntityPacket.Rot(this.entity.getId(), yRot, xRot, this.entity.onGround()));
                        this.yRotp = yRot;
                        this.xRotp = xRot;
                    }

                    this.positionCodec.setBase(this.entity.trackingPosition());
                    this.sendDirtyEntityData();
                    this.wasRiding = true;
                } else {
                    this.teleportDelay++;
                    Vec3 currentPosition = this.entity.trackingPosition();
                    boolean positionChanged = this.positionCodec.delta(currentPosition).lengthSqr() >= 7.6293945E-6F;
                    Packet<ClientGamePacketListener> packet = null;
                    boolean pos = positionChanged || this.tickCount % 60 == 0;
                    boolean sendPosition = false;
                    boolean sendRotation = false;
                    long x = this.positionCodec.encodeX(currentPosition);
                    long y = this.positionCodec.encodeY(currentPosition);
                    long z = this.positionCodec.encodeZ(currentPosition);
                    boolean deltaTooBig = x < -32768L || x > 32767L || y < -32768L || y > 32767L || z < -32768L || z > 32767L;
                    if (mob.getRequiresPrecisePosition() || deltaTooBig || this.teleportDelay > 400 || this.wasRiding || this.wasOnGround != this.entity.onGround()) {
                        this.wasOnGround = this.entity.onGround();
                        this.teleportDelay = 0;
                        packet = new ClientboundTeleportEntityPacket(this.entity);
                        sendPosition = true;
                        sendRotation = true;
                    } else if ((!pos || !shouldSendRotation) && !(this.entity instanceof AbstractArrow)) {
                        if (pos) {
                            packet = new ClientboundMoveEntityPacket.Pos(this.entity.getId(), (short) x, (short) y, (short) z, this.entity.onGround());
                            sendPosition = true;
                        } else if (shouldSendRotation) {
                            packet = new ClientboundMoveEntityPacket.Rot(this.entity.getId(), yRot, xRot, this.entity.onGround());
                            sendRotation = true;
                        }
                    } else {
                        packet = new ClientboundMoveEntityPacket.PosRot(this.entity.getId(), (short) x, (short) y, (short) z, yRot, xRot, this.entity.onGround());
                        sendPosition = true;
                        sendRotation = true;
                    }

                    if (this.entity.hasImpulse || this.trackDelta || this.entity instanceof LivingEntity living && living.isFallFlying()) {
                        Vec3 movement = this.entity.getDeltaMovement();
                        double diff = movement.distanceToSqr(this.ap);

                        if (diff > 1.0E-7 || diff > 0.0 && movement.lengthSqr() == 0.0) {
                            this.ap = movement;
                            this.broadcast.accept(new ClientboundSetEntityMotionPacket(this.entity.getId(), this.ap));
                        }
                    }

                    if (packet != null) this.broadcast.accept(packet);

                    this.sendDirtyEntityData();
                    if (sendPosition) this.positionCodec.setBase(currentPosition);

                    if (sendRotation) {
                        this.yRotp = yRot;
                        this.xRotp = xRot;
                    }

                    this.wasRiding = false;
                }

                byte yHeadRot = MthUtils.packDegrees(this.entity.getYHeadRot());
                if (Math.abs(yHeadRot - this.yHeadRotp) >= 1) {
                    this.broadcast.accept(new ClientboundRotateHeadPacket(this.entity, yHeadRot));
                    this.yHeadRotp = yHeadRot;
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
        if (this.entity instanceof Boat boat && boat instanceof Leashable leashable && leashable.vb$isLeashed()) {
            consumer.accept(new ClientboundSetEntityLinkPacket(boat, leashable.vb$getLeashHolder()));
        }
    }
}
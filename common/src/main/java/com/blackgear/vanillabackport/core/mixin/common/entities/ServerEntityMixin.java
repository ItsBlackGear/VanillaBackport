package com.blackgear.vanillabackport.core.mixin.common.entities;

import com.blackgear.vanillabackport.common.level.entities.happyghast.HappyGhast;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerEntity;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
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
    @Shadow private int tickCount;
    @Shadow private int lastSentXRot;
    @Shadow private int lastSentYRot;
    @Shadow private int teleportDelay;
    @Shadow private boolean wasRiding;
    @Shadow private boolean wasOnGround;
    @Shadow @Final private Entity entity;
    @Shadow private int lastSentYHeadRot;
    @Shadow private Vec3 lastSentMovement;
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
                byte yRot = (byte) Mth.floor(this.entity.getYRot() * 256.0F / 360.0F);
                byte xRot = (byte) Mth.floor(this.entity.getXRot() * 256.0F / 360.0F);
                boolean shouldSendRotation = Math.abs(yRot - this.lastSentYRot) >= 1 || Math.abs(xRot - this.lastSentXRot) >= 1;

                if (!this.entity.isPassenger()) {
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

                    if (ghast.getRequiresPrecisePosition() || deltaTooBig || this.teleportDelay > 400 || this.wasRiding || this.wasOnGround != this.entity.onGround()) {
                        this.wasOnGround = this.entity.onGround();
                        this.teleportDelay = 0;
                        packet = new ClientboundTeleportEntityPacket(this.entity);
                        sendPosition = true;
                        sendRotation = true;
                    } else if (!pos || !shouldSendRotation) {
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

                    if (this.trackDelta || this.entity.hasImpulse || this.entity instanceof LivingEntity living && living.isFallFlying()) {
                        Vec3 movement = this.entity.getDeltaMovement();
                        double diff = movement.distanceToSqr(this.lastSentMovement);
                        if (diff > 1.0E-7 || diff > 0.0 && movement.lengthSqr() == 0.0) {
                            this.lastSentMovement = movement;
                            this.broadcast.accept(new ClientboundSetEntityMotionPacket(this.entity.getId(), this.lastSentMovement));
                        }
                    }

                    if (packet != null) this.broadcast.accept(packet);

                    this.sendDirtyEntityData();
                    if (sendPosition) this.positionCodec.setBase(currentPosition);

                    if (sendRotation) {
                        this.lastSentYRot = yRot;
                        this.lastSentXRot = xRot;
                    }

                    this.wasRiding = false;
                }

                byte yHeadRot = (byte) Mth.floor(this.entity.getYHeadRot() * 256.0F / 360.0F);
                if (Math.abs(yHeadRot - this.lastSentYHeadRot) >= 1) {
                    this.broadcast.accept(new ClientboundRotateHeadPacket(this.entity, yHeadRot));
                    this.lastSentYHeadRot = yHeadRot;
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
}
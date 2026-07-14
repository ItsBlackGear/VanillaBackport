package com.blackgear.vanillabackport.core.mixin.common.entity_movement;

import com.blackgear.vanillabackport.common.api.extensions.entity.MotionAwareEntity;
import com.blackgear.vanillabackport.core.network.ServerboundClientTickEndPacket;
import net.minecraft.network.protocol.game.ServerboundMovePlayerPacket;
import net.minecraft.network.protocol.game.ServerboundMoveVehiclePacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public abstract class ServerGamePacketListenerImplMixin {
    @Shadow public ServerPlayer player;
    @Shadow @Nullable private Entity lastVehicle;
    
    @Unique private double vb$playerStartX;
    @Unique private double vb$playerStartY;
    @Unique private double vb$playerStartZ;
    
    @Unique private double vb$vehicleStartX;
    @Unique private double vb$vehicleStartY;
    @Unique private double vb$vehicleStartZ;
    
    @Inject(method = "handleMovePlayer", at = @At("HEAD"))
    private void vb$capturePlayerStartPos(ServerboundMovePlayerPacket packet, CallbackInfo ci) {
        if (this.player != null) {
            this.vb$playerStartX = this.player.getX();
            this.vb$playerStartY = this.player.getY();
            this.vb$playerStartZ = this.player.getZ();
        }
    }
    
    @Inject(
        method = "handleMovePlayer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerPlayer;setOnGroundWithKnownMovement(ZLnet/minecraft/world/phys/Vec3;)V"
        )
    )
    private void vb$handleMovePlayer(ServerboundMovePlayerPacket packet, CallbackInfo ci) {
        Vec3 clientDeltaMovement = new Vec3(
            this.player.getX() - this.vb$playerStartX,
            this.player.getY() - this.vb$playerStartY,
            this.player.getZ() - this.vb$playerStartZ
        );
        this.handlePlayerKnownMovement(clientDeltaMovement);
    }
    
    @Inject(method = "handleMoveVehicle", at = @At("HEAD"))
    private void vb$captureVehicleStartPos(ServerboundMoveVehiclePacket packet, CallbackInfo ci) {
        if (this.player != null) {
            Entity vehicle = this.player.getRootVehicle();
            if (vehicle != null) {
                this.vb$vehicleStartX = vehicle.getX();
                this.vb$vehicleStartY = vehicle.getY();
                this.vb$vehicleStartZ = vehicle.getZ();
            }
        }
    }
    
    @Inject(
        method = "handleMoveVehicle",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerPlayer;checkMovementStatistics(DDD)V"
        )
    )
    private void vb$handleMoveVehicle(ServerboundMoveVehiclePacket packet, CallbackInfo ci) {
        Entity vehicle = this.player.getRootVehicle();
        if (vehicle != this.player && vehicle.getControllingPassenger() == this.player && vehicle == this.lastVehicle) {
            Vec3 clientDeltaMovement = new Vec3(
                vehicle.getX() - this.vb$vehicleStartX,
                vehicle.getY() - this.vb$vehicleStartY,
                vehicle.getZ() - this.vb$vehicleStartZ
            );
            this.handlePlayerKnownMovement(clientDeltaMovement);
        }
    }
    
    @Unique
    private void handlePlayerKnownMovement(Vec3 movement) {
        if (movement.lengthSqr() > Mth.EPSILON) {
            this.player.resetLastActionTime();
        }
        
        ((MotionAwareEntity) this.player).setKnownMovement(movement);
        ServerboundClientTickEndPacket.HANDLER.receivedMovementThisTick = true;
    }
}
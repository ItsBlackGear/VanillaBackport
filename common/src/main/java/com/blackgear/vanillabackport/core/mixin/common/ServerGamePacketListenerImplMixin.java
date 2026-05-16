package com.blackgear.vanillabackport.core.mixin.common;

import com.blackgear.vanillabackport.common.api.extensions.MotionAwareEntity;
import com.blackgear.vanillabackport.common.level.entities.happyghast.HappyGhast;
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
    @Shadow private int aboveGroundVehicleTickCount;
    @Shadow @Nullable private Entity lastVehicle;

    @Inject(method = "tick", at = @At("TAIL"))
    private void vb$preventFlyingKick(CallbackInfo ci) {
        Entity vehicle = this.player.getVehicle();
        if (vehicle instanceof HappyGhast ghast && !ghast.isBaby()) {
            this.aboveGroundVehicleTickCount = 0;
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
            double oldX = vehicle.getX();
            double oldY = vehicle.getY();
            double oldZ = vehicle.getZ();
            Vec3 clientDeltaMovement = new Vec3(vehicle.getX() - oldX, vehicle.getY() - oldY, vehicle.getZ() - oldZ);
            this.handlePlayerKnownMovement(clientDeltaMovement);
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
        double startX = this.player.getX();
        double startY = this.player.getY();
        double startZ = this.player.getZ();
        Vec3 clientDeltaMovement = new Vec3(this.player.getX() - startX, this.player.getY() - startY, this.player.getZ() - startZ);
        this.handlePlayerKnownMovement(clientDeltaMovement);
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
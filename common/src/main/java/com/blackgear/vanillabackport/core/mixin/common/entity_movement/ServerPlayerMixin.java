package com.blackgear.vanillabackport.core.mixin.common.entity_movement;

import com.blackgear.vanillabackport.common.api.extensions.entity.MotionAwareEntity;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player implements MotionAwareEntity {
    @Unique private Vec3 lastKnownClientMovement = Vec3.ZERO;

    public ServerPlayerMixin(Level level, BlockPos pos, float yRot, GameProfile gameProfile) {
        super(level, pos, yRot, gameProfile);
    }

    @Override
    public Vec3 getKnownMovement() {
        Entity vehicle = this.getVehicle();
        return vehicle != null && vehicle.getControllingPassenger() != this ? ((MotionAwareEntity) vehicle).getKnownMovement() : this.lastKnownClientMovement;
    }

    @Override
    public Vec3 getKnownSpeed() {
        Entity vehicle = this.getVehicle();
        return vehicle != null && vehicle.getControllingPassenger() != this ? ((MotionAwareEntity) vehicle).getKnownSpeed() : this.lastKnownClientMovement;
    }

    @Override
    public void setKnownMovement(Vec3 lastKnownMovement) {
        this.lastKnownClientMovement = lastKnownMovement;
    }
}
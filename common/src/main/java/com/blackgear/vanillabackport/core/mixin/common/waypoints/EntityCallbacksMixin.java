package com.blackgear.vanillabackport.core.mixin.common.waypoints;

import com.blackgear.vanillabackport.common.api.modules.waypoints.ServerWaypointManager;
import com.blackgear.vanillabackport.common.api.modules.waypoints.WaypointTransmitter;
import com.blackgear.vanillabackport.common.api.modules.waypoints.WaypointUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerLevel.EntityCallbacks.class)
public class EntityCallbacksMixin {
    @Inject(method = "onCreated(Lnet/minecraft/world/entity/Entity;)V", at = @At("HEAD"))
    private void vb$onCreated(Entity entity, CallbackInfo ci) {
        if (entity instanceof WaypointTransmitter waypoint && waypoint.isTransmittingWaypoint()) {
            ServerWaypointManager.get((ServerLevel) entity.level()).trackWaypoint(waypoint);
        }
    }
    
    @Inject(method = "onDestroyed(Lnet/minecraft/world/entity/Entity;)V", at = @At("HEAD"))
    private void vb$onDestroyed(Entity entity, CallbackInfo ci) {
        if (entity instanceof WaypointTransmitter waypoint) {
            ServerWaypointManager.get((ServerLevel) entity.level()).untrackWaypoint(waypoint);
        }
    }
    
    @Inject(method = "onTrackingStart(Lnet/minecraft/world/entity/Entity;)V", at = @At("HEAD"))
    private void vb$onTrackingStart(Entity entity, CallbackInfo ci) {
        if (entity instanceof ServerPlayer player && WaypointUtils.isReceivingWaypoints(player)) {
            ServerWaypointManager.get((ServerLevel) entity.level()).addPlayer(player);
        }
        
        if (entity instanceof WaypointTransmitter waypoint && waypoint.isTransmittingWaypoint()) {
            ServerWaypointManager.get((ServerLevel) entity.level()).trackWaypoint(waypoint);
        }
    }
    
    @Inject(method = "onTrackingEnd(Lnet/minecraft/world/entity/Entity;)V", at = @At("HEAD"))
    private void vb$onTrackingEnd(Entity entity, CallbackInfo ci) {
        if (entity instanceof ServerPlayer player) {
            ServerWaypointManager.get((ServerLevel) entity.level()).removePlayer(player);
        }
    }
}
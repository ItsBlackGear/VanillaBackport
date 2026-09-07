package com.blackgear.vanillabackport.core.mixin.common.waypoints;

import com.blackgear.vanillabackport.common.api.modules.waypoints.ServerWaypointManager;
import com.blackgear.vanillabackport.common.api.modules.waypoints.WaypointTransmitter;
import com.blackgear.vanillabackport.common.api.modules.waypoints.WaypointUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow private Vec3 position;
    @Shadow protected boolean firstTick;
    
    @Inject(method = "removeAfterChangingDimensions", at = @At("TAIL"))
    private void vb$removeAfterChangingDimensions(CallbackInfo ci) {
        Entity self = (Entity)(Object) this;
        if (self instanceof WaypointTransmitter waypoint && self.level() instanceof ServerLevel level) {
            ServerWaypointManager.get(level).untrackWaypoint(waypoint);
        }
    }
    
    @Inject(method = "setPosRaw", at = @At("TAIL"))
    private void vb$setPosRaw(double x, double y, double z, CallbackInfo ci) {
        if (this.position.x != x || this.position.y != y || this.position.z != z) {
            Entity self = (Entity)(Object) this;
            if (!this.firstTick && self.level() instanceof ServerLevel level && !self.isRemoved()) {
                if (self instanceof WaypointTransmitter waypoint && waypoint.isTransmittingWaypoint()) {
                    ServerWaypointManager.get(level).updateWaypoint(waypoint);
                }
                
                if (self instanceof ServerPlayer player && WaypointUtils.isReceivingWaypoints(player) && player.connection != null) {
                    ServerWaypointManager.get(level).updatePlayer(player);
                }
            }
        }
    }
}
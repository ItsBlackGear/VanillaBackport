package com.blackgear.vanillabackport.core.mixin.common.waypoints;

import com.blackgear.vanillabackport.common.api.modules.waypoints.ServerWaypointManager;
import com.blackgear.vanillabackport.common.api.modules.waypoints.WaypointUtils;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerChunkCache.class)
public class ServerChunkCacheMixin {
    @Shadow @Final ServerLevel level;
    
    @Inject(method = "move", at = @At("TAIL"))
    private void vb$move(ServerPlayer player, CallbackInfo ci) {
        if (!player.isRemoved() && WaypointUtils.isReceivingWaypoints(player)) {
            ServerWaypointManager.get(this.level).updatePlayer(player);
        }
    }
}
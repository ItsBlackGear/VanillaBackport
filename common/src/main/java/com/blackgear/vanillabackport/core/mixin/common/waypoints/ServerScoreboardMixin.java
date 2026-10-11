package com.blackgear.vanillabackport.core.mixin.common.waypoints;

import com.blackgear.vanillabackport.common.api.modules.waypoints.ServerWaypointManager;
import com.blackgear.vanillabackport.common.api.modules.waypoints.WaypointTransmitter;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerScoreboard;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.scores.PlayerTeam;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Objects;

@Mixin(ServerScoreboard.class)
public class ServerScoreboardMixin {
    @Shadow @Final private MinecraftServer server;
    
    @Inject(method = "addPlayerToTeam", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/ServerScoreboard;setDirty()V", shift = At.Shift.BEFORE))
    private void vb$addPlayerToTeam(String username, PlayerTeam team, CallbackInfoReturnable<Boolean> cir) {
        this.vb$updatePlayerWaypoint(username);
    }
    
    @Inject(method = "removePlayerFromTeam", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/ServerScoreboard;setDirty()V", shift = At.Shift.BEFORE))
    private void vb$removePlayerFromTeam(String username, PlayerTeam team, CallbackInfo ci) {
        this.vb$updatePlayerWaypoint(username);
    }
    
    @Inject(method = "onTeamChanged", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/ServerScoreboard;setDirty()V", shift = At.Shift.BEFORE))
    private void vb$onTeamChanged(PlayerTeam team, CallbackInfo ci) {
        this.vb$updateTeamWaypoints(team);
    }
    
    @Inject(method = "onTeamRemoved", at = @At(value = "INVOKE", target = "Lnet/minecraft/server/ServerScoreboard;setDirty()V", shift = At.Shift.BEFORE))
    private void vb$onTeamRemoved(PlayerTeam team, CallbackInfo ci) {
        this.vb$updateTeamWaypoints(team);
    }
    
    @Unique
    private void vb$updatePlayerWaypoint(String username) {
        ServerPlayer player = this.server.getPlayerList().getPlayerByName(username);
        if (player != null) {
            ServerWaypointManager.get((ServerLevel) player.level()).remakeConnections(WaypointTransmitter.of(player));
        }
    }
    
    @Unique
    private void vb$updateTeamWaypoints(PlayerTeam team) {
        this.server.getAllLevels().forEach(level ->
            team.getPlayers().stream()
                .map(username -> this.server.getPlayerList().getPlayerByName(username))
                .filter(Objects::nonNull)
                .forEach(player -> ServerWaypointManager.get(level).remakeConnections(WaypointTransmitter.of(player)))
        );
    }
}
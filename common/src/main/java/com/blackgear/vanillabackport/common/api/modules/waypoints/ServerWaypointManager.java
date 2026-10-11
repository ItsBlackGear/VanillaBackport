package com.blackgear.vanillabackport.common.api.modules.waypoints;

import com.blackgear.platform.common.v2.entity.EntityAttributeEvents;
import com.blackgear.vanillabackport.common.api.modules.waypoints.WaypointTransmitter.Connection;
import com.blackgear.vanillabackport.common.registries.ModGameRules;
import com.google.common.collect.*;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ServerWaypointManager implements WaypointManager<WaypointTransmitter> {
    private static final Map<ResourceKey<Level>, ServerWaypointManager> MANAGERS = new ConcurrentHashMap<>();
    private final Set<WaypointTransmitter> waypoints = new HashSet<>();
    private final Set<ServerPlayer> players = new HashSet<>();
    private final Table<ServerPlayer, WaypointTransmitter, Connection> connections = HashBasedTable.create();
    
    public static ServerWaypointManager get(ServerLevel level) {
        return MANAGERS.computeIfAbsent(level.dimension(), key -> new ServerWaypointManager());
    }
    
    public static void bootstrap() {
        EntityAttributeEvents.ON_LIVING_ATTRIBUTE_UPDATE.register(WaypointUtils::onEntityAttributeUpdate);
        EntityAttributeEvents.UPDATE_PLAYER_ATTRIBUTE.register(WaypointUtils::onPlayerAttributeUpdate);
    }
    
    public static void clearAll() {
        MANAGERS.values().forEach(ServerWaypointManager::breakAllConnections);
        MANAGERS.clear();
    }
    
    @Override
    public void trackWaypoint(WaypointTransmitter waypoint) {
        this.waypoints.add(waypoint);
        
        for (ServerPlayer player : this.players) {
            this.createConnection(player, waypoint);
        }
    }
    
    @Override
    public void updateWaypoint(WaypointTransmitter waypoint) {
        if (this.waypoints.contains(waypoint)) {
            Map<ServerPlayer, Connection> playerConnection = Tables.transpose(this.connections).row(waypoint);
            Sets.SetView<ServerPlayer> potentialPlayers = Sets.difference(this.players, playerConnection.keySet());
            
            for (var waypointConnection : ImmutableSet.copyOf(playerConnection.entrySet())) {
                this.updateConnection(waypointConnection.getKey(), waypoint, waypointConnection.getValue());
            }
            
            for (ServerPlayer player : potentialPlayers) {
                this.createConnection(player, waypoint);
            }
        }
    }
    
    @Override
    public void untrackWaypoint(WaypointTransmitter waypoint) {
        this.connections.column(waypoint).forEach((player, connection) -> connection.disconnect());
        Tables.transpose(this.connections).row(waypoint).clear();
        this.waypoints.remove(waypoint);
    }
    
    public void addPlayer(ServerPlayer player) {
        this.players.add(player);
        
        for (WaypointTransmitter waypoint : this.waypoints) {
            this.createConnection(player, waypoint);
        }
        
        if (WaypointTransmitter.of(player).isTransmittingWaypoint()) {
            this.trackWaypoint(WaypointTransmitter.of(player));
        }
    }
    
    public void updatePlayer(ServerPlayer player) {
        Map<WaypointTransmitter, Connection> waypointConnections = this.connections.row(player);
        Sets.SetView<WaypointTransmitter> potentialWaypoints = Sets.difference(this.waypoints, waypointConnections.keySet());
        
        for (var waypointConnection : ImmutableSet.copyOf(waypointConnections.entrySet())) {
            this.updateConnection(player, waypointConnection.getKey(), waypointConnection.getValue());
        }
        
        for (WaypointTransmitter waypoint : potentialWaypoints) {
            this.createConnection(player, waypoint);
        }
    }
    
    public void removePlayer(ServerPlayer player) {
        this.connections.row(player).values().removeIf(connection -> {
            connection.disconnect();
            return true;
        });
        this.untrackWaypoint(WaypointTransmitter.of(player));
        this.players.remove(player);
    }
    
    public void breakAllConnections() {
        this.connections.values().forEach(Connection::disconnect);
        this.connections.clear();
    }
    
    public void remakeConnections(WaypointTransmitter waypoint) {
        for (ServerPlayer player : this.players) {
            this.createConnection(player, waypoint);
        }
    }
    
    public Set<WaypointTransmitter> transmitters() {
        return this.waypoints;
    }
    
    private static boolean isLocatorBarEnabledFor(ServerPlayer player) {
        return player.level().getServer().getGameRules().getBoolean(ModGameRules.RULE_LOCATOR_BAR);
    }
    
    private void createConnection(ServerPlayer player, WaypointTransmitter waypoint) {
        if (WaypointTransmitter.of(player) != waypoint) {
            if (isLocatorBarEnabledFor(player)) {
                waypoint.makeWaypointConnectionWith(player).ifPresentOrElse(connection -> {
                    this.connections.put(player, waypoint, connection);
                    connection.connect();
                }, () -> {
                    Connection connection = this.connections.remove(player, waypoint);
                    if (connection != null) connection.disconnect();
                });
            }
        }
    }
    
    private void updateConnection(ServerPlayer player, WaypointTransmitter waypoint, Connection connection) {
        if (WaypointTransmitter.of(player) != waypoint) {
            if (isLocatorBarEnabledFor(player)) {
                if (!connection.isBroken()) {
                    connection.update();
                } else {
                    waypoint.makeWaypointConnectionWith(player).ifPresentOrElse(newConnection -> {
                        newConnection.connect();
                        this.connections.put(player, waypoint, newConnection);
                    }, () -> {
                        connection.disconnect();
                        this.connections.remove(player, waypoint);
                    });
                }
            }
        }
    }
}
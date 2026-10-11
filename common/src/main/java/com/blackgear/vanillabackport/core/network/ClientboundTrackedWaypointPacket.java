package com.blackgear.vanillabackport.core.network;

import com.blackgear.platform.core.network.base.Packet;
import com.blackgear.platform.core.network.base.PacketContext;
import com.blackgear.platform.core.network.base.PacketHandler;
import com.blackgear.vanillabackport.client.api.modules.waypoints.ClientWaypointManager;
import com.blackgear.vanillabackport.common.api.modules.waypoints.TrackedWaypoint;
import com.blackgear.vanillabackport.common.api.modules.waypoints.TrackedWaypointManager;
import com.blackgear.vanillabackport.common.api.modules.waypoints.Waypoint;
import com.blackgear.vanillabackport.common.api.modules.waypoints.WaypointManager;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.Vec3i;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;

import java.util.UUID;
import java.util.function.BiConsumer;

public record ClientboundTrackedWaypointPacket(Operation operation, TrackedWaypoint waypoint) implements Packet<ClientboundTrackedWaypointPacket> {
    public static final ResourceLocation ID = VanillaBackport.resource("tracked_waypoint");
    public static final ClientboundTrackedWaypointHandler HANDLER = new ClientboundTrackedWaypointHandler();
    
    public static ClientboundTrackedWaypointPacket removeWaypoint(UUID identifier) {
        return new ClientboundTrackedWaypointPacket(Operation.UNTRACK, TrackedWaypoint.empty(identifier));
    }
    
    public static ClientboundTrackedWaypointPacket addWaypointPosition(UUID identifier, Waypoint.Icon icon, Vec3i position) {
        return new ClientboundTrackedWaypointPacket(Operation.TRACK, TrackedWaypoint.setPosition(identifier, icon, position));
    }
    
    public static ClientboundTrackedWaypointPacket updateWaypointPosition(UUID identifier, Waypoint.Icon icon, Vec3i position) {
        return new ClientboundTrackedWaypointPacket(Operation.UPDATE, TrackedWaypoint.setPosition(identifier, icon, position));
    }
    
    public static ClientboundTrackedWaypointPacket addWaypointChunk(UUID identifier, Waypoint.Icon icon, ChunkPos chunk) {
        return new ClientboundTrackedWaypointPacket(Operation.TRACK, TrackedWaypoint.setChunk(identifier, icon, chunk));
    }
    
    public static ClientboundTrackedWaypointPacket updateWaypointChunk(UUID identifier, Waypoint.Icon icon, ChunkPos chunk) {
        return new ClientboundTrackedWaypointPacket(Operation.UPDATE, TrackedWaypoint.setChunk(identifier, icon, chunk));
    }
    
    public static ClientboundTrackedWaypointPacket addWaypointAzimuth(UUID identifier, Waypoint.Icon icon, float angle) {
        return new ClientboundTrackedWaypointPacket(Operation.TRACK, TrackedWaypoint.setAzimuth(identifier, icon, angle));
    }
    
    public static ClientboundTrackedWaypointPacket updateWaypointAzimuth(UUID identifier, Waypoint.Icon icon, float angle) {
        return new ClientboundTrackedWaypointPacket(Operation.UPDATE, TrackedWaypoint.setAzimuth(identifier, icon, angle));
    }
    
    @Override
    public ResourceLocation getId() {
        return ID;
    }
    
    @Override
    public PacketHandler<ClientboundTrackedWaypointPacket> getHandler() {
        return HANDLER;
    }
    
    public static class ClientboundTrackedWaypointHandler implements PacketHandler<ClientboundTrackedWaypointPacket> {
        @Override
        public void encode(ClientboundTrackedWaypointPacket packet, FriendlyByteBuf buf) {
            buf.writeEnum(packet.operation);
            packet.waypoint.write(buf);
        }
        
        @Override
        public ClientboundTrackedWaypointPacket decode(FriendlyByteBuf buf) {
            return new ClientboundTrackedWaypointPacket(buf.readEnum(Operation.class), TrackedWaypoint.read(buf));
        }
        
        @Override
        public PacketContext handle(ClientboundTrackedWaypointPacket packet) {
            return (player, level) -> packet.apply(ClientWaypointManager.INSTANCE);
        }
    }
    
    public void apply(TrackedWaypointManager manager) {
        this.operation.action.accept(manager, this.waypoint);
    }
    
    private enum Operation {
        TRACK(WaypointManager::trackWaypoint),
        UNTRACK(WaypointManager::untrackWaypoint),
        UPDATE(WaypointManager::updateWaypoint);
        
        private final BiConsumer<TrackedWaypointManager, TrackedWaypoint> action;
        
        Operation(BiConsumer<TrackedWaypointManager, TrackedWaypoint> action) {
            this.action = action;
        }
    }
}
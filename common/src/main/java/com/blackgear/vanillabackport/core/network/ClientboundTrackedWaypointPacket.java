package com.blackgear.vanillabackport.core.network;

import com.blackgear.platform.core.networking.PayloadContext;
import com.blackgear.vanillabackport.client.api.modules.waypoints.ClientWaypointManager;
import com.blackgear.vanillabackport.common.api.modules.waypoints.TrackedWaypoint;
import com.blackgear.vanillabackport.common.api.modules.waypoints.TrackedWaypointManager;
import com.blackgear.vanillabackport.common.api.modules.waypoints.Waypoint.Icon;
import com.blackgear.vanillabackport.common.api.modules.waypoints.WaypointManager;
import com.blackgear.vanillabackport.core.VanillaBackport;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Vec3i;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.util.ByIdMap;
import net.minecraft.world.level.ChunkPos;

import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.IntFunction;

public record ClientboundTrackedWaypointPacket(Operation operation, TrackedWaypoint waypoint) implements CustomPacketPayload {
    public static final Type<ClientboundTrackedWaypointPacket> TYPE = new Type<>(VanillaBackport.resource("tracked_waypoint"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundTrackedWaypointPacket> STREAM_CODEC = StreamCodec.composite(
        Operation.STREAM_CODEC, ClientboundTrackedWaypointPacket::operation,
        TrackedWaypoint.STREAM_CODEC, ClientboundTrackedWaypointPacket::waypoint,
        ClientboundTrackedWaypointPacket::new
    );
    
    public static ClientboundTrackedWaypointPacket removeWaypoint(UUID identifier) {
        return new ClientboundTrackedWaypointPacket(Operation.UNTRACK, TrackedWaypoint.empty(identifier));
    }
    
    public static ClientboundTrackedWaypointPacket addWaypointPosition(UUID identifier, Icon icon, Vec3i position) {
        return new ClientboundTrackedWaypointPacket(Operation.TRACK, TrackedWaypoint.setPosition(identifier, icon, position));
    }
    
    public static ClientboundTrackedWaypointPacket updateWaypointPosition(UUID identifier, Icon icon, Vec3i position) {
        return new ClientboundTrackedWaypointPacket(Operation.UPDATE, TrackedWaypoint.setPosition(identifier, icon, position));
    }
    
    public static ClientboundTrackedWaypointPacket addWaypointChunk(UUID identifier, Icon icon, ChunkPos chunk) {
        return new ClientboundTrackedWaypointPacket(Operation.TRACK, TrackedWaypoint.setChunk(identifier, icon, chunk));
    }
    
    public static ClientboundTrackedWaypointPacket updateWaypointChunk(UUID identifier, Icon icon, ChunkPos chunk) {
        return new ClientboundTrackedWaypointPacket(Operation.UPDATE, TrackedWaypoint.setChunk(identifier, icon, chunk));
    }
    
    public static ClientboundTrackedWaypointPacket addWaypointAzimuth(UUID identifier, Icon icon, float angle) {
        return new ClientboundTrackedWaypointPacket(Operation.TRACK, TrackedWaypoint.setAzimuth(identifier, icon, angle));
    }
    
    public static ClientboundTrackedWaypointPacket updateWaypointAzimuth(UUID identifier, Icon icon, float angle) {
        return new ClientboundTrackedWaypointPacket(Operation.UPDATE, TrackedWaypoint.setAzimuth(identifier, icon, angle));
    }
    
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    
    public static void handler(ClientboundTrackedWaypointPacket payload, PayloadContext context) {
        context.enqueueWork(() -> payload.apply(ClientWaypointManager.INSTANCE));
    }
    
    public void apply(TrackedWaypointManager manager) {
        this.operation.action.accept(manager, this.waypoint);
    }
    
    private enum Operation {
        TRACK(WaypointManager::trackWaypoint),
        UNTRACK(WaypointManager::untrackWaypoint),
        UPDATE(WaypointManager::updateWaypoint);
        
        public static final IntFunction<Operation> BY_ID = ByIdMap.continuous(Enum::ordinal, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
        public static final StreamCodec<ByteBuf, Operation> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Enum::ordinal);
        private final BiConsumer<TrackedWaypointManager, TrackedWaypoint> action;
        
        Operation(BiConsumer<TrackedWaypointManager, TrackedWaypoint> action) {
            this.action = action;
        }
    }
}
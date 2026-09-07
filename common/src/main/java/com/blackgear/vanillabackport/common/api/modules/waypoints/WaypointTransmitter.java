package com.blackgear.vanillabackport.common.api.modules.waypoints;

import com.blackgear.vanillabackport.common.registries.entities.ModAttributes;
import com.blackgear.vanillabackport.core.mixin.common.access.ChunkMapAccessor;
import com.blackgear.vanillabackport.core.network.ClientboundTrackedWaypointPacket;
import com.blackgear.vanillabackport.core.network.NetworkHandler;
import com.blackgear.vanillabackport.core.util.Utilities.VectorUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.phys.Vec3;

import java.util.Optional;

public interface WaypointTransmitter extends Waypoint {
    WaypointTransmitter DEFAULT = new WaypointTransmitter() {
        @Override public boolean isTransmittingWaypoint() { return false; }
        @Override public Optional<Connection> makeWaypointConnectionWith(ServerPlayer player) { return Optional.empty(); }
        @Override public Icon waypointIcon() { return Icon.NULL; }
    };
    
    static WaypointTransmitter of(Object o) {
        return o instanceof WaypointTransmitter transmitter ? transmitter : DEFAULT;
    }
    
    boolean isTransmittingWaypoint();
    
    Optional<Connection> makeWaypointConnectionWith(ServerPlayer player);
    
    Icon waypointIcon();
    
    static boolean doesSourceIgnoreReceiver(LivingEntity source, ServerPlayer receiver) {
        if (receiver.isSpectator()) {
            return false;
        } else if (!source.isSpectator() && !source.hasIndirectPassenger(receiver)) {
            double broadcastRange = Math.min(source.getAttributeValue(ModAttributes.WAYPOINT_TRANSMIT_RANGE), receiver.getAttributeValue(ModAttributes.WAYPOINT_RECEIVE_RANGE));
            return source.distanceTo(receiver) >= broadcastRange;
        } else {
            return true;
        }
    }
    
    static boolean isChunkVisible(ChunkPos pos, ServerPlayer receiver) {
        ChunkPos playerChunk = receiver.chunkPosition();
        int viewDistance = ((ChunkMapAccessor) receiver.serverLevel().getChunkSource().chunkMap).getViewDistance();
        return Math.abs(pos.x - playerChunk.x) <= viewDistance && Math.abs(pos.z - playerChunk.z) <= viewDistance;
    }
    
    static boolean isReallyFar(LivingEntity source, ServerPlayer receiver) {
        return source.distanceTo(receiver) > 332.0F;
    }
    
    interface BlockConnection extends Connection {
        int distanceManhattan();
        
        @Override
        default boolean isBroken() {
            return this.distanceManhattan() > 1;
        }
    }
    
    interface ChunkConnection extends Connection {
        int distanceChessboard();
        
        @Override
        default boolean isBroken() {
            return this.distanceChessboard() > 1;
        }
    }
    
    interface Connection {
        void connect();
        
        void disconnect();
        
        void update();
        
        boolean isBroken();
    }
    
    class EntityAzimuthConnection implements Connection {
        private final LivingEntity source;
        private final Icon icon;
        private final ServerPlayer receiver;
        private float lastAngle;
        
        public EntityAzimuthConnection(LivingEntity source, Icon icon, ServerPlayer receiver) {
            this.source = source;
            this.icon = icon;
            this.receiver = receiver;
            Vec3 direction = VectorUtils.rotateClockwise90(receiver.position().subtract(source.position()));
            this.lastAngle = (float) Mth.atan2(direction.z(), direction.x());
        }
        
        @Override
        public boolean isBroken() {
            return WaypointTransmitter.doesSourceIgnoreReceiver(this.source, this.receiver)
                || WaypointTransmitter.isChunkVisible(this.source.chunkPosition(), this.receiver)
                || !WaypointTransmitter.isReallyFar(this.source, this.receiver);
        }
        
        @Override
        public void connect() {
            NetworkHandler.DEFAULT_CHANNEL.sendToPlayer(ClientboundTrackedWaypointPacket.addWaypointAzimuth(this.source.getUUID(), this.icon, this.lastAngle), this.receiver);
        }
        
        @Override
        public void disconnect() {
            NetworkHandler.DEFAULT_CHANNEL.sendToPlayer(ClientboundTrackedWaypointPacket.removeWaypoint(this.source.getUUID()), this.receiver);
        }
        
        @Override
        public void update() {
            Vec3 direction = VectorUtils.rotateClockwise90(this.receiver.position().subtract(this.source.position()));
            float currentAngle = (float) Mth.atan2(direction.z(), direction.x());
            if (Mth.abs(currentAngle - this.lastAngle) > 0.008726646F) {
                NetworkHandler.DEFAULT_CHANNEL.sendToPlayer(ClientboundTrackedWaypointPacket.updateWaypointAzimuth(this.source.getUUID(), this.icon, currentAngle), this.receiver);
                this.lastAngle = currentAngle;
            }
        }
    }
    
    class EntityBlockConnection implements BlockConnection {
        private final LivingEntity source;
        private final Icon icon;
        private final ServerPlayer receiver;
        private BlockPos lastPosition;
        
        public EntityBlockConnection(LivingEntity source, Icon icon, ServerPlayer receiver) {
            this.source = source;
            this.icon = icon;
            this.receiver = receiver;
            this.lastPosition = source.blockPosition();
        }
        
        @Override
        public void connect() {
            NetworkHandler.DEFAULT_CHANNEL.sendToPlayer(ClientboundTrackedWaypointPacket.addWaypointPosition(this.source.getUUID(), this.icon, this.lastPosition), this.receiver);
        }
        
        @Override
        public void disconnect() {
            NetworkHandler.DEFAULT_CHANNEL.sendToPlayer(ClientboundTrackedWaypointPacket.removeWaypoint(this.source.getUUID()), this.receiver);
        }
        
        @Override
        public void update() {
            BlockPos currentPosition = this.source.blockPosition();
            if (currentPosition.distManhattan(this.lastPosition) > 0) {
                NetworkHandler.DEFAULT_CHANNEL.sendToPlayer(ClientboundTrackedWaypointPacket.updateWaypointPosition(this.source.getUUID(), this.icon, currentPosition), this.receiver);
                this.lastPosition = currentPosition;
            }
        }
        
        @Override
        public int distanceManhattan() {
            return this.lastPosition.distManhattan(this.source.blockPosition());
        }
        
        @Override
        public boolean isBroken() {
            return BlockConnection.super.isBroken()
                || WaypointTransmitter.doesSourceIgnoreReceiver(this.source, this.receiver);
        }
    }
    
    class EntityChunkConnection implements ChunkConnection {
        private final LivingEntity source;
        private final Icon icon;
        private final ServerPlayer receiver;
        private ChunkPos lastPosition;
        
        public EntityChunkConnection(LivingEntity source, Icon icon, ServerPlayer receiver) {
            this.source = source;
            this.icon = icon;
            this.receiver = receiver;
            this.lastPosition = source.chunkPosition();
        }
        
        @Override
        public int distanceChessboard() {
            return this.lastPosition.getChessboardDistance(this.source.chunkPosition());
        }
        
        @Override
        public void connect() {
            NetworkHandler.DEFAULT_CHANNEL.sendToPlayer(ClientboundTrackedWaypointPacket.addWaypointChunk(this.source.getUUID(), this.icon, this.lastPosition), this.receiver);
        }
        
        @Override
        public void disconnect() {
            NetworkHandler.DEFAULT_CHANNEL.sendToPlayer(ClientboundTrackedWaypointPacket.removeWaypoint(this.source.getUUID()), this.receiver);
        }
        
        @Override
        public void update() {
            ChunkPos currentPosition = this.source.chunkPosition();
            if (currentPosition.getChessboardDistance(this.lastPosition) > 0) {
                NetworkHandler.DEFAULT_CHANNEL.sendToPlayer(ClientboundTrackedWaypointPacket.updateWaypointChunk(this.source.getUUID(), this.icon, currentPosition), this.receiver);
                this.lastPosition = currentPosition;
            }
        }
        
        @Override
        public boolean isBroken() {
            return ChunkConnection.super.isBroken()
                || WaypointTransmitter.doesSourceIgnoreReceiver(this.source, this.receiver)
                || WaypointTransmitter.isChunkVisible(this.lastPosition, this.receiver);
        }
    }
}
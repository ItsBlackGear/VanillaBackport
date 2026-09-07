package com.blackgear.vanillabackport.common.api.modules.waypoints;

import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.mixin.common.access.LevelAccessor;
import com.blackgear.vanillabackport.core.util.Utilities.BufferUtils;
import com.blackgear.vanillabackport.core.util.Utilities.VectorUtils;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.Vec3i;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.function.TriFunction;

import java.util.UUID;

public abstract class TrackedWaypoint implements Waypoint {
    protected final Either<UUID, String> identifier;
    private final Icon icon;
    private final Type type;
    
    protected TrackedWaypoint(Either<UUID, String> identifier, Icon icon, Type type) {
        this.identifier = identifier;
        this.icon = icon;
        this.type = type;
    }
    
    public Either<UUID, String> id() {
        return this.identifier;
    }
    
    public abstract void update(TrackedWaypoint other);
    
    public void write(FriendlyByteBuf buf) {
        BufferUtils.writeEither(buf, this.identifier, FriendlyByteBuf::writeUUID, FriendlyByteBuf::writeUtf);
        BufferUtils.writeIcon(buf, this.icon);
        buf.writeEnum(this.type);
        this.writeContents(buf);
    }
    
    protected abstract void writeContents(FriendlyByteBuf buf);
    
    public static TrackedWaypoint read(FriendlyByteBuf buf) {
        Either<UUID, String> identifier = BufferUtils.readEither(buf, FriendlyByteBuf::readUUID, FriendlyByteBuf::readUtf);
        Icon icon = BufferUtils.readIcon(buf);
        Type type = buf.readEnum(Type.class);
        return type.constructor.apply(identifier, icon, buf);
    }
    
    public static TrackedWaypoint setPosition(UUID identifier, Icon icon, Vec3i position) {
        return new Vec3iWaypoint(identifier, icon, position);
    }
    
    public static TrackedWaypoint setChunk(UUID identifier, Icon icon, ChunkPos chunk) {
        return new ChunkWaypoint(identifier, icon, chunk);
    }
    
    public static TrackedWaypoint setAzimuth(UUID identifier, Icon icon, float angle) {
        return new AzimuthWaypoint(identifier, icon, angle);
    }
    
    public static TrackedWaypoint empty(UUID identifier) {
        return new EmptyWaypoint(identifier);
    }
    
    public abstract double yawAngleToCamera(Level level, Camera camera);
    
    public abstract PitchDirection pitchDirectionToCamera(Level level, Projector projector);
    
    public abstract double distanceSquared(Entity fromEntity);
    
    public Icon icon() {
        return this.icon;
    }
    
    public interface Camera {
        float vb$yaw();
        
        Vec3 vb$position();
    }
    
    public enum PitchDirection {
        NONE, UP, DOWN
    }
    
    public interface Projector {
        Vec3 vb$projectPointToScreen(Vec3 point);
        
        double vb$projectHorizonToScreen();
    }
    
    public enum Type {
        EMPTY(EmptyWaypoint::new),
        VEC3I(Vec3iWaypoint::new),
        CHUNK(ChunkWaypoint::new),
        AZIMUTH(AzimuthWaypoint::new);
        
        private final TriFunction<Either<UUID, String>, Icon, FriendlyByteBuf, TrackedWaypoint> constructor;
        
        Type(TriFunction<Either<UUID, String>, Icon, FriendlyByteBuf, TrackedWaypoint> constructor) {
            this.constructor = constructor;
        }
    }
    
    private static class AzimuthWaypoint extends TrackedWaypoint {
        private float angle;
        
        public AzimuthWaypoint(UUID identifier, Icon icon, float angle) {
            super(Either.left(identifier), icon, Type.AZIMUTH);
            this.angle = angle;
        }
        
        public AzimuthWaypoint(Either<UUID, String> identifier, Icon icon, FriendlyByteBuf buf) {
            super(identifier, icon, Type.AZIMUTH);
            this.angle = buf.readFloat();
        }
        
        @Override
        public void update(TrackedWaypoint other) {
            if (other instanceof AzimuthWaypoint waypoint) {
                this.angle = waypoint.angle;
            } else {
                VanillaBackport.LOGGER.warn("Unsupported Waypoint update operation: {}", other.getClass());
            }
        }
        
        @Override
        protected void writeContents(FriendlyByteBuf buf) {
            buf.writeFloat(this.angle);
        }
        
        @Override
        public double yawAngleToCamera(Level level, Camera camera) {
            return Mth.degreesDifference(camera.vb$yaw(), this.angle * Mth.RAD_TO_DEG);
        }
        
        @Override
        public PitchDirection pitchDirectionToCamera(Level level, Projector projector) {
            double horizon = projector.vb$projectHorizonToScreen();
            if (horizon < -1.0) {
                return PitchDirection.DOWN;
            } else {
                return horizon > 1.0 ? PitchDirection.UP : PitchDirection.NONE;
            }
        }
        
        @Override
        public double distanceSquared(Entity fromEntity) {
            return Double.POSITIVE_INFINITY;
        }
    }
    
    private static class ChunkWaypoint extends TrackedWaypoint {
        private ChunkPos chunkPos;
        
        public ChunkWaypoint(UUID identifier, Icon icon, ChunkPos chunkPos) {
            super(Either.left(identifier), icon, Type.CHUNK);
            this.chunkPos = chunkPos;
        }
        
        public ChunkWaypoint(Either<UUID, String> identifier, Icon icon, FriendlyByteBuf buf) {
            super(identifier, icon, Type.CHUNK);
            this.chunkPos = new ChunkPos(buf.readVarInt(), buf.readVarInt());
        }
        
        @Override
        public void update(TrackedWaypoint other) {
            if (other instanceof ChunkWaypoint waypoint) {
                this.chunkPos = waypoint.chunkPos;
            } else {
                VanillaBackport.LOGGER.warn("Unsupported Waypoint update operation: {}", other.getClass());
            }
        }
        
        @Override
        protected void writeContents(FriendlyByteBuf buf) {
            buf.writeVarInt(this.chunkPos.x);
            buf.writeVarInt(this.chunkPos.z);
        }
        
        private Vec3 position(double positionY) {
            return Vec3.atCenterOf(this.chunkPos.getMiddleBlockPosition((int) positionY));
        }
        
        @Override
        public double yawAngleToCamera(Level level, Camera camera) {
            Vec3 cameraPosition = camera.vb$position();
            Vec3 direction = VectorUtils.rotateClockwise90(cameraPosition.subtract(this.position(cameraPosition.y())));
            float waypointAngle = (float) Mth.atan2(direction.z(), direction.x()) * Mth.RAD_TO_DEG;
            return Mth.degreesDifference(camera.vb$yaw(), waypointAngle);
        }
        
        @Override
        public PitchDirection pitchDirectionToCamera(Level level, Projector projector) {
            double onScreenHorizon = projector.vb$projectHorizonToScreen();
            if (onScreenHorizon < -1.0) {
                return PitchDirection.DOWN;
            } else {
                return onScreenHorizon > 1.0 ? PitchDirection.UP : PitchDirection.NONE;
            }
        }
        
        @Override
        public double distanceSquared(Entity fromEntity) {
            return fromEntity.distanceToSqr(Vec3.atCenterOf(this.chunkPos.getMiddleBlockPosition(fromEntity.getBlockY())));
        }
    }
    
    private static class EmptyWaypoint extends TrackedWaypoint {
        private EmptyWaypoint(Either<UUID, String> identifier, Icon icon, FriendlyByteBuf buf) {
            super(identifier, icon, Type.EMPTY);
        }
        
        private EmptyWaypoint(UUID identifier) {
            super(Either.left(identifier), Icon.NULL, Type.EMPTY);
        }
        
        @Override
        public void update(TrackedWaypoint other) {
        }
        
        @Override
        protected void writeContents(FriendlyByteBuf buf) {
        }
        
        @Override
        public double yawAngleToCamera(Level level, Camera camera) {
            return Double.NaN;
        }
        
        @Override
        public PitchDirection pitchDirectionToCamera(Level level, Projector projector) {
            return PitchDirection.NONE;
        }
        
        @Override
        public double distanceSquared(Entity fromEntity) {
            return Double.POSITIVE_INFINITY;
        }
    }
    
    public static class Vec3iWaypoint extends TrackedWaypoint {
        private Vec3i vector;
        
        public Vec3iWaypoint(UUID identifier, Icon icon, Vec3i vector) {
            super(Either.left(identifier), icon, Type.VEC3I);
            this.vector = vector;
        }
        
        public Vec3iWaypoint(Either<UUID, String> identifier, Icon icon, FriendlyByteBuf byteBuf) {
            super(identifier, icon, Type.VEC3I);
            this.vector = new Vec3i(byteBuf.readVarInt(), byteBuf.readVarInt(), byteBuf.readVarInt());
        }
        
        @Override
        public void update(TrackedWaypoint other) {
            if (other instanceof Vec3iWaypoint waypoint) {
                this.vector = waypoint.vector;
            } else {
                VanillaBackport.LOGGER.warn("Unsupported Waypoint update operation: {}", other.getClass());
            }
        }
        
        @Override
        protected void writeContents(FriendlyByteBuf buf) {
            buf.writeVarInt(this.vector.getX());
            buf.writeVarInt(this.vector.getY());
            buf.writeVarInt(this.vector.getZ());
        }
        
        private Vec3 position(Level level) {
            return this.identifier.left()
                .map(uuid -> ((LevelAccessor) level).callGetEntities().get(uuid))
                .map(entity -> entity.blockPosition().distManhattan(this.vector) > 3 ? null : entity.getEyePosition())
                .orElseGet(() -> Vec3.atCenterOf(this.vector));
        }
        
        @Override
        public double yawAngleToCamera(Level level, Camera camera) {
            Vec3 direction = VectorUtils.rotateClockwise90(camera.vb$position().subtract(this.position(level)));
            float waypointAngle = (float) Mth.atan2(direction.z(), direction.x()) * Mth.RAD_TO_DEG;
            return Mth.degreesDifference(camera.vb$yaw(), waypointAngle);
        }
        
        @Override
        public PitchDirection pitchDirectionToCamera(Level level, Projector projector) {
            Vec3 pointOnScreen = projector.vb$projectPointToScreen(this.position(level));
            boolean isBehindCamera = pointOnScreen.z > 1.0;
            double yInFrontOfCamera = isBehindCamera ? -pointOnScreen.y : pointOnScreen.y;
            if (yInFrontOfCamera < -1.0) {
                return PitchDirection.DOWN;
            } else if (yInFrontOfCamera > 1.0) {
                return PitchDirection.UP;
            } else {
                if (isBehindCamera) {
                    if (pointOnScreen.y > 0.0) {
                        return PitchDirection.UP;
                    }
                    
                    if (pointOnScreen.y < 0.0) {
                        return PitchDirection.DOWN;
                    }
                }
                
                return PitchDirection.NONE;
            }
        }
        
        @Override
        public double distanceSquared(Entity fromEntity) {
            return fromEntity.distanceToSqr(Vec3.atCenterOf(this.vector));
        }
    }
}
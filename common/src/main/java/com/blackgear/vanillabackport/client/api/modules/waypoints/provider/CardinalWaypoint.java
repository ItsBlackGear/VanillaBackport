package com.blackgear.vanillabackport.client.api.modules.waypoints.provider;

import com.blackgear.vanillabackport.common.api.modules.waypoints.TrackedWaypoint;
import com.blackgear.vanillabackport.common.api.modules.waypoints.WaypointStyleAsset;
import com.blackgear.vanillabackport.common.api.modules.waypoints.WaypointStyleAssets;
import com.mojang.datafixers.util.Either;
import io.netty.buffer.ByteBuf;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.CommonColors;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

@Environment(EnvType.CLIENT)
public final class CardinalWaypoint extends TrackedWaypoint {
    private static final double BACKGROUND_DISTANCE_SQUARED = 1_000_000.0;
    public final int yaw;
    
    private CardinalWaypoint(String id, ResourceKey<WaypointStyleAsset> style, int yaw) {
        super(Either.right("cardinal_" + id), new Icon(style, Optional.of(CommonColors.WHITE)), Type.EMPTY);
        this.yaw = yaw;
    }
    
    public static List<CardinalWaypoint> createPoints() {
        return List.of(
            new CardinalWaypoint("north", WaypointStyleAssets.NORTH, -180),
            new CardinalWaypoint("northeast", WaypointStyleAssets.CORNER, -135),
            new CardinalWaypoint("east", WaypointStyleAssets.EAST, -90),
            new CardinalWaypoint("southeast", WaypointStyleAssets.CORNER, -45),
            new CardinalWaypoint("south", WaypointStyleAssets.SOUTH, 0),
            new CardinalWaypoint("southwest", WaypointStyleAssets.CORNER, 45),
            new CardinalWaypoint("west", WaypointStyleAssets.WEST, 90),
            new CardinalWaypoint("northwest", WaypointStyleAssets.CORNER, 135)
        );
    }
    
    @Override
    public void update(TrackedWaypoint waypoint) { /* NO-OP */ }
    
    @Override
    public void writeContents(FriendlyByteBuf buf) { /* NO-OP */ }
    
    @Override
    public double yawAngleToCamera(Level level, Camera camera) {
        return Mth.degreesDifference(camera.vb$yaw(), this.yaw);
    }
    
    @Override
    public PitchDirection pitchDirectionToCamera(Level level, Projector projector) {
        return PitchDirection.NONE;
    }
    
    @Override
    public double distanceSquared(Entity entity) {
        return BACKGROUND_DISTANCE_SQUARED;
    }
}
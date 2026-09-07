package com.blackgear.vanillabackport.common.api.modules.waypoints;

import com.blackgear.vanillabackport.common.registries.entities.ModAttributes;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerPlayer;

import java.util.Objects;

public class WaypointUtils {
    public static boolean isReceivingWaypoints(ServerPlayer player) {
        return player.getAttributeValue(ModAttributes.WAYPOINT_RECEIVE_RANGE) > 0.0;
    }
    
    public static int positionHash(GlobalPos pos) {
        int rawHash = Objects.hash(pos.dimension().location(), pos.pos());
        return avalanche(rawHash);
    }
    
    private static int avalanche(int value) {
        value ^= value >>> 16;
        value *= 0x85ebca6b;
        value ^= value >>> 13;
        value *= 0xc2b2ae35;
        value ^= value >>> 16;
        return value;
    }
}
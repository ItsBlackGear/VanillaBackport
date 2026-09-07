package com.blackgear.vanillabackport.client.api.modules.waypoints.provider;

import com.blackgear.vanillabackport.common.api.modules.waypoints.TrackedWaypointManager;
import net.minecraft.client.player.LocalPlayer;

public interface WaypointProvider {
    default void tick(LocalPlayer player, TrackedWaypointManager manager) { /* NO-OP */ }
}
package com.blackgear.vanillabackport.client.api.modules.waypoints.provider;

import com.blackgear.vanillabackport.common.api.modules.waypoints.TrackedWaypointManager;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.player.LocalPlayer;

import java.util.List;

@Environment(EnvType.CLIENT)
public class CardinalWaypointProvider implements WaypointProvider {
    private final List<CardinalWaypoint> points = CardinalWaypoint.createPoints();
    private boolean tracked = false;

    @Override
    public void tick(LocalPlayer player, TrackedWaypointManager manager) {
        boolean enabled = VanillaBackport.CLIENT_CONFIG.locatorDisplayCardinalPoints.get();
        if (enabled && !this.tracked) {
            this.points.forEach(manager::trackWaypoint);
            this.tracked = true;
        } else if (!enabled && this.tracked) {
            this.points.forEach(manager::untrackWaypoint);
            this.tracked = false;
        }
    }
}
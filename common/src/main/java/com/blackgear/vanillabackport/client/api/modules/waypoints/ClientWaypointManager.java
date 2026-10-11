package com.blackgear.vanillabackport.client.api.modules.waypoints;

import com.blackgear.platform.client.event.screen.HudInteractions;
import com.blackgear.vanillabackport.client.api.modules.waypoints.provider.CardinalWaypointProvider;
import com.blackgear.vanillabackport.client.api.modules.waypoints.provider.ItemWaypointProvider;
import com.blackgear.vanillabackport.client.api.modules.waypoints.provider.WaypointProvider;
import com.blackgear.vanillabackport.common.api.modules.waypoints.TrackedWaypoint;
import com.blackgear.vanillabackport.common.api.modules.waypoints.TrackedWaypointManager;
import com.mojang.datafixers.util.Either;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.EnchantmentScreen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class ClientWaypointManager implements TrackedWaypointManager {
    public static final ClientWaypointManager INSTANCE = new ClientWaypointManager();
    
    private final Map<Either<UUID, String>, TrackedWaypoint> waypoints = new ConcurrentHashMap<>();
    private final List<WaypointProvider> providers = new CopyOnWriteArrayList<>();
    
    private long lastTickUpdate = -1;
    
    public static void bootstrap() {
        HudInteractions.CONTAINER_TICK.register((minecraft, screen) -> {
            LocalPlayer player = minecraft.player;
            if (player == null) return;
            if (screen instanceof EnchantmentScreen || screen instanceof AnvilScreen) {
                ExperienceDisplay.of(player).setExperienceDisplayStartTick(player.tickCount);
            }
        });
        
        INSTANCE.registerProvider(new ItemWaypointProvider());
        INSTANCE.registerProvider(new CardinalWaypointProvider());
    }
    
    public void registerProvider(WaypointProvider provider) {
        this.providers.add(provider);
    }
    
    public void tick(LocalPlayer player) {
        if (player == null) return;
        
        if (player.tickCount == this.lastTickUpdate) return;
        this.lastTickUpdate = player.tickCount;
        
        for (WaypointProvider provider : this.providers) {
            provider.tick(player, this);
        }
    }
    
    @Override
    public void trackWaypoint(TrackedWaypoint waypoint) {
        this.waypoints.put(waypoint.id(), waypoint);
    }
    
    @Override
    public void updateWaypoint(TrackedWaypoint waypoint) {
        TrackedWaypoint trackedWaypoint = this.waypoints.get(waypoint.id());
        if (trackedWaypoint != null) trackedWaypoint.update(waypoint);
    }
    
    @Override
    public void untrackWaypoint(TrackedWaypoint waypoint) {
        this.waypoints.remove(waypoint.id());
    }
    
    public boolean hasWaypoints() {
        return !this.waypoints.isEmpty();
    }
    
    public void forEachWaypoint(Entity fromEntity, Consumer<TrackedWaypoint> consumer) {
        this.waypoints.values().stream()
            .sorted(Comparator.comparingDouble((TrackedWaypoint waypoint) -> waypoint.distanceSquared(fromEntity)).reversed())
            .forEachOrdered(consumer);
    }
    
    public Map<Either<UUID, String>, TrackedWaypoint> waypoints() {
        return this.waypoints;
    }
    
    public void clear() {
        this.waypoints.clear();
    }
}
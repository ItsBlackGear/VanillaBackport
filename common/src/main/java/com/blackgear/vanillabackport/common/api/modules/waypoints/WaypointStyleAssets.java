package com.blackgear.vanillabackport.common.api.modules.waypoints;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public interface WaypointStyleAssets {
    ResourceKey<? extends Registry<WaypointStyleAsset>> ROOT_ID = ResourceKey.createRegistryKey(new ResourceLocation("waypoint_style_asset"));
    ResourceKey<WaypointStyleAsset> DEFAULT = create("default");
    ResourceKey<WaypointStyleAsset> BOWTIE = create("bowtie");
    
    ResourceKey<WaypointStyleAsset> LODESTONE = create("lodestone");
    ResourceKey<WaypointStyleAsset> DEATH = create("death");
    
    ResourceKey<WaypointStyleAsset> NORTH = create("north");
    ResourceKey<WaypointStyleAsset> SOUTH = create("south");
    ResourceKey<WaypointStyleAsset> WEST = create("west");
    ResourceKey<WaypointStyleAsset> EAST = create("east");
    ResourceKey<WaypointStyleAsset> CORNER = create("corner");
    
    static ResourceKey<WaypointStyleAsset> create(String name) {
        return ResourceKey.create(ROOT_ID, new ResourceLocation(name));
    }
}
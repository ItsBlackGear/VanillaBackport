package com.blackgear.vanillabackport.client.api.modules.waypoints;

import com.blackgear.vanillabackport.client.resource.SimpleJsonResourceReloadListener;
import com.blackgear.vanillabackport.common.api.modules.waypoints.WaypointStyleAsset;
import com.blackgear.vanillabackport.common.api.modules.waypoints.WaypointStyleAssets;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.resources.FileToIdConverter;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Environment(EnvType.CLIENT)
public class WaypointStyleManager extends SimpleJsonResourceReloadListener<WaypointStyle> {
    private static final FileToIdConverter ASSET_LISTER = FileToIdConverter.json("waypoint_style");
    private static final WaypointStyle MISSING = new WaypointStyle(0, 1, List.of(MissingTextureAtlasSprite.getLocation()));
    private Map<ResourceKey<WaypointStyleAsset>, WaypointStyle> waypointStyles = Map.of();
    public static final WaypointStyleManager INSTANCE = new WaypointStyleManager();
    
    public WaypointStyleManager() {
        super(WaypointStyle.CODEC, ASSET_LISTER);
    }
    
    @Override
    protected void apply(Map<ResourceLocation, WaypointStyle> preparations, ResourceManager manager, ProfilerFiller profiler) {
        this.waypointStyles = preparations.entrySet().stream()
            .collect(Collectors.toUnmodifiableMap(e -> ResourceKey.create(WaypointStyleAssets.ROOT_ID, e.getKey()), Map.Entry::getValue));
    }
    
    public WaypointStyle get(ResourceKey<WaypointStyleAsset> id) {
        return this.waypointStyles.getOrDefault(id, MISSING);
    }
}
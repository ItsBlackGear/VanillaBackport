package com.blackgear.vanillabackport.client.registries;

import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.client.model.geom.ModelLayerLocation;

public class ModModelLayers {
    public static final ModelLayerLocation CREAKING = register("creaking");
    public static final ModelLayerLocation HAPPY_GHAST = register("happy_ghast");
    public static final ModelLayerLocation HAPPY_GHAST_BABY = register("happy_ghast", "baby");
    public static final ModelLayerLocation HAPPY_GHAST_HARNESS = register("happy_ghast", "harness");
    public static final ModelLayerLocation HAPPY_GHAST_ROPES = register("happy_ghast", "ropes");
    public static final ModelLayerLocation HAPPY_GHAST_BABY_ROPES = register("happy_ghast", "baby_ropes");
    public static final ModelLayerLocation PALE_OAK_BOAT = register("pale_oak_boat");
    public static final ModelLayerLocation PALE_OAK_CHEST_BOAT = register("pale_oak_chest_boat");
    public static final ModelLayerLocation BAT = register("bat");

    public static final ModelLayerLocation COLD_PIG = register("cold_pig");
    public static final ModelLayerLocation COLD_CHICKEN = register("cold_chicken");
    public static final ModelLayerLocation COLD_COW = register("cold_cow");
    public static final ModelLayerLocation WARM_COW = register("warm_cow");

    private static ModelLayerLocation register(String name) {
        return register(name, "main");
    }

    private static ModelLayerLocation register(String name, String layer) {
        return new ModelLayerLocation(VanillaBackport.resource(name), layer);
    }
}
package com.blackgear.vanillabackport.client.registries;

import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.resources.ResourceLocation;

public class ModModelLayers {
    // BATS & POTS
    public static final ModelLayerLocation BAT = registerBuiltIn("bat");

    // ARMORED PAWS
    public static final ModelLayerLocation WOLF_ARMOR = register("wolf_armor");
    public static final ModelLayerLocation ARMADILLO = register("armadillo");

    // THE GARDEN AWAKENS
    public static final ModelLayerLocation CREAKING = register("creaking");
    public static final ModelLayerLocation PALE_OAK_BOAT = register("pale_oak_boat");
    public static final ModelLayerLocation PALE_OAK_CHEST_BOAT = register("pale_oak_chest_boat");

    // SPRING TO LIFE
    public static final ModelLayerLocation COLD_PIG = register("cold_pig");
    public static final ModelLayerLocation COLD_CHICKEN = register("cold_chicken");
    public static final ModelLayerLocation COLD_COW = register("cold_cow");
    public static final ModelLayerLocation WARM_COW = register("warm_cow");

    // CHASE THE SKIES
    public static final ModelLayerLocation HAPPY_GHAST = register("happy_ghast");
    public static final ModelLayerLocation HAPPY_GHAST_HARNESS = register("happy_ghast", "harness");
    public static final ModelLayerLocation HAPPY_GHAST_ROPES = register("happy_ghast", "ropes");

    // CHAOS CUBED
    public static final ModelLayerLocation SULFUR_CUBE = register("sulfur_cube");
    public static final ModelLayerLocation SULFUR_CUBE_INNER = register("sulfur_cube", "inner");
    public static final ModelLayerLocation SULFUR_CUBE_SMALL = register("sulfur_cube_small");
    public static final ModelLayerLocation SULFUR_CUBE_SMALL_INNER = register("sulfur_cube_small", "inner");

    private static ModelLayerLocation register(String name) {
        return register(name, "main");
    }

    private static ModelLayerLocation register(String name, String layer) {
        return new ModelLayerLocation(new ResourceLocation(name), layer);
    }

    private static ModelLayerLocation registerBuiltIn(String name) {
        return registerBuiltIn(name, "main");
    }

    private static ModelLayerLocation registerBuiltIn(String name, String layer) {
        return new ModelLayerLocation(VanillaBackport.resource(name), layer);
    }
}
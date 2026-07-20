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
    
    // COPPER AGE
    public static final ModelLayerLocation COPPER_GOLEM = register("copper_golem");
    public static final ModelLayerLocation COPPER_GOLEM_RUNNING = register("copper_golem_running");
    public static final ModelLayerLocation COPPER_GOLEM_SITTING = register("copper_golem_sitting");
    public static final ModelLayerLocation COPPER_GOLEM_STAR = register("copper_golem_star");

    // MOUNTS OF MAYHEM
    public static final ModelLayerLocation PARCHED = register("parched");
    public static final ModelLayerLocation PARCHED_INNER_ARMOR = register("parched", "inner_armor");
    public static final ModelLayerLocation PARCHED_OUTER_ARMOR = register("parched", "outer_armor");
    
    public static final ModelLayerLocation CAMEL_HUSK = register("camel_husk");
    
    public static final ModelLayerLocation UNDEAD_HORSE_ARMOR = register("undead_horse_armor");
    
    public static final ModelLayerLocation NAUTILUS = register("nautilus");
    public static final ModelLayerLocation NAUTILUS_BABY = register("nautilus_baby");
    public static final ModelLayerLocation NAUTILUS_ARMOR = register("nautilus_armor");
    public static final ModelLayerLocation NAUTILUS_SADDLE = register("nautilus", "saddle");
    public static final ModelLayerLocation ZOMBIE_NAUTILUS = register("zombie_nautilus");
    public static final ModelLayerLocation ZOMBIE_NAUTILUS_CORAL = register("zombie_nautilus", "coral");
    
    // CHAOS CUBED
    public static final ModelLayerLocation SULFUR_CUBE = register("sulfur_cube");
    public static final ModelLayerLocation SULFUR_CUBE_INNER = register("sulfur_cube", "inner");
    public static final ModelLayerLocation SULFUR_CUBE_SMALL = register("sulfur_cube_small");
    public static final ModelLayerLocation SULFUR_CUBE_SMALL_INNER = register("sulfur_cube_small", "inner");
    
    // MISC
    public static final ModelLayerLocation CUSHION = register("cushion");
    
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
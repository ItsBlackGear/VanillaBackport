package com.blackgear.vanillabackport.core.forge;

import com.blackgear.vanillabackport.common.worldgen.biomes.terrablender.OverworldRegion;
import com.blackgear.vanillabackport.core.VanillaBackport;
import terrablender.api.RegionType;
import terrablender.api.Regions;

public class VanillaBackportTerrablender {
    public static void onTerraBlenderInitialized() {
        Regions.register(new OverworldRegion(VanillaBackport.resource("overworld"), RegionType.OVERWORLD, 1));
    }
}
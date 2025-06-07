package com.blackgear.vanillabackport.core.fabric;

import com.blackgear.vanillabackport.common.worldgen.biomes.terrablender.OverworldRegion;
import com.blackgear.vanillabackport.core.VanillaBackport;
import terrablender.api.RegionType;
import terrablender.api.Regions;
import terrablender.api.TerraBlenderApi;

public class VanillaBackportTerrablender implements TerraBlenderApi {
    @Override
    public void onTerraBlenderInitialized() {
        Regions.register(new OverworldRegion(VanillaBackport.resource("overworld"), RegionType.OVERWORLD, 1));
    }
}
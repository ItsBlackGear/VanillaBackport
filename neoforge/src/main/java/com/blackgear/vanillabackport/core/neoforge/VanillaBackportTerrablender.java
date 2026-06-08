package com.blackgear.vanillabackport.core.neoforge;

import com.blackgear.vanillabackport.common.worldgen.ModSurfaceRuleData;
import com.blackgear.vanillabackport.common.worldgen.biomes.terrablender.OverworldRegion;
import com.blackgear.vanillabackport.core.VanillaBackport;
import terrablender.api.RegionType;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;

public class VanillaBackportTerrablender {
    public static void onTerraBlenderInitialized() {
        if (VanillaBackport.COMMON_CONFIG.hasPaleGarden.get() || VanillaBackport.COMMON_CONFIG.hasSulfurCaves.get()) {
            Regions.register(new OverworldRegion(VanillaBackport.resource("overworld"), RegionType.OVERWORLD, 5));
        }
        SurfaceRuleManager.addToDefaultSurfaceRulesAtStage(SurfaceRuleManager.RuleCategory.OVERWORLD, SurfaceRuleManager.RuleStage.BEFORE_BEDROCK, 5, ModSurfaceRuleData.makeRules());
    }
}
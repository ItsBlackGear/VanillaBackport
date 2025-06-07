package com.blackgear.vanillabackport.core.neoforge;

import com.blackgear.platform.core.Environment;
import net.neoforged.fml.common.Mod;

import com.blackgear.vanillabackport.core.VanillaBackport;

@Mod(VanillaBackport.MOD_ID)
public final class VanillaBackportNeoForge {
    public VanillaBackportNeoForge() {
        VanillaBackport.bootstrap();
        if (Environment.hasModLoaded("terrablender")) {
            VanillaBackportTerrablender.onTerraBlenderInitialized();
        }
    }
}

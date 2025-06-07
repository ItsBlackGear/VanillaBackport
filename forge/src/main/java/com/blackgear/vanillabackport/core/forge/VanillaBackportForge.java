package com.blackgear.vanillabackport.core.forge;

import com.blackgear.platform.core.Environment;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraftforge.fml.common.Mod;

@Mod(VanillaBackport.MOD_ID)
public final class VanillaBackportForge {
    public VanillaBackportForge() {
        VanillaBackport.bootstrap();
        if (Environment.hasModLoaded("terrablender")) {
            VanillaBackportTerrablender.onTerraBlenderInitialized();
        }
    }
}
package com.blackgear.vanillabackport.common.integrations.compat.everycompat;

import com.blackgear.vanillabackport.core.VanillaBackport;
import net.mehvahdjukaar.every_compat.api.EveryCompatAPI;

public class EveryCompatHandler {
    public static void bootstrap() {
        EveryCompatAPI.registerModule(new ShelfModule(VanillaBackport.MOD_ID));
    }
}
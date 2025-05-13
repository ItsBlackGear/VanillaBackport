package com.blackgear.vanillabackport.core.fabric;

import com.blackgear.vanillabackport.core.VanillaBackport;
import net.fabricmc.api.ModInitializer;

public final class VanillaBackportFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        VanillaBackport.bootstrap();
    }
}
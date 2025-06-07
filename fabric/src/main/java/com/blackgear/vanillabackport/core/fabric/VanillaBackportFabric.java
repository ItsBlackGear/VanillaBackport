package com.blackgear.vanillabackport.core.fabric;

import net.fabricmc.api.ModInitializer;

import com.blackgear.vanillabackport.core.VanillaBackport;

public final class VanillaBackportFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        // Run our common setup.
        VanillaBackport.bootstrap();
    }
}

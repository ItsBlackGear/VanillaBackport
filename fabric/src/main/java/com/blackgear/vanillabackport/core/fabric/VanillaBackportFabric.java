package com.blackgear.vanillabackport.core.fabric;

import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.fabric.registries.VanillaBackportFabricItems;
import net.fabricmc.api.ModInitializer;

public final class VanillaBackportFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        VanillaBackport.bootstrap();

        // Class to handle decorated pot update, fabric platform. - Echo2craft.
        VanillaBackportFabricItems.registerVanillaChanges();
    }
}
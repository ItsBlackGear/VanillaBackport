package com.blackgear.vanillabackport.client;

import com.blackgear.platform.client.GameRendering;
import com.blackgear.platform.common.block.WoodTypeRegistry;
import com.blackgear.platform.core.ParallelDispatch;
import com.blackgear.vanillabackport.common.registries.ModWoodTypes;

public class ClientSetup {
    public static void setup() {
        GameRendering.registerParticleFactories(Rendering::particleFactories);
        GameRendering.registerModelLayers(Rendering::modelLayers);
        GameRendering.registerEntityRenderers(Rendering::entityRendering);
    }

    public static void asyncSetup(ParallelDispatch dispatch) {
        GameRendering.registerBlockRenderers(Rendering::blockRendering);
        WoodTypeRegistry.registerWoodType(ModWoodTypes.PALE_OAK);
    }
}
package com.blackgear.vanillabackport.client;

import com.blackgear.platform.client.GameRendering;
import com.blackgear.platform.common.block.WoodTypeRegistry;
import com.blackgear.platform.core.ParallelDispatch;
import com.blackgear.platform.core.events.ResourceReloadManager;
import com.blackgear.vanillabackport.client.resources.DryFoliageColorReloadListener;
import com.blackgear.vanillabackport.common.registries.ModWoodTypes;
import com.blackgear.vanillabackport.core.VanillaBackport;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.InputStream;

public class ClientSetup {
    public static void setup() {
        ResourceReloadManager.registerClient(event -> {
            event.register(VanillaBackport.resource("dry_foliage"), new DryFoliageColorReloadListener());
        });

        GameRendering.registerParticleFactories(Rendering::particleFactories);
        GameRendering.registerModelLayers(Rendering::modelLayers);
        GameRendering.registerEntityRenderers(Rendering::entityRendering);
    }

    public static void asyncSetup(ParallelDispatch dispatch) {
        GameRendering.registerBlockRenderers(Rendering::blockRendering);
        GameRendering.registerBlockColors(Rendering::blockColors);
        WoodTypeRegistry.registerWoodType(ModWoodTypes.PALE_OAK);
        CreativeTabIntegration.bootstrap();
    }
}
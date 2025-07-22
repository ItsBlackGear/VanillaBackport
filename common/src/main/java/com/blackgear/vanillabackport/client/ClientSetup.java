package com.blackgear.vanillabackport.client;

import com.blackgear.platform.client.GameRendering;
import com.blackgear.platform.client.event.LocalPlayerEvents;
import com.blackgear.platform.common.block.WoodTypeRegistry;
import com.blackgear.platform.core.ParallelDispatch;
import com.blackgear.platform.core.events.ResourceReloadManager;
import com.blackgear.vanillabackport.client.api.tabs.BundledTabSelector;
import com.blackgear.vanillabackport.client.level.bundle.BundleMouseActions;
import com.blackgear.vanillabackport.client.resources.DryFoliageColorReloadListener;
import com.blackgear.vanillabackport.client.resources.LeafColorReloadListener;
import com.blackgear.vanillabackport.common.registries.ModWoodTypes;
import com.blackgear.vanillabackport.core.VanillaBackport;

public class ClientSetup {
    public static void setup() {
        ResourceReloadManager.registerClient(event -> {
            event.register(VanillaBackport.vanilla("dry_foliage"), DryFoliageColorReloadListener.INSTANCE);
            event.register(VanillaBackport.vanilla("leaf_colors"), LeafColorReloadListener.INSTANCE);
        });

        GameRendering.registerParticleFactories(Rendering::particleFactories);
        GameRendering.registerModelLayers(Rendering::modelLayers);
        GameRendering.registerEntityRenderers(Rendering::entityRendering);
        GameRendering.registerBlockColors(Rendering::blockColors);
        GameRendering.registerItemColors(Rendering::itemColors);

        BundleMouseActions.bootstrap();
        LocalPlayerEvents.ON_LOGIN.register(player -> BundledTabSelector.INSTANCE.bootstrap());
    }

    public static void asyncSetup(ParallelDispatch dispatch) {
        GameRendering.registerBlockRenderers(Rendering::blockRendering);
        WoodTypeRegistry.registerWoodType(ModWoodTypes.PALE_OAK);
        CreativeTabIntegration.bootstrap();
    }
}
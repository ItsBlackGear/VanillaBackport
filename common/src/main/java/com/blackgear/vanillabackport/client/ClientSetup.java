package com.blackgear.vanillabackport.client;

import com.blackgear.platform.client.GameRendering;
import com.blackgear.platform.client.event.screen.ContainerInteractEvents;
import com.blackgear.platform.client.event.screen.ScreenMouseInputEvents;
import com.blackgear.platform.common.block.WoodTypeRegistry;
import com.blackgear.platform.core.ParallelDispatch;
import com.blackgear.platform.core.events.ResourceReloadManager;
import com.blackgear.platform.core.util.event.EventResult;
import com.blackgear.vanillabackport.client.level.bundle.BundleMouseActions;
import com.blackgear.vanillabackport.client.level.bundle.ItemSlotMouseAction;
import com.blackgear.vanillabackport.client.resources.DryFoliageColorReloadListener;
import com.blackgear.vanillabackport.client.resources.LeafColorReloadListener;
import com.blackgear.vanillabackport.common.registries.ModWoodTypes;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;

public class ClientSetup {
    public static void setup() {
        ResourceReloadManager.registerClient(event -> {
            event.register(VanillaBackport.resource("dry_foliage"), DryFoliageColorReloadListener.INSTANCE);
            event.register(VanillaBackport.resource("leaf_colors"), LeafColorReloadListener.INSTANCE);
        });

        GameRendering.registerParticleFactories(Rendering::particleFactories);
        GameRendering.registerModelLayers(Rendering::modelLayers);
        GameRendering.registerEntityRenderers(Rendering::entityRendering);
        GameRendering.registerBlockColors(Rendering::blockColors);
        GameRendering.registerItemColors(Rendering::itemColors);

        BundleMouseActions.bootstrap();
    }

    public static void asyncSetup(ParallelDispatch dispatch) {
        GameRendering.registerBlockRenderers(Rendering::blockRendering);
        WoodTypeRegistry.registerWoodType(ModWoodTypes.PALE_OAK);
        CreativeTabIntegration.bootstrap();
    }
}
package com.blackgear.vanillabackport.client;

import com.blackgear.platform.client.GameRendering;
import com.blackgear.platform.client.event.LocalPlayerEvents;
import com.blackgear.platform.common.block.WoodTypeRegistry;
import com.blackgear.platform.core.ParallelDispatch;
import com.blackgear.platform.core.events.ResourcePackManager;
import com.blackgear.platform.core.events.ResourceReloadManager;
import com.blackgear.vanillabackport.client.api.modules.bundle_ui.BundleMouseActions;
import com.blackgear.vanillabackport.client.api.bundled_tabs.BundledTabSelector;
import com.blackgear.vanillabackport.client.integrations.*;
import com.blackgear.vanillabackport.client.integrations.rendering.ColorRendering;
import com.blackgear.vanillabackport.client.integrations.rendering.EntityRendering;
import com.blackgear.vanillabackport.client.integrations.rendering.ItemLikeRendering;
import com.blackgear.vanillabackport.client.integrations.rendering.ParticleRendering;
import com.blackgear.vanillabackport.client.api.modules.leaf_litter.DryFoliageColorReloadListener;
import com.blackgear.vanillabackport.client.api.modules.falling_leaves.LeafColorReloadListener;
import com.blackgear.vanillabackport.common.registries.blocks.ModWoodTypes;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.resources.ResourceLocation;

public class ClientSetup {
    public static void setup() {
        ResourcePackManager.registerBuiltResourcePack(VanillaBackport.resource("freshly_animated"), VanillaBackport.MOD_ID, "Freshly Animated");
        ResourcePackManager.registerBuiltResourcePack(VanillaBackport.resource("freshly_animated_legacy"), VanillaBackport.MOD_ID, "Freshly Animated Legacy");

        ResourceReloadManager.registerClient(event -> {
            event.register(ResourceLocation.withDefaultNamespace("dry_foliage"), DryFoliageColorReloadListener.INSTANCE);
            event.register(ResourceLocation.withDefaultNamespace("leaf_colors"), LeafColorReloadListener.INSTANCE);
        });

        GameRendering.registerParticleFactories(ParticleRendering::factories);
        GameRendering.registerModelLayers(EntityRendering::modelLayers);
        GameRendering.registerEntityRenderers(EntityRendering::renderers);
        GameRendering.registerBlockColors(ColorRendering::blockColors);
        GameRendering.registerItemColors(ColorRendering::itemColors);
        GameRendering.registerBlockEntityRenderers(ItemLikeRendering::blockEntityRendering);
        GameRendering.registerSpecialModels(ItemLikeRendering::specialRendering);
    }

    public static void asyncSetup(ParallelDispatch dispatch) {
        dispatch.enqueueWork(() -> {
            ItemPropertyIntegrations.bootstrap();
            LocalPlayerEvents.ON_LOGIN.register(player -> BundledTabSelector.bootstrap());
            GameRendering.registerHandHeldModels(ItemLikeRendering::handHeldModelRendering);
        });
        BundleMouseActions.bootstrap();

        GameRendering.registerBlockRenderers(ItemLikeRendering::renderTypes);
        WoodTypeRegistry.registerWoodType(ModWoodTypes.PALE_OAK);
        CreativeTabIntegration.bootstrap();
    }
}
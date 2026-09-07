package com.blackgear.vanillabackport.client;

import com.blackgear.platform.client.GameRendering;
import com.blackgear.platform.client.event.LocalPlayerEvents;
import com.blackgear.platform.client.event.rendering.LivingEntityRendererCallback;
import com.blackgear.platform.client.event.screen.hud.HudElementRegistryImpl;
import com.blackgear.platform.client.event.screen.hud.VanillaHudElements;
import com.blackgear.platform.common.block.WoodTypeRegistry;
import com.blackgear.platform.common.events.TickEvents;
import com.blackgear.platform.common.v2.creative_tabs.CreativeTabIntegrations;
import com.blackgear.platform.core.ParallelDispatch;
import com.blackgear.platform.core.events.ResourcePackManager;
import com.blackgear.platform.core.events.ResourceReloadManager;
import com.blackgear.vanillabackport.client.api.modules.bundle_ui.BundleMouseActions;
import com.blackgear.vanillabackport.client.api.bundled_tabs.BundledTabSelector;
import com.blackgear.vanillabackport.client.api.modules.leaf_litter.DryLeafColorReloadListener;
import com.blackgear.vanillabackport.client.api.modules.waypoints.ClientWaypointManager;
import com.blackgear.vanillabackport.client.api.modules.waypoints.LocatorBarRenderer;
import com.blackgear.vanillabackport.client.api.modules.waypoints.WaypointStyleManager;
import com.blackgear.vanillabackport.client.integrations.*;
import com.blackgear.vanillabackport.client.integrations.rendering.ColorRendering;
import com.blackgear.vanillabackport.client.integrations.rendering.EntityRendering;
import com.blackgear.vanillabackport.client.integrations.rendering.ItemLikeRendering;
import com.blackgear.vanillabackport.client.integrations.rendering.ParticleRendering;
import com.blackgear.vanillabackport.client.api.modules.leaf_litter.DryFoliageColorReloadListener;
import com.blackgear.vanillabackport.client.api.modules.falling_leaves.LeafColorReloadListener;
import com.blackgear.vanillabackport.client.level.layer.GhastEquipmentManager;
import com.blackgear.vanillabackport.common.registries.blocks.ModWoodTypes;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

public class ClientSetup {
    public static void setup() {
        ResourcePackManager.registerBuiltResourcePack(VanillaBackport.resource("backported_ost"), VanillaBackport.MOD_ID, "Backported Ost");
        ResourcePackManager.registerBuiltResourcePack(VanillaBackport.resource("freshly_animated"), VanillaBackport.MOD_ID, "Freshly Animated");
        ResourcePackManager.registerBuiltResourcePack(VanillaBackport.resource("freshly_animated_legacy"), VanillaBackport.MOD_ID, "Freshly Animated Legacy");

        ResourceReloadManager.registerClient(event -> {
            event.register(ResourceLocation.withDefaultNamespace("dry_foliage"), DryFoliageColorReloadListener.INSTANCE);
            event.register(ResourceLocation.withDefaultNamespace("leaf_colors"), LeafColorReloadListener.INSTANCE);
            event.register(ResourceLocation.withDefaultNamespace("dry_foliage_colors"), DryLeafColorReloadListener.INSTANCE);
            event.register(ResourceLocation.withDefaultNamespace("waypoint_style_manager"), WaypointStyleManager.INSTANCE);
            event.register(ResourceLocation.withDefaultNamespace("ghast_equipment"), GhastEquipmentManager.INSTANCE);
        });
        
        HudElementRegistryImpl.attachElementBefore(
            VanillaHudElements.EXPERIENCE_LEVEL,
            ResourceLocation.withDefaultNamespace("locator_bar_background"),
            LocatorBarRenderer.INSTANCE::renderBackground
        );
        
        HudElementRegistryImpl.attachElementAfter(
            VanillaHudElements.EXPERIENCE_LEVEL,
            ResourceLocation.withDefaultNamespace("locator_bar_icons"),
            LocatorBarRenderer.INSTANCE::renderWaypoints
        );
        
        GameRendering.registerParticleFactories(ParticleRendering::factories);
        GameRendering.registerModelLayers(EntityRendering::modelLayers);
        GameRendering.registerEntityRenderers(EntityRendering::renderers);
        GameRendering.registerBlockColors(ColorRendering::blockColors);
        GameRendering.registerItemColors(ColorRendering::itemColors);
        GameRendering.registerItemLikeRenderers(ItemLikeRendering::itemLikeRendering);
        GameRendering.registerBlockEntityRenderers(ItemLikeRendering::blockEntityRendering);
        TickEvents.CLIENT_TICK_POST.register(() -> ClientWaypointManager.INSTANCE.tick(Minecraft.getInstance().player));
    }

    public static void asyncSetup(ParallelDispatch dispatch) {
        dispatch.enqueueWork(() -> {
            ItemPropertyIntegrations.bootstrap();
            LocalPlayerEvents.ON_LOGIN.register(player -> {
                BundledTabSelector.bootstrap();
                ClientWaypointManager.bootstrap();
            });
            LivingEntityRendererCallback.APPEND_LAYERS.register(EntityRendering::renderLayers);
            LocalPlayerEvents.ON_LOGOUT.register(player -> ClientWaypointManager.INSTANCE.clear());
        });
        BundleMouseActions.bootstrap();
        
        GameRendering.registerBlockRenderers(ItemLikeRendering::renderTypes);
        WoodTypeRegistry.registerWoodType(ModWoodTypes.PALE_OAK);
        CreativeTabIntegrations.register(VanillaTabIntegrations::bootstrap);
    }
}
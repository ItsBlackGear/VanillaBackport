package com.blackgear.vanillabackport.client;

import com.blackgear.platform.client.GameRendering;
import com.blackgear.platform.common.block.WoodTypeRegistry;
import com.blackgear.platform.common.item.ItemPropertyRegistry;
import com.blackgear.platform.core.ParallelDispatch;
import com.blackgear.platform.core.events.ResourceReloadManager;
import com.blackgear.vanillabackport.client.level.bundle.BundleMouseActions;
import com.blackgear.vanillabackport.client.resources.DryFoliageColorReloadListener;
import com.blackgear.vanillabackport.client.resources.LeafColorReloadListener;
import com.blackgear.vanillabackport.common.api.bundle.BundleContents;
import com.blackgear.vanillabackport.common.registries.ModWoodTypes;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.Items;

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
        BundleContents.getAllBundleItemColors()
            .stream()
            .filter(item -> !item.getDefaultInstance().is(Items.BUNDLE))
            .forEach(item -> ItemPropertyRegistry.register(item, new ResourceLocation("filled"), (stack, level, entity, seed) -> BundleItem.getFullnessDisplay(stack)));
    }

    public static void asyncSetup(ParallelDispatch dispatch) {
        GameRendering.registerBlockRenderers(Rendering::blockRendering);
        WoodTypeRegistry.registerWoodType(ModWoodTypes.PALE_OAK);
        CreativeTabIntegration.bootstrap();
    }
}
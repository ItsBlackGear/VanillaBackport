package com.blackgear.vanillabackport.client;

import com.blackgear.platform.client.GameRendering;
import com.blackgear.vanillabackport.client.level.entities.model.BatModel;
import com.blackgear.vanillabackport.client.level.entities.model.CreakingModel;
import com.blackgear.vanillabackport.client.level.entities.model.HappyGhastHarnessModel;
import com.blackgear.vanillabackport.client.level.entities.model.HappyGhastModel;
import com.blackgear.vanillabackport.client.level.entities.renderer.BatRenderer;
import com.blackgear.vanillabackport.client.level.entities.renderer.CreakingRenderer;
import com.blackgear.vanillabackport.client.level.entities.renderer.HappyGhastRenderer;
import com.blackgear.vanillabackport.client.level.particles.FallingLeavesParticle;
import com.blackgear.vanillabackport.client.level.particles.TrailParticle;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.client.registries.ModParticles;
import com.blackgear.vanillabackport.client.level.entities.renderer.PaleOakBoatRenderer;
import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.blackgear.vanillabackport.common.registries.ModEntities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.EntityType;

@Environment(EnvType.CLIENT)
public class Rendering {
    public static void particleFactories(GameRendering.ParticleFactoryEvent event) {
        event.register(ModParticles.PALE_OAK_LEAVES, FallingLeavesParticle.PaleOakProvider::new);
        event.register(ModParticles.TRAIL, TrailParticle.Provider::new);
    }

    public static void entityRendering(GameRendering.EntityRendererEvent event) {
        event.register(ModEntities.CREAKING.get(), CreakingRenderer::new);
        event.register(ModEntities.HAPPY_GHAST.get(), HappyGhastRenderer::new);
        event.register(ModEntities.PALE_OAK_BOAT.get(), context -> new PaleOakBoatRenderer(context, false));
        event.register(ModEntities.PALE_OAK_CHEST_BOAT.get(), context -> new PaleOakBoatRenderer(context, true));

        event.register(EntityType.BAT, BatRenderer::new);
    }

    public static void modelLayers(GameRendering.ModelLayerEvent event) {
        event.register(ModModelLayers.CREAKING, CreakingModel::createBodyLayer);
        event.register(ModModelLayers.HAPPY_GHAST, () -> HappyGhastModel.createBodyLayer(false, CubeDeformation.NONE));
        event.register(ModModelLayers.HAPPY_GHAST_BABY, () -> HappyGhastModel.createBodyLayer(true, CubeDeformation.NONE));
        event.register(ModModelLayers.HAPPY_GHAST_HARNESS, HappyGhastHarnessModel::createHarnessLayer);
        event.register(ModModelLayers.HAPPY_GHAST_ROPES, () -> HappyGhastModel.createBodyLayer(false, new CubeDeformation(0.2F)));
        event.register(ModModelLayers.HAPPY_GHAST_BABY_ROPES, () -> HappyGhastModel.createBodyLayer(true, new CubeDeformation(0.2F)));
        event.register(ModModelLayers.PALE_OAK_BOAT, BoatModel::createBodyModel);
        event.register(ModModelLayers.PALE_OAK_CHEST_BOAT, ChestBoatModel::createBodyModel);

        event.register(ModModelLayers.BAT, BatModel::createBodyLayer);
    }

    public static void blockRendering(GameRendering.BlockRendererEvent event) {
        event.register(
            RenderType.cutoutMipped(),
            ModBlocks.PALE_OAK_LEAVES.get()
        );
        event.register(
            RenderType.cutout(),
            ModBlocks.PALE_MOSS_CARPET.get(),
            ModBlocks.PALE_HANGING_MOSS.get(),
            ModBlocks.OPEN_EYEBLOSSOM.get(),
            ModBlocks.CLOSED_EYEBLOSSOM.get(),
            ModBlocks.POTTED_OPEN_EYEBLOSSOM.get(),
            ModBlocks.POTTED_CLOSED_EYEBLOSSOM.get(),
            ModBlocks.PALE_OAK_SAPLING.get(),
            ModBlocks.POTTED_PALE_OAK_SAPLING.get(),
            ModBlocks.RESIN_CLUMP.get()
        );
    }
}

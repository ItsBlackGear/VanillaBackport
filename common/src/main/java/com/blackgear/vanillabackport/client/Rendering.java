package com.blackgear.vanillabackport.client;

import com.blackgear.platform.client.GameRendering;
import com.blackgear.platform.client.v2.render.DynamicItemRenderer;
import com.blackgear.platform.client.v2.render.ItemRendererRegistry;
import com.blackgear.vanillabackport.client.api.color.DryFoliageColor;
import com.blackgear.vanillabackport.client.api.color.LeafColors;
import com.blackgear.vanillabackport.client.level.entities.model.*;
import com.blackgear.vanillabackport.client.level.entities.model.chicken.ColdChickenModel;
import com.blackgear.vanillabackport.client.level.entities.model.cow.ColdCowModel;
import com.blackgear.vanillabackport.client.level.entities.model.cow.WarmCowModel;
import com.blackgear.vanillabackport.client.level.entities.model.happy_ghast.HappyGhastHarnessModel;
import com.blackgear.vanillabackport.client.level.entities.model.happy_ghast.HappyGhastModel;
import com.blackgear.vanillabackport.client.level.entities.model.pig.ColdPigModel;
import com.blackgear.vanillabackport.client.level.entities.model.sulfur_cube.SmallSulfurCubeModel;
import com.blackgear.vanillabackport.client.level.entities.model.sulfur_cube.SulfurCubeModel;
import com.blackgear.vanillabackport.client.level.entities.renderer.*;
import com.blackgear.vanillabackport.client.level.entities.renderer.mob.CreakingRenderer;
import com.blackgear.vanillabackport.client.level.entities.renderer.mob.HappyGhastRenderer;
import com.blackgear.vanillabackport.client.level.entities.renderer.mob.SulfurCubeRenderer;
import com.blackgear.vanillabackport.client.level.item.BundleRenderer;
import com.blackgear.vanillabackport.client.level.item.SpawnEggRenderer;
import com.blackgear.vanillabackport.client.level.particles.*;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.client.registries.ModParticles;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.renderer.BiomeColors;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.GrassColor;

@Environment(EnvType.CLIENT)
public class Rendering {
    public static void particleFactories(GameRendering.ParticleFactoryEvent event) {
        event.register(ModParticles.PALE_OAK_LEAVES, FallingLeavesParticle.PaleOakProvider::new);
        event.register(ModParticles.TRAIL, TrailParticle.Provider::new);

        event.register(ModParticles.FIREFLY, FireflyParticle.Provider::new);
        event.register(ModParticles.TINTED_LEAVES, FallingLeavesParticle.TintedLeavesProvider::new);
        event.register(ModParticles.TINTED_NEEDLES, FallingLeavesParticle.TintedLeavesProvider::new);

        event.register(ModParticles.SULFUR_BUBBLES, SulfurBubbleParticle.Provider::new);
        event.register(ModParticles.NOXIOUS_GAS, NoxiousGasParticle.Provider::new);
        event.register(ModParticles.NOXIOUS_GAS_CLOUD, new NoxiousGasCloudParticle.Provider());
        event.register(ModParticles.SULFUR_CUBE_GOO, SulfurCubeParticle.Provider::new);
        event.register(ModParticles.GEYSER, GeyserEruptionParticle.Provider::new);
        event.register(ModParticles.GEYSER_BASE, GeyserBaseParticle.Provider::new);
        event.register(ModParticles.GEYSER_POOF, GeyserBaseParticle.Provider::new);
        event.register(ModParticles.GEYSER_PLUME, GeyserPlumeParticle.Provider::new);
    }

    public static void modelLayers(GameRendering.ModelLayerEvent event) {
        event.register(ModModelLayers.CREAKING, CreakingModel::createBodyLayer);
        event.register(ModModelLayers.PALE_OAK_BOAT, BoatModel::createBodyModel);
        event.register(ModModelLayers.PALE_OAK_CHEST_BOAT, ChestBoatModel::createBodyModel);

        event.register(ModModelLayers.HAPPY_GHAST, () -> HappyGhastModel.createBodyLayer(CubeDeformation.NONE));
        event.register(ModModelLayers.HAPPY_GHAST_HARNESS, HappyGhastHarnessModel::createHarnessLayer);
        event.register(ModModelLayers.HAPPY_GHAST_ROPES, () -> HappyGhastModel.createBodyLayer(new CubeDeformation(0.2F)));

        event.register(ModModelLayers.COLD_PIG, ColdPigModel::createBodyLayer);
        event.register(ModModelLayers.COLD_CHICKEN, ColdChickenModel::createBodyLayer);
        event.register(ModModelLayers.COLD_COW, ColdCowModel::createBodyLayer);
        event.register(ModModelLayers.WARM_COW, WarmCowModel::createBodyLayer);

        event.register(ModModelLayers.SULFUR_CUBE, SulfurCubeModel::createOuterBodyLayer);
        event.register(ModModelLayers.SULFUR_CUBE_INNER, SulfurCubeModel::createInnerBodyLayer);
        event.register(ModModelLayers.SULFUR_CUBE_SMALL, SmallSulfurCubeModel::createOuterBodyLayer);
        event.register(ModModelLayers.SULFUR_CUBE_SMALL_INNER, SmallSulfurCubeModel::createInnerBodyLayer);
    }

    public static void specialModels(GameRendering.SpecialModelEvent event) {
        BundleRenderer.BUNDLES.forEach(item -> ItemRendererRegistry.INSTANCE.get().register(item, new BundleRenderer()));
        SpawnEggRenderer.SPAWN_EGGS.forEach(item -> DynamicItemRenderer.INSTANCE.get().register(item, new SpawnEggRenderer()));
    }

    public static void entityRendering(GameRendering.EntityRendererEvent event) {
        event.register(ModEntityTypes.CREAKING.get(), CreakingRenderer::new);
        event.register(ModEntityTypes.HAPPY_GHAST.get(), HappyGhastRenderer::new);
        event.register(ModEntityTypes.PALE_OAK_BOAT.get(), context -> new PaleOakBoatRenderer(context, false));
        event.register(ModEntityTypes.PALE_OAK_CHEST_BOAT.get(), context -> new PaleOakBoatRenderer(context, true));
        event.register(ModEntityTypes.SULFUR_CUBE.get(), SulfurCubeRenderer::new);
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
            ModBlocks.RESIN_CLUMP.get(),
            ModBlocks.BUSH.get(),
            ModBlocks.FIREFLY_BUSH.get(),
            ModBlocks.WILDFLOWERS.get(),
            ModBlocks.LEAF_LITTER.get(),
            ModBlocks.CACTUS_FLOWER.get(),
            ModBlocks.SHORT_DRY_GRASS.get(),
            ModBlocks.TALL_DRY_GRASS.get(),
            ModBlocks.PALE_OAK_DOOR.get(),
            ModBlocks.PALE_OAK_TRAPDOOR.get(),
            ModBlocks.SULFUR_SPIKE.get()
        );
    }

    public static void blockColors(GameRendering.BlockColorEvent event) {
        event.register(
            (state, level, pos, tint) -> level != null && pos != null
                ? LeafColors.getAverageDryFoliageColor(pos)
                : DryFoliageColor.FOLIAGE_DRY_DEFAULT,
            ModBlocks.LEAF_LITTER.get()
        );
        event.register(
            (state, level, pos, tint) -> level != null && pos != null
                ? BiomeColors.getAverageGrassColor(level, pos)
                : GrassColor.getDefaultColor(),
            ModBlocks.BUSH.get()
        );
        event.register((state, level, pos, tint) -> {
                if (tint != 0) {
                    return level != null && pos != null ? BiomeColors.getAverageGrassColor(level, pos) : GrassColor.getDefaultColor();
                } else {
                    return -1;
                }
            },
            ModBlocks.WILDFLOWERS.get()
        );
    }

    public static void itemColors(GameRendering.ItemColorEvent event) {
        event.register(event::getColor, ModBlocks.BUSH.get());
    }
}
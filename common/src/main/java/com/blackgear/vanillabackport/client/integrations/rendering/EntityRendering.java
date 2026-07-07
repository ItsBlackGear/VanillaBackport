package com.blackgear.vanillabackport.client.integrations.rendering;

import com.blackgear.vanillabackport.client.level.model.entity.CopperGolemModel;
import com.blackgear.vanillabackport.client.level.model.entity.CreakingModel;
import com.blackgear.vanillabackport.client.level.model.entity.chicken.ColdChickenModel;
import com.blackgear.vanillabackport.client.level.model.entity.cow.ColdCowModel;
import com.blackgear.vanillabackport.client.level.model.entity.cow.WarmCowModel;
import com.blackgear.vanillabackport.client.level.model.entity.happy_ghast.HappyGhastHarnessModel;
import com.blackgear.vanillabackport.client.level.model.entity.happy_ghast.HappyGhastModel;
import com.blackgear.vanillabackport.client.level.model.entity.pig.ColdPigModel;
import com.blackgear.vanillabackport.client.level.model.entity.sulfur_cube.SmallSulfurCubeModel;
import com.blackgear.vanillabackport.client.level.model.entity.sulfur_cube.SulfurCubeModel;
import com.blackgear.vanillabackport.client.level.renderer.entity.PaleOakBoatRenderer;
import com.blackgear.vanillabackport.client.level.renderer.entity.mob.CopperGolemRenderer;
import com.blackgear.vanillabackport.client.level.renderer.entity.mob.CreakingRenderer;
import com.blackgear.vanillabackport.client.level.renderer.entity.mob.HappyGhastRenderer;
import com.blackgear.vanillabackport.client.level.renderer.entity.mob.SulfurCubeRenderer;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.model.ChestBoatModel;
import net.minecraft.client.model.geom.builders.CubeDeformation;

import static com.blackgear.platform.client.GameRendering.*;

@Environment(EnvType.CLIENT)
public class EntityRendering {
    public static void modelLayers(ModelLayerEvent event) {
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
        
        event.register(ModModelLayers.COPPER_GOLEM, CopperGolemModel::createBodyLayer);
        event.register(ModModelLayers.COPPER_GOLEM_RUNNING, CopperGolemModel::createRunningPoseBodyLayer);
        event.register(ModModelLayers.COPPER_GOLEM_SITTING, CopperGolemModel::createSittingPoseBodyLayer);
        event.register(ModModelLayers.COPPER_GOLEM_STAR, CopperGolemModel::createStarPoseBodyLayer);
    }
    
    public static void renderers(EntityRendererEvent event) {
        event.register(ModEntityTypes.CREAKING.get(), CreakingRenderer::new);
        event.register(ModEntityTypes.HAPPY_GHAST.get(), HappyGhastRenderer::new);
        event.register(ModEntityTypes.PALE_OAK_BOAT.get(), context -> new PaleOakBoatRenderer(context, false));
        event.register(ModEntityTypes.PALE_OAK_CHEST_BOAT.get(), context -> new PaleOakBoatRenderer(context, true));
        event.register(ModEntityTypes.SULFUR_CUBE.get(), SulfurCubeRenderer::new);
        event.register(ModEntityTypes.COPPER_GOLEM.get(), CopperGolemRenderer::new);
    }
}
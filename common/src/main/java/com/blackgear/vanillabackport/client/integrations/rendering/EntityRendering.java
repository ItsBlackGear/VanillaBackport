package com.blackgear.vanillabackport.client.integrations.rendering;

import com.blackgear.vanillabackport.client.level.entity.model.CreakingModel;
import com.blackgear.vanillabackport.client.level.entity.model.chicken.ColdChickenModel;
import com.blackgear.vanillabackport.client.level.entity.model.cow.ColdCowModel;
import com.blackgear.vanillabackport.client.level.entity.model.cow.WarmCowModel;
import com.blackgear.vanillabackport.client.level.entity.model.happy_ghast.HappyGhastHarnessModel;
import com.blackgear.vanillabackport.client.level.entity.model.happy_ghast.HappyGhastModel;
import com.blackgear.vanillabackport.client.level.entity.model.pig.ColdPigModel;
import com.blackgear.vanillabackport.client.level.entity.model.sulfur_cube.SmallSulfurCubeModel;
import com.blackgear.vanillabackport.client.level.entity.model.sulfur_cube.SulfurCubeModel;
import com.blackgear.vanillabackport.client.level.entity.renderer.PaleOakBoatRenderer;
import com.blackgear.vanillabackport.client.level.entity.renderer.mob.CreakingRenderer;
import com.blackgear.vanillabackport.client.level.entity.renderer.mob.HappyGhastRenderer;
import com.blackgear.vanillabackport.client.level.entity.renderer.mob.SulfurCubeRenderer;
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
    }
    
    public static void renderers(EntityRendererEvent event) {
        event.register(ModEntityTypes.CREAKING.get(), CreakingRenderer::new);
        event.register(ModEntityTypes.HAPPY_GHAST.get(), HappyGhastRenderer::new);
        event.register(ModEntityTypes.PALE_OAK_BOAT.get(), context -> new PaleOakBoatRenderer(context, false));
        event.register(ModEntityTypes.PALE_OAK_CHEST_BOAT.get(), context -> new PaleOakBoatRenderer(context, true));
        event.register(ModEntityTypes.SULFUR_CUBE.get(), SulfurCubeRenderer::new);
    }
}
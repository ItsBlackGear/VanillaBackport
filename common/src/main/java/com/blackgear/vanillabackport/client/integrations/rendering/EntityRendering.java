package com.blackgear.vanillabackport.client.integrations.rendering;

import com.blackgear.vanillabackport.client.level.model.entity.ArmadilloModel;
import com.blackgear.vanillabackport.client.level.model.entity.BatModel;
import com.blackgear.vanillabackport.client.level.model.entity.CopperGolemModel;
import com.blackgear.vanillabackport.client.level.model.entity.CreakingModel;
import com.blackgear.vanillabackport.client.level.model.entity.chicken.ColdChickenModel;
import com.blackgear.vanillabackport.client.level.model.entity.cow.ColdCowModel;
import com.blackgear.vanillabackport.client.level.model.entity.cow.WarmCowModel;
import com.blackgear.vanillabackport.client.level.model.entity.happy_ghast.HappyGhastHarnessModel;
import com.blackgear.vanillabackport.client.level.model.entity.happy_ghast.HappyGhastModel;
import com.blackgear.vanillabackport.client.level.model.entity.nautilus.NautilusModel;
import com.blackgear.vanillabackport.client.level.model.entity.nautilus.ZombieNautilusCoralModel;
import com.blackgear.vanillabackport.client.level.model.entity.pig.ColdPigModel;
import com.blackgear.vanillabackport.client.level.model.entity.sulfur_cube.SmallSulfurCubeModel;
import com.blackgear.vanillabackport.client.level.model.entity.sulfur_cube.SulfurCubeModel;
import com.blackgear.vanillabackport.client.level.model.entity.wolf.WolfArmorModel;
import com.blackgear.vanillabackport.client.level.model.object.CushionModel;
import com.blackgear.vanillabackport.client.level.renderer.entity.PaleOakBoatRenderer;
import com.blackgear.vanillabackport.client.level.renderer.entity.mob.*;
import com.blackgear.vanillabackport.client.level.renderer.object.CushionRenderer;
import com.blackgear.vanillabackport.client.registries.ModModelLayers;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.LayerDefinition;

import static com.blackgear.platform.client.GameRendering.*;

@Environment(EnvType.CLIENT)
public class EntityRendering {
    public static void modelLayers(ModelLayerEvent event) {
        LayerDefinition outerArmorDefinition = LayerDefinition.create(HumanoidArmorModel.createBodyLayer(new CubeDeformation(1.0F)), 64, 32);
        LayerDefinition innerArmorDefinition = LayerDefinition.create(HumanoidArmorModel.createBodyLayer(new CubeDeformation(0.5F)), 64, 32);
        
        event.register(ModModelLayers.BAT, BatModel::createBodyLayer);
        
        event.register(ModModelLayers.ARMADILLO, ArmadilloModel::createBodyLayer);
        event.register(ModModelLayers.WOLF_ARMOR, () -> LayerDefinition.create(WolfArmorModel.createMeshDefinition(new CubeDeformation(0.2F)), 64, 32));
        
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
        
        event.register(ModModelLayers.PARCHED, ParchedRenderer::createSingleModelDualBodyLayer);
        event.register(ModModelLayers.PARCHED_OUTER_ARMOR, () -> outerArmorDefinition);
        event.register(ModModelLayers.PARCHED_INNER_ARMOR, () -> innerArmorDefinition);
        event.register(ModModelLayers.CAMEL_HUSK, CamelModel::createBodyLayer);
        event.register(ModModelLayers.UNDEAD_HORSE_ARMOR, () -> LayerDefinition.create(HorseModel.createBodyMesh(new CubeDeformation(0.1F)), 64, 64));
        
        event.register(ModModelLayers.CUSHION, CushionModel::createBodyLayer);
        event.register(ModModelLayers.NAUTILUS, NautilusModel::createBodyLayer);
        event.register(ModModelLayers.NAUTILUS_BABY, NautilusModel::createBabyBodyLayer);
        event.register(ModModelLayers.NAUTILUS_SADDLE, NautilusModel::createSaddleLayer);
        event.register(ModModelLayers.NAUTILUS_ARMOR, NautilusModel::createBodyArmorLayer);
        event.register(ModModelLayers.ZOMBIE_NAUTILUS, NautilusModel::createBodyLayer);
        event.register(ModModelLayers.ZOMBIE_NAUTILUS_CORAL, ZombieNautilusCoralModel::createBodyLayer);
    }
    
    public static void renderers(EntityRendererEvent event) {
        event.register(ModEntityTypes.ARMADILLO.get(), ArmadilloRenderer::new);
        event.register(ModEntityTypes.CREAKING.get(), CreakingRenderer::new);
        event.register(ModEntityTypes.HAPPY_GHAST.get(), HappyGhastRenderer::new);
        event.register(ModEntityTypes.PALE_OAK_BOAT.get(), context -> new PaleOakBoatRenderer(context, false));
        event.register(ModEntityTypes.PALE_OAK_CHEST_BOAT.get(), context -> new PaleOakBoatRenderer(context, true));
        event.register(ModEntityTypes.SULFUR_CUBE.get(), SulfurCubeRenderer::new);
        
        event.register(ModEntityTypes.COPPER_GOLEM.get(), CopperGolemRenderer::new);
        event.register(ModEntityTypes.PARCHED.get(), ParchedRenderer::new);
        event.register(ModEntityTypes.CAMEL_HUSK.get(), CamelHuskRenderer::new);
        
        event.register(ModEntityTypes.CUSHION.get(), CushionRenderer::new);
        event.register(ModEntityTypes.NAUTILUS.get(), NautilusRenderer::new);
        event.register(ModEntityTypes.ZOMBIE_NAUTILUS.get(), ZombieNautilusRenderer::new);
    }
}
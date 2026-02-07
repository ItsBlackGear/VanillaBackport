package com.blackgear.vanillabackport.core.forge.emissive;

import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Map;

@Mod.EventBusSubscriber(modid = VanillaBackport.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EmissiveModelHandler {
    private static final String[] EMISSIVE_BLOCKS = {
        "open_eyeblossom",
        "potted_open_eyeblossom",
        "firefly_bush"
    };

    private static final String EMISSIVE_SUFFIX = "_emissive";

    @SubscribeEvent
    public static void onModelBake(ModelEvent.ModifyBakingResult event) {
        Map<ResourceLocation, BakedModel> modelRegistry = event.getModels();

        for (String blockName : EMISSIVE_BLOCKS) {
            ModelResourceLocation blockModelLocation = new ModelResourceLocation(new ResourceLocation(blockName), "");

            BakedModel baseModel = modelRegistry.get(blockModelLocation);
            if (baseModel == null) continue;

            ResourceLocation emissiveModelLocation = new ResourceLocation("block/" + blockName + EMISSIVE_SUFFIX);

            BakedModel emissiveModel = modelRegistry.get(emissiveModelLocation);
            if (emissiveModel == null) continue;

            BakedModel wrappedModel = new EmissiveModelWrapper(baseModel, emissiveModel);
            modelRegistry.put(blockModelLocation, wrappedModel);
        }
    }

    @SubscribeEvent
    public static void onRegisterAdditionalModels(ModelEvent.RegisterAdditional event) {
        for (String blockName : EMISSIVE_BLOCKS) {
            ResourceLocation emissiveModelLocation = new ResourceLocation("block/" + blockName + EMISSIVE_SUFFIX);
            event.register(emissiveModelLocation);
        }
    }
}
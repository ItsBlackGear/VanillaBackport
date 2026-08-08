package com.blackgear.vanillabackport.core.forge.emissive;

import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;

@Mod.EventBusSubscriber(modid = VanillaBackport.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EmissiveModelHandler {
    private static final String[] EMISSIVE_BLOCKS = {
        "open_eyeblossom",
        "potted_open_eyeblossom",
        "firefly_bush"
    };

    private static ResourceLocation emissiveModel(String blockName) {
        return new ResourceLocation("block/" + blockName + "_emissive");
    }
    
    @SubscribeEvent
    public static void onModelBake(ModelEvent.ModifyBakingResult event) {
        Map<ResourceLocation, BakedModel> modelRegistry = event.getModels();
        Map<ResourceLocation, BakedModel> replacements = new HashMap<>();
        
        for (String blockName : EMISSIVE_BLOCKS) {
            ModelResourceLocation blockModelLocation = new ModelResourceLocation(new ResourceLocation(blockName), "");
            BakedModel defaultModel = modelRegistry.get(blockModelLocation);

            if (defaultModel == null) continue;

            ResourceLocation emissiveModelLocation = emissiveModel(blockName);
            BakedModel emissiveModel = modelRegistry.get(emissiveModelLocation);

            if (emissiveModel == null) continue;
            replacements.put(blockModelLocation, new EmissiveModelWrapper(defaultModel, emissiveModel));
        }
        
        modelRegistry.putAll(replacements);
    }
    
    @SubscribeEvent
    public static void onRegisterAdditionalModels(ModelEvent.RegisterAdditional event) {
        for (String blockName : EMISSIVE_BLOCKS) event.register(emissiveModel(blockName));
    }
}
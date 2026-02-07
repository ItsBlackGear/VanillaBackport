package com.blackgear.vanillabackport.core.neoforge.emissive;

import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ModelEvent;

import java.util.Map;

@EventBusSubscriber(modid = VanillaBackport.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class EmissiveModelHandler {
    private static final String[] EMISSIVE_BLOCKS = {
        "open_eyeblossom",
        "potted_open_eyeblossom",
        "firefly_bush"
    };

    private static final String EMISSIVE_SUFFIX = "_emissive";

    @SubscribeEvent
    public static void onModelBake(ModelEvent.ModifyBakingResult event) {
        Map<ModelResourceLocation, BakedModel> modelRegistry = event.getModels();

        for (String blockName : EMISSIVE_BLOCKS) {
            ModelResourceLocation emissiveModelLocation = ModelResourceLocation.standalone(
                ResourceLocation.withDefaultNamespace("block/" + blockName + EMISSIVE_SUFFIX)
            );
            BakedModel emissiveModel = modelRegistry.get(emissiveModelLocation);

            if (emissiveModel == null) continue;

            for (Map.Entry<ModelResourceLocation, BakedModel> entry : modelRegistry.entrySet()) {
                ModelResourceLocation location = entry.getKey();

                if (!location.variant().isEmpty()) continue;

                String path = location.id().getPath();
                if (path.equals("block/" + blockName) || path.equals(blockName)) {
                    BakedModel wrappedModel = new EmissiveModelWrapper(entry.getValue(), emissiveModel);
                    modelRegistry.put(location, wrappedModel);
                }
            }
        }
    }

    @SubscribeEvent
    public static void onRegisterAdditionalModels(ModelEvent.RegisterAdditional event) {
        for (String blockName : EMISSIVE_BLOCKS) {
            ModelResourceLocation emissiveModelLocation = ModelResourceLocation.standalone(ResourceLocation.withDefaultNamespace("block/" + blockName + EMISSIVE_SUFFIX));
            event.register(emissiveModelLocation);
        }
    }
}
package com.blackgear.vanillabackport.core.fabric.emissive;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class EmissiveModelHandler implements ClientModInitializer {
    private static final String[] EMISSIVE_BLOCKS = {
        "open_eyeblossom",
        "potted_open_eyeblossom",
        "firefly_bush"
    };

    private static final String EMISSIVE_SUFFIX = "_emissive";

    @Override
    public void onInitializeClient() {
        ModelLoadingPlugin.register(pluginContext -> {
            pluginContext.addModels(getEmissiveModelLocations());

            pluginContext.modifyModelAfterBake().register(ModelModifier.WRAP_PHASE, (model, context) -> {
                ModelResourceLocation modelLocation = context.topLevelId();

                if (modelLocation == null) return model;

                ResourceLocation id = modelLocation.id();

                if (!modelLocation.variant().isEmpty()) {
                    return model;
                }

                String path = id.getPath();
                String namespace = id.getNamespace();

                for (String blockName : EMISSIVE_BLOCKS) {
                    boolean matches = false;

                    if (namespace.equals("minecraft")) {
                        if (path.equals(blockName) || path.equals("block/" + blockName)) {
                            matches = true;
                        }

                        if (id.toString().contains("minecraft:" + blockName)) {
                            matches = true;
                        }
                    }

                    if (matches) {
                        ResourceLocation emissiveModelId = ResourceLocation.withDefaultNamespace("block/" + blockName + EMISSIVE_SUFFIX);
                        BakedModel emissiveModel = context.baker().bake(emissiveModelId, context.settings());

                        if (emissiveModel != null) {
                            return new EmissiveModelWrapper(model, emissiveModel);
                        }
                    }
                }

                return model;
            });
        });
    }

    private static ResourceLocation[] getEmissiveModelLocations() {
        ResourceLocation[] locations = new ResourceLocation[EMISSIVE_BLOCKS.length];
        for (int i = 0; i < EMISSIVE_BLOCKS.length; i++) {
            locations[i] = ResourceLocation.withDefaultNamespace("block/" + EMISSIVE_BLOCKS[i] + EMISSIVE_SUFFIX);
        }

        return locations;
    }
}
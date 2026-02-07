package com.blackgear.vanillabackport.core.fabric.emissive;

import com.blackgear.vanillabackport.core.VanillaBackport;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelLoadingPlugin;
import net.fabricmc.fabric.api.client.model.loading.v1.ModelModifier;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.resources.model.BakedModel;
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
        boolean shouldWarn = FabricLoader.getInstance().isModLoaded("sodium") && !FabricLoader.getInstance().isModLoaded("indium");
        if (shouldWarn) {
            VanillaBackport.LOGGER.warn("Sodium is present without Indium. Emissive textures may not render correctly. Please install Indium for the best experience.");
        }

        ModelLoadingPlugin.register(pluginContext -> {
            pluginContext.addModels(getEmissiveModelLocations());

            pluginContext.modifyModelAfterBake().register(ModelModifier.WRAP_PHASE, (model, context) -> {
                ResourceLocation id = context.id();

                if (id == null) return model;

                String path = id.getPath();
                String namespace = id.getNamespace();

                // Only match block models (path starts with "block/"), not item models
                if (!namespace.equals("minecraft") || !path.startsWith("block/")) {
                    return model;
                }

                for (String blockName : EMISSIVE_BLOCKS) {
                    // Match only the exact block model path
                    if (path.equals("block/" + blockName)) {
                        ResourceLocation emissiveModelId = new ResourceLocation("block/" + blockName + EMISSIVE_SUFFIX);
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
            locations[i] = new ResourceLocation("block/" + EMISSIVE_BLOCKS[i] + EMISSIVE_SUFFIX);
        }

        return locations;
    }
}
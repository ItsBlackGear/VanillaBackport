package com.blackgear.vanillabackport.data.client.model;

import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TexturedModel;

public class ModTexturedModels {
    public static final TexturedModel.Provider MOSSY_CARPET_SIDE = TexturedModel.createDefault(ModTextureMappings::side, ModModelTemplates.MOSSY_CARPET_SIDE);
    public static final TexturedModel.Provider LEAF_LITTER_1 = TexturedModel.createDefault(TextureMapping::defaultTexture, ModModelTemplates.LEAF_LITTER_1);
    public static final TexturedModel.Provider LEAF_LITTER_2 = TexturedModel.createDefault(TextureMapping::defaultTexture, ModModelTemplates.LEAF_LITTER_2);
    public static final TexturedModel.Provider LEAF_LITTER_3 = TexturedModel.createDefault(TextureMapping::defaultTexture, ModModelTemplates.LEAF_LITTER_3);
    public static final TexturedModel.Provider LEAF_LITTER_4 = TexturedModel.createDefault(TextureMapping::defaultTexture, ModModelTemplates.LEAF_LITTER_4);
    public static final TexturedModel.Provider CHAIN = TexturedModel.createDefault(TextureMapping::defaultTexture, ModModelTemplates.CHAIN);
}
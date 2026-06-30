package com.blackgear.vanillabackport.data.client.model;

import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;

public class ModModelTemplates {
    public static final ModelTemplate MOSSY_CARPET_SIDE = block("mossy_carpet_side", TextureSlot.SIDE);
    public static final ModelTemplate DRIED_GHAST = block(
        "dried_ghast",
        TextureSlot.PARTICLE,
        TextureSlot.TOP,
        TextureSlot.BOTTOM,
        TextureSlot.NORTH,
        TextureSlot.SOUTH,
        TextureSlot.EAST,
        TextureSlot.WEST,
        TextureSlots.TENTACLES
    );
    public static final ModelTemplate LEAF_LITTER_1 = block("template_leaf_litter_1", "_1", TextureSlot.TEXTURE);
    public static final ModelTemplate LEAF_LITTER_2 = block("template_leaf_litter_2", "_2", TextureSlot.TEXTURE);
    public static final ModelTemplate LEAF_LITTER_3 = block("template_leaf_litter_3", "_3", TextureSlot.TEXTURE);
    public static final ModelTemplate LEAF_LITTER_4 = block("template_leaf_litter_4", "_4", TextureSlot.TEXTURE);
    
    public static final ModelTemplate CHEST = item("chest", TextureSlot.PARTICLE);
    
    private static ModelTemplate block(String name, String suffix, TextureSlot... slots) {
        return new ModelTemplate(Optional.of(new ResourceLocation("block/" + name)), Optional.of(suffix), slots);
    }
    
    private static ModelTemplate item(String name, TextureSlot... slots) {
        return new ModelTemplate(Optional.of(new ResourceLocation("item/" + name)), Optional.empty(), slots);
    }
    
    private static ModelTemplate block(String name, TextureSlot... slots) {
        return new ModelTemplate(Optional.of(new ResourceLocation("block/" + name)), Optional.empty(), slots);
    }
}
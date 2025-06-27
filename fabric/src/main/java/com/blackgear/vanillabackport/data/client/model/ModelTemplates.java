package com.blackgear.vanillabackport.data.client.model;

import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.data.models.model.ModelTemplate;
import net.minecraft.data.models.model.TextureSlot;

import java.util.Optional;

public class ModelTemplates {
    public static final ModelTemplate MOSSY_CARPET_SIDE = create("mossy_carpet_side", TextureSlot.SIDE);
    public static final ModelTemplate DRIED_GHAST = create(
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
    public static final ModelTemplate LEAF_LITTER_1 = create("template_leaf_litter_1", "_1", TextureSlot.TEXTURE);
    public static final ModelTemplate LEAF_LITTER_2 = create("template_leaf_litter_2", "_2", TextureSlot.TEXTURE);
    public static final ModelTemplate LEAF_LITTER_3 = create("template_leaf_litter_3", "_3", TextureSlot.TEXTURE);
    public static final ModelTemplate LEAF_LITTER_4 = create("template_leaf_litter_4", "_4", TextureSlot.TEXTURE);

    private static ModelTemplate create(String name, TextureSlot... slots) {
        return new ModelTemplate(Optional.of(VanillaBackport.resource("block/" + name)), Optional.empty(), slots);
    }

    private static ModelTemplate create(String name, String suffix, TextureSlot... slots) {
        return new ModelTemplate(Optional.of(VanillaBackport.resource("block/" + name)), Optional.of(suffix), slots);
    }
}
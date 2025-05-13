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

    private static ModelTemplate create(String name, TextureSlot... slots) {
        return new ModelTemplate(Optional.of(VanillaBackport.resource("block/" + name)), Optional.empty(), slots);
    }
}
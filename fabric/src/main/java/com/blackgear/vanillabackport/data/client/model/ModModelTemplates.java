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
        ModTextureSlots.TENTACLES
    );
    public static final ModelTemplate LEAF_LITTER_1 = block("template_leaf_litter_1", "_1", TextureSlot.TEXTURE);
    public static final ModelTemplate LEAF_LITTER_2 = block("template_leaf_litter_2", "_2", TextureSlot.TEXTURE);
    public static final ModelTemplate LEAF_LITTER_3 = block("template_leaf_litter_3", "_3", TextureSlot.TEXTURE);
    public static final ModelTemplate LEAF_LITTER_4 = block("template_leaf_litter_4", "_4", TextureSlot.TEXTURE);
    
    public static final ModelTemplate CHEST = item("chest", TextureSlot.PARTICLE);
    public static final ModelTemplate COPPER_GOLEM_STATUE = item("template_copper_golem_statue", TextureSlot.PARTICLE);
    
    public static final ModelTemplate CHAIN = block("template_chain", TextureSlot.TEXTURE);
    public static final ModelTemplate LIGHTNING_ROD = block("template_lightning_rod", TextureSlot.TEXTURE);
    
    public static final ModelTemplate BARS_CAP = block("template_bars_cap", "_cap", ModTextureSlots.BARS, TextureSlot.EDGE);
    public static final ModelTemplate BARS_CAP_ALT = block("template_bars_cap_alt", "_cap_alt", ModTextureSlots.BARS, TextureSlot.EDGE);
    public static final ModelTemplate BARS_POST = block("template_bars_post", "_post", ModTextureSlots.BARS, TextureSlot.EDGE);
    public static final ModelTemplate BARS_POST_ENDS = block("template_bars_post_ends", "_post_ends", ModTextureSlots.BARS, TextureSlot.EDGE);
    public static final ModelTemplate BARS_POST_SIDE = block("template_bars_side", "_side", ModTextureSlots.BARS, TextureSlot.EDGE);
    public static final ModelTemplate BARS_POST_SIDE_ALT = block("template_bars_side_alt", "_side_alt", ModTextureSlots.BARS, TextureSlot.EDGE);
    
    public static final ModelTemplate SHELF_BODY = block("template_shelf_body", TextureSlot.ALL, TextureSlot.PARTICLE);
    public static final ModelTemplate SHELF_INVENTORY = block("template_shelf_inventory", "_inventory", TextureSlot.ALL, TextureSlot.PARTICLE);
    public static final ModelTemplate SHELF_UNPOWERED = block("template_shelf_unpowered", "_unpowered", TextureSlot.ALL, TextureSlot.PARTICLE);
    public static final ModelTemplate SHELF_UNCONNECTED = block("template_shelf_unconnected", "_unconnected", TextureSlot.ALL, TextureSlot.PARTICLE);
    public static final ModelTemplate SHELF_LEFT = block("template_shelf_left", "_left", TextureSlot.ALL, TextureSlot.PARTICLE);
    public static final ModelTemplate SHELF_CENTER = block("template_shelf_center", "_center", TextureSlot.ALL, TextureSlot.PARTICLE);
    public static final ModelTemplate SHELF_RIGHT = block("template_shelf_right", "_right", TextureSlot.ALL, TextureSlot.PARTICLE);
    
    private static ModelTemplate block(String name, TextureSlot... slots) {
        return new ModelTemplate(Optional.of(ResourceLocation.withDefaultNamespace("block/" + name)), Optional.empty(), slots);
    }
    
    private static ModelTemplate item(String name, TextureSlot... slots) {
        return new ModelTemplate(Optional.of(ResourceLocation.withDefaultNamespace("item/" + name)), Optional.empty(), slots);
    }

    private static ModelTemplate block(String name, String suffix, TextureSlot... slots) {
        return new ModelTemplate(Optional.of(ResourceLocation.withDefaultNamespace("block/" + name)), Optional.of(suffix), slots);
    }
}
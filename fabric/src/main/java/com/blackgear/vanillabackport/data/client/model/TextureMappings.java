package com.blackgear.vanillabackport.data.client.model;

import com.blackgear.vanillabackport.common.registries.ModBlocks;
import net.minecraft.data.models.model.TextureMapping;
import net.minecraft.data.models.model.TextureSlot;
import net.minecraft.world.level.block.Block;

public class TextureMappings {
    public static TextureMapping side(Block block) {
        return TextureMapping.singleSlot(TextureSlot.SIDE, TextureMapping.getBlockTexture(block));
    }

    public static TextureMapping driedGhast(String string) {
        return new TextureMapping()
            .put(TextureSlot.PARTICLE, TextureMapping.getBlockTexture(ModBlocks.DRIED_GHAST.get(), string + "_north"))
            .put(TextureSlot.BOTTOM, TextureMapping.getBlockTexture(ModBlocks.DRIED_GHAST.get(), string + "_bottom"))
            .put(TextureSlot.TOP, TextureMapping.getBlockTexture(ModBlocks.DRIED_GHAST.get(), string + "_top"))
            .put(TextureSlot.NORTH, TextureMapping.getBlockTexture(ModBlocks.DRIED_GHAST.get(), string + "_north"))
            .put(TextureSlot.SOUTH, TextureMapping.getBlockTexture(ModBlocks.DRIED_GHAST.get(), string + "_south"))
            .put(TextureSlot.EAST, TextureMapping.getBlockTexture(ModBlocks.DRIED_GHAST.get(), string + "_west"))
            .put(TextureSlot.WEST, TextureMapping.getBlockTexture(ModBlocks.DRIED_GHAST.get(), string + "_east"))
            .put(TextureSlots.TENTACLES, TextureMapping.getBlockTexture(ModBlocks.DRIED_GHAST.get(), string + "_tentacles"));
    }
}

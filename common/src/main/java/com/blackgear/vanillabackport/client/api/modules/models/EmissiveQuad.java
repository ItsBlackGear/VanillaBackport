package com.blackgear.vanillabackport.client.api.modules.models;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.model.BakedQuad;

@Environment(EnvType.CLIENT)
public class EmissiveQuad extends BakedQuad {
    private static final int FULL_BRIGHTNESS = 0x00F000F0;

    public EmissiveQuad(BakedQuad original) {
        super(makeEmissive(original.getVertices().clone()),
            original.getTintIndex(),
            original.getDirection(),
            original.getSprite(),
            original.isShade());
    }

    private static int[] makeEmissive(int[] vertexData) {
        int vertexSize = 8;
        int lightsOffset = 6;

        for (int i = 0; i < vertexData.length; i += vertexSize) {
            vertexData[i + lightsOffset] = FULL_BRIGHTNESS;
        }

        return vertexData;
    }
}
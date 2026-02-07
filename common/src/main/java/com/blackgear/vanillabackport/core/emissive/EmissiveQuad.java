package com.blackgear.vanillabackport.core.emissive;

import net.minecraft.client.renderer.block.model.BakedQuad;

/**
 * A BakedQuad wrapper that forces full brightness (emissive rendering)
 * Works for both Forge and Fabric
 */
public class EmissiveQuad extends BakedQuad {
    private static final int FULL_BRIGHTNESS = 0x00F000F0; // Max block light (15) and sky light (15)

    public EmissiveQuad(BakedQuad original) {
        super(makeEmissive(original.getVertices().clone()),
              original.getTintIndex(),
              original.getDirection(),
              original.getSprite(),
              original.isShade());
    }

    /**
     * Modifies vertex data to set full brightness
     */
    private static int[] makeEmissive(int[] vertexData) {
        // Vertex format: position (3 floats), color (1 int), uv (2 floats), lightmap (1 int), normal (1 int)
        // Each vertex is 8 ints (32 bytes)
        int vertexSize = 8;
        int lightsOffset = 6; // Lightmap is at offset 6 in the vertex data

        for (int i = 0; i < vertexData.length; i += vertexSize) {
            vertexData[i + lightsOffset] = FULL_BRIGHTNESS;
        }

        return vertexData;
    }
}

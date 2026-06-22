package com.blackgear.vanillabackport.client.api.experiment;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;

@Environment(EnvType.CLIENT)
public class EnvironmentalFogRenderer {
    public static void renderFog(
            PoseStack poseStack,
            float fogStart, float fogEnd, float fogHeight,
            float r, float g, float b, int fogAlpha,
            int numShells, int subdivisions
    ) {
        for (int s = numShells; s >= 1; s--) {
            float t = (float) s / numShells;

            float radius = fogStart + t * (fogEnd - fogStart);

            float smooth = t * t * (3.0f - 2.0f * t);
            int shellAlpha = (int) (fogAlpha * smooth);

            shellAlpha = Math.clamp(shellAlpha, 0, 255);

            renderCylinderShell(poseStack, radius, fogHeight, subdivisions, r, g, b, shellAlpha);
        }
    }

    private static void renderCylinderShell(
            PoseStack poseStack,
            float radius, float fogHeight,
            int subdivisions,
            float r, float g, float b, int currentAlpha
    ) {
        RenderSystem.setShader(GameRenderer::getPositionColorShader);

        BufferBuilder buffer = Tesselator.getInstance().begin(
                VertexFormat.Mode.TRIANGLE_STRIP,
                DefaultVertexFormat.POSITION_COLOR
        );

        Matrix4f matrix = poseStack.last().pose();
        int verticalBands = Math.max(4, subdivisions / 2);

        for (int v = 0; v < verticalBands; v++) {
            float y0 = fogHeight * (-1.0f + 2.0f * v / verticalBands);
            float y1 = fogHeight * (-1.0f + 2.0f * (v + 1) / verticalBands);

            float t0 = Math.abs(y0 / fogHeight);
            float t1 = Math.abs(y1 / fogHeight);

            float hf0 = (float) Math.pow(Math.cos(t0 * Math.PI * 0.5), 8.0);
            float hf1 = (float) Math.pow(Math.cos(t1 * Math.PI * 0.5), 8.0);

            int a0 = Math.min(255, (int) (currentAlpha * hf0));
            int a1 = Math.min(255, (int) (currentAlpha * hf1));

            for (int j = 0; j <= subdivisions; j++) {
                float angle = (float) (2.0 * Math.PI * j / subdivisions);
                float x = (float) Math.cos(angle) * radius;
                float z = (float) Math.sin(angle) * radius;

                buffer.addVertex(matrix, x, y0, z).setColor(r, g, b, a0);
                buffer.addVertex(matrix, x, y1, z).setColor(r, g, b, a1);
            }
        }

        BufferUploader.drawWithShader(buffer.buildOrThrow());
    }
}
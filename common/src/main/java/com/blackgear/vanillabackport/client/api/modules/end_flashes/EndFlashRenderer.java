package com.blackgear.vanillabackport.client.api.modules.end_flashes;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.mojang.blaze3d.platform.GlStateManager.DestFactor;
import com.mojang.blaze3d.platform.GlStateManager.SourceFactor;
import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.WinScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import org.joml.Matrix4f;

public class EndFlashRenderer {
    private static final ResourceLocation END_FLASH_LOCATION = new ResourceLocation("textures/environment/end_flash.png");
    private static final int END_SKY_COLOR = 0xFFCD60AC;

    public static void tick(Minecraft minecraft, ClientLevel level, EndFlashState state) {
        if (!VanillaBackport.CLIENT_CONFIG.endFlashSkyVisuals.get()) return;
        
        if (state == null) return;

        state.tick(level.getGameTime());
        
        if (state.flashStartedThisTick() && !(minecraft.screen instanceof WinScreen)) {
            minecraft.getSoundManager().playDelayed(
                new DirectionalSoundInstance(
                    ModSoundEvents.WEATHER_END_FLASH.get(),
                    SoundSource.WEATHER,
                    level.getRandom(),
                    minecraft.gameRenderer.getMainCamera(),
                    state.getXAngle(),
                    state.getYAngle()
                ), 30
            );
        }
    }

    public static void render(PoseStack poseStack, Minecraft minecraft) {
        if (!VanillaBackport.CLIENT_CONFIG.endFlashSkyVisuals.get()) return;
        
        ClientLevel level = minecraft.level;
        if (level == null || minecraft.options.hideLightningFlash().get() || minecraft.gui.getBossOverlay().shouldCreateWorldFog()) {
            return;
        }

        EndFlashState endFlashState = ((EndFlashGetter) level).endFlashState();
        if (endFlashState == null) return;

        float intensity = endFlashState.getIntensity(minecraft.getFrameTime());
        if (intensity <= 1.0E-5F) return;

        poseStack.pushPose();
        
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - endFlashState.getYAngle()));
        poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F - endFlashState.getXAngle()));
        poseStack.translate(0.0F, 100.0F, 0.0F);
        poseStack.scale(60.0F, 1.0F, 60.0F);

        Matrix4f matrix4f = poseStack.last().pose();

        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(SourceFactor.SRC_ALPHA, DestFactor.ONE, SourceFactor.ONE, DestFactor.ZERO);
        RenderSystem.depthMask(false);
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        RenderSystem.setShaderTexture(0, END_FLASH_LOCATION);

        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        
        bufferbuilder.vertex(matrix4f, -1.0F, 0.0F, -1.0F).uv(0.0F, 0.0F).color(intensity, intensity, intensity, intensity).endVertex();
        bufferbuilder.vertex(matrix4f, 1.0F, 0.0F, -1.0F).uv(1.0F, 0.0F).color(intensity, intensity, intensity, intensity).endVertex();
        bufferbuilder.vertex(matrix4f, 1.0F, 0.0F, 1.0F).uv(1.0F, 1.0F).color(intensity, intensity, intensity, intensity).endVertex();
        bufferbuilder.vertex(matrix4f, -1.0F, 0.0F, 1.0F).uv(0.0F, 1.0F).color(intensity, intensity, intensity, intensity).endVertex();

        Tesselator.getInstance().end();

        RenderSystem.depthMask(true);
        RenderSystem.defaultBlendFunc();
        poseStack.popPose();
    }

    public static float calculateLightIntensity(Minecraft minecraft, float partialTicks) {
        if (!VanillaBackport.CLIENT_CONFIG.endFlashTerrainVisuals.get()) return 0.0F;
        
        ClientLevel level = minecraft.level;
        if (level == null || level.effects().skyType() != DimensionSpecialEffects.SkyType.END) {
            return 0.0F;
        }

        EndFlashState state = ((EndFlashGetter) level).endFlashState();
        if (state == null || minecraft.options.hideLightningFlash().get()) {
            return 0.0F;
        }

        float intensity = state.getIntensity(partialTicks);
        if (intensity <= 0.0F) return 0.0F;

        if (minecraft.gui.getBossOverlay().shouldCreateWorldFog()) {
            intensity /= 3.0F;
        }

        return intensity;
    }
    
    public static void applyLightTint(NativeImage lightPixels, float intensity) {
        if (!VanillaBackport.CLIENT_CONFIG.endFlashTerrainVisuals.get() || intensity <= 0.0F) return;
        
        float fr = (END_SKY_COLOR & 0xFF) / 255.0F;
        float fg = ((END_SKY_COLOR >> 8) & 0xFF) / 255.0F;
        float fb = ((END_SKY_COLOR >> 16) & 0xFF) / 255.0F;
        
        for (int sky = 0; sky < 16; ++sky) {
            final float baseFlash = intensity * (sky / 15.0F);
            
            for (int block = 0; block < 16; ++block) {
                if (sky == 15 && block == 15) continue;
                
                int pixel = lightPixels.getPixelRGBA(block, sky);
                float r = (pixel & 0xFF) / 255.0F;
                float g = ((pixel >> 8) & 0xFF) / 255.0F;
                float b = ((pixel >> 16) & 0xFF) / 255.0F;
                
                float blockFactor = block / 15.0F;
                float invBlock = 1.0F - blockFactor;
                
                float lerpR = r + baseFlash * (fr - r);
                float lerpG = g + baseFlash * (fg - g);
                float lerpB = b + baseFlash * (fb - b);
                
                float addR = r + fr * baseFlash * invBlock;
                float addG = g + fg * baseFlash * invBlock * 0.4F;
                float addB = b + fb * baseFlash * invBlock * 1.2F;
                
                int nr = (int) (Mth.clamp(lerpR + blockFactor * (addR - lerpR), 0.0F, 1.0F) * 255.0F);
                int ng = (int) (Mth.clamp(lerpG + blockFactor * (addG - lerpG), 0.0F, 1.0F) * 255.0F);
                int nb = (int) (Mth.clamp(lerpB + blockFactor * (addB - lerpB), 0.0F, 1.0F) * 255.0F);
                
                lightPixels.setPixelRGBA(block, sky, 0xFF000000 | nr | (ng << 8) | (nb << 16));
            }
        }
    }
}
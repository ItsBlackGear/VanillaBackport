package com.blackgear.vanillabackport.core.neoforge;

import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.client.Camera;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.CubicSampler;
import net.minecraft.util.Mth;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import org.joml.Vector3f;

@EventBusSubscriber(modid = VanillaBackport.MOD_ID, value = Dist.CLIENT)
public class ClientModEvents {
    private static float RAIN_FOG_MULTIPLIER = 0.0F;

    private static float fogRed = 200;
    private static float fogGreen = 30;
    private static float fogBlue = 30;
    private static final int FOG_ALPHA = 20;

    private static float FOG_START = 10.0f;
    private static float FOG_END   = 96.0f;

    private static final float FOG_HEIGHT = 20.0f;

    private static final int FOG_SHELLS      = 320;
    private static final int FOG_SUBDIVISIONS = 20;

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
//        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) return;
//
//        Minecraft mc = Minecraft.getInstance();
//        if (mc.player == null) return;
//
//        PoseStack poseStack = event.getPoseStack();
//        poseStack.pushPose();
//
//        RenderSystem.enableBlend();
//        RenderSystem.defaultBlendFunc();
//        RenderSystem.enableDepthTest();
//        RenderSystem.depthMask(false);
//        RenderSystem.disableCull();
//
//        EnvironmentalFogRenderer.renderFog(
//                poseStack,
//                FOG_START, FOG_END, FOG_HEIGHT,
//                fogRed, fogGreen, fogBlue, FOG_ALPHA,
//                FOG_SHELLS, FOG_SUBDIVISIONS
//        );
//        setupFog(event.getCamera(), mc.level, event.getPartialTick());
//        getBaseColor(mc.level, event.getCamera(), mc.options.getEffectiveRenderDistance(), event.getPartialTick().getGameTimeDeltaPartialTick(false));
//
//        RenderSystem.depthMask(true);
//        RenderSystem.enableCull();
//        RenderSystem.disableBlend();
//
//        poseStack.popPose();
    }

    public static void setupFog(final Camera camera, final ClientLevel level, final DeltaTracker deltaTracker) {
        Biome biome = level.getBiome(camera.getBlockPosition()).value();
        float partialTicks = deltaTracker.getRealtimeDeltaTicks();
        boolean hasPrecipitation = biome.hasPrecipitation();
        float skyLight = Mth.clamp(((float) level.getLightEngine().getLayerListener(LightLayer.SKY).getLightValue(camera.getBlockPosition()) - 8.0F) / 7.0F, 0.0F, 1.0F);
        float rainModifier = level.getRainLevel(deltaTracker.getGameTimeDeltaPartialTick(false)) * skyLight * (hasPrecipitation ? 1.0F : 0.5F);
        RAIN_FOG_MULTIPLIER += (rainModifier - RAIN_FOG_MULTIPLIER) * partialTicks * 0.2F;
        FOG_START = RAIN_FOG_MULTIPLIER * -160.0F;
        FOG_END = 1024.0f + -256.0f * rainModifier;
    }

    public static void getBaseColor(ClientLevel level, Camera activeRenderInfo, int renderDistanceChunks, float partialTicks) {
        float f4 = 0.25F + 0.75F * (float)renderDistanceChunks / 32.0F;
        f4 = 1.0F - (float)Math.pow(f4, 0.25);
        Vec3 vec3 = level.getSkyColor(activeRenderInfo.getPosition(), partialTicks);
        float f6 = (float)vec3.x;
        float f8 = (float)vec3.y;
        float f10 = (float)vec3.z;
        float f11 = Mth.clamp(Mth.cos(level.getTimeOfDay(partialTicks) * (float) (Math.PI * 2)) * 2.0F + 0.5F, 0.0F, 1.0F);
        BiomeManager biomemanager = level.getBiomeManager();
        Vec3 vec31 = activeRenderInfo.getPosition().subtract(2.0, 2.0, 2.0).scale(0.25);
        Vec3 vec32 = CubicSampler.gaussianSampleVec3(
                vec31,
                (ix, jx, k) -> level.effects().getBrightnessDependentFogColor(Vec3.fromRGB24(biomemanager.getNoiseBiomeAtQuart(ix, jx, k).value().getFogColor()), f11)
        );
        fogRed = (float)vec32.x();
        fogGreen = (float)vec32.y();
        fogBlue = (float)vec32.z();
        if (renderDistanceChunks >= 4) {
            float f12 = Mth.sin(level.getSunAngle(partialTicks)) > 0.0F ? -1.0F : 1.0F;
            Vector3f vector3f = new Vector3f(f12, 0.0F, 0.0F);
            float f16 = activeRenderInfo.getLookVector().dot(vector3f);
            if (f16 < 0.0F) {
                f16 = 0.0F;
            }

            if (f16 > 0.0F) {
                float[] afloat = level.effects().getSunriseColor(level.getTimeOfDay(partialTicks), partialTicks);
                if (afloat != null) {
                    f16 *= afloat[3];
                    fogRed = fogRed * (1.0F - f16) + afloat[0] * f16;
                    fogGreen = fogGreen * (1.0F - f16) + afloat[1] * f16;
                    fogBlue = fogBlue * (1.0F - f16) + afloat[2] * f16;
                }
            }
        }

        fogRed = fogRed + (f6 - fogRed) * f4;
        fogGreen = fogGreen + (f8 - fogGreen) * f4;
        fogBlue = fogBlue + (f10 - fogBlue) * f4;
        float f13 = level.getRainLevel(partialTicks);
        if (f13 > 0.0F) {
            float f14 = 1.0F - f13 * 0.5F;
            float f17 = 1.0F - f13 * 0.4F;
            fogRed *= f14;
            fogGreen *= f14;
            fogBlue *= f17;
        }

        float f15 = level.getThunderLevel(partialTicks);
        if (f15 > 0.0F) {
            float f18 = 1.0F - f15 * 0.5F;
            fogRed *= f18;
            fogGreen *= f18;
            fogBlue *= f18;
        }
    }
}
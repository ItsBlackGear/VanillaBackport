package com.blackgear.vanillabackport.common.api.leash;

import com.blackgear.vanillabackport.core.mixin.access.EntityRendererAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class LeashRenderer<T extends Entity> {
    private final EntityRenderDispatcher dispatcher;
    @Nullable private List<LeashState> leashStates;

    public LeashRenderer(EntityRenderDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public boolean shouldRender(T entity, Frustum camera, boolean isVisible, boolean fallback) {
        if (!isVisible) {
            AABB entityBox = entity.getBoundingBoxForCulling().inflate(0.5);
            if (entityBox.hasNaN() || entityBox.getSize() == 0.0) {
                entityBox = new AABB(entity.getX() - 2.0, entity.getY() - 2.0, entity.getZ() - 2.0, entity.getX() + 2.0, entity.getY() + 2.0, entity.getZ() + 2.0);
            }

            if (camera.isVisible(entityBox)) {
                return true;
            } else if (entity instanceof Leashable leashable) {
                Entity holder = leashable.getLeashHolder();
                if (holder != null) {
                    AABB holderBox = holder.getBoundingBoxForCulling();
                    return camera.isVisible(holderBox) || camera.isVisible(entityBox.minmax(holderBox));
                }
            }
        }

        return fallback;
    }

    public boolean shouldRender(T entity, Frustum camera, boolean isVisible) {
        return this.shouldRender(entity, camera, isVisible, isVisible);
    }

    public void render(T entity, float partialTick, PoseStack poseStack, MultiBufferSource buffer) {
        this.setupLeashRendering(entity, partialTick);

        if (this.leashStates != null) {
            for (LeashState state : this.leashStates) {
                this.renderLeash(poseStack, buffer, state);
            }
        }
    }

    private void renderLeash(PoseStack stack, MultiBufferSource buffer, LeashState state) {
        float deltaX = (float) (state.end.x - state.start.x);
        float deltaY = (float) (state.end.y - state.start.y);
        float deltaZ = (float) (state.end.z - state.start.z);

        float scaleFactor = Mth.invSqrt(deltaX * deltaX + deltaZ * deltaZ) * 0.05F / 2.0F;

        float offsetZ = deltaZ * scaleFactor;
        float offsetX = deltaX * scaleFactor;

        stack.pushPose();
        stack.translate(state.offset.x, state.offset.y, state.offset.z);

        VertexConsumer vertices = buffer.getBuffer(RenderType.leash());
        Matrix4f matrices = stack.last().pose();

        for (int segment = 0; segment <= 24; segment++) {
            addVertexPair(vertices, matrices, deltaX, deltaY, deltaZ, 0.05F, 0.05F, offsetZ, offsetX, segment, false, state);
        }

        for (int segment = 24; segment >= 0; segment--) {
            addVertexPair(vertices, matrices, deltaX, deltaY, deltaZ, 0.05F, 0.0F, offsetZ, offsetX, segment, true, state);
        }

        stack.popPose();
    }

    private static void addVertexPair(
        VertexConsumer vertices, Matrix4f matrices,
        float deltaX, float deltaY, float deltaZ,
        float thickness1, float thickness2,
        float offsetZ, float offsetX,
        int segment, boolean isInnerFace, LeashState state
    ) {
        float progress = (float) segment / 24.0f;

        int blockLight = (int) Mth.lerp(progress, state.startBlockLight, state.endBlockLight);
        int skyLight = (int) Mth.lerp(progress, state.startSkyLight, state.endSkyLight);
        int packedLight = LightTexture.pack(blockLight, skyLight);

        float colorModifier = segment % 2 == (isInnerFace ? 1 : 0) ? 0.7f : 1.0f;
        float red = 0.5f * colorModifier;
        float green = 0.4f * colorModifier;
        float blue = 0.3f * colorModifier;

        float posX = deltaX * progress;
        float posZ = deltaZ * progress;
        float posY = state.slack
            ? (deltaY > 0.0f
            ? deltaY * progress * progress
            : deltaY - deltaY * (1.0f - progress) * (1.0f - progress))
            : deltaY * progress;

        vertices.addVertex(matrices, posX - offsetZ, posY + thickness2, posZ + offsetX).setColor(red, green, blue, 1.0f).setLight(packedLight);
        vertices.addVertex(matrices, posX + offsetZ, posY + thickness1 - thickness2, posZ - offsetX).setColor(red, green, blue, 1.0f).setLight(packedLight);
    }

    private void setupLeashRendering(T entity, float partialTicks) {
        if (!(entity instanceof Leashable leashable)) {
            this.leashStates = null;
            return;
        }

        LeashExtension extension = (LeashExtension) leashable;

        Entity leashHolder = leashable.getLeashHolder();

        if (leashHolder != null) {
            float entityRotation = LeashExtension.getPreciseBodyRotation(entity, partialTicks) * ((float) Math.PI / 180);
            Vec3 leashOffset = entity.getLeashOffset(partialTicks);

            BlockPos entityPos = BlockPos.containing(entity.getEyePosition(partialTicks));
            BlockPos holderPos = BlockPos.containing(leashHolder.getEyePosition(partialTicks));
            int entityBlockLight = this.getBlockLightLevel(entity, entityPos);
            int holderBlockLight = this.getBlockLightLevel(leashHolder, holderPos);
            int entitySkyLight = entity.level().getBrightness(LightLayer.SKY, entityPos);
            int holderSkyLight = entity.level().getBrightness(LightLayer.SKY, holderPos);

            boolean handleHolderQuadLeash = leashHolder instanceof LeashExtension ext && ext.supportQuadLeashAsHolder();
            boolean handleQuadLeash = extension.supportQuadLeash();
            boolean useQuadLeash = handleHolderQuadLeash && handleQuadLeash;
            int leashCount = useQuadLeash ? 4 : 1;

            if (this.leashStates == null || this.leashStates.size() != leashCount) {
                this.leashStates = new ArrayList<>(leashCount);
                for (int i = 0; i < leashCount; i++) {
                    this.leashStates.add(new LeashState());
                }
            }

            if (useQuadLeash) {
                float holderRotation = LeashExtension.getPreciseBodyRotation(leashHolder, partialTicks) * ((float) Math.PI / 180);
                Vec3 holderPosition = leashHolder.getPosition(partialTicks);
                Vec3[] entityOffsets = extension.getQuadLeashOffsets();
                Vec3[] holderOffsets = ((LeashExtension) leashHolder).getQuadLeashHolderOffsets();

                for (int i = 0; i < leashCount; i++) {
                    LeashState leashState = this.leashStates.get(i);
                    leashState.offset = entityOffsets[i].yRot(-entityRotation);
                    leashState.start = entity.getPosition(partialTicks).add(leashState.offset);
                    leashState.end = holderPosition.add(holderOffsets[i].yRot(-holderRotation));
                    leashState.startBlockLight = entityBlockLight;
                    leashState.endBlockLight = holderBlockLight;
                    leashState.startSkyLight = entitySkyLight;
                    leashState.endSkyLight = holderSkyLight;
                    leashState.slack = false;
                }
            } else {
                Vec3 rotatedOffset = leashOffset.yRot(-entityRotation);
                LeashState leashState = this.leashStates.getFirst();
                leashState.offset = rotatedOffset;
                leashState.start = entity.getPosition(partialTicks).add(rotatedOffset);
                leashState.end = leashHolder.getRopeHoldPosition(partialTicks);
                leashState.startBlockLight = entityBlockLight;
                leashState.endBlockLight = holderBlockLight;
                leashState.startSkyLight = entitySkyLight;
                leashState.endSkyLight = holderSkyLight;
            }
        } else {
            this.leashStates = null;
        }
    }

    private int getBlockLightLevel(Entity entity, BlockPos pos) {
        return ((EntityRendererAccessor) this.dispatcher.getRenderer(entity)).callGetBlockLightLevel(entity, pos);
    }
}
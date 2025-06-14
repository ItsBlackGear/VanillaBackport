package com.blackgear.vanillabackport.core.mixin.leashable.client;

import com.blackgear.vanillabackport.common.api.leash.LeashState;
import com.blackgear.vanillabackport.common.api.leash.Leashable;
import com.blackgear.vanillabackport.core.mixin.access.EntityRendererAccessor;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.LightLayer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;

@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin<T extends Entity> {
    @Shadow protected abstract int getBlockLightLevel(T entity, BlockPos pos);

    @Shadow @Final protected EntityRenderDispatcher entityRenderDispatcher;
    @Unique @Nullable private List<LeashState> leashStates;

    @Inject(method = "render", at = @At("TAIL"))
    private void renderAdditional(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (entity instanceof Leashable) {
            this.setupLeashRendering(entity, partialTick);

            if (this.leashStates != null) {
                for (LeashState state : this.leashStates) {
                    this.renderLeash(poseStack, buffer, state);
                }
            }
        }
    }

    @Inject(method = "shouldRender(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/culling/Frustum;DDD)Z", at = @At("TAIL"), cancellable = true)
    private void shouldRenderLeash(T entity, Frustum camera, double camX, double camY, double camZ, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue() && entity instanceof Leashable leashable) {
            AABB aabb = entity.getBoundingBoxForCulling().inflate(0.5);
            if (aabb.hasNaN() || aabb.getSize() == 0.0) {
                aabb = new AABB(entity.getX() - 2.0, entity.getY() - 2.0, entity.getZ() - 2.0,entity.getX() + 2.0, entity.getY() + 2.0, entity.getZ() + 2.0);
            }

            if (camera.isVisible(aabb)) {
                cir.setReturnValue(true);
            } else {
                Entity leashHolder = leashable.getLeashHolder();
                if (leashHolder != null) {
                    AABB aabb2 = leashHolder.getBoundingBoxForCulling();
                    cir.setReturnValue(camera.isVisible(aabb2) || camera.isVisible(aabb.minmax(aabb2)));
                }
            }
        }
    }

    @Unique
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

    @Unique
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

        vertices.vertex(matrices, posX - offsetZ, posY + thickness2, posZ + offsetX).color(red, green, blue, 1.0f).uv2(packedLight).endVertex();
        vertices.vertex(matrices, posX + offsetZ, posY + thickness1 - thickness2, posZ - offsetX).color(red, green, blue, 1.0f).uv2(packedLight).endVertex();
    }

    @Unique
    private void setupLeashRendering(T entity, float partialTicks) {
        if (entity instanceof Leashable leashable) {
            Entity leashHolder = leashable.getLeashHolder();

            if (leashHolder != null) {
                float entityRotation = Leashable.getPreciseBodyRotation(entity, partialTicks) * ((float) Math.PI / 180);
                Vec3 leashOffset = entity.getLeashOffset(partialTicks);

                BlockPos entityPos = BlockPos.containing(entity.getEyePosition(partialTicks));
                BlockPos holderPos = BlockPos.containing(leashHolder.getEyePosition(partialTicks));
                int entityBlockLight = this.getBlockLightLevel(entity, entityPos);
                int holderBlockLight = ((EntityRendererAccessor) this.entityRenderDispatcher.getRenderer(leashHolder)).callGetBlockLightLevel(leashHolder, holderPos);
                int entitySkyLight = entity.level().getBrightness(LightLayer.SKY, entityPos);
                int holderSkyLight = entity.level().getBrightness(LightLayer.SKY, holderPos);

                boolean handleHolderQuadLeash = leashHolder instanceof Leashable ext && ext.supportQuadLeashAsHolder();
                boolean handleQuadLeash = leashable.supportQuadLeash();
                boolean useQuadLeash = handleHolderQuadLeash && handleQuadLeash;
                int leashCount = useQuadLeash ? 4 : 1;

                if (this.leashStates == null || this.leashStates.size() != leashCount) {
                    this.leashStates = new ArrayList<>(leashCount);
                    for (int i = 0; i < leashCount; i++) {
                        this.leashStates.add(new LeashState());
                    }
                }

                if (useQuadLeash) {
                    float holderRotation = Leashable.getPreciseBodyRotation(leashHolder, partialTicks) * ((float) Math.PI / 180);
                    Vec3 holderPosition = leashHolder.getPosition(partialTicks);
                    Vec3[] entityOffsets = leashable.getQuadLeashOffsets();
                    Vec3[] holderOffsets = ((Leashable) leashHolder).getQuadLeashHolderOffsets();

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
                    LeashState leashState = this.leashStates.get(0);
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
        } else {
            this.leashStates = null;
        }
    }
}
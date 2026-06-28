package com.blackgear.vanillabackport.core.mixin.client.leash_behavior;

import com.blackgear.vanillabackport.common.api.modules.leash_behavior.LeashFeatureRenderer;
import com.blackgear.vanillabackport.common.api.modules.leash_behavior.LeashableCallback;
import com.blackgear.vanillabackport.core.ModChecker;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {
    @Unique private LeashFeatureRenderer<T> leashRenderer;

    @Shadow @Final protected EntityRenderDispatcher entityRenderDispatcher;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void vb$init(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.leashRenderer = new LeashFeatureRenderer<>(this.entityRenderDispatcher);
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void renderAdditional(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        if (!ModChecker.SABLE)
            this.leashRenderer.render(entity, partialTick, poseStack, buffer);
    }

    @Inject(method = "renderLeash", at = @At("HEAD"), cancellable = true)
    private <E extends Entity & Leashable> void vb$cancelVanillaLeash(E entity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, Entity leashHolder, CallbackInfo ci) {
        if (!ModChecker.SABLE && ((LeashableCallback) entity).vb$supportsQuadLeash())
            ci.cancel();
    }

    @Inject(method = "shouldRender", at = @At("TAIL"), cancellable = true)
    private void vb$shouldRender(T entity, Frustum camera, double camX, double camY, double camZ, CallbackInfoReturnable<Boolean> cir) {
        if (!ModChecker.SABLE)
            cir.setReturnValue(this.leashRenderer.shouldRender(entity, camera, cir.getReturnValue()));
    }
}
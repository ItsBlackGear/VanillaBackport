package com.blackgear.vanillabackport.core.mixin.leash;

import com.blackgear.vanillabackport.common.api.leash.LeashFeatureRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
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
    @Unique private LeashFeatureRenderer<T> leashFeatureRenderer;

    @Shadow @Final protected EntityRenderDispatcher entityRenderDispatcher;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void vb$init(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.leashFeatureRenderer = new LeashFeatureRenderer<>(this.entityRenderDispatcher);
    }

    @Inject(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;renderLeash(Lnet/minecraft/world/entity/Entity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/Entity;)V"
        ),
        cancellable = true
    )
    private void vb$renderLeash(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, CallbackInfo ci) {
        ci.cancel();
        this.leashFeatureRenderer.render(entity, partialTick, poseStack, bufferSource);
    }

    @Inject(method = "shouldRender", at = @At("TAIL"), cancellable = true)
    private void vb$shouldRender(T entity, Frustum camera, double camX, double camY, double camZ, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this.leashFeatureRenderer.shouldRender(entity, camera, cir.getReturnValue()));
    }
}
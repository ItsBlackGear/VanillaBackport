package com.blackgear.vanillabackport.core.mixin.leashable.client;

import com.blackgear.vanillabackport.common.api.leash.LeashRenderer;
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
public abstract class EntityRendererMixin<T extends Entity> {
    @Unique private LeashRenderer<T> leashRenderer;

    @Shadow @Final protected EntityRenderDispatcher entityRenderDispatcher;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void vb$init(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.leashRenderer = new LeashRenderer<>(this.entityRenderDispatcher);
    }

    @Inject(method = "render", at = @At("HEAD"))
    private void renderAdditional(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        this.leashRenderer.render(entity, partialTick, poseStack, buffer);
    }

    @Inject(method = "shouldRender(Lnet/minecraft/world/entity/Entity;Lnet/minecraft/client/renderer/culling/Frustum;DDD)Z", at = @At("TAIL"), cancellable = true)
    private void shouldRenderLeash(T entity, Frustum camera, double camX, double camY, double camZ, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(this.leashRenderer.shouldRender(entity, camera, cir.getReturnValue()));
    }
}
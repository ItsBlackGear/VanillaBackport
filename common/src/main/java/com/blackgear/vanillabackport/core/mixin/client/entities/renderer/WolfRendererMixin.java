package com.blackgear.vanillabackport.core.mixin.client.entities.renderer;

import com.blackgear.vanillabackport.client.level.entities.layer.WolfArmorLayer;
import com.blackgear.vanillabackport.client.level.entities.renderer.ageable.AbstractAgeableRenderer;
import com.blackgear.vanillabackport.client.level.entities.renderer.ageable.WolfAgeableRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.WolfModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WolfRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Wolf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Supplier;

@Mixin(WolfRenderer.class)
public abstract class WolfRendererMixin extends MobRendererMixin<Wolf, WolfModel<Wolf>> {
    @Unique private Supplier<WolfAgeableRenderer> renderer;

    public WolfRendererMixin(EntityRendererProvider.Context context, WolfModel<Wolf> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void vb$addLayer(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.addLayer(new WolfArmorLayer(this, context.getModelSet()));
        this.renderer = AbstractAgeableRenderer.create(context, WolfAgeableRenderer::new);
    }

    @Inject(
        method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Wolf;)Lnet/minecraft/resources/ResourceLocation;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void vb$getTextureLocation(Wolf entity, CallbackInfoReturnable<ResourceLocation> cir) {
        this.renderer.get().getTexture(entity).ifPresent(cir::setReturnValue);
    }

    @Inject(
        method = "render(Lnet/minecraft/world/entity/animal/Wolf;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
        at = @At("HEAD")
    )
    public void render(Wolf entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo ci) {
        this.model = this.renderer.get().getModel(entity).orElseGet(() -> this.defaultModel);
    }
}
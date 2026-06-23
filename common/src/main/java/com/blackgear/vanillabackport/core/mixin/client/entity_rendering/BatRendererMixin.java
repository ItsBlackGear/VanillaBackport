package com.blackgear.vanillabackport.core.mixin.client.entity_rendering;

import com.blackgear.vanillabackport.client.api.modules.mob_variants.RenderConditions;
import com.blackgear.vanillabackport.client.api.modules.mob_variants.SpecialMobRenderer;
import com.blackgear.vanillabackport.client.api.modules.mob_variants.BatSpecialRenderer;
import com.blackgear.vanillabackport.core.mixin.client.extension.MobRendererMixin;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.BatModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.BatRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ambient.Bat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BatRenderer.class)
public abstract class BatRendererMixin extends MobRendererMixin<Bat, BatModel> {
    @Unique private SpecialMobRenderer<Bat, BatModel> renderer;

    public BatRendererMixin(EntityRendererProvider.Context context, BatModel model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void onInit(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.renderer = SpecialMobRenderer.create(context, BatSpecialRenderer::new, RenderConditions.BATS);
    }

    @Inject(
        method = "getTextureLocation(Lnet/minecraft/world/entity/ambient/Bat;)Lnet/minecraft/resources/ResourceLocation;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void vb$getTextureLocation(Bat entity, CallbackInfoReturnable<ResourceLocation> cir) {
        this.renderer.getTexture(entity).ifPresent(cir::setReturnValue);
    }

    @Override
    public void render(Bat entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.model = this.renderer.getModel(entity).orElseGet(() -> this.defaultModel);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Inject(
        method = "scale(Lnet/minecraft/world/entity/ambient/Bat;Lcom/mojang/blaze3d/vertex/PoseStack;F)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void scale(Bat entity, PoseStack poseStack, float partialTick, CallbackInfo ci) {
        this.renderer.ifPresent(renderer -> ci.cancel());
    }
}
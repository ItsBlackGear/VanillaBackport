package com.blackgear.vanillabackport.core.mixin.client.entities;

import com.blackgear.vanillabackport.client.level.entities.variant.ChickenVariantRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ChickenRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.animal.Chicken;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChickenRenderer.class)
public abstract class ChickenRendererMixin extends MobRendererMixin<Chicken, ChickenModel<Chicken>> {
    @Unique private ChickenVariantRenderer renderer;

    public ChickenRendererMixin(EntityRendererProvider.Context context, ChickenModel<Chicken> model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Inject(method = "<init>", at = @At("TAIL"))
    private void vb$init(EntityRendererProvider.Context context, CallbackInfo ci) {
        this.renderer = new ChickenVariantRenderer(context, this.defaultModel);
    }

    @Inject(
        method = "getTextureLocation(Lnet/minecraft/world/entity/animal/Chicken;)Lnet/minecraft/resources/ResourceLocation;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void vb$getTextureLocation(Chicken entity, CallbackInfoReturnable<ResourceLocation> cir) {
        if (this.renderer.getTexture(entity) != null) {
            cir.setReturnValue(this.renderer.getTexture(entity));
        }
    }

    @Override
    public void render(Chicken entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (this.renderer.getModel(entity) != null) {
            this.model = this.renderer.getModel(entity);
        }

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
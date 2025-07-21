package com.blackgear.vanillabackport.core.mixin.client.entities;

import com.blackgear.vanillabackport.client.level.entities.BatRendererModule;
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
    @Unique private BatRendererModule<Bat, BatModel> module;

    public BatRendererMixin(EntityRendererProvider.Context context, BatModel model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Unique
    private BatRendererModule<Bat, BatModel> module() {
        if (this.module == null) {
            this.module = new BatRendererModule<>(this.context);
        }

        return this.module;
    }

    @Inject(
        method = "getTextureLocation(Lnet/minecraft/world/entity/ambient/Bat;)Lnet/minecraft/resources/ResourceLocation;",
        at = @At("HEAD"),
        cancellable = true
    )
    private void vb$getTextureLocation(Bat entity, CallbackInfoReturnable<ResourceLocation> cir) {
        if (this.module().enabled()) {
            cir.setReturnValue(this.module().getTexture());
        }
    }

    @Override
    public void render(Bat entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        this.model = this.module().getModel().orElseGet(() -> this.defaultModel);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Inject(
        method = "scale(Lnet/minecraft/world/entity/ambient/Bat;Lcom/mojang/blaze3d/vertex/PoseStack;F)V",
        at = @At("HEAD"),
        cancellable = true
    )
    private void scale(Bat entity, PoseStack poseStack, float partialTick, CallbackInfo ci) {
        if (this.module().enabled()) {
            ci.cancel();
            super.scale(entity, poseStack, partialTick);
        }
    }

    @Inject(
        method = "setupRotations(Lnet/minecraft/world/entity/ambient/Bat;Lcom/mojang/blaze3d/vertex/PoseStack;FFF)V",
        at = @At("HEAD"),
        cancellable = true
    )
    protected void setupRotations(Bat entityLiving, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTicks, CallbackInfo ci) {
        if (this.module().enabled()) {
            ci.cancel();
            super.setupRotations(entityLiving, poseStack, ageInTicks, rotationYaw, partialTicks);
        }
    }
}
package com.blackgear.vanillabackport.core.mixin.client.entity_rendering;

import com.blackgear.vanillabackport.client.api.modules.mob_rendering.DynamicMobRendererRegistry;
import com.blackgear.vanillabackport.client.api.modules.mob_variants.SpecialMobRenderer;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(value = LivingEntityRenderer.class, priority = 500) @SuppressWarnings("unchecked")
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>> {
    @Shadow protected M model;
    @Unique private SpecialMobRenderer<T, M> renderer;
    
    @Inject(method = "<init>", at = @At("TAIL"))
    private void vb$onInit(EntityRendererProvider.Context context, M model, float shadowRadius, CallbackInfo ci) {
        this.renderer = DynamicMobRendererRegistry.getRenderer((LivingEntityRenderer<T, M>) (Object) this, context);
    }
    
    @WrapOperation(
        method = "getRenderType",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;getTextureLocation(Lnet/minecraft/world/entity/Entity;)Lnet/minecraft/resources/ResourceLocation;"
        ))
    private ResourceLocation vb$changeTexture(LivingEntityRenderer<T, M> instance, Entity entity, Operation<ResourceLocation> original) {
        if (this.renderer != null && entity instanceof LivingEntity living) {
            Optional<ResourceLocation> texture = this.renderer.getTexture((T) living);
            if (texture.isPresent()) return texture.get();
        }
        
        return original.call(instance, entity);
    }
    
    @WrapMethod(method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V")
    private void vb$changeModel(T entity, float yaw, float partialTicks, PoseStack pose, MultiBufferSource buffer, int light, Operation<Void> original) {
        if (this.renderer == null) {
            original.call(entity, yaw, partialTicks, pose, buffer, light);
            return;
        }

        this.renderer.getModel(entity).ifPresentOrElse(modded -> {
            M vanilla = this.model;
            this.model = modded;
            try {
                original.call(entity, yaw, partialTicks, pose, buffer, light);
            } finally {
                this.model = vanilla;
            }
        }, () -> original.call(entity, yaw, partialTicks, pose, buffer, light));
    }
    
    @WrapOperation(
        method = "render(Lnet/minecraft/world/entity/LivingEntity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/LivingEntityRenderer;scale(Lnet/minecraft/world/entity/LivingEntity;Lcom/mojang/blaze3d/vertex/PoseStack;F)V"
        ))
    private void vb$suppressScale(LivingEntityRenderer<T, M> instance, T entity, PoseStack pose, float partialTicks, Operation<Void> original) {
        if (this.renderer == null || !this.renderer.suppressScale(entity)) {
            original.call(instance, entity, pose, partialTicks);
        }
    }
}
package com.blackgear.vanillabackport.core.mixin.client.leash_behavior;

import com.blackgear.vanillabackport.core.VanillaBackport;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MobRenderer.class)
public class MobRendererMixin<T extends Mob, M extends EntityModel<T>> {
    @WrapOperation(
        method = "render(Lnet/minecraft/world/entity/Mob;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/entity/MobRenderer;renderLeash(Lnet/minecraft/world/entity/Mob;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/Entity;)V"
        )
    )
    private void onRenderLeash(MobRenderer<T, M> instance, T entityLiving, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, Entity leashHolder, Operation<Void> original) {
        if (!VanillaBackport.COMMON_CONFIG.hasModernLeashes.get()) original.call(instance, entityLiving, partialTicks, poseStack, buffer, leashHolder);
        /* NO-OP */
    }
}
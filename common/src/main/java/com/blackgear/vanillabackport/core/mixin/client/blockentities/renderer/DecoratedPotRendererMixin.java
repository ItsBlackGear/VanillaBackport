package com.blackgear.vanillabackport.core.mixin.client.blockentities.renderer;

import com.blackgear.vanillabackport.common.api.extensions.block.entity.DecoratedPot;
import com.blackgear.vanillabackport.common.level.block_entity.decorated_pot.WobbleStyle;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.DecoratedPotRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.entity.DecoratedPotBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// Special thanks to Echo2craft
@Mixin(DecoratedPotRenderer.class)
public class DecoratedPotRendererMixin {
    @Inject(
        method = "render(Lnet/minecraft/world/level/block/entity/DecoratedPotBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/resources/model/Material;buffer(Lnet/minecraft/client/renderer/MultiBufferSource;Ljava/util/function/Function;)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
        )
    )
    public void vb$render(DecoratedPotBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, CallbackInfo ci) {
        if (blockEntity instanceof DecoratedPot pot) {
            WobbleStyle wobbleStyle = pot.getLastWobbleStyle();
            if (wobbleStyle != null && blockEntity.getLevel() != null) {
                float progress = ((float) (blockEntity.getLevel().getGameTime() - pot.getWobbleStartedAtTick()) + partialTick) / wobbleStyle.duration;
                if (progress >= 0.0F && progress <= 1.0F) {
                    if (wobbleStyle == WobbleStyle.POSITIVE) {
                        float intensity = 0.015625F;
                        float angle = progress * Mth.TWO_PI;
                        float xRot = -1.5F * (Mth.cos(angle) + 0.5F) * Mth.sin(angle / 2.0F);
                        poseStack.rotateAround(Axis.XP.rotation(xRot * intensity), 0.5F, 0.0F, 0.5F);
                        float zRot = Mth.sin(angle);
                        poseStack.rotateAround(Axis.ZP.rotation(zRot * intensity), 0.5F, 0.0F, 0.5F);
                    } else {
                        float yaw = Mth.sin(-progress * 3.0F * Mth.PI) * 0.125F;
                        float damp = 1.0F - progress;
                        poseStack.rotateAround(Axis.YP.rotation(yaw * damp), 0.5F, 0.0F, 0.5F);
                    }
                }
            }
        }
    }
}
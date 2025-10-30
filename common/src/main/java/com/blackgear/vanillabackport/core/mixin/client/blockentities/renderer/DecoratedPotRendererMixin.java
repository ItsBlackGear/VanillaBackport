package com.blackgear.vanillabackport.core.mixin.client.blockentities.renderer;

import com.blackgear.vanillabackport.common.api.block.entity.IDecoratedPotBlockEntityHelper;
import com.blackgear.vanillabackport.common.level.blockentities.decoratedpot.WobbleStyle;
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

@Mixin(DecoratedPotRenderer.class)
public class DecoratedPotRendererMixin {
    @Inject(
            method = "render(Lnet/minecraft/world/level/block/entity/DecoratedPotBlockEntity;FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;II)V",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/resources/model/Material;buffer(Lnet/minecraft/client/renderer/MultiBufferSource;Ljava/util/function/Function;)Lcom/mojang/blaze3d/vertex/VertexConsumer;"
            )
    )
    public void vb$render(DecoratedPotBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay, CallbackInfo ci){
        if(blockEntity instanceof IDecoratedPotBlockEntityHelper advanceBlockEntity){
            WobbleStyle vLastWobbleStyle = advanceBlockEntity.getLastWobbleStyle();
            if (vLastWobbleStyle != null && blockEntity.getLevel() != null) {
                float f = ((float)(blockEntity.getLevel().getGameTime() - advanceBlockEntity.getWobbleStartedAtTick()) + partialTick) / vLastWobbleStyle.duration;
                if (f >= 0.0F && f <= 1.0F) {
                    if (vLastWobbleStyle == WobbleStyle.POSITIVE) {
                        float f1 = 0.015625F;
                        float f2 = f * (float) (Math.PI * 2);
                        float f3 = -1.5F * (Mth.cos(f2) + 0.5F) * Mth.sin(f2 / 2.0F);
                        poseStack.rotateAround(Axis.XP.rotation(f3 * f1), 0.5F, 0.0F, 0.5F);
                        float f4 = Mth.sin(f2);
                        poseStack.rotateAround(Axis.ZP.rotation(f4 * f1), 0.5F, 0.0F, 0.5F);
                    } else {
                        float f5 = Mth.sin(-f * 3.0F * (float) Math.PI) * 0.125F;
                        float f6 = 1.0F - f;
                        poseStack.rotateAround(Axis.YP.rotation(f5 * f6), 0.5F, 0.0F, 0.5F);
                    }
                }
            }
        }
    }
}

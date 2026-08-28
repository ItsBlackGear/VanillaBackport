package com.blackgear.vanillabackport.core.mixin.client.spear_rendering;

import com.blackgear.vanillabackport.common.api.extensions.entity.arms.ItemUseAnimations;
import com.blackgear.vanillabackport.common.api.extensions.entity.spear.PlayerSpearHandler;
import com.blackgear.vanillabackport.common.api.extensions.entity.spear.MobSpearHandler;
import com.blackgear.vanillabackport.common.level.components.SwingAnimation;
import com.blackgear.vanillabackport.common.level.items.spear.SpearAnimations;
import com.blackgear.vanillabackport.common.level.components.SwingAnimationType;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
    @Unique private boolean vb$firstPersonAttack = false;
    
    @Inject(
        method = "renderArmWithItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;getUseAnimation()Lnet/minecraft/world/item/UseAnim;",
            ordinal = 0
        )
    )
    private void vb$spearChargeAnimation(
        AbstractClientPlayer player, float partialTicks, float pitch, InteractionHand hand, float swingProgress,
        ItemStack stack, float equippedProgress, PoseStack poseStack, MultiBufferSource buffer, int combinedLight,
        CallbackInfo ci,
        @Local(ordinal = 0) HumanoidArm arm,
        @Local(ordinal = 1) int invert
    ) {
        if (stack.getUseAnimation() == ItemUseAnimations.REAL_SPEAR.get()) {
            poseStack.translate((float) invert * 0.56F, -0.52F, -0.72F);
            float timeHeld = (float) stack.getUseDuration(player) - ((float) player.getUseItemRemainingTicks() - partialTicks + 1.0F);
            SpearAnimations.firstPersonUse(((MobSpearHandler) player).vb$getTicksSinceLastKineticHitFeedback(partialTicks), poseStack, timeHeld, arm, stack);
        }
    }
    
    @Inject(
        method = "renderArmWithItem",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V",
            ordinal = 12,
            shift = At.Shift.BEFORE
        )
    )
    private void vb$spearStabCondition(
        AbstractClientPlayer player, float partialTicks, float pitch, InteractionHand hand, float swingProgress,
        ItemStack stack, float equippedProgress, PoseStack poseStack, MultiBufferSource buffer, int combinedLight,
        CallbackInfo ci
    ) {
        SwingAnimationType type = SwingAnimation.get(stack).type();
        if (type == SwingAnimationType.STAB) {
            this.vb$firstPersonAttack = true;
        }
    }

    @WrapOperation(
        method = "renderArmWithItem",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V",
            ordinal = 12
        )
    )
    private void vb$spearSwingTranslation(PoseStack instance, float x, float y, float z, Operation<Void> original) {
        if (!this.vb$firstPersonAttack) {
            original.call(instance, x, y, z);
        }
    }
    
    @Inject(
        method = "renderArmWithItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;applyItemArmTransform(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/HumanoidArm;F)V",
            ordinal = 8,
            shift = At.Shift.AFTER
        )
    )
    private void vb$spearStabAnimation(
        AbstractClientPlayer player, float partialTicks, float pitch, InteractionHand hand, float swingProgress,
        ItemStack stack, float equippedProgress, PoseStack poseStack, MultiBufferSource buffer, int combinedLight,
        CallbackInfo ci,
        @Local(ordinal = 1) int invert
    ) {
        if (this.vb$firstPersonAttack) {
            SpearAnimations.firstPersonAttack(swingProgress, poseStack, invert);
        }
    }

    @WrapOperation(
        method = "renderArmWithItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;applyItemArmAttackTransform(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/HumanoidArm;F)V",
            ordinal = 1
        )
    )
    private void vb$spearAttackTransform(ItemInHandRenderer instance, PoseStack poseStack, HumanoidArm arm, float swingProgress, Operation<Void> original) {
        if (!this.vb$firstPersonAttack) {
            original.call(instance, poseStack, arm, swingProgress);
        }
    }
    
    @Inject(
        method = "renderArmWithItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;applyItemArmAttackTransform(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/HumanoidArm;F)V",
            ordinal = 1,
            shift = At.Shift.AFTER
        )
    )
    private void vb$spearAttackState(
        AbstractClientPlayer player,
        float partialTicks,
        float pitch,
        InteractionHand hand,
        float swingProgress,
        ItemStack stack,
        float equippedProgress,
        PoseStack poseStack,
        MultiBufferSource buffer,
        int combinedLight,
        CallbackInfo ci
    ) {
        this.vb$firstPersonAttack = false;
    }

    @WrapOperation(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/player/LocalPlayer;getAttackStrengthScale(F)F"
        )
    )
    private float vb$itemSwapScale(LocalPlayer player, float partialTicks, Operation<Float> original) {
        return ((PlayerSpearHandler) player).vb$getItemSwapScale(partialTicks);
    }
}
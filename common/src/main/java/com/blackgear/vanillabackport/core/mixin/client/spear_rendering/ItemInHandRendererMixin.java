package com.blackgear.vanillabackport.core.mixin.client.spear_rendering;

import com.blackgear.vanillabackport.common.api.extensions.entity.arms.ItemUseAnimations;
import com.blackgear.vanillabackport.common.api.extensions.entity.spear.PlayerSpearHandler;
import com.blackgear.vanillabackport.common.api.extensions.entity.spear.MobSpearHandler;
import com.blackgear.vanillabackport.common.level.item.spear.SpearAnimations;
import com.blackgear.vanillabackport.common.level.item.spear.SwingAnimation;
import com.blackgear.vanillabackport.common.level.item.spear.SwingAnimationType;
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
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandRenderer.class)
public abstract class ItemInHandRendererMixin {
    @Shadow public abstract void applyItemArmTransform(PoseStack poseStack, HumanoidArm hand, float equippedProgress);
    @Shadow public abstract void applyItemArmAttackTransform(PoseStack poseStack, HumanoidArm hand, float swingProgress);
    @Unique private boolean vb$performStabAnimation;

    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/player/LocalPlayer;getAttackStrengthScale(F)F"))
    private float vb$attackAnimation(LocalPlayer player, float scale) {
        return ((PlayerSpearHandler) player).getItemSwapScale(scale);
    }

    @Inject(
        method = "renderArmWithItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;getUseAnimation()Lnet/minecraft/world/item/UseAnim;",
            ordinal = 0
        )
    )
    private void vb$useSpearFirstPerson(
        AbstractClientPlayer player,
        float partial,
        float pitch,
        InteractionHand hand,
        float swingProgress,
        ItemStack stack,
        float equippedProgress,
        PoseStack pose,
        MultiBufferSource buffer,
        int combinedLight,
        CallbackInfo ci,
        @Local(ordinal = 0) HumanoidArm arm,
        @Local(ordinal = 1) int invert
    ) {
        if (stack.getUseAnimation() == ItemUseAnimations.REAL_SPEAR.get()) {
            pose.translate(invert * 0.56F, -0.52F, -0.72F);
            float timeHeld = stack.getUseDuration() - ((float) player.getUseItemRemainingTicks() - partial + 1.0F);
            SpearAnimations.firstPersonUse(((MobSpearHandler) player).getTicksSinceLastKineticHitFeedback(partial), pose, timeHeld, arm, stack);
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
    private void vb$handleSwingTranslation(
        PoseStack pose, float x, float y, float z,
        Operation<Void> original,
        @Local(ordinal = 0) HumanoidArm arm,
        @Local(ordinal = 1) int invert,
        @Local(argsOnly = true, ordinal = 2) float swingProgress,
        @Local(argsOnly = true, ordinal = 3) float equippedProgress
    ) {
        if (this.vb$performStabAnimation) {
            this.applyItemArmTransform(pose, arm, equippedProgress);
            SpearAnimations.firstPersonAttack(swingProgress, pose, invert);
        } else {
            original.call(pose, x, y, z);
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
    private void vb$detectStabAnimation(
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
        SwingAnimation animation = SwingAnimation.getSwingAnimation(stack);
        if (animation == null) return;
        
        this.vb$performStabAnimation = animation.type() == SwingAnimationType.STAB;
    }

    @Redirect(
        method = "renderArmWithItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;applyItemArmTransform(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/HumanoidArm;F)V",
            ordinal = 8
        )
    )
    private void vb$skipArmTransformWhenStab(ItemInHandRenderer renderer, PoseStack pose, HumanoidArm arm, float equippedProgress) {
        if (!this.vb$performStabAnimation) {
            this.applyItemArmTransform(pose, arm, equippedProgress);
        }
    }

    @Redirect(
        method = "renderArmWithItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;applyItemArmAttackTransform(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/world/entity/HumanoidArm;F)V",
            ordinal = 1
        )
    )
    private void vb$skipAttackTransformWhenStab(ItemInHandRenderer renderer, PoseStack pose, HumanoidArm arm, float swingProgress) {
        if (!this.vb$performStabAnimation) {
            this.applyItemArmAttackTransform(pose, arm, swingProgress);
        }
        
        this.vb$performStabAnimation = false;
    }
}
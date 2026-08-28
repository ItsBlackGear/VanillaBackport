package com.blackgear.vanillabackport.core.mixin.client.spear_rendering;

import com.blackgear.vanillabackport.common.api.extensions.entity.arms.ArmPoses;
import com.blackgear.vanillabackport.common.level.items.spear.SpearAnimations;
import com.blackgear.vanillabackport.common.level.items.spear.SwingAnimation;
import com.blackgear.vanillabackport.common.level.items.spear.SwingAnimationType;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(HumanoidModel.class)
public abstract class HumanoidModelMixin<T extends LivingEntity> extends AgeableListModel<T> {
    @Shadow public HumanoidModel.ArmPose rightArmPose;
    @Shadow public HumanoidModel.ArmPose leftArmPose;

    @Shadow @Final public ModelPart rightArm;
    @Shadow @Final public ModelPart leftArm;
    @Shadow @Final public ModelPart head;
    @Shadow @Final public ModelPart body;

    @Unique
    private HumanoidModel.ArmPose vb$getArmPose(T entity, HumanoidArm arm) {
        ItemStack stack = this.vb$getItemHeldByArm(entity, arm);

        if (stack.is(ModItemTags.SPEARS)) {
            boolean isUsing = entity.isUsingItem();
            InteractionHand hand = entity.getUsedItemHand();

            boolean matchesArm = (arm == entity.getMainArm() && hand == InteractionHand.MAIN_HAND)
                || (arm != entity.getMainArm() && hand == InteractionHand.OFF_HAND);

            if (isUsing && matchesArm) {
                return ArmPoses.SPEAR.get();
            }
        }

        return HumanoidModel.ArmPose.EMPTY;
    }

    @Inject(method = "setupAnim(Lnet/minecraft/world/entity/LivingEntity;FFFFF)V", at = @At("HEAD"))
    private void vb$setupAnimPoses(T entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, CallbackInfo ci) {
        HumanoidModel.ArmPose rightPose = this.vb$getArmPose(entity, HumanoidArm.RIGHT);
        if (rightPose == ArmPoses.SPEAR.get()) {
            this.rightArmPose = rightPose;
        }

        HumanoidModel.ArmPose leftPose = this.vb$getArmPose(entity, HumanoidArm.LEFT);
        if (leftPose == ArmPoses.SPEAR.get()) {
            this.leftArmPose = leftPose;
        }
    }

    @Inject(method = "poseRightArm", at = @At("HEAD"), cancellable = true)
    private void vb$poseRightArm(T entity, CallbackInfo ci) {
        if (this.rightArmPose == ArmPoses.SPEAR.get()) {
            SpearAnimations.thirdPersonHandUse(
                this.rightArm,
                this.head,
                true,
                this.vb$getItemHeldByArm(entity, HumanoidArm.RIGHT),
                entity
            );
            ci.cancel();
        }
    }

    @Inject(method = "poseLeftArm", at = @At("HEAD"), cancellable = true)
    private void vb$poseLeftArm(T entity, CallbackInfo ci) {
        if (this.leftArmPose == ArmPoses.SPEAR.get()) {
            SpearAnimations.thirdPersonHandUse(
                this.leftArm,
                this.head,
                false,
                this.vb$getItemHeldByArm(entity, HumanoidArm.LEFT),
                entity
            );
            ci.cancel();
        }
    }

    @SuppressWarnings("unchecked")
    @Inject(method = "setupAttackAnimation", at = @At("HEAD"), cancellable = true)
    private void vb$attackAnimation(T entity, float ageInTicks, CallbackInfo ci) {
        if (this.attackTime > 0.0F) {
            ItemStack heldItem = entity.getMainHandItem();
            SwingAnimation animation = SwingAnimation.getSwingAnimation(heldItem);

            if (!heldItem.isEmpty() && animation != null && animation.type() == SwingAnimationType.STAB) {
                HumanoidArm attackArm = entity.swingingArm == InteractionHand.MAIN_HAND
                    ? entity.getMainArm()
                    : entity.getMainArm().getOpposite();
                
                float bodyRotation = Mth.sin(Mth.sqrt(this.attackTime) * Mth.TWO_PI) * 0.2F;
                if (attackArm == HumanoidArm.LEFT) {
                    bodyRotation *= -1.0F;
                }
                
                this.body.yRot = bodyRotation;
                this.rightArm.yRot += bodyRotation;
                this.leftArm.yRot += bodyRotation;
                this.leftArm.xRot += bodyRotation;

                SpearAnimations.thirdPersonAttackHand((HumanoidModel<T>)(Object)this, entity);
                ci.cancel();
            }
        }
    }

    @Unique
    private ItemStack vb$getItemHeldByArm(T entity, HumanoidArm arm) {
        return entity.getMainArm() == arm ? entity.getMainHandItem() : entity.getOffhandItem();
    }
}
package com.blackgear.vanillabackport.core.mixin.client.spears;

import com.blackgear.vanillabackport.common.api.extensions.entity.spear.ArmPoses;
import com.blackgear.vanillabackport.common.level.components.SwingAnimation;
import com.blackgear.vanillabackport.common.level.components.SwingAnimationType;
import com.blackgear.vanillabackport.common.level.item.spear.SpearAnimations;
import com.blackgear.vanillabackport.common.level.item.spear.SpearItem;
import com.blackgear.vanillabackport.common.registries.items.ModDataComponents;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import net.minecraft.client.model.AgeableListModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
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

    @Unique
    private HumanoidModel.ArmPose getArmPose(T entity, HumanoidArm arm) {
        ItemStack stack = this.getItemHeldByArm(entity, arm);

        if (stack.is(ModItemTags.SPEARS)) {
            boolean isUsing = entity.isUsingItem();
            InteractionHand hand = entity.getUsedItemHand();

            boolean matchesArm =
                (arm == entity.getMainArm() && hand == InteractionHand.MAIN_HAND) ||
                    (arm != entity.getMainArm() && hand == InteractionHand.OFF_HAND);

            if (isUsing && matchesArm) {
                return ArmPoses.SPEAR.get();
            }
        }

        return HumanoidModel.ArmPose.EMPTY;
    }

    @Inject(method = "prepareMobModel(Lnet/minecraft/world/entity/LivingEntity;FFF)V", at = @At("TAIL"))
    private void vb$setupAnim(
        T entity,
        float limbSwing,
        float limbSwingAmount,
        float partialTick,
        CallbackInfo ci
    ) {
        HumanoidArm mainArm = entity.getMainArm();

        HumanoidModel.ArmPose mainPose = this.getArmPose(entity, mainArm);
        HumanoidModel.ArmPose offPose = this.getArmPose(entity, mainArm.getOpposite());

        if (mainPose == ArmPoses.SPEAR.get()) {
            if (mainArm == HumanoidArm.RIGHT) {
                this.rightArmPose = mainPose;
                this.leftArmPose = HumanoidModel.ArmPose.EMPTY;
            } else {
                this.leftArmPose = mainPose;
                this.rightArmPose = HumanoidModel.ArmPose.EMPTY;
            }
        } else if (offPose == ArmPoses.SPEAR.get()) {
            if (mainArm == HumanoidArm.RIGHT) {
                this.leftArmPose = offPose;
            } else {
                this.rightArmPose = offPose;
            }
        }
    }

    @Inject(method = "poseRightArm", at = @At("HEAD"), cancellable = true)
    private void vb$poseRightArm(T entity, CallbackInfo ci) {
        if (this.rightArmPose == ArmPoses.SPEAR.get()) {
            SpearAnimations.thirdPersonHandUse(
                this.rightArm,
                this.head,
                true,
                this.getItemHeldByArm(entity, HumanoidArm.RIGHT),
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
                this.getItemHeldByArm(entity, HumanoidArm.LEFT),
                entity
            );
            ci.cancel();
        }
    }

    @Inject(method = "setupAttackAnimation", at = @At("HEAD"), cancellable = true)
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void vb$attackAnimation(T entity, float ageInTicks, CallbackInfo ci) {
        ItemStack heldItem = entity.getMainHandItem();

        if (!heldItem.isEmpty()) {
            SwingAnimation anim = SpearItem.getSwingAnimation(heldItem);
            
            if (anim.type() == SwingAnimationType.STAB) {
                SpearAnimations.thirdPersonAttackHand((HumanoidModel)(Object)this, entity);
                ci.cancel();
            }
        }
    }

    @Unique
    private ItemStack getItemHeldByArm(T entity, HumanoidArm arm) {
        return entity.getMainArm() == arm ? entity.getMainHandItem() : entity.getOffhandItem();
    }
}
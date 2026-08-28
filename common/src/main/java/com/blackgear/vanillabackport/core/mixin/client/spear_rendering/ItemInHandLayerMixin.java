package com.blackgear.vanillabackport.core.mixin.client.spear_rendering;

import com.blackgear.vanillabackport.common.api.extensions.entity.arms.ArmPoses;
import com.blackgear.vanillabackport.common.level.components.SwingAnimation;
import com.blackgear.vanillabackport.common.level.components.SwingAnimationType;
import com.blackgear.vanillabackport.common.level.items.spear.SpearAnimations;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ArmedModel;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemInHandLayer.class)
public abstract class ItemInHandLayerMixin<T extends LivingEntity, M extends EntityModel<T> & ArmedModel> extends RenderLayer<T, M> {
    public ItemInHandLayerMixin(RenderLayerParent<T, M> renderer) {
        super(renderer);
    }

    @Inject(
        method = "renderArmWithItem",
        at = @At(
            value = "INVOKE",
            target = "Lcom/mojang/blaze3d/vertex/PoseStack;translate(FFF)V",
            shift = At.Shift.AFTER
        )
    )
    private void vb$renderArm(
        LivingEntity entity,
        ItemStack stack,
        ItemDisplayContext displayContext,
        HumanoidArm arm,
        PoseStack pose,
        MultiBufferSource buffer,
        int packedLight,
        CallbackInfo ci
    ) {
        float partial = Minecraft.getInstance().getTimer().getGameTimeDeltaPartialTick(true);
        if (entity.getAttackAnim(partial) > 0.0F && entity.getMainArm() == arm && SwingAnimation.get(stack).type() == SwingAnimationType.STAB) {
            SpearAnimations.thirdPersonAttackItem(entity, pose);
        }

        float ticksUsingItem = entity.isUsingItem() && entity.getUsedItemHand() == InteractionHand.MAIN_HAND == (arm == entity.getMainArm()) ? entity.getTicksUsingItem() : 0.0F;
        EntityModel<T> model = this.getParentModel();
        if (ticksUsingItem != 0.0F && model instanceof HumanoidModel<T> parent) {
            HumanoidModel.ArmPose armPose = (arm == HumanoidArm.RIGHT ? parent.rightArmPose : parent.leftArmPose);
            if (armPose == ArmPoses.SPEAR.get()) {
                ArmPoses.SPEAR.animateUseItem(entity, pose, ticksUsingItem, arm, stack, partial);
            }
        }
    }
}
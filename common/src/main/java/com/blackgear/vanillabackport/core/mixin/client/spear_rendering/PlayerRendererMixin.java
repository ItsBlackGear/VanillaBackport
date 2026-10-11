package com.blackgear.vanillabackport.core.mixin.client.spear_rendering;

import com.blackgear.vanillabackport.common.api.extensions.entity.arms.ArmPoses;
import com.blackgear.vanillabackport.common.api.extensions.entity.spear.SpearSwingTracker;
import com.blackgear.vanillabackport.common.level.items.spear.SwingAnimation;
import com.blackgear.vanillabackport.common.level.items.spear.SwingAnimationType;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin {
    @ModifyReturnValue(method = "getArmPose", at = @At("RETURN"))
    private static HumanoidModel.ArmPose vb$getArmPose(HumanoidModel.ArmPose original, AbstractClientPlayer player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);
        HumanoidArm arm = (hand == InteractionHand.MAIN_HAND)
            ? player.getMainArm()
            : player.getMainArm().getOpposite();
        
        if (vb$usesSpearPose(heldItem, arm, player)) {
            return ArmPoses.SPEAR.get();
        }
        
        return original;
    }
    
    @Unique
    private static boolean vb$usesSpearPose(ItemStack item, HumanoidArm arm, LivingEntity entity) {
        if (entity.swinging) {
            if (entity instanceof SpearSwingTracker tracker && !tracker.vb$isAttackSwing()) return false;
            InteractionHand swingingHand = entity.swingingArm;
            HumanoidArm swingingArm = (swingingHand == InteractionHand.MAIN_HAND)
                ? entity.getMainArm()
                : entity.getMainArm().getOpposite();
            
            if (swingingArm == arm) {
                ItemStack swingingItem = entity.getItemInHand(swingingHand);
                SwingAnimation animation = SwingAnimation.get(swingingItem);
                if (animation != null && animation.type() == SwingAnimationType.STAB) {
                    return true;
                }
            }
        }
        
        return item.is(ModItemTags.SPEARS);
    }
}
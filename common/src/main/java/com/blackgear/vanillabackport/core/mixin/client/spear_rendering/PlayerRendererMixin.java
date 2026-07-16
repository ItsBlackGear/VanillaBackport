package com.blackgear.vanillabackport.core.mixin.client.spear_rendering;

import com.blackgear.vanillabackport.common.api.extensions.entity.arms.ArmPoses;
import com.blackgear.vanillabackport.common.api.extensions.entity.arms.ItemUseAnimations;
import com.blackgear.vanillabackport.common.level.components.SwingAnimation;
import com.blackgear.vanillabackport.common.level.components.SwingAnimationType;
import com.blackgear.vanillabackport.common.registries.items.ModDataComponents;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {
    @ModifyReturnValue(method = "getArmPose", at = @At("RETURN"))
    private static HumanoidModel.ArmPose vb$getArmPose(HumanoidModel.ArmPose original, AbstractClientPlayer player, InteractionHand hand) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem.isEmpty()) return original;
        
        
        if (player.getUsedItemHand() == hand && player.getUseItemRemainingTicks() > 0) {
            UseAnim useAnim = heldItem.getUseAnimation();
            if (useAnim == ItemUseAnimations.REAL_SPEAR.get()) {
                return ArmPoses.SPEAR.get();
            }
            
            return original;
        }
        
        SwingAnimation animation = heldItem.get(ModDataComponents.SWING_ANIMATION.get());
        if (animation != null && animation.type() == SwingAnimationType.STAB && player.swinging) {
            return ArmPoses.SPEAR.get();
        }
        
        if (heldItem.is(ModItemTags.SPEARS)) {
            return ArmPoses.SPEAR.get();
        }
        
        return original;
    }
}
package com.blackgear.vanillabackport.core.mixin.client.spears;

import com.blackgear.vanillabackport.common.api.extensions.entity.spear.ArmPoses;
import com.blackgear.vanillabackport.common.api.extensions.entity.spear.ItemUseAnimations;
import com.blackgear.vanillabackport.common.level.components.SwingAnimation;
import com.blackgear.vanillabackport.common.level.components.SwingAnimationType;
import com.blackgear.vanillabackport.common.registries.items.ModDataComponents;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerRenderer.class)
public class PlayerRendererMixin {
    @Inject(method = "getArmPose", at = @At("RETURN"), cancellable = true)
    private static void vb$getArmPose(AbstractClientPlayer player, InteractionHand hand, CallbackInfoReturnable<HumanoidModel.ArmPose> cir) {
        ItemStack heldItem = player.getItemInHand(hand);
        if (player.getUsedItemHand() == hand && player.getUseItemRemainingTicks() > 0) {
            UseAnim useAnim = heldItem.getUseAnimation();
            if (useAnim == ItemUseAnimations.REAL_SPEAR.get()) {
                cir.setReturnValue(ArmPoses.SPEAR.get());
            }
        }

        SwingAnimation animation = heldItem.get(ModDataComponents.SWING_ANIMATION.get());
        if (animation != null && animation.type() == SwingAnimationType.STAB && player.swinging) {
            cir.setReturnValue(ArmPoses.SPEAR.get());
        }

        if (heldItem.is(ModItemTags.SPEARS)) {
            cir.setReturnValue(ArmPoses.SPEAR.get());
        }
    }
}
package com.blackgear.vanillabackport.core.mixin.client.spear_behavior;

import com.blackgear.vanillabackport.common.level.components.UseEffects;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {
    public LocalPlayerMixin(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel, gameProfile);
    }

    @Unique
    private static boolean vb$anyComponentAllowsSprint(ItemStack stack) {
        for (var typed : stack.getComponents()) {
            Object component = typed.value();
            try {
                var method = component.getClass().getMethod("canSprint");
                if (method.getReturnType() == boolean.class && (boolean) method.invoke(component)) {
                    return true;
                }
            } catch (Exception ignored) {
            }
        }
        return false;
    }
    
    @ModifyExpressionValue(
        method = "aiStep",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z",
            ordinal = 0
        )
    )
    private boolean vb$slowDownWhileUsingItem(boolean isUsing) {
        return isUsing && this.vb$isSlowDueToUsingItem();
    }
    
    @WrapOperation(
        method = "canStartSprinting",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z"
        )
    )
    private boolean vb$canStartSprinting(LocalPlayer instance, Operation<Boolean> original) {
        return this.vb$isSlowDueToUsingItem();
    }

    @Unique
    private boolean vb$isSlowDueToUsingItem() {
        if (!this.isUsingItem()) {
            return false;
        }
        if (UseEffects.get(this.useItem).canSprint()) {
            return false;
        }
        return !vb$anyComponentAllowsSprint(this.useItem);
    }
}

package com.blackgear.vanillabackport.core.mixin.common.spear_behavior;

import com.blackgear.vanillabackport.common.level.components.UseEffects;
import com.blackgear.vanillabackport.common.registries.items.ModDataComponents;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {
    @Shadow public abstract boolean isUsingItem();

    public LocalPlayerMixin(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel, gameProfile);
    }

    @ModifyExpressionValue(
        method = "aiStep",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/player/LocalPlayer;canStartSprinting()Z"
        )
    )
    private boolean vb$preventSprinting(boolean original) {
        return original && !this.isSlowDueToUsingItem();
    }

    @ModifyExpressionValue(
        method = "aiStep",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z"
        )
    )
    private boolean vb$slowDownWhileUsingItem(boolean original) {
        return original && this.isSlowDueToUsingItem();
    }

    @ModifyExpressionValue(
        method = "canStartSprinting",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z"
        )
    )
    private boolean vb$canStartSprinting(boolean original) {
        return original && this.isSlowDueToUsingItem();
    }

    @Unique
    private boolean isSlowDueToUsingItem() {
        return this.isUsingItem() && !this.useItem.getOrDefault(ModDataComponents.USE_EFFECTS.get(), UseEffects.DEFAULT).canSprint();
    }
}
package com.blackgear.vanillabackport.core.mixin.client.spear_behavior;

import com.blackgear.vanillabackport.common.level.items.spear.UseEffects;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {
    public LocalPlayerMixin(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel, gameProfile);
    }

    @ModifyExpressionValue(method = "aiStep",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;isSprinting()Z",
                    ordinal = 0
            ))
    private boolean vb$preventSprinting(boolean isSprinting) {
        if (isSprinting && this.vb$isSlowDueToUsingItem()) {
            this.setSprinting(false);
            return false;
        }

        return isSprinting;
    }

    @ModifyExpressionValue(method = "aiStep",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z",
                    ordinal = 0
            ))
    private boolean vb$slowDownWhileUsingItem(boolean isUsing) {
        return isUsing && this.vb$isSlowDueToUsingItem();
    }

    @ModifyExpressionValue(method = "canStartSprinting",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/player/LocalPlayer;isUsingItem()Z"
            ))
    private boolean vb$canStartSprinting(boolean isUsingItem) {
        return isUsingItem && this.vb$isSlowDueToUsingItem();
    }

    @Unique
    private boolean vb$isSlowDueToUsingItem() {
        if (!this.isUsingItem()) {
            return false;
        }
        if (UseEffects.get(this.getUseItem()).canSprint()) {
            return false;
        }
        return !vb$anyNbtTagAllowsSprint(this.getUseItem().getTag());
    }

    @Unique
    private static boolean vb$anyNbtTagAllowsSprint(CompoundTag tag) {
        if (tag == null) {
            return false;
        }
        for (String key : tag.getAllKeys()) {
            if (key.equals("can_sprint") && tag.getBoolean(key)) {
                return true;
            }
            Tag subTag = tag.get(key);
            if (subTag instanceof CompoundTag compound && vb$anyNbtTagAllowsSprint(compound)) {
                return true;
            }
        }
        return false;
    }
}

package com.blackgear.vanillabackport.core.mixin.client.spear_behavior;

import com.blackgear.vanillabackport.common.api.extensions.entity.spear.PlayerSpearHandler;
import com.blackgear.vanillabackport.common.api.extensions.entity.spear.ServerSpearHandler;
import com.blackgear.vanillabackport.common.level.components.AttackRange;
import com.blackgear.vanillabackport.common.level.components.PiercingWeapon;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Minecraft.class)
public abstract class MinecraftMixin {
    @Shadow @Nullable public MultiPlayerGameMode gameMode;
    @Shadow @Nullable public HitResult hitResult;
    @Shadow @Nullable public LocalPlayer player;
    @Shadow @Nullable public ClientLevel level;
    @Shadow protected int missTime;
    
    @Inject(
        method = "startAttack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/item/ItemStack;isItemEnabled(Lnet/minecraft/world/flag/FeatureFlagSet;)Z"
        ),
        cancellable = true
    )
    private void vb$startAttack(CallbackInfoReturnable<Boolean> cir) {
        if (this.player == null || this.level == null || this.gameMode == null) return;
        
        ItemStack stack = this.player.getItemInHand(InteractionHand.MAIN_HAND);
        if (((PlayerSpearHandler) this.player).vb$cannotAttackWithItem(stack, 0)) {
            cir.setReturnValue(false);
            return;
        }
        
        PiercingWeapon weapon = PiercingWeapon.get(stack);
        if (weapon != null) {
            ((ServerSpearHandler) this.gameMode).piercingAttack(weapon);
            this.player.swing(InteractionHand.MAIN_HAND);
            cir.setReturnValue(true);
        }
    }
    
    @WrapOperation(
        method = "startAttack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;attack(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;)V"
        )
    )
    private void vb$applyAttackRange(MultiPlayerGameMode instance, Player player, Entity target, Operation<Void> original) {
        if (this.player != null && this.hitResult != null) {
            ItemStack stack = this.player.getItemInHand(InteractionHand.MAIN_HAND);
            AttackRange range = AttackRange.get(stack);
            
            if (range == null || range.isInRange(this.player, this.hitResult.getLocation())) {
                original.call(instance, player, target);
            }
        } else {
            original.call(instance, player, target);
        }
    }
    
    @Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
    private void vb$continueAttack(boolean down, CallbackInfo ci) {
        if (!down) {
            this.missTime = 0;
        }
        
        if (this.player != null) {
            ItemStack stack = this.player.getItemInHand(InteractionHand.MAIN_HAND);
            if (PiercingWeapon.get(stack) != null) {
                ci.cancel();
            }
        }
    }
}
package com.blackgear.vanillabackport.core.mixin.client.spear_behavior;

import com.blackgear.vanillabackport.common.api.extensions.entity.spear.PlayerSpearHandler;
import com.blackgear.vanillabackport.common.api.extensions.entity.spear.ServerSpearHandler;
import com.blackgear.vanillabackport.common.level.components.AttackRange;
import com.blackgear.vanillabackport.common.level.components.PiercingWeapon;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.MultiPlayerGameMode;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameType;
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
            target = "Lnet/minecraft/client/player/LocalPlayer;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;"
        ),
        cancellable = true
    )
    private void vb$startAttack(CallbackInfoReturnable<Boolean> cir) {
        if (this.player == null || this.level == null || this.gameMode == null) return;
        
        ItemStack stack = this.player.getItemInHand(InteractionHand.MAIN_HAND);
        if (!stack.isItemEnabled(this.level.enabledFeatures())) {
            return;
        }
        
        if (((PlayerSpearHandler) this.player).vb$cannotAttackWithItem(stack, 0)) {
            cir.setReturnValue(false);
            return;
        }
        
        PiercingWeapon weapon = PiercingWeapon.get(stack);
        if (weapon != null && this.gameMode.getPlayerMode() != GameType.SPECTATOR) {
            ((ServerSpearHandler) this.gameMode).piercingAttack(weapon);
            this.player.swing(InteractionHand.MAIN_HAND);
            cir.setReturnValue(true);
        }
    }
    
    @Inject(
        method = "startAttack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/multiplayer/MultiPlayerGameMode;attack(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/entity/Entity;)V"
        ),
        cancellable = true
    )
    private void vb$checkAttackRange(CallbackInfoReturnable<Boolean> cir) {
        if (this.player == null || this.hitResult == null) return;
        
        ItemStack stack = this.player.getItemInHand(InteractionHand.MAIN_HAND);
        AttackRange range = AttackRange.get(stack);
        if (range != null && !range.isInRange(this.player, this.hitResult.getLocation())) {
            cir.setReturnValue(false);
        }
    }
    
    @Inject(
        method = "startAttack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/player/LocalPlayer;swing(Lnet/minecraft/world/InteractionHand;)V"
        ),
        cancellable = true
    )
    private void vb$preventSpectatorSwing(CallbackInfoReturnable<Boolean> cir) {
        if (this.player != null && this.player.isSpectator()) {
            cir.setReturnValue(false);
        }
    }
    
    @Inject(method = "continueAttack", at = @At("HEAD"), cancellable = true)
    private void vb$continueAttack(boolean down, CallbackInfo ci) {
        if (this.player == null) return;
        
        ItemStack stack = this.player.getItemInHand(InteractionHand.MAIN_HAND);
        if (PiercingWeapon.get(stack) != null) {
            if (!down) this.missTime = 0;
            ci.cancel();
        }
    }
}
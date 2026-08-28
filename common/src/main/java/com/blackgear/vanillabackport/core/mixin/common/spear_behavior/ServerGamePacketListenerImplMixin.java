package com.blackgear.vanillabackport.core.mixin.common.spear_behavior;

import com.blackgear.vanillabackport.common.api.extensions.entity.arms.PlayerActions;
import com.blackgear.vanillabackport.common.api.extensions.entity.spear.PlayerSpearHandler;
import com.blackgear.vanillabackport.common.level.items.spear.PiercingWeapon;
import net.minecraft.network.protocol.game.ServerboundPlayerActionPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {
    @Shadow public ServerPlayer player;

    @Inject(method = "handlePlayerAction", at = @At("HEAD"), cancellable = true)
    private void vb$handlePlayerAction(ServerboundPlayerActionPacket packet, CallbackInfo ci) {
        if (packet.getAction() != PlayerActions.STAB.get()) return;
        
        ci.cancel();
        
        this.player.server.execute(() -> {
            this.player.resetLastActionTime();
    
            if (!this.player.isSpectator()) {
                ItemStack stack = this.player.getItemInHand(InteractionHand.MAIN_HAND);
                PiercingWeapon weapon = PiercingWeapon.get(stack);
                if (!((PlayerSpearHandler) this.player).vb$cannotAttackWithItem(stack, 5) && weapon != null) {
                    weapon.attack(this.player, EquipmentSlot.MAINHAND);
                }
            }
        });
    }
}
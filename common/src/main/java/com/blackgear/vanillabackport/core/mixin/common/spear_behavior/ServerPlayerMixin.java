package com.blackgear.vanillabackport.core.mixin.common.spear_behavior;

import com.blackgear.vanillabackport.common.api.extensions.entity.spear.PlayerSpearHandler;
import com.blackgear.vanillabackport.common.registries.items.ModDataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public class ServerPlayerMixin {
    @Inject(method = "attack", at = @At("HEAD"), cancellable = true)
    private void vb$validateAttack(Entity target, CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer)(Object) this;
        ItemStack stack = player.getMainHandItem();
        if (stack.has(ModDataComponents.PIERCING_WEAPON.get()) && ((PlayerSpearHandler) player).cannotAttackWithItem(stack, 5)) {
            ci.cancel();
        }
    }
}
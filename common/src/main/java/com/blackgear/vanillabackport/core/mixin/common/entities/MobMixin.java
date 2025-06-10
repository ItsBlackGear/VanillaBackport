package com.blackgear.vanillabackport.core.mixin.common.entities;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public abstract class MobMixin {
    @Inject(method = "canBeLeashed", at = @At("HEAD"), cancellable = true)
    private void vb$canBeLeashed(Player player, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(!(this instanceof Enemy));
    }
}
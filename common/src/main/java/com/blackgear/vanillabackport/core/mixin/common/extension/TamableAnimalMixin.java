package com.blackgear.vanillabackport.core.mixin.common.extension;

import com.blackgear.vanillabackport.common.api.extensions.access.TamableAnimalAccess;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TamableAnimal.class)
public abstract class TamableAnimalMixin implements TamableAnimalAccess {
    @Inject(method = "setTame", at = @At("TAIL"))
    private void vb$onSetTame(boolean tamed, CallbackInfo ci) {
        this.applyTamingSideEffects();
    }

    @Inject(method = "tame", at = @At("TAIL"))
    private void vb$onTame(Player player, CallbackInfo ci) {
        this.applyTamingSideEffects();
    }
}
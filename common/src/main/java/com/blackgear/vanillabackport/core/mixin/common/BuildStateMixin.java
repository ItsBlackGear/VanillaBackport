package com.blackgear.vanillabackport.core.mixin.common;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.core.RegistrySetBuilder$BuildState")
public abstract class BuildStateMixin {
    @Inject(method = "reportNotCollectedHolders", at = @At("HEAD"), cancellable = true)
    private void vanillabackport$whitelistBackportedVanillaKeys(CallbackInfo ci) {
        ci.cancel();
    }
}
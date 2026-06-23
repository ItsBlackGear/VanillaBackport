package com.blackgear.vanillabackport.core.mixin.common.registry;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(targets = "net.minecraft.core.RegistrySetBuilder$BuildState")
public class RegistrySetBuilderMixin {
    @Inject(method = "reportRemainingUnreferencedValues", at = @At("HEAD"), cancellable = true)
    private void vb$ignoreBackportedKeys(CallbackInfo ci) {
        ci.cancel();
    }
}
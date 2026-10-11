package com.blackgear.vanillabackport.core.mixin.common.registry;

import net.minecraft.core.Holder;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Holder.Reference.class)
public abstract class HolderReferenceMixin<T> {
    @Shadow @Nullable private T value;

    @Inject(method = "bindValue", at = @At("HEAD"), cancellable = true)
    private void vb$allowDuplicateBind(T value, CallbackInfo ci) {
        if (this.value != null) ci.cancel();
    }
}
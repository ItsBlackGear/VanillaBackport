package com.blackgear.vanillabackport.core.mixin.common.registry;

import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

@Mixin(MappedRegistry.class)
public abstract class MappedRegistryMixin<T> {
    @Shadow @Final private Map<ResourceLocation, Holder.Reference<T>> byLocation;
    @Shadow @Nullable
    private Map<T, Holder.Reference<T>> unregisteredIntrusiveHolders;
    
    @Inject(method = "register", at = @At("HEAD"), cancellable = true)
    private void vb$interceptDuplicates(ResourceKey<T> key, T value, Lifecycle lifecycle, CallbackInfoReturnable<Holder.Reference<T>> cir) {
        ResourceLocation location = key.location();
        if ("minecraft".equals(location.getNamespace()) && this.byLocation.containsKey(location)) {
            Holder.Reference<T> existingReference = this.byLocation.get(location);
            
            if (this.unregisteredIntrusiveHolders != null) {
                this.unregisteredIntrusiveHolders.remove(value);
            }
            
            cir.setReturnValue(existingReference);
        }
    }
}
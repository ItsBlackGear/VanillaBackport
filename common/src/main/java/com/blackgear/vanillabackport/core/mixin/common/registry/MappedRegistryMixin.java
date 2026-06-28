package com.blackgear.vanillabackport.core.mixin.common.registry;

import com.blackgear.vanillabackport.core.registries.neo_registries.RegistryInterceptor;
import com.mojang.serialization.Lifecycle;
import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MappedRegistry.class)
public abstract class MappedRegistryMixin<T> {
    @Shadow @Final ResourceKey<? extends Registry<T>> key;
    
    @Inject(method = "register", at = @At("HEAD"))
    private void vb$interceptRegistries(ResourceKey<T> key, T value, Lifecycle lifecycle, CallbackInfoReturnable<Holder.Reference<T>> cir) {
        RegistryInterceptor.onIntercept(this.key, key.location());
    }
}
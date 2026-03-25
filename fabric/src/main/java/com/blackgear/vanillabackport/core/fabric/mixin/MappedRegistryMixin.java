package com.blackgear.vanillabackport.core.fabric.mixin;

import com.bawnorton.mixinsquared.TargetHandler;
import com.blackgear.platform.core.fabric.CoreRegistryImpl;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Set;

@SuppressWarnings({"UnresolvedMixinReference", "InvalidMemberReference", "MixinAnnotationTarget"})
@Mixin(value = MappedRegistry.class, priority = 1500)
public abstract class MappedRegistryMixin<T> {
    @TargetHandler(
        mixin = "net.fabricmc.fabric.mixin.registry.sync.SimpleRegistryMixin",
        name = "onChange"
    )
    @WrapOperation(
        method = "@MixinSquared:Handler",
        at = @At(
            value = "INVOKE",
            target = "Ljava/util/Set;contains(Ljava/lang/Object;)Z",
            remap = false
        )
    )
    private boolean shouldSkipVanillaRegistry(
        Set<String> vanillaNamespaces,
        Object namespace,
        Operation<Boolean> original,
        ResourceKey<T> registryKey
    ) {
        boolean isVanillaNamespace = original.call(vanillaNamespaces, namespace);
        if (isVanillaNamespace && CoreRegistryImpl.REGISTERED_KEYS.contains(registryKey)) {
            return false;
        }

        return isVanillaNamespace;
    }
}
package com.blackgear.vanillabackport.core.mixin.common.access;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Holder.Reference.class)
public interface ReferenceAccessor<T> {
    @Invoker void callBindKey(ResourceKey<T> key);
    
    @Invoker void callBindValue(T value);
}

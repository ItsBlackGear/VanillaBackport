package com.blackgear.vanillabackport.core.registries.neo_registries;

import com.blackgear.platform.core.CoreRegistry;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

public final class VanillaRegistry<T> {
    private final CoreRegistry<T> registry;
    private final ResourceKey<? extends Registry<T>> key;
    
    private VanillaRegistry(ResourceKey<? extends Registry<T>> key) {
        this.key = key;
        this.registry = CoreRegistry.create(key, "minecraft");
    }
    
    public static <T> VanillaRegistry<T> create(ResourceKey<? extends Registry<T>> key) {
        return new VanillaRegistry<>(key);
    }
    
    public <E extends T> FeatureHolder<E> register(String name, Supplier<E> factory) {
        return RegistryInterceptor.register(this.key, name, factory);
    }
    
    public void register() {
        RegistryInterceptor.flush(this.key, this.registry::register);
        this.registry.register();
    }
}
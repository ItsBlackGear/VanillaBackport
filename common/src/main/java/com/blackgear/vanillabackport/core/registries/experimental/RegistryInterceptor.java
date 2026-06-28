package com.blackgear.vanillabackport.core.registries.experimental;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public final class RegistryInterceptor {
    private static final Map<ResourceKey<? extends Registry<?>>, Map<ResourceLocation, Supplier<?>>> PENDING = new ConcurrentHashMap<>();
    
    private RegistryInterceptor() {}

    @SuppressWarnings("unchecked")
    public static <V, T extends V> FeatureHolder<T> register(ResourceKey<? extends Registry<V>> key, String name, Supplier<T> factory) {
        ResourceLocation id = ResourceLocation.withDefaultNamespace(name);
        
        Registry<V> registry = (Registry<V>) BuiltInRegistries.REGISTRY.get(key.location());
        if (registry != null && registry.containsKey(id)) {
            return FeatureHolder.of(() -> (T) registry.get(id));
        }
        
        PENDING.computeIfAbsent(key, k -> new HashMap<>()).put(id, factory);
        
        return FeatureHolder.of(() -> {
            if (registry != null) return (T) registry.get(id);
            throw new IllegalStateException("Registry wasn't ready to evaluate FeatureHolder");
        });
    }
    
    public static void onIntercept(ResourceKey<? extends Registry<?>> key, ResourceLocation id) {
        Map<ResourceLocation, Supplier<?>> map = PENDING.get(key);
        if (map != null) map.remove(id);
    }

    @SuppressWarnings("unchecked")
    public static <V> void flush(ResourceKey<? extends Registry<V>> key, BiConsumer<String, Supplier<V>> registrar) {
        Map<ResourceLocation, Supplier<?>> map = PENDING.remove(key);
        if (map != null) {
            map.forEach((id, factory) -> registrar.accept(id.getPath(), (Supplier<V>) factory));
        }
    }
}
package com.blackgear.vanillabackport.common.api.variant;

import com.blackgear.platform.core.BuiltInCoreRegistry;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;

public interface EnhancedVariants {
    String VARIANT_KEY = "variant";

    static <T> void addVariantSaveData(VariantHolder<T> entity, CompoundTag tag, BuiltInCoreRegistry<T> registry) {
        T variant = entity.vb$getVariant();
        if (variant != null) {
            ResourceLocation key = registry.getKey(variant);
            if (key != null) {
                tag.putString(VARIANT_KEY, key.toString());
            }
        }
    }

    static <T> void readVariantSaveData(VariantHolder<T> entity, CompoundTag tag, BuiltInCoreRegistry<T> registry) {
        if (tag.contains(VARIANT_KEY)) {
            T variant = registry.get(ResourceLocation.tryParse(tag.getString(VARIANT_KEY)));
            if (variant != null) {
                entity.vb$setVariant(variant);
            }
        }
    }

    // Check if a variant from the vanilla registry exists in the data driven registry
    static <T, V> boolean hasVariantInclusive(Registry<T> vanillaRegistry, T variant, BuiltInCoreRegistry<V> dataRegistry) {
        ResourceLocation id = vanillaRegistry.getKey(variant);
        return id != null && dataRegistry.entries().containsKey(id);
    }

    // Check if a variant from the data driven registry does NOT exist in the vanilla registry
    static <T, V> boolean hasVariantExclusive(BuiltInCoreRegistry<T> dataRegistry, T variant, Registry<V> vanillaRegistry) {
        ResourceLocation id = dataRegistry.getKey(variant);
        return id != null && !vanillaRegistry.containsKey(id);
    }
}
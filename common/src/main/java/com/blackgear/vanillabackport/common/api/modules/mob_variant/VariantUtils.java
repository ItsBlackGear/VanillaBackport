package com.blackgear.vanillabackport.common.api.modules.mob_variant;

import com.blackgear.platform.core.BuiltInCoreRegistry;
import com.blackgear.platform.core.api.RegistryKey;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.PriorityProvider;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.SpawnContext;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ServerLevelAccessor;

import java.util.Optional;

public class VariantUtils {
    public static final String VARIANT_KEY = "variant";

    public static <T> Optional<T> getOrDefault(BuiltInCoreRegistry<T> registry, String id) {
        T variant = registry.get(ResourceLocation.tryParse(id));
        return variant == null ? Optional.empty() : Optional.of(variant);
    }

    public static <T> T getDefault(BuiltInCoreRegistry<T> registry, RegistryKey<T> key) {
        return registry.getOrThrow(key);
    }

    public static <T> T getVariant(BuiltInCoreRegistry<T> registry, String id) {
        return registry.get(ResourceLocation.tryParse(id));
    }

    public static <T> String getID(BuiltInCoreRegistry<T> registry, T value) {
        return registry.getKey(value).toString();
    }

    public static <T> String getDefaultID(BuiltInCoreRegistry<T> registry, RegistryKey<T> value) {
        return getID(registry, getDefault(registry, value));
    }

    public static <T> boolean matches(BuiltInCoreRegistry<T> registry, T variant, RegistryKey<T> value) {
        return variant == registry.get(value);
    }

    public static <T> void addVariantSaveData(VariantDataHolder<T> entity, CompoundTag tag, BuiltInCoreRegistry<T> registry) {
        entity.getVariantData().ifPresent(variant -> {
            ResourceLocation key = registry.getKey(variant);
            if (key != null) tag.putString(VARIANT_KEY, key.toString());
        });
    }

    public static <T> void readVariantSaveData(VariantDataHolder<T> entity, CompoundTag tag, BuiltInCoreRegistry<T> registry) {
        if (tag.contains(VARIANT_KEY)) {
            T variant = registry.get(ResourceLocation.tryParse(tag.getString(VARIANT_KEY)));
            if (variant != null) entity.setVariantData(variant);
        }
    }

    public static <T extends PriorityProvider<SpawnContext, ?>> Optional<T> selectVariantToSpawn(SpawnContext context, BuiltInCoreRegistry<T> registry, VariantSpawner spawner) {
        if (!spawner.apply()) return Optional.empty();

        ServerLevelAccessor level = context.level();
        return PriorityProvider.pick(registry.values().stream(), entry -> entry, level.getRandom(), context);
    }

    public static <T extends PriorityProvider<SpawnContext, ?>> Optional<T> selectVariantToSpawn(SpawnContext context, BuiltInCoreRegistry<T> registry) {
        return selectVariantToSpawn(context, registry, VariantSpawner.DEFAULT);
    }
}
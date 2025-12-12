package com.blackgear.vanillabackport.common.api.variant;

import com.blackgear.platform.core.BuiltInCoreRegistry;
import com.blackgear.vanillabackport.common.api.variant.spawn.PriorityProvider;
import com.blackgear.vanillabackport.common.api.variant.spawn.SpawnContext;
import com.blackgear.vanillabackport.core.ModChecker;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ServerLevelAccessor;

import java.util.Optional;

public class VariantUtils {
    public static final String VARIANT_KEY = "variant";

    public static <T> T getOrDefault(BuiltInCoreRegistry<T> registry, String id, ResourceKey<T> fallback) {
        T variant = registry.get(ResourceLocation.tryParse(id));
        return variant == null ? getDefault(registry, fallback) : variant;
    }

    public static <T> T getDefault(BuiltInCoreRegistry<T> registry, ResourceKey<T> key) {
        return registry.getOrThrow(key);
    }

    public static <T> T getVariant(BuiltInCoreRegistry<T> registry, String id) {
        return registry.get(ResourceLocation.tryParse(id));
    }

    public static <T> String getID(BuiltInCoreRegistry<T> registry, T value) {
        return registry.getKey(value).toString();
    }

    public static <T> String getDefaultID(BuiltInCoreRegistry<T> registry, ResourceKey<T> value) {
        return getID(registry, getDefault(registry, value));
    }

    public static <T> boolean matches(BuiltInCoreRegistry<T> registry, T variant, ResourceKey<T> value) {
        return variant == registry.get(value);
    }

    public static <T> void addVariantSaveData(VariantHolder<T> entity, CompoundTag tag, BuiltInCoreRegistry<T> registry) {
        tag.putString(VARIANT_KEY, registry.getKey(entity.vb$getVariant()).toString());
    }

    public static <T> void readVariantSaveData(VariantHolder<T> entity, CompoundTag tag, BuiltInCoreRegistry<T> registry) {
        T variant = registry.get(ResourceLocation.tryParse(tag.getString(VARIANT_KEY)));
        if (variant != null) entity.vb$setVariant(variant);
    }

    public static <T extends PriorityProvider<SpawnContext, ?>> Optional<T> selectVariantToSpawn(SpawnContext context, BuiltInCoreRegistry<T> registry) {
        ServerLevelAccessor level = context.level();
        return PriorityProvider.pick(registry.values().stream(), entry -> entry, level.getRandom(), context);
    }

    public static <T extends PriorityProvider<SpawnContext, ?>> Optional<T> selectFarmAnimalVariantToSpawn(SpawnContext context, BuiltInCoreRegistry<T> registry, ResourceKey<T> fallback) {
        if (!VanillaBackport.COMMON_CONFIG.hasFarmAnimalVariants.get() || ModChecker.MIXED_LITTER_LOADED) return Optional.of(registry.getOrThrow(fallback));

        ServerLevelAccessor level = context.level();
        return PriorityProvider.pick(registry.values().stream(), entry -> entry, level.getRandom(), context);
    }
}
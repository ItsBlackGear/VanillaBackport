package com.blackgear.vanillabackport.common.api.variant;

import com.blackgear.platform.core.BuiltInCoreRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ServerLevelAccessor;

import java.util.Optional;

public class VariantUtils {
    public static final String VARIANT_KEY = "variant";

    public static <T> T getDefault(BuiltInCoreRegistry<T> registry, ResourceKey<T> key) {
        return registry.getOrThrow(key);
    }

    public static <T> void addVariantSaveData(VariantHolder<T> entity, CompoundTag tag, BuiltInCoreRegistry<T> registry) {
        tag.putString(VARIANT_KEY, registry.getKey(entity.getVariant()).toString());
    }

    public static <T> void readVariantSaveData(VariantHolder<T> entity, CompoundTag tag, BuiltInCoreRegistry<T> registry) {
        T variant = registry.get(ResourceLocation.tryParse(tag.getString(VARIANT_KEY)));
        if (variant != null) entity.setVariant(variant);
    }

    public static <T extends PriorityProvider<SpawnContext, ?>> Optional<T> selectVariantToSpawn(SpawnContext context, BuiltInCoreRegistry<T> registry) {
        ServerLevelAccessor level = context.level();
        return PriorityProvider.pick(registry.values().stream(), entry -> entry, level.getRandom(), context);
    }
}
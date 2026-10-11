package com.blackgear.vanillabackport.common.api.modules.mob_variant;

import com.blackgear.platform.common.data.entity.SyncedDataHolder;
import com.blackgear.platform.common.data.entity.SyncedDataKey;
import com.blackgear.platform.core.BuiltInCoreRegistry;
import com.blackgear.platform.core.api.RegistryKey;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.PriorityProvider;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.SpawnContext;
import com.blackgear.vanillabackport.common.api.modules.sound_variant.SoundVariantHolder;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ServerLevelAccessor;

import java.util.Optional;

public class VariantUtils {
    public static final String VARIANT_KEY = "variant";
    public static final String SOUND_VARIANT_KEY = "sound_variant";
    
    public static <T> void setVariant(Entity holder, T variant, BuiltInCoreRegistry<T> registry, SyncedDataKey<RegistryKey<T>> key) {
       SyncedDataHolder.get(holder).set(key, registry.getRegistryKey(variant));
    }
    
    public static <T> T getVariant(Entity holder, BuiltInCoreRegistry<T> registry, SyncedDataKey<RegistryKey<T>> key) {
        return registry.get(SyncedDataHolder.get(holder).get(key));
    }
    
    public static <T> Optional<T> getOrDefault(BuiltInCoreRegistry<T> registry, ResourceLocation id) {
        return Optional.ofNullable(registry.get(id));
    }
    
    public static <T> Optional<T> getOrDefault(BuiltInCoreRegistry<T> registry, String id) {
        ResourceLocation location = ResourceLocation.tryParse(id);
        return location == null ? Optional.empty() : getOrDefault(registry, location);
    }

    public static <T> T getDefault(BuiltInCoreRegistry<T> registry, RegistryKey<T> key) {
        return registry.getOrThrow(key);
    }

    public static <T> T getVariant(BuiltInCoreRegistry<T> registry, String id) {
        return registry.get(ResourceLocation.tryParse(id));
    }
    
    public static <T> ResourceLocation getKey(BuiltInCoreRegistry<T> registry, T value) {
        return registry.getKey(value);
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

    public static <T> void addSoundVariantSaveData(SoundVariantHolder<T> holder, CompoundTag tag, BuiltInCoreRegistry<T> registry) {
        tag.putString(SOUND_VARIANT_KEY, registry.getKey(holder.vb$getSoundVariant()).toString());
    }
    
    public static <T> void readVariantSaveData(VariantDataHolder<T> entity, CompoundTag tag, BuiltInCoreRegistry<T> registry) {
        if (tag.contains(VARIANT_KEY)) {
            T variant = registry.get(ResourceLocation.tryParse(tag.getString(VARIANT_KEY)));
            if (variant != null) entity.setVariantData(variant);
        }
    }
    
    public static <T> void readSoundVariantSaveData(SoundVariantHolder<T> holder, CompoundTag tag, BuiltInCoreRegistry<T> registry) {
        if (tag.contains(SOUND_VARIANT_KEY)) {
            T variant = registry.get(ResourceLocation.tryParse(tag.getString(SOUND_VARIANT_KEY)));
            if (variant != null) holder.vb$setSoundVariant(variant);
        }
    }

    public static <T extends PriorityProvider<SpawnContext, ?>> Optional<T> selectVariantToSpawn(SpawnContext context, BuiltInCoreRegistry<T> registry, VariantSpawner spawner) {
        if (!spawner.apply()) return Optional.empty();
        
        ServerLevelAccessor level = context.level();
        RandomSource random = level.getRandom();
        
        if (random.nextFloat() >= VanillaBackport.COMMON_CONFIG.mobVariantSpawnChance.get()) {
            return Optional.empty();
        }
        
        return PriorityProvider.pick(registry.values().stream(), entry -> entry, random, context);
    }

    public static <T extends PriorityProvider<SpawnContext, ?>> Optional<T> selectVariantToSpawn(SpawnContext context, BuiltInCoreRegistry<T> registry) {
        return selectVariantToSpawn(context, registry, VariantSpawner.DEFAULT);
    }
}
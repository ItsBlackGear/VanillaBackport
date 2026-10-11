package com.blackgear.vanillabackport.common.api.variant;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

/**
 * added for retro-compatibility with MC Earth Mobs Addon, highly recommended for the dev to migrate!
 */
@Deprecated(forRemoval = true, since = "1.3") @SuppressWarnings("unchecked")
public interface VariantDataHolder<T> extends com.blackgear.vanillabackport.common.api.modules.mob_variant.VariantDataHolder<T> {
    static <T> VariantDataHolder<T> getHolder(LivingEntity entity) {
        return entity instanceof VariantDataHolder<?> ? (VariantDataHolder<T>) entity : null;
    }
    
    static <T> VariantDataHolder<T> getHolder(Entity entity) {
        return entity instanceof VariantDataHolder<?> ? (VariantDataHolder<T>) entity : null;
    }
}
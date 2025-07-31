package com.blackgear.vanillabackport.common.api.variant;

import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;

@SuppressWarnings("unchecked")
public interface VariantHolder<T> {
    static <T> VariantHolder<T> getVariantHolder(LivingEntity entity) {
        return entity instanceof VariantHolder<?> ? (VariantHolder<T>) entity : null;
    }

    static void trySetOffspringVariant(LivingEntity child, LivingEntity father, LivingEntity mother) {
        if (!VanillaBackport.COMMON_CONFIG.hasFarmAnimalVariants.get()) return;

        RandomSource random = father.getRandom();
        getVariantHolder(child).setVariant(random.nextBoolean() ? getVariantHolder(father).getVariant() : getVariantHolder(mother).getVariant());
    }

    T getVariant();

    void setVariant(T variant);
}
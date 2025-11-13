package com.blackgear.vanillabackport.common.api.variant;

import com.blackgear.vanillabackport.core.ModChecker;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;

@SuppressWarnings("unchecked")
public interface VariantHolder<T> {
    static <T> VariantHolder<T> vb$getVariantHolder(LivingEntity entity) {
        return entity instanceof VariantHolder<?> ? (VariantHolder<T>) entity : null;
    }

    static void vb$trySetOffspringVariant(LivingEntity child, LivingEntity father, LivingEntity mother) {
        if (!VanillaBackport.COMMON_CONFIG.hasFarmAnimalVariants.get() || ModChecker.MIXED_LITTER_LOADED) return;

        RandomSource random = father.getRandom();
        vb$getVariantHolder(child).vb$setVariant(random.nextBoolean() ? vb$getVariantHolder(father).vb$getVariant() : vb$getVariantHolder(mother).vb$getVariant());
    }

    T vb$getVariant();

    void vb$setVariant(T variant);
}
package com.blackgear.vanillabackport.common.level.entities;

import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;

public interface AnimalVariantHolder {
    static AnimalVariantHolder testFor(LivingEntity entity) {
        if (entity instanceof AnimalVariantHolder holder) {
            return holder;
        }

        return null;
    }

    static void trySetOffspringVariant(LivingEntity child, LivingEntity parentA, LivingEntity parentB) {
        if (!VanillaBackport.COMMON_CONFIG.hasFarmAnimalVariants.get()) return;

        RandomSource random = parentA.getRandom();
        testFor(child).setVariant(random.nextBoolean() ? testFor(parentA).getVariant() : testFor(parentB).getVariant());
    }

    AnimalVariant getVariant();

    void setVariant(AnimalVariant variant);
}
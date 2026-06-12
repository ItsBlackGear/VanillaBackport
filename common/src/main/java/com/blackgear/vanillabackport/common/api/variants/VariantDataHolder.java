package com.blackgear.vanillabackport.common.api.variants;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.VariantHolder;

import java.util.Optional;

@SuppressWarnings("unchecked")
public interface VariantDataHolder<T> {
    static <T> VariantDataHolder<T> getHolder(LivingEntity entity) {
        return entity instanceof VariantDataHolder<?> ? (VariantDataHolder<T>) entity : null;
    }

    static <A, B> void trySetOffspringVariant(LivingEntity child, LivingEntity father, LivingEntity mother) {
        RandomSource random = child.getRandom();
        Optional<B> dataVariant;

        Optional<B> fromFather = VariantDataHolder.<B>getHolder(father).getVariantData();
        Optional<B> fromMother = VariantDataHolder.<B>getHolder(mother).getVariantData();

        if (fromFather.isPresent() && fromMother.isPresent()) {
            dataVariant = random.nextBoolean() ? fromFather : fromMother;
        } else if (random.nextBoolean()) {
            dataVariant = fromFather.or(() -> fromMother);
        } else {
            dataVariant = Optional.empty();
        }

        if (dataVariant.isPresent()) {
            dataVariant.ifPresent(variant -> getHolder(child).setVariantData(variant));
        } else {
            A variant = random.nextBoolean() ? ((VariantHolder<A>) father).getVariant() : ((VariantHolder<A>) mother).getVariant();
            ((VariantHolder<A>) child).setVariant(variant);
        }
    }

    Optional<T> getVariantData();

    void setVariantData(T variant);
}
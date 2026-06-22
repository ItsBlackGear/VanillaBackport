package com.blackgear.vanillabackport.common.api.modules.mob_variant;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.VariantHolder;

import java.util.Optional;

@SuppressWarnings("unchecked")
public interface VariantDataHolder<T> {
    static <T> VariantDataHolder<T> getHolder(Entity entity) {
        return entity instanceof VariantDataHolder<?> ? (VariantDataHolder<T>) entity : null;
    }

    static <A, B> void trySetOffspringVariant(LivingEntity child, LivingEntity father, LivingEntity mother) {
        RandomSource random = child.getRandom();
        Optional<B> dataVariant;

        Optional<B> fromFather = VariantDataHolder.<B>getHolder(father).getVariantData();
        Optional<B> fromMother = VariantDataHolder.<B>getHolder(mother).getVariantData();

        if (fromFather.isPresent() && fromMother.isPresent()) { // if both parents have data variants, pick one randomly
            dataVariant = random.nextBoolean() ? fromFather : fromMother;
        } else if (random.nextBoolean()) { // else, try to get one from either parent
            dataVariant = fromFather.or(() -> fromMother);
        } else { // else, no data variant available
            dataVariant = Optional.empty();
        }

        if (dataVariant.isPresent()) { // if it's present, then apply
            dataVariant.ifPresent(variant -> getHolder(child).setVariantData(variant));
        } else { // if no data variant is present, fallback to vanilla behavior
            A variant = random.nextBoolean() ? ((VariantHolder<A>) father).getVariant() : ((VariantHolder<A>) mother).getVariant();
            ((VariantHolder<A>) child).setVariant(variant);
        }
    }

    Optional<T> getVariantData();

    void setVariantData(T variant);
}
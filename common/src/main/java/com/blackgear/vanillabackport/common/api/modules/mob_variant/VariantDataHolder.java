package com.blackgear.vanillabackport.common.api.modules.mob_variant;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.VariantHolder;

import java.util.Optional;

@SuppressWarnings("unchecked")
public interface VariantDataHolder<T> {
    static <T> Optional<VariantDataHolder<T>> getHolder(LivingEntity entity) {
        return entity instanceof VariantDataHolder<?> ? Optional.of((VariantDataHolder<T>) entity) : Optional.empty();
    }
    
    void setVariantData(T variant);
    
    Optional<T> getVariantData();
    
    static <T, V> void trySetOffspringVariant(LivingEntity child, LivingEntity father, LivingEntity mother) {
        Optional<T> fatherData = VariantDataHolder.<T>getHolder(father).flatMap(VariantDataHolder::getVariantData);
        Optional<T> motherData = VariantDataHolder.<T>getHolder(mother).flatMap(VariantDataHolder::getVariantData);
        
        RandomSource random = child.getRandom();
        Optional<T> heritableData;
        if (fatherData.isPresent() && motherData.isPresent()) {
            heritableData = random.nextBoolean() ? fatherData : motherData;
        } else if (fatherData.isPresent()) {
            heritableData = random.nextBoolean() ? fatherData : Optional.empty();
        } else if (motherData.isPresent()) {
            heritableData = random.nextBoolean() ? motherData : Optional.empty();
        } else {
            heritableData = Optional.empty();
        }
        
        if (heritableData.isPresent()) {
            VariantDataHolder.<T>getHolder(child).ifPresent(h -> h.setVariantData(heritableData.get()));
            return;
        }
        
        if (father instanceof VariantHolder<?> && mother instanceof VariantHolder<?>) {
            V variant = random.nextBoolean() ? ((VariantHolder<V>) father).getVariant() : ((VariantHolder<V>) mother).getVariant();
            if (child instanceof VariantHolder<?>) ((VariantHolder<V>) child).setVariant(variant);
        }
    }
}
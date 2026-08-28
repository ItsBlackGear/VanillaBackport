package com.blackgear.vanillabackport.common.api.modules.sound_variant;

import com.blackgear.platform.core.BuiltInCoreRegistry;
import net.minecraft.world.entity.LivingEntity;

public interface SoundVariantHolder<T> {
    @SuppressWarnings("unchecked")
    static <T> SoundVariantHolder<T> of(LivingEntity entity) {
        if (entity instanceof SoundVariantHolder<?> holder) {
            return (SoundVariantHolder<T>) holder;
        }
        
        return null;
    }
    
    T vb$getSoundVariant();
    
    void vb$setSoundVariant(T variant);
    
    static <T> void trySetOffspringVariant(LivingEntity child, LivingEntity father, BuiltInCoreRegistry<T> registry) {
        SoundVariantHolder.of(child).vb$setSoundVariant(registry.getRandomElement(father.getRandom()));
    }
}
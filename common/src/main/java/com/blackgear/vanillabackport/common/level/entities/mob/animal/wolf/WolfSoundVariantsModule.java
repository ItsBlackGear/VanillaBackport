package com.blackgear.vanillabackport.common.level.entities.mob.animal.wolf;

import com.blackgear.vanillabackport.common.api.modules.sound_variant.SoundVariantHolder;
import com.blackgear.vanillabackport.common.api.modules.sound_variant.WolfSoundVariant;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.animal.Wolf;

public class WolfSoundVariantsModule {
    public static <T extends Wolf & SoundVariantHolder<WolfSoundVariant>> SoundEvent getAmbientSound(T wolf) {
        if (!VanillaBackport.COMMON_CONFIG.hasWolfSoundVariants.get()) return null;
        if (wolf.isAngry()) {
            return wolf.vb$getSoundVariant().growlSound().value();
        } else if (wolf.getRandom().nextInt(3) == 0) {
            return wolf.isTame() && wolf.getHealth() < 20.0F
                ? wolf.vb$getSoundVariant().whineSound().value()
                : wolf.vb$getSoundVariant().pantSound().value();
        } else {
            return wolf.vb$getSoundVariant().ambientSound().value();
        }
    }
    
    public static <T extends Wolf & SoundVariantHolder<WolfSoundVariant>> SoundEvent getHurtSound(T wolf) {
        if (!VanillaBackport.COMMON_CONFIG.hasWolfSoundVariants.get()) return null;
        return wolf.vb$getSoundVariant().hurtSound().value();
    }
    
    public static <T extends Wolf & SoundVariantHolder<WolfSoundVariant>> SoundEvent getDeathSound(T wolf) {
        if (!VanillaBackport.COMMON_CONFIG.hasWolfSoundVariants.get()) return null;
        return wolf.vb$getSoundVariant().deathSound().value();
    }
}
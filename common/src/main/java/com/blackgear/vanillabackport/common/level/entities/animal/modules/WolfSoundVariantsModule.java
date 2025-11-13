package com.blackgear.vanillabackport.common.level.entities.animal.modules;

import com.blackgear.vanillabackport.common.api.wolf.WolfSoundVariantHolder;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.animal.Wolf;

public class WolfSoundVariantsModule {
    public static <T extends Wolf & WolfSoundVariantHolder> SoundEvent getAmbientSound(T wolf) {
        if (!VanillaBackport.COMMON_CONFIG.hasWolfSoundVariants.get()) return null;

        if (wolf.isAngry()) {
            return wolf.getSoundVariant().growlSound().value();
        } else if (wolf.getRandom().nextInt(3) == 0) {
            return wolf.isTame() && wolf.getHealth() < 20.0F
                ? wolf.getSoundVariant().whineSound().value()
                : wolf.getSoundVariant().pantSound().value();
        } else {
            return wolf.getSoundVariant().ambientSound().value();
        }
    }

    public static <T extends Wolf & WolfSoundVariantHolder> SoundEvent getHurtSound(T wolf) {
        if (!VanillaBackport.COMMON_CONFIG.hasWolfSoundVariants.get()) return null;

        return wolf.getSoundVariant().hurtSound().value();
    }

    public static <T extends Wolf & WolfSoundVariantHolder> SoundEvent getDeathSound(T wolf) {
        if (!VanillaBackport.COMMON_CONFIG.hasWolfSoundVariants.get()) return null;

        return wolf.getSoundVariant().deathSound().value();
    }
}
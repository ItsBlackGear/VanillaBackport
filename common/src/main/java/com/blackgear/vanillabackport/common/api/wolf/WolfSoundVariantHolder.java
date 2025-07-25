package com.blackgear.vanillabackport.common.api.wolf;

import net.minecraft.world.entity.animal.Wolf;

public interface WolfSoundVariantHolder {
    static WolfSoundVariantHolder of(Wolf wolf) {
        if (wolf instanceof WolfSoundVariantHolder holder) {
            return holder;
        }

        return null;
    }

    WolfSoundVariant getSoundVariant();

    void setSoundVariant(WolfSoundVariant variant);
}

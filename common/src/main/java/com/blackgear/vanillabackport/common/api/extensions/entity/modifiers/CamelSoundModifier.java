package com.blackgear.vanillabackport.common.api.extensions.entity.modifiers;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.animal.camel.Camel;
import org.jetbrains.annotations.Nullable;

public interface CamelSoundModifier {
    static @Nullable CamelSoundModifier of(Camel camel) {
        return camel instanceof CamelSoundModifier extension ? extension : null;
    }
    
    default SoundEvent getDashingSound() {
        return SoundEvents.CAMEL_DASH;
    }
    
    default SoundEvent getDashReadySound() {
        return SoundEvents.CAMEL_DASH_READY;
    }
    
    default SoundEvent getStandUpSound() {
        return SoundEvents.CAMEL_STAND;
    }
    
    default SoundEvent getSitDownSound() {
        return SoundEvents.CAMEL_SIT;
    }
}
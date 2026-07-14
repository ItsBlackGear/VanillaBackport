package com.blackgear.vanillabackport.common.api.extensions;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public interface SoundExtensions {
    default void playSound(
        @Nullable Entity except,
        double x,
        double y,
        double z,
        Holder<SoundEvent> sound,
        SoundSource source,
        float volume,
        float pitch
    ) {}
    
    default void playSeededSound(
        @Nullable Entity except,
        double x,
        double y,
        double z,
        Holder<SoundEvent> sound,
        SoundSource source,
        float volume,
        float pitch,
        long seed
    ) {}
}
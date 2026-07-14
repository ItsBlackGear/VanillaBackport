package com.blackgear.vanillabackport.common.api.extensions;

import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public interface SoundExtension {
    default void playSound(
        @Nullable Entity except,
        double x,
        double y,
        double z,
        Holder<SoundEvent> sound,
        SoundSource source,
        float volume,
        float pitch
    ) { /* NO-OP */ }
    
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
    ) { /* NO-OP */ }
    
    default void playLocalSound(
        Entity entity,
        SoundEvent sound,
        SoundSource source,
        float volume,
        float pitch
    ) { /* NO-OP */ }
}
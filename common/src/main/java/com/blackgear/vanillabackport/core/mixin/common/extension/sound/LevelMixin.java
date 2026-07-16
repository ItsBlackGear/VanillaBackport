package com.blackgear.vanillabackport.core.mixin.common.extension.sound;

import com.blackgear.vanillabackport.common.api.extensions.SoundExtensions;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;

import java.util.concurrent.ThreadLocalRandom;

@Mixin(Level.class)
public class LevelMixin implements SoundExtensions {
    @Override
    public void playSound(@Nullable Entity except, double x, double y, double z, Holder<SoundEvent> sound, SoundSource source, float volume, float pitch) {
        this.playSeededSound(except, x, y, z, sound, source, volume, pitch, ThreadLocalRandom.current().nextLong());
    }
}
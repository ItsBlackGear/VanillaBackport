package com.blackgear.vanillabackport.core.mixin.client.sound_extension;

import com.blackgear.vanillabackport.common.api.extensions.SoundExtensions;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin implements SoundExtensions {
    @Shadow protected abstract void playSound(double x, double y, double z, SoundEvent soundEvent, SoundSource source, float volume, float pitch, boolean distanceDelay, long seed);
    @Shadow @Final private Minecraft minecraft;
    
    @Override
    public void playSeededSound(@Nullable Entity except, double x, double y, double z, Holder<SoundEvent> sound, SoundSource source, float volume, float pitch, long seed) {
        if (except == this.minecraft.player) {
            this.playSound(x, y, z, sound.value(), source, volume, pitch, false, seed);
        }
    }
}
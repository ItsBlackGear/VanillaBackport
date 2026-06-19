package com.blackgear.vanillabackport.core.mixin.client.music_frequency;

import com.blackgear.vanillabackport.client.experiment.music_frequency.MusicFrequency;
import com.blackgear.vanillabackport.client.experiment.music_frequency.MusicFrequencyAccess;
import com.blackgear.vanillabackport.client.experiment.options.OptionsAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.sounds.Music;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MusicManager.class)
public abstract class MusicManagerMixin implements MusicFrequencyAccess {
    @Shadow @Final private Minecraft minecraft;
    @Shadow @Final private RandomSource random;
    @Shadow private int nextSongDelay;
    @Shadow @Nullable private SoundInstance currentMusic;
    @Unique private MusicFrequency gameMusicFrequency = MusicFrequency.DEFAULT;
    
    @Inject(method = "tick", at = @At("HEAD"))
    private void vb$ensureOptionsLoaded(CallbackInfo ci) {
        if (this.gameMusicFrequency == MusicFrequency.DEFAULT) {
            OptionsAccess access = (OptionsAccess) this.minecraft.options;
            if (access.musicFrequency() != null) {
                this.gameMusicFrequency = access.musicFrequency().get();
            }
        }
    }
    
    @Inject(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/sounds/SoundManager;isActive(Lnet/minecraft/client/resources/sounds/SoundInstance;)Z",
            shift = At.Shift.AFTER
        )
    )
    private void onSongFinishedDelay(CallbackInfo ci) {
        if (!this.minecraft.getSoundManager().isActive(this.currentMusic)) {
            Music situationalMusic = this.minecraft.getSituationalMusic();
            int customDelay = this.gameMusicFrequency.getNextSongDelay(situationalMusic, this.random);
            this.nextSongDelay = Math.min(this.nextSongDelay, customDelay);
        }
    }
    
    @Inject(method = "stopPlaying()V", at = @At("TAIL"))
    private void vb$b(CallbackInfo ci) {
        this.nextSongDelay = this.gameMusicFrequency.getNextSongDelay(this.minecraft.getSituationalMusic(), this.random) + 100;
    }
    
    @Override
    public void setMinutesBetweenSongs(MusicFrequency frequency) {
        this.gameMusicFrequency = frequency;
        this.nextSongDelay = this.gameMusicFrequency.getNextSongDelay(this.minecraft.getSituationalMusic(), this.random);
    }
}
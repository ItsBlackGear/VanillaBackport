package com.blackgear.vanillabackport.core.mixin.client.music_fade;

import com.blackgear.vanillabackport.client.api.modules.music_fade.MusicFadeManager;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.sounds.Music;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MusicManager.class)
public abstract class MusicManagerMixin {
    @Shadow @Nullable private SoundInstance currentMusic;
    @Shadow private int nextSongDelay;

    @Unique private MusicFadeManager vb$fadeManager;

    @Unique
    private MusicFadeManager vb$fadeManager() {
        if (this.vb$fadeManager == null) {
            this.vb$fadeManager = new MusicFadeManager((MusicManager) (Object) this);
        }
        return this.vb$fadeManager;
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void vb$onTick(CallbackInfo ci) {
        MusicFadeManager fade = this.vb$fadeManager();

        if (fade.tickFade(this.currentMusic)) {
            ci.cancel();
            return;
        }

        if (this.currentMusic == null && fade.isSilentBiome()) {
            this.nextSongDelay = Math.max(this.nextSongDelay, 100);
            ci.cancel();
        }
    }

    @Inject(method = "startPlaying", at = @At("TAIL"))
    private void vb$onStartPlaying(Music selector, CallbackInfo ci) {
        this.vb$fadeManager().onStartPlaying();
    }
}
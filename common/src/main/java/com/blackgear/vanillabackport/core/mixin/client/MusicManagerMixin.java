package com.blackgear.vanillabackport.core.mixin.client;

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

import java.util.function.Supplier;

@Mixin(MusicManager.class)
public abstract class MusicManagerMixin {
    @Shadow
    private @Nullable SoundInstance currentMusic;

    @Unique
    private Supplier<MusicFadeManager> fadeManager;

    @Unique
    private Supplier<MusicFadeManager> getFadeManager() {
        if (this.fadeManager == null) {
            this.fadeManager = () -> new MusicFadeManager((MusicManager) (Object) this);
        }
        return this.fadeManager;
    }

    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    private void onTick(CallbackInfo ci) {
        if (this.getFadeManager().get().onTick(this.currentMusic)) {
            ci.cancel();
        }
    }

    @Inject(method = "startPlaying", at = @At("HEAD"), cancellable = true)
    private void preventPlayingInPaleGarden(Music selector, CallbackInfo ci) {
        if (this.getFadeManager().get().preventPlayingInPaleGarden()) {
            ci.cancel();
        }
    }

    @Inject(
        method = "startPlaying",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/sounds/SoundManager;play(Lnet/minecraft/client/resources/sounds/SoundInstance;)V"
        )
    )
    private void updateVolume(Music selector, CallbackInfo ci) {
        this.getFadeManager().get().updateVolume(this.currentMusic);
    }

    @Inject(method = "startPlaying", at = @At("TAIL"))
    private void onStartPlaying(Music selector, CallbackInfo ci) {
        this.getFadeManager().get().onStartPlaying();
    }
}
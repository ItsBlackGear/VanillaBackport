package com.blackgear.vanillabackport.core.mixin.client;

import com.blackgear.vanillabackport.common.registries.ModBiomes;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.mixin.access.SoundEngineAccessor;
import com.blackgear.vanillabackport.core.mixin.access.SoundManagerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.core.Holder;
import net.minecraft.sounds.Music;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MusicManager.class)
public abstract class MusicManagerMixin {
    @Shadow public abstract void stopPlaying();
    @Shadow private @Nullable SoundInstance currentMusic;
    @Shadow @Final private Minecraft minecraft;

    @Unique private static final float FADE_OUT_FACTOR = 0.97F;
    @Unique private static final float FADE_IN_MIN_STEP = 5.0E-4F;
    @Unique private static final float FADE_IN_MAX_STEP = 0.005F;
    @Unique private static final float VOLUME_THRESHOLD = 1.0E-4F;

    @Unique private float currentGain = 1.0F;

    @Inject(
        method = "tick",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onTick(CallbackInfo ci) {
        if (!VanillaBackport.CLIENT_CONFIG.fadeMusicOnPaleGarden.get() || this.currentMusic == null) {
            return;
        }

        float targetVolume = this.getBackgroundMusicVolume();
        if (this.currentGain != targetVolume && this.fadePlaying(targetVolume)) {
            ci.cancel();
        }
    }

    @Inject(
        method = "startPlaying",
        at = @At("HEAD"),
        cancellable = true
    )
    private void preventPlayingInPaleGarden(Music selector, CallbackInfo ci) {
        if (!VanillaBackport.CLIENT_CONFIG.fadeMusicOnPaleGarden.get() || this.minecraft.player == null) {
            return;
        }

        Holder<Biome> biome = this.minecraft.player.level().getBiome(this.minecraft.player.blockPosition());
        if (biome.is(ModBiomes.PALE_GARDEN)) {
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
        if (VanillaBackport.CLIENT_CONFIG.fadeMusicOnPaleGarden.get() && this.currentMusic != null) {
            SoundEngine engine = ((SoundManagerAccessor) this.minecraft.getSoundManager()).getSoundEngine();
            this.setVolume(engine, this.currentMusic, this.getBackgroundMusicVolume());
        }
    }

    @Inject(
        method = "startPlaying",
        at = @At("TAIL")
    )
    private void onStartPlaying(Music selector, CallbackInfo ci) {
        if (VanillaBackport.CLIENT_CONFIG.fadeMusicOnPaleGarden.get()) {
            this.currentGain = this.getBackgroundMusicVolume();
        }
    }

    @Unique
    private boolean fadePlaying(float targetVolume) {
        if (this.currentGain == targetVolume) {
            return true;
        }

        // Fade in (increasing volume)
        if (this.currentGain < targetVolume) {
            float step = Mth.clamp(this.currentGain, FADE_IN_MIN_STEP, FADE_IN_MAX_STEP);
            this.currentGain = Math.min(this.currentGain + step, targetVolume);
        }
        // Fade out (decreasing volume)
        else {
            this.currentGain = FADE_OUT_FACTOR * this.currentGain + (1 - FADE_OUT_FACTOR) * targetVolume;
            if (Math.abs(this.currentGain - targetVolume) < VOLUME_THRESHOLD) {
                this.currentGain = targetVolume;
            }
        }

        this.currentGain = Mth.clamp(this.currentGain, 0.0F, 1.0F);

        // Stop playing completely if volume is near zero
        if (this.currentGain <= VOLUME_THRESHOLD) {
            this.stopPlaying();
            return false;
        }

        SoundEngine engine = ((SoundManagerAccessor) this.minecraft.getSoundManager()).getSoundEngine();
        this.setVolume(engine, this.currentMusic, this.currentGain);
        return true;
    }

    @Unique
    private float getBackgroundMusicVolume() {
        LocalPlayer player = this.minecraft.player;
        if (player == null) {
            return 1.0F;
        }

        Holder<Biome> biome = player.level().getBiome(player.blockPosition());
        return biome.is(ModBiomes.PALE_GARDEN) ? 0.0F : 1.0F;
    }

    @Unique
    private void setVolume(SoundEngine engine, SoundInstance instance, float volume) {
        SoundEngineAccessor accessor = (SoundEngineAccessor) engine;
        if (!accessor.isLoaded()) {
            return;
        }

        ChannelAccess.ChannelHandle handle = accessor.getInstanceToChannel().get(instance);
        if (handle != null) {
            handle.execute(channel -> channel.setVolume(volume * accessor.callCalculateVolume(instance)));
        }
    }
}
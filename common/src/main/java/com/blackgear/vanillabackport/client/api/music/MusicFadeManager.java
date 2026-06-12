package com.blackgear.vanillabackport.client.api.music;

import com.blackgear.vanillabackport.common.registries.worldgen.ModBiomes;
import com.blackgear.vanillabackport.core.mixin.access.SoundEngineAccessor;
import com.blackgear.vanillabackport.core.mixin.access.SoundManagerAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.ChannelAccess;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.client.sounds.SoundEngine;
import net.minecraft.core.Holder;
import net.minecraft.util.Mth;
import net.minecraft.world.level.biome.Biome;
import org.jetbrains.annotations.Nullable;

public class MusicFadeManager {
    private static final float FADE_OUT_FACTOR = 0.97F;
    private static final float FADE_IN_MIN_STEP = 5.0E-4F;
    private static final float FADE_IN_MAX_STEP = 0.005F;
    private static final float VOLUME_THRESHOLD = 1.0E-4F;

    private final MusicManager manager;
    private final Minecraft minecraft;
    private float currentGain = 1.0F;

    public MusicFadeManager(MusicManager manager) {
        this.manager = manager;
        this.minecraft = Minecraft.getInstance();
    }

    public static boolean isFeatureEnabled() {
        return true;
    }

    public boolean onTick(@Nullable SoundInstance currentMusic) {
        if (!isFeatureEnabled()) return false;
        if (currentMusic == null) return false;

        float targetVolume = this.getBackgroundMusicVolume();
        return this.currentGain != targetVolume && this.fadePlaying(targetVolume, currentMusic);
    }

    public boolean preventPlayingInPaleGarden() {
        if (!isFeatureEnabled()) return false;
        if (this.minecraft.player == null) return false;

        Holder<Biome> biome = this.minecraft.player.level().getBiome(this.minecraft.player.blockPosition());
        return biome.is(ModBiomes.PALE_GARDEN);
    }

    public void updateVolume(@Nullable SoundInstance currentMusic) {
        if (!isFeatureEnabled()) return;
        if (currentMusic != null) {
            SoundEngine engine = ((SoundManagerAccessor) this.minecraft.getSoundManager()).getSoundEngine();
            this.setVolume(engine, currentMusic, this.getBackgroundMusicVolume());
        }
    }

    public void onStartPlaying() {
        if (!isFeatureEnabled()) return;
        this.currentGain = this.getBackgroundMusicVolume();
    }

    private boolean fadePlaying(float targetVolume, SoundInstance currentMusic) {
        if (this.currentGain == targetVolume) {
            return true;
        }

        this.updateCurrentGain(targetVolume);

        if (this.currentGain <= VOLUME_THRESHOLD) {
            this.manager.stopPlaying();
            return false;
        }

        SoundEngine engine = ((SoundManagerAccessor) this.minecraft.getSoundManager()).getSoundEngine();
        this.setVolume(engine, currentMusic, this.currentGain);
        return true;
    }

    private void updateCurrentGain(float targetVolume) {
        if (this.currentGain < targetVolume) {
            this.fadeIn(targetVolume);
        } else {
            this.fadeOut(targetVolume);
        }

        this.currentGain = Mth.clamp(this.currentGain, 0.0F, 1.0F);
    }

    private void fadeIn(float targetVolume) {
        float step = Mth.clamp(this.currentGain, FADE_IN_MIN_STEP, FADE_IN_MAX_STEP);
        this.currentGain = Math.min(this.currentGain + step, targetVolume);
    }

    private void fadeOut(float targetVolume) {
        this.currentGain = FADE_OUT_FACTOR * this.currentGain + (1 - FADE_OUT_FACTOR) * targetVolume;
        if (Math.abs(this.currentGain - targetVolume) < VOLUME_THRESHOLD) {
            this.currentGain = targetVolume;
        }
    }

    private float getBackgroundMusicVolume() {
        LocalPlayer player = this.minecraft.player;
        if (player == null) return 1.0F;

        Holder<Biome> biome = player.level().getBiome(player.blockPosition());
        return biome.is(ModBiomes.PALE_GARDEN) ? 0.0F : 1.0F;
    }

    private void setVolume(SoundEngine engine, SoundInstance instance, float volume) {
        SoundEngineAccessor accessor = (SoundEngineAccessor) engine;
        if (!accessor.isLoaded()) return;

        ChannelAccess.ChannelHandle handle = accessor.getInstanceToChannel().get(instance);
        if (handle != null) {
            handle.execute(channel -> channel.setVolume(volume * accessor.callCalculateVolume(instance)));
        }
    }
}
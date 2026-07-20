package com.blackgear.vanillabackport.client.level.sound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

@Environment(EnvType.CLIENT)
public class RidingEntitySoundInstance extends AbstractTickableSoundInstance {
    private final Player player;
    private final Entity entity;
    private final boolean underwaterSound;
    private final float volumeMin;
    private final float volumeMax;
    private final float volumeAmplifier;
    
    public RidingEntitySoundInstance(
        Player player,
        Entity entity,
        boolean underwaterSound,
        SoundEvent sound,
        float volumeMin,
        float volumeMax,
        float volumeAmplifier
    ) {
        super(sound, entity.getSoundSource(), SoundInstance.createUnseededRandom());
        this.player = player;
        this.entity = entity;
        this.underwaterSound = underwaterSound;
        this.volumeMin = volumeMin;
        this.volumeMax = volumeMax;
        this.volumeAmplifier = volumeAmplifier;
        this.attenuation = Attenuation.NONE;
        this.looping = true;
        this.delay = 0;
        this.volume = volumeMin;
    }
    
    @Override
    public boolean canPlaySound() {
        return !this.entity.isSilent();
    }
    
    @Override
    public boolean canStartSilent() {
        return true;
    }
    
    private boolean shouldNotPlayUnderwaterSound() {
        return this.underwaterSound != this.entity.isUnderWater();
    }
    
    protected float getEntitySpeed() {
        return (float) this.entity.getDeltaMovement().length();
    }
    
    protected boolean shouldPlaySound() {
        return true;
    }
    
    @Override
    public void tick() {
        if (this.entity.isRemoved() || !this.player.isPassenger() || this.player.getVehicle() != this.entity) {
            this.stop();
        } else if (this.shouldNotPlayUnderwaterSound()) {
            this.volume = this.volumeMin;
        } else {
            float speed = this.getEntitySpeed();
            if (speed >= 0.01F && this.shouldPlaySound()) {
                this.volume = this.volumeAmplifier * Mth.clampedLerp(this.volumeMin, this.volumeMax, speed);
            } else {
                this.volume = this.volumeMin;
            }
        }
    }
}
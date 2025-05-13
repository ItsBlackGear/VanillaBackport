package com.blackgear.vanillabackport.client.level.sound;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.level.entities.happyghast.HappyGhast;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.resources.sounds.AbstractTickableSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

@Environment(EnvType.CLIENT)
public class RidingHappyGhastSoundInstance extends AbstractTickableSoundInstance {
    private static final float VOLUME_MIN = 0.0F;
    private static final float VOLUME_MAX = 1.0F;
    private final Player player;
    private final HappyGhast happyGhast;

    public RidingHappyGhastSoundInstance(Player player, HappyGhast happyGhast) {
        super(ModSoundEvents.HAPPY_GHAST_RIDING.get(), happyGhast.getSoundSource(), SoundInstance.createUnseededRandom());
        this.player = player;
        this.happyGhast = happyGhast;
        this.attenuation = Attenuation.NONE;
        this.looping = true;
        this.delay = 0;
        this.volume = VOLUME_MIN;
    }

    @Override
    public boolean canStartSilent() {
        return true;
    }

    @Override
    public void tick() {
        if (!this.happyGhast.isRemoved() && this.player.isPassenger() && this.player.getVehicle() == this.happyGhast) {
            float velocity = (float) this.happyGhast.getDeltaMovement().length();
            if (velocity >= 0.01F) {
                this.volume = 5.0F * Mth.clampedLerp(VOLUME_MIN, VOLUME_MAX, velocity);
            } else {
                this.volume = VOLUME_MIN;
            }
        } else {
            this.stop();
        }
    }
}
package com.blackgear.vanillabackport.client.api.modules.end_flashes;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.SingleThreadedRandomSource;

@Environment(EnvType.CLIENT)
public class EndFlashState {
    public static final int SOUND_DELAY_IN_TICKS = 30;
    private static final int FLASH_INTERVAL_IN_TICKS = 600;
    private static final int MAX_FLASH_OFFSET_IN_TICKS = 200;
    private static final int MIN_FLASH_DURATION_IN_TICKS = 100;
    private static final int MAX_FLASH_DURATION_IN_TICKS = 380;
    private long flashSeed;
    private int offset;
    private int duration;
    private float intensity;
    private float oldIntensity;
    private float xAngle;
    private float yAngle;
    
    public void tick(long clockTime) {
        this.calculateFlashParameters(clockTime);
        this.oldIntensity = this.intensity;
        this.intensity = this.calculateIntensity(clockTime);
    }
    
    private void calculateFlashParameters(long clockTime) {
        long seed = clockTime / FLASH_INTERVAL_IN_TICKS;
        if (seed != this.flashSeed) {
            RandomSource random = new SingleThreadedRandomSource(seed);
            random.nextFloat();
            this.offset = Mth.randomBetweenInclusive(random, 0, MAX_FLASH_OFFSET_IN_TICKS);
            this.duration = Mth.randomBetweenInclusive(random, MIN_FLASH_DURATION_IN_TICKS, Math.min(MAX_FLASH_DURATION_IN_TICKS, FLASH_INTERVAL_IN_TICKS - this.offset));
            this.xAngle = Mth.randomBetween(random, -60.0F, 10.0F);
            this.yAngle = Mth.randomBetween(random, -180.0F, 180.0F);
            this.flashSeed = seed;
        }
    }
    
    private float calculateIntensity(long clockTime) {
        long clockTimeWithinInterval = clockTime % FLASH_INTERVAL_IN_TICKS;
        return clockTimeWithinInterval >= this.offset && clockTimeWithinInterval <= this.offset + this.duration
            ? Mth.sin((float) (clockTimeWithinInterval - this.offset) * Mth.PI / this.duration)
            : 0.0F;
    }
    
    public float getXAngle() {
        return this.xAngle;
    }
    
    public float getYAngle() {
        return this.yAngle;
    }
    
    public float getIntensity(float partialTicks) {
        return Mth.lerp(partialTicks, this.oldIntensity, this.intensity);
    }
    
    public boolean flashStartedThisTick() {
        return this.intensity > 0.0F && this.oldIntensity <= 0.0F;
    }
}
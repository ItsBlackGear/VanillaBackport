package com.blackgear.vanillabackport.client.level.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class FireflyParticle extends TextureSheetParticle {
    private static final float PARTICLE_FADE_OUT_LIGHT_TIME = 0.3F;
    private static final float PARTICLE_FADE_IN_LIGHT_TIME = 0.1F;
    private static final float PARTICLE_FADE_OUT_ALPHA_TIME = 0.5F;
    private static final float PARTICLE_FADE_IN_ALPHA_TIME = 0.3F;
    private static final int PARTICLE_MIN_LIFETIME = 200;
    private static final int PARTICLE_MAX_LIFETIME = 300;

    protected FireflyParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.speedUpWhenYMotionIsBlocked = true;
        this.friction = 0.96F;
        this.quadSize *= 0.75F;
        this.yd *= 0.8F;
        this.xd *= 0.8F;
        this.zd *= 0.8F;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    protected int getLightColor(float partialTick) {
        return (int) (255.0F * this.getFadeAmount(this.getLifetimeProgress(this.age + partialTick), PARTICLE_FADE_IN_LIGHT_TIME, PARTICLE_FADE_OUT_LIGHT_TIME));
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.getBlockState(BlockPos.containing(this.x, this.y, this.z)).isAir()) {
            this.remove();
        } else {
            this.setAlpha(this.getFadeAmount(this.getLifetimeProgress(this.age), PARTICLE_FADE_IN_ALPHA_TIME, PARTICLE_FADE_OUT_ALPHA_TIME));
            if (Math.random() > 0.95 || this.age == 1) {
                this.setParticleSpeed(-0.05 + 0.1F * Math.random(), -0.05 + 0.1F * Math.random(), -0.05 + 0.1F * Math.random());
            }
        }
    }

    private float getLifetimeProgress(float age) {
        return Mth.clamp(age / this.lifetime, 0.0F, 1.0F);
    }

    private float getFadeAmount(float lifetime, float fadeIn, float fadeOut) {
        if (lifetime >= 1.0F - fadeIn) {
            return (1.0F - lifetime) / fadeIn;
        } else {
            return lifetime <= fadeOut ? lifetime / fadeOut : 1.0F;
        }
    }

    @Environment(EnvType.CLIENT)
    public record Provider(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            FireflyParticle particle = new FireflyParticle(level, x, y, z, 0.5 - level.random.nextDouble(), level.random.nextBoolean() ? ySpeed : -ySpeed, 0.5 - level.random.nextDouble());
            particle.setLifetime(level.random.nextIntBetweenInclusive(PARTICLE_MIN_LIFETIME, PARTICLE_MAX_LIFETIME));
            particle.scale(1.5F);
            particle.pickSprite(this.sprite);
            particle.setAlpha(0.0F);
            return particle;
        }
    }
}
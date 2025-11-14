package com.blackgear.vanillabackport.client.level.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BaseAshSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.FastColor;

@Environment(EnvType.CLIENT)
public class DustPlumeParticle extends BaseAshSmokeParticle {
    protected DustPlumeParticle(ClientLevel level, double x, double y, double z,double xSpeed, double ySpeed, double zSpeed, float quadSizeMultiplier, SpriteSet sprites) {
        super(level, x, y, z, 0.7F, 0.6F, 0.7F, xSpeed, ySpeed + 0.15F, zSpeed, quadSizeMultiplier, sprites, 0.5F, 7, 0.5F, false);
        float offset = (float) Math.random() * 0.2F;
        this.rCol = FastColor.ARGB32.red(12235202) / 255.0F - offset;
        this.gCol = FastColor.ARGB32.green(12235202) / 255.0F - offset;
        this.bCol = FastColor.ARGB32.blue(12235202) / 255.0F - offset;
    }

    @Override
    public void tick() {
        this.gravity = 0.88F * this.gravity;
        this.friction = 0.92F * this.friction;
        super.tick();
    }

    @Environment(EnvType.CLIENT)
    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
        public Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new DustPlumeParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, 1.0F, this.sprites);
        }
    }
}

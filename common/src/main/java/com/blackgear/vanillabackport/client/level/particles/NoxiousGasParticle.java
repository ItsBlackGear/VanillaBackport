package com.blackgear.vanillabackport.client.level.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class NoxiousGasParticle extends BaseAshSmokeParticle {
    private final float fadeOutStartingPoint;

    public NoxiousGasParticle(
        ClientLevel level,
        double x,
        double y,
        double z,
        double xSpeed,
        double ySpeed,
        double zSpeed,
        float scale,
        SpriteSet sprites
    ) {
        super(level, x, y, z, 0.1F, 0.1F, 0.1F, xSpeed, ySpeed, zSpeed, scale, sprites, 0.3F, 5, -0.02F, true);
        this.rCol = 1.0F;
        this.gCol = 1.0F;
        this.bCol = 1.0F;
        this.lifetime = (int) (6.0 / (this.random.nextFloat() * 0.5 + 0.5) * scale);
        this.fadeOutStartingPoint = this.lifetime / 2.0F;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age > this.fadeOutStartingPoint) {
            float framesSinceFadeOutStart = this.age - this.fadeOutStartingPoint;
            this.setAlpha((this.lifetime - framesSinceFadeOutStart) / this.lifetime);
        }
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Environment(EnvType.CLIENT)
    public record Provider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
        @Override @Nullable
        public Particle createParticle(
            SimpleParticleType type,
            ClientLevel level,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            return new NoxiousGasParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, 3.0F, this.sprites);
        }
    }
}

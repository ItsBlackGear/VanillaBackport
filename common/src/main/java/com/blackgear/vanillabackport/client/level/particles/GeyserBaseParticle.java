package com.blackgear.vanillabackport.client.level.particles;

import com.blackgear.vanillabackport.client.level.particles.particleoptions.GeyserBaseParticleOptions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BaseAshSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.util.RandomSource;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class GeyserBaseParticle extends BaseAshSmokeParticle {
    protected GeyserBaseParticle(
        ClientLevel level,
        double x,
        double y,
        double z,
        double xAux,
        double yAux,
        double zAux,
        float burstImpulse,
        float size,
        SpriteSet sprites
    ) {
        super(level, x, y, z, burstImpulse, burstImpulse, burstImpulse, xAux, yAux, zAux, size, sprites, 0.0F, 0, 0, true);
        this.friction = 0.725F;
        this.rCol = 1.0F;
        this.gCol = 1.0F;
        this.bCol = 1.0F;
        this.yd = Math.abs(this.yd);
        float lifetime = 0.8F + 0.2F * level.getRandom().nextFloat();
        this.lifetime = (int) (25.0F * lifetime);
    }

    @Environment(EnvType.CLIENT)
    public record Provider(SpriteSet sprites) implements ParticleProvider<GeyserBaseParticleOptions> {
        @Override
        public @Nullable Particle createParticle(
            GeyserBaseParticleOptions type,
            ClientLevel level,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            RandomSource random = level.getRandom();
            double randomX = x + (random.nextFloat() - 0.5F) * 0.5F;
            double randomY = y + (random.nextFloat() - 0.5F) * 0.5F + 0.2F;
            double randomZ = z + (random.nextFloat() - 0.5F) * 0.5F;
            float burstImpulse = type.burstImpulseBase() + 0.25F + type.waterBlocks();
            float size = 3.0F + 0.125F * type.waterBlocks();
            return new GeyserBaseParticle(
                level,
                randomX,
                randomY,
                randomZ,
                xSpeed,
                ySpeed,
                zSpeed,
                burstImpulse,
                size,
                this.sprites
            );
        }
    }
}
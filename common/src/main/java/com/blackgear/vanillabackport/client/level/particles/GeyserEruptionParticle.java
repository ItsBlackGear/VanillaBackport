package com.blackgear.vanillabackport.client.level.particles;

import com.blackgear.vanillabackport.client.level.particles.particleoptions.GeyserBaseParticleOptions;
import com.blackgear.vanillabackport.client.level.particles.particleoptions.GeyserParticleOptions;
import com.blackgear.vanillabackport.client.registries.ModParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.NoRenderParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;

@Environment(EnvType.CLIENT)
public class GeyserEruptionParticle extends NoRenderParticle {
    private final int waterBlocks;
    private final double xa;
    private final double ya;
    private final double za;
    private final GeyserParticleOptions plumeParticle;
    private final GeyserBaseParticleOptions baseParticle;
    private final GeyserBaseParticleOptions poofParticle;

    protected GeyserEruptionParticle(
        ClientLevel level,
        double x,
        double y,
        double z,
        double xAux,
        double yAux,
        double zAux,
        GeyserParticleOptions options
    ) {
        super(level, x, y, z);
        this.xa = xAux;
        this.ya = yAux;
        this.za = zAux;
        this.waterBlocks = options.waterBlocks();
        this.lifetime = 20;
        this.plumeParticle = new GeyserParticleOptions(ModParticles.GEYSER_PLUME.get(), this.waterBlocks);
        this.baseParticle = new GeyserBaseParticleOptions(ModParticles.GEYSER_BASE.get(), this.waterBlocks, 1.5F);
        this.poofParticle = new GeyserBaseParticleOptions(ModParticles.GEYSER_POOF.get(), this.waterBlocks, 2.0F);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age % 2 == 0) {
            for (int i = 0; i < 2; i++) {
                this.level.addParticle(this.baseParticle, this.x, this.y, this.z, this.xa, this.ya, this.za);
            }
        }

        for (int i = 0; i < this.waterBlocks + 2; i++) {
            this.level.addParticle(this.plumeParticle, this.x, this.y, this.z, this.xa, this.ya, this.za);
        }

        if (this.age % 10 == 0) {
            for (int i = 0; i < 20; i++) {
                this.level.addParticle(this.poofParticle, this.x, this.y, this.z, this.xa, this.ya, this.za);
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public record Provider(SpriteSet sprites) implements ParticleProvider<GeyserParticleOptions> {
        @Override
        public Particle createParticle(
            GeyserParticleOptions type,
            ClientLevel level,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            return new GeyserEruptionParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, type);
        }
    }
}
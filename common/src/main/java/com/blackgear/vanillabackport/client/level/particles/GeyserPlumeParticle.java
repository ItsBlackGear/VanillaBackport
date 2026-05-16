package com.blackgear.vanillabackport.client.level.particles;

import com.blackgear.vanillabackport.client.level.particles.particleoptions.GeyserParticleOptions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class GeyserPlumeParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    private final double startY;
    private final double maxY;
    private final float initialPropulsion;
    private final float horizontalSprayX;
    private final float horizontalSprayZ;
    private final float minSize;
    private final float maxSize;
    private boolean done;

    public GeyserPlumeParticle(
        ClientLevel level,
        double x,
        double y,
        double z,
        double xSpeed,
        double ySpeed,
        double zSpeed,
        GeyserParticleOptions options,
        SpriteSet sprites
    ) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        int plumeHeight = 5 * Math.max(1, options.waterBlocks());
        this.hasPhysics = true;
        this.speedUpWhenYMotionIsBlocked = true;
        this.lifetime = plumeHeight * 5;
        this.yd = 0.0;
        this.startY = y;
        this.maxY = this.startY + plumeHeight - 1.0;
        this.horizontalSprayX = (level.getRandom().nextFloat() - 0.5F) * 0.2F;
        this.horizontalSprayZ = (level.getRandom().nextFloat() - 0.5F) * 0.2F;
        this.friction = 1.0F;
        this.initialPropulsion = (options.waterBlocks() == 1 ? 1.5F : 1.0F) * plumeHeight * 1.45F;
        this.gravity = -this.initialPropulsion;
        float size = this.quadSize * 0.75F;
        this.minSize = size * (2.0F + plumeHeight / 8.0F);
        this.maxSize = size * (3.0F + plumeHeight / 8.0F);
        this.quadSize = this.minSize;
        this.sprites = sprites;
        this.setSpriteFromAge(sprites);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.done && (this.yd < 0.0 || this.y > this.maxY || this.y == this.yo)) {
            this.lifetime = Math.min(this.lifetime, this.age + 5);
            this.friction = 0.0F;
            this.done = true;
        }

        double yProgressLinear = Mth.clamp((this.y - this.startY) / (this.maxY - this.startY), 0.0, 1.0);
        double yProgressExponential = Math.pow(yProgressLinear, 3.0);
        this.gravity = this.initialPropulsion * (float) yProgressExponential * 0.12F;
        this.xd = yProgressLinear * this.horizontalSprayX;
        this.zd = yProgressLinear * this.horizontalSprayZ;
        this.setSpriteFromAge(this.sprites);
        this.quadSize = minSize + (float) (yProgressLinear * (this.maxSize - this.minSize));
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Environment(EnvType.CLIENT)
    public record Provider(SpriteSet sprites) implements ParticleProvider<GeyserParticleOptions> {
        @Override
        public Particle createParticle(
            GeyserParticleOptions options,
            ClientLevel level,
            double x,
            double y,
            double z,
            double xSpeed,
            double ySpeed,
            double zSpeed
        ) {
            RandomSource random = level.getRandom();
            double randomX = x + (random.nextFloat() - 0.5F) * 0.2F;
            double randomY = y + random.nextFloat();
            double randomZ = z + (random.nextFloat() - 0.5F) * 0.2F;
            return new GeyserPlumeParticle(level, randomX, randomY, randomZ, xSpeed, ySpeed, zSpeed, options, this.sprites);
        }
    }
}
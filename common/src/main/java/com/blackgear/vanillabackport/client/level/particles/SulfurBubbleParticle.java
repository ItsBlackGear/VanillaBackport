package com.blackgear.vanillabackport.client.level.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.material.Fluids;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class SulfurBubbleParticle extends TextureSheetParticle {
    private final double yStart;
    private final double yEnd;
    private final float sizeStart;
    private double yPrev;

    public SulfurBubbleParticle(
        ClientLevel level,
        double x,
        double y,
        double z,
        double xSpeed,
        double ySpeed,
        double zSpeed
    ) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        this.gravity = -0.04F;
        this.friction = 0.85F;
        this.setSize(0.02F, 0.02F);
        this.xd = xSpeed * 0.2F + (this.random.nextFloat() * 2.0F - 1.0F) * 0.02F;
        this.zd = zSpeed * 0.2F + (this.random.nextFloat() * 2.0F - 1.0F) * 0.02F;
        this.sizeStart = 0.02F + 0.02F * this.random.nextFloat();
        this.quadSize = this.sizeStart;
        this.lifetime = Integer.MAX_VALUE;
        this.yStart = this.yo;
        this.yEnd = this.yo + 4.0 - 1.0;
        this.yPrev = y;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.removed && !this.level.getFluidState(BlockPos.containing(this.x, this.y, this.z)).isSourceOfType(Fluids.WATER)) {
            this.remove();
        }

        if (!this.removed && this.y >= this.yEnd) {
            this.remove();
        }

        if (!this.removed && this.y <= this.yPrev) {
            this.remove();
        }

        this.xd = this.xd + this.randomHorizontalWiggling();
        this.zd = this.zd + this.randomHorizontalWiggling();
        this.move(this.xd, 0.0, this.zd);
        float travelProgress = (float) ((this.y - this.yStart) / (this.yEnd - this.yStart));
        this.quadSize = this.sizeStart + travelProgress * (0.15F - this.sizeStart);
        this.yPrev = this.y;
    }

    private double randomHorizontalWiggling() {
        return this.random.nextFloat() * 0.003F * (this.random.nextBoolean() ? 1 : -1) * 0.5;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Environment(EnvType.CLIENT)
    public record Provider(SpriteSet sprite) implements ParticleProvider<SimpleParticleType> {
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
            SulfurBubbleParticle particle = new SulfurBubbleParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.pickSprite(this.sprite);
            return particle;
        }
    }
}
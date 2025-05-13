package com.blackgear.vanillabackport.client.level.particles;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

@Environment(EnvType.CLIENT)
public class FallingLeavesParticle extends TextureSheetParticle {
    private static final float ACCELERATION_SCALE = 0.0025F;
    private static final int INITIAL_LIFETIME = 300;
    private static final int CURVE_ENDPOINT_TIME = 300;
    private float rotSpeed;
    private final float spinAcceleration;
    private final float windBig;
    private final boolean swirl;
    private final boolean flowAway;
    private final double xaFlowScale;
    private final double zaFlowScale;
    private final double swirlPeriod;

    protected FallingLeavesParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites, float fallAcceleration, float windBig, boolean swirl, boolean flowAway, float scale, float initialVelocity) {
        super(level, x, y, z);
        this.setSprite(sprites.get(this.random.nextInt(12), 12));
        this.rotSpeed = (float) Math.toRadians(this.random.nextBoolean() ? -30.0 : 30.0);
        float particleRandom = this.random.nextFloat();
        this.spinAcceleration = (float) Math.toRadians(this.random.nextBoolean() ? -5.0 : 5.0);
        this.windBig = windBig;
        this.swirl = swirl;
        this.flowAway = flowAway;
        this.lifetime = INITIAL_LIFETIME;
        this.gravity = fallAcceleration * 1.2F * ACCELERATION_SCALE;
        float size = scale * (this.random.nextBoolean() ? 0.05F : 0.075F);
        this.quadSize = size;
        this.setSize(size, size);
        this.friction = 1.0F;
        this.yd = -initialVelocity;
        this.xaFlowScale = Math.cos(Math.toRadians(particleRandom * 60.0F)) * (double) this.windBig;
        this.zaFlowScale = Math.sin(Math.toRadians(particleRandom * 60.0F)) * (double) this.windBig;
        this.swirlPeriod = Math.toRadians(1000.0F + particleRandom * 3000.0F);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (this.lifetime-- <= 0) {
            this.remove();
        }

        if (!this.removed) {
            float ageInTicks = INITIAL_LIFETIME - this.lifetime;
            float ageRatio = Math.min(ageInTicks / CURVE_ENDPOINT_TIME, 1.0F);
            double xAcceleration = 0.0;
            double zAcceleration = 0.0;
            if (this.flowAway) {
                xAcceleration += this.xaFlowScale * Math.pow(ageRatio, 1.25);
                zAcceleration += this.zaFlowScale * Math.pow(ageRatio, 1.25);
            }

            if (this.swirl) {
                xAcceleration += ageRatio * Math.cos(ageRatio * this.swirlPeriod) * this.windBig;
                zAcceleration += ageRatio * Math.sin(ageRatio * this.swirlPeriod) * this.windBig;
            }

            this.xd += xAcceleration * ACCELERATION_SCALE;
            this.zd += zAcceleration * ACCELERATION_SCALE;
            this.yd = this.yd - this.gravity;
            this.rotSpeed = this.rotSpeed + this.spinAcceleration / 20.0F;
            this.oRoll = this.roll;
            this.roll = this.roll + this.rotSpeed / 20.0F;
            this.move(this.xd, this.yd, this.zd);
            if (this.onGround || this.lifetime < INITIAL_LIFETIME - 1 && (this.xd == 0.0 || this.zd == 0.0)) {
                this.remove();
            }

            if (!this.removed) {
                this.xd = this.xd * this.friction;
                this.yd = this.yd * this.friction;
                this.zd = this.zd * this.friction;
            }
        }
    }

    @Environment(EnvType.CLIENT)
    public record PaleOakProvider(SpriteSet sprites) implements ParticleProvider<SimpleParticleType> {
        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new FallingLeavesParticle(level, x, y, z, this.sprites, 0.07F, 10.0F, true, false, 2.0F, 0.021F);
        }
    }
}
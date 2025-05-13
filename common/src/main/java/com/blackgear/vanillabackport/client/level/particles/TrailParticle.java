package com.blackgear.vanillabackport.client.level.particles;

import com.blackgear.vanillabackport.client.level.particles.particleoptions.TrailParticleOption;
import com.blackgear.vanillabackport.core.util.ColorUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public class TrailParticle extends TextureSheetParticle {
    private final Vec3 target;

    protected TrailParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, Vec3 target, int color) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed);
        color = ColorUtils.scaleRGB(color, 0.875F + this.random.nextFloat() * 0.25F, 0.875F + this.random.nextFloat() * 0.25F, 0.875F + this.random.nextFloat() * 0.25F);
        this.rCol = FastColor.ARGB32.red(color) / 255.0F;
        this.gCol = FastColor.ARGB32.green(color) / 255.0F;
        this.bCol = FastColor.ARGB32.blue(color) / 255.0F;
        this.quadSize = 0.26F;
        this.target = target;
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
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            int ageInTicks = this.lifetime - this.age;
            double deltaTime = 1.0 / ageInTicks;
            this.x = Mth.lerp(deltaTime, this.x, this.target.x());
            this.y = Mth.lerp(deltaTime, this.y, this.target.y());
            this.z = Mth.lerp(deltaTime, this.z, this.target.z());
        }
    }

    @Override
    protected int getLightColor(float partialTick) {
        return 15728880;
    }

    @Environment(EnvType.CLIENT)
    public record Provider(SpriteSet sprites) implements ParticleProvider<TrailParticleOption> {
        @Override
        public Particle createParticle(TrailParticleOption option, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            TrailParticle particle = new TrailParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, option.target(), option.color());
            particle.pickSprite(this.sprites);
            particle.setLifetime(option.duration());
            return particle;
        }
    }
}
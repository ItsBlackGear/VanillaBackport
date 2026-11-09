package com.blackgear.vanillabackport.client.level.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

@Environment(EnvType.CLIENT)
public class GustParticle extends TextureSheetParticle {
    private final SpriteSet sprites;
    
    protected GustParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
        super(level, x, y, z);
        this.sprites = sprites;
        this.setSpriteFromAge(sprites);
        this.lifetime = 12 + this.random.nextInt(4);
        this.quadSize = 1.0F;
        this.setSize(1.0F, 1.0F);
    }
    
    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_LIT;
    }
    
    @Override
    protected int getLightColor(float partialTick) {
        return 15728880;
    }
    
    @Override
    public void tick() {
        if (this.age++ >= this.lifetime) {
            this.remove();
        } else {
            this.setSpriteFromAge(this.sprites);
        }
    }
    
    @Environment(EnvType.CLIENT)
    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        
        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }
        
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            return new GustParticle(level, x, y, z, this.sprites);
        }
    }
    
    @Environment(EnvType.CLIENT)
    public static class SmallProvider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;
        
        public SmallProvider(SpriteSet sprites) {
            this.sprites = sprites;
        }
        
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            Particle particle = new GustParticle(level, x, y, z, this.sprites);
            particle.scale(0.15F);
            return particle;
        }
    }
}

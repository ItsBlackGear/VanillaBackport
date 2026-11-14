package com.blackgear.vanillabackport.client.level.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.BaseAshSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

@Environment(EnvType.CLIENT)
public class WhiteSmokeParticle extends BaseAshSmokeParticle {
	public WhiteSmokeParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, float quadSizeMultiplier, SpriteSet sprites) {
		super(level, x, y, z, 0.1F, 0.1F, 0.1F, xSpeed, ySpeed, zSpeed, quadSizeMultiplier, sprites, 0.3F, 8, -0.1F, true);
		this.rCol = 0.7294118F;
		this.gCol = 0.69411767F;
		this.bCol = 0.7607843F;
	}

	@Environment(EnvType.CLIENT)
	public static class Provider implements ParticleProvider<SimpleParticleType> {
		private final SpriteSet sprites;

		public Provider(SpriteSet sprites) {
			this.sprites = sprites;
		}

		public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
			return new WhiteSmokeParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, 1.0F, this.sprites);
		}
	}
}

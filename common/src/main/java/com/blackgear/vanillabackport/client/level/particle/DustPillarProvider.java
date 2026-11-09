package com.blackgear.vanillabackport.client.level.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class DustPillarProvider implements ParticleProvider<BlockParticleOption> {
	@Override @Nullable
	public Particle createParticle(BlockParticleOption type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
		BlockState state = type.getState();
		Particle particle = !state.isAir() && !state.is(Blocks.MOVING_PISTON)  ? new TerrainParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, state) : null;
		if (particle != null) {
			particle.setParticleSpeed(level.random.nextGaussian() / 30.0, ySpeed + level.random.nextGaussian() / 2.0, level.random.nextGaussian() / 30.0);
			particle.setLifetime(level.random.nextInt(20) + 20);
		}

		return particle;
	}
}
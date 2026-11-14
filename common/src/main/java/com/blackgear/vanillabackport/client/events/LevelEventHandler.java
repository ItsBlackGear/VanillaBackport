package com.blackgear.vanillabackport.client.events;

import com.blackgear.vanillabackport.client.registries.ModParticles;
import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.core.network.handlers.LevelEvents;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public class LevelEventHandler {
    public static final LevelEventHandler INSTANCE = new LevelEventHandler();
    
    public void levelEvent(LevelEvents event, BlockPos pos, int data) {
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            RandomSource random = level.getRandom();
            
            switch (event) {
                case CRAFTER_CRAFT -> level.playLocalSound(pos, ModSoundEvents.CRAFTER_CRAFT.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
                case CRAFTER_FAIL -> level.playLocalSound(pos, ModSoundEvents.CRAFTER_FAIL.get(), SoundSource.BLOCKS, 1.0F, 1.0F, false);
                case PARTICLES_SHOOT_WHITE_SMOKE -> this.shootParticles(level, data, pos, random, ModParticles.WHITE_SMOKE.get());
                case MACE_SMASH -> this.spawnSmashAttackParticles(level, pos, data);
                case SOUND_SULFUR_SPIKE_LAND -> level.playLocalSound(pos, ModSoundEvents.SULFUR_SPIKE_LAND.get(), SoundSource.BLOCKS, 2.0F, random.nextFloat() * 0.1F + 0.9F, false);
                case SOUND_ICICLE_LAND -> level.playLocalSound(pos, ModSoundEvents.ICICLE_LAND.get(), SoundSource.BLOCKS, 2.0F, random.nextFloat() * 0.1F + 0.9F, false);
                case CONVERT_ZOMBIE_TO_FROSTBITE -> level.playLocalSound(pos, ModSoundEvents.ZOMBIE_CONVERTED_TO_FROSTBITE.get(), SoundSource.HOSTILE, 2.0F, random.nextFloat() * 0.1F + 0.9F, false);
                case CONVERT_FROSTBITE_TO_ZOMBIE -> level.playLocalSound(pos, ModSoundEvents.FROSTBITE_CONVERTED_TO_ZOMBIE.get(), SoundSource.HOSTILE, 2.0F, random.nextFloat() * 0.1F + 0.9F, false);
                case CONVERT_DROWNED_TO_ZOMBIE -> level.playLocalSound(pos, SoundEvents.ZOMBIE_CONVERTED_TO_DROWNED, SoundSource.HOSTILE, 2.0F, (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F, false);
                default -> {}
            }
        }
    }
    
    private void spawnSmashAttackParticles(Level level, BlockPos pos, int count) {
        Vec3 center = pos.getCenter().add(0.0F, 0.5F, 0.0F);
        BlockParticleOption particle = new BlockParticleOption(ModParticles.DUST_PILLAR.get(), level.getBlockState(pos));
        
        for (int i = 0; i < count / 3.0F; ++i) {
            double x = center.x + level.getRandom().nextGaussian() / 2.0F;
            double y = center.y;
            double z = center.z + level.getRandom().nextGaussian() / 2.0F;
            double xd = level.getRandom().nextGaussian() * 0.2F;
            double yd = level.getRandom().nextGaussian() * 0.2F;
            double zd = level.getRandom().nextGaussian() * 0.2F;
            level.addParticle(particle, x, y, z, xd, yd, zd);
        }
        
        for (int i = 0; i < count / 1.5F; ++i) {
            double x = center.x + 3.5F * Math.cos(i) + level.getRandom().nextGaussian() / 2.0F;
            double y = center.y;
            double z = center.z + 3.5F * Math.sin(i) + level.getRandom().nextGaussian() / 2.0F;
            double xd = level.getRandom().nextGaussian() * 0.05F;
            double yd = level.getRandom().nextGaussian() * 0.05F;
            double zd = level.getRandom().nextGaussian() * 0.05F;
            level.addParticle(particle, x, y, z, xd, yd, zd);
        }
    }
    
    private void shootParticles(Level level, int direction, BlockPos pos, RandomSource random, SimpleParticleType particleType) {
        Direction directionx = Direction.from3DDataValue(direction);
        int i = directionx.getStepX();
        int j = directionx.getStepY();
        int k = directionx.getStepZ();
        double d0 = pos.getX() + i * 0.6 + 0.5;
        double d1 = pos.getY() + j * 0.6 + 0.5;
        double d2 = pos.getZ() + k * 0.6 + 0.5;
        
        for (int l = 0; l < 10; l++) {
            double d3 = random.nextDouble() * 0.2 + 0.01;
            double d4 = d0 + i * 0.01 + (random.nextDouble() - 0.5) * k * 0.5;
            double d5 = d1 + j * 0.01 + (random.nextDouble() - 0.5) * j * 0.5;
            double d6 = d2 + k * 0.01 + (random.nextDouble() - 0.5) * i * 0.5;
            double d7 = i * d3 + random.nextGaussian() * 0.01;
            double d8 = j * d3 + random.nextGaussian() * 0.01;
            double d9 = k * d3 + random.nextGaussian() * 0.01;
            level.addParticle(particleType, d4, d5, d6, d7, d8, d9);
        }
    }
}
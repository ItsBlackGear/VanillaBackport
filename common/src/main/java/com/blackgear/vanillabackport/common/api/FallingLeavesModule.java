package com.blackgear.vanillabackport.common.api;

import com.blackgear.vanillabackport.client.level.color.LeafColors;
import com.blackgear.vanillabackport.client.level.particles.particleoptions.ColorParticleOption;
import com.blackgear.vanillabackport.client.registries.ModParticles;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

@Environment(EnvType.CLIENT)
public class FallingLeavesModule {
    public void makeFallingLeavesParticles(Level level, BlockPos pos, RandomSource random, BlockState state, BlockPos offset) {
        if (random.nextFloat() < VanillaBackport.CLIENT_CONFIG.fallingLeavesFrequency.get()) {
            if (!Block.isFaceFull(state.getCollisionShape(level, offset), Direction.UP)) {
                this.spawnFallingLeavesParticle(level, pos, random);
            }
        }
    }

    private void spawnFallingLeavesParticle(Level level, BlockPos pos, RandomSource random) {
        BlockState state = level.getBlockState(pos);
        if (VanillaBackport.CLIENT_CONFIG.fallingLeaves.get()) {
            ParticleType<ColorParticleOption> particle = null;

            if (state.is(ModBlockTags.SPAWN_FALLING_LEAVES)) {
                particle = ModParticles.TINTED_LEAVES.get();
            } else if (state.is(ModBlockTags.SPAWN_FALLING_NEEDLES)) {
                particle = ModParticles.TINTED_NEEDLES.get();
            }

            if (particle != null) {
                ColorParticleOption option = ColorParticleOption.create(particle, LeafColors.getClientLeafTintColor(pos));
                ParticleUtils.spawnParticleBelow(level, pos, random, option);
            }
        }
    }
}
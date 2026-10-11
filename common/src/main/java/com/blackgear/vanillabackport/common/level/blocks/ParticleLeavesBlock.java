package com.blackgear.vanillabackport.common.level.blocks;

import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class ParticleLeavesBlock extends LeavesBlock {
    private final Supplier<? extends ParticleOptions> particle;
    private final float leafParticleChance;
    
    public ParticleLeavesBlock(float leafParticleChance, Supplier<? extends ParticleOptions> particle, Properties properties) {
        super(properties);
        this.leafParticleChance = leafParticleChance;
        this.particle = particle;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        if (level.isClientSide && !VanillaBackport.CLIENT_CONFIG.hasFallingLeaves.get()) return;
        
        if (random.nextFloat() < this.leafParticleChance) {
            BlockPos below = pos.below();
            BlockState belowState = level.getBlockState(below);
            if (!isFaceFull(belowState.getCollisionShape(level, below), Direction.UP)) {
                ParticleUtils.spawnParticleBelow(level, pos, random, this.particle.get());
            }
        }
    }
}
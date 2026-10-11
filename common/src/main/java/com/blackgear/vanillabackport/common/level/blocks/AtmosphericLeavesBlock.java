package com.blackgear.vanillabackport.common.level.blocks;

import com.blackgear.vanillabackport.common.level.sounds.AmbientLeavesBlockSoundPlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class AtmosphericLeavesBlock extends ParticleLeavesBlock {
    protected final AmbientLeavesBlockSoundPlayer ambientLeavesBlockSoundPlayer;
    
    public AtmosphericLeavesBlock(float leafParticleChance, Supplier<? extends ParticleOptions> particle, Properties properties, AmbientLeavesBlockSoundPlayer ambientLeavesBlockSoundPlayer) {
        super(leafParticleChance, particle, properties);
        this.ambientLeavesBlockSoundPlayer = ambientLeavesBlockSoundPlayer;
    }
    
    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
        this.ambientLeavesBlockSoundPlayer.playAmbientLeavesSounds(level, pos, this, random);
    }
}
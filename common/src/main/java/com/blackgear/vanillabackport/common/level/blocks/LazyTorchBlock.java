package com.blackgear.vanillabackport.common.level.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class LazyTorchBlock extends TorchBlock {
    private final Supplier<? extends ParticleOptions> particle;

    public LazyTorchBlock(Supplier<? extends ParticleOptions> particle, Properties properties) {
        super(properties, null);
        this.particle = particle;
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.7;
        double z = pos.getZ() + 0.5;
        level.addParticle(ParticleTypes.SMOKE, x, y, z, 0.0, 0.0, 0.0);
        level.addParticle(this.particle.get(), x, y, z, 0.0, 0.0, 0.0);
    }
}

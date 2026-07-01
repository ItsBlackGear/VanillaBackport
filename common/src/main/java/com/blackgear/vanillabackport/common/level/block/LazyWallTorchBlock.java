package com.blackgear.vanillabackport.common.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.WallTorchBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.function.Supplier;

public class LazyWallTorchBlock extends WallTorchBlock {
    private final Supplier<? extends ParticleOptions> particle;

    public LazyWallTorchBlock(Supplier<? extends ParticleOptions> particle, Properties properties) {
        super(properties, null);
        this.particle = particle;
    }

    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        Direction direction = state.getValue(FACING);
        double x = pos.getX() + 0.5;
        double y = pos.getY() + 0.7;
        double z = pos.getZ() + 0.5;
        Direction opposite = direction.getOpposite();
        level.addParticle(ParticleTypes.SMOKE, x + 0.27 * opposite.getStepX(), y + 0.22, z + 0.27 * opposite.getStepZ(), 0.0, 0.0, 0.0);
        level.addParticle(this.particle.get(), x + 0.27 * opposite.getStepX(), y + 0.22, z + 0.27 * opposite.getStepZ(), 0.0, 0.0, 0.0);
    }
}
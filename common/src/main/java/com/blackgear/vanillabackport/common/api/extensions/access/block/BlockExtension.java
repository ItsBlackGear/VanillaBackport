package com.blackgear.vanillabackport.common.api.extensions.access.block;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockExtension {
    default void vb$AnimateTick(BlockState state, Level level, BlockPos pos, RandomSource random) { /* NO-OP */ }
}
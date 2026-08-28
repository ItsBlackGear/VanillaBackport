package com.blackgear.vanillabackport.common.api.extensions.access.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface BlockExtension {
    BlockExtension DEFAULT = new BlockExtension() {};
    
    static BlockExtension of(Object block) {
        return block instanceof BlockExtension extension ? extension : DEFAULT;
    }
    
    default void vb$animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) { /* NO-OP */ }
    
    default void vb$randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) { /* NO-OP */ }
    
    default boolean vb$isRandomlyTicking(BlockState state) {
        return false;
    }
}
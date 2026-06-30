package com.blackgear.vanillabackport.core.mixin.common.access;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ContainerOpenersCounter.class)
public interface ContainerOpenersCounterAccessor {
    @Invoker boolean callIsOwnContainer(Player player);
    
    @Invoker void callOpenerCountChanged(Level level, BlockPos pos, BlockState state, int count, int openCount);
    
    @Invoker void callOnOpen(Level level, BlockPos pos, BlockState state);
    
    @Invoker void callOnClose(Level level, BlockPos pos, BlockState state);
}

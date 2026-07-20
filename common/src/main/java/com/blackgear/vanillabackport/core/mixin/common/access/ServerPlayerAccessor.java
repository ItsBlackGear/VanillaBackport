package com.blackgear.vanillabackport.core.mixin.common.access;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerPlayer.class)
public interface ServerPlayerAccessor {
    @Invoker void callNextContainerCounter();
    
    @Accessor int getContainerCounter();
    
    @Invoker void callInitMenu(AbstractContainerMenu menu);
}

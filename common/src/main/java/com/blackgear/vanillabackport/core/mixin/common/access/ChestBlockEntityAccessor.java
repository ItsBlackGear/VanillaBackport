package com.blackgear.vanillabackport.core.mixin.common.access;

import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ChestBlockEntity.class)
public interface ChestBlockEntityAccessor {
    @Accessor ContainerOpenersCounter getOpenersCounter();
    
    @Mutable @Accessor void setOpenersCounter(ContainerOpenersCounter counter);
}
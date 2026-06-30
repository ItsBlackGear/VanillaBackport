package com.blackgear.vanillabackport.core.mixin.common.access;

import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BarrelBlockEntity.class)
public interface BarrelBlockEntityAccessor {
    @Accessor ContainerOpenersCounter getOpenersCounter();
}
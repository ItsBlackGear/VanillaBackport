package com.blackgear.vanillabackport.core.mixin.common.access;

import net.minecraft.world.LockCode;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BaseContainerBlockEntity.class)
public interface BaseContainerBlockEntityAccessor {
    @Accessor LockCode getLockKey();
}

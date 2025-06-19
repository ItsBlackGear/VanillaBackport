package com.blackgear.vanillabackport.core.mixin.access;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityAccessor {
    @Accessor EntityDimensions getDimensions();
    @Invoker void callReapplyPosition();
    @Invoker void callSetRot(float yRot, float xRot);
}
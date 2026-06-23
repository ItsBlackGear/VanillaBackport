package com.blackgear.vanillabackport.core.mixin.common.access;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(Entity.class)
public interface EntityAccessor {
    @Invoker BlockPos callGetBlockPosBelowThatAffectsMyMovement();
    @Accessor EntityDimensions getDimensions();
    @Invoker void callSetRot(float yRot, float xRot);
    @Invoker void callReapplyPosition();
    @Invoker Vec3 callCalculateViewVector(float xRot, float yRot);
}
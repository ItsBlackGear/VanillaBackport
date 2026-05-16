package com.blackgear.vanillabackport.core.mixin.access;

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
    @Invoker
    static Vec3 callCollideWithShapes(Vec3 deltaMovement, AABB entityBB, List<VoxelShape> shapes) {
        throw new UnsupportedOperationException();
    }

    @Invoker BlockPos callGetBlockPosBelowThatAffectsMyMovement();
    @Accessor EntityDimensions getDimensions();
    @Invoker void callReapplyPosition();
    @Invoker void callSetRot(float yRot, float xRot);

    @Invoker
    Vec3 callCollide(Vec3 vec);

    @Accessor
    float getNextStep();

    @Accessor
    void setNextStep(float nextStep);

    @Invoker
    boolean callVibrationAndSoundEffectsFromBlock(BlockPos pos, BlockState state, boolean playStepSound, boolean broadcastGameEvent, Vec3 vec3);

    @Invoker
    boolean callIsStateClimbable(BlockState state);

    @Invoker Vec3 callCalculateViewVector(float xRot, float yRot);
}
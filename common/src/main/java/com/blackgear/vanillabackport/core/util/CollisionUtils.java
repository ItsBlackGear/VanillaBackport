package com.blackgear.vanillabackport.core.util;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CollisionUtils {
    public static boolean intersects(AABB box, BlockPos pos) {
        return box.intersects(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
    }

    /**
     * Checks if an entity would collide with fluid when moving from origin to target
     */
    public static boolean collidedWithFluid(LivingEntity entity, FluidState state, BlockPos pos, Vec3 origin, Vec3 target) {
        AABB box = getFluidAABB(state, entity.level(), pos);
        return box != null && collidedWithShapeMovingFrom(entity, origin, target, List.of(box));
    }

    /**
     * Checks if an entity would collide with any shapes when moving between positions
     */
    public static boolean collidedWithShapeMovingFrom(LivingEntity entity, Vec3 origin, Vec3 target, List<AABB> boxes) {
        AABB box = entity.dimensions.makeBoundingBox(origin);
        Vec3 distance = target.subtract(origin);
        return collidedAlongVector(box, distance, boxes);
    }

    /**
     * Creates a bounding box for a fluid at the given position
     */
    @Nullable
    public static AABB getFluidAABB(FluidState state, BlockGetter level, BlockPos pos) {
        if (state.isEmpty()) return null;

        float fluidHeight = state.getHeight(level, pos);
        return new AABB(
            pos.getX(), pos.getY(), pos.getZ(),
            (double) pos.getX() + 1.0, (float) pos.getY() + fluidHeight, (double) pos.getZ() + 1.0
        );
    }

    /**
     * Determines if an entity box would collide with any obstacles when moving along a vector
     */
    public static boolean collidedAlongVector(AABB entityBox, Vec3 origin, List<AABB> obstacles) {
        Vec3 center = entityBox.getCenter();
        Vec3 distance = center.add(origin);

        for (AABB obstacle : obstacles) {
            AABB box = obstacle.inflate(
                entityBox.getXsize() * 0.5,
                entityBox.getYsize() * 0.5,
                entityBox.getZsize() * 0.5
            );

            if (box.contains(distance) || box.contains(center)) return true;

            if (box.clip(center, distance).isPresent()) return true;
        }

        return false;
    }
}
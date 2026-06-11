package com.blackgear.vanillabackport.core.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CollisionUtils {
    public static Vec3 getMinPosition(AABB aabb) {
        return new Vec3(aabb.minX, aabb.minY, aabb.minZ);
    }
    
    public static Vec3 getMaxPosition(AABB aabb) {
        return new Vec3(aabb.maxX, aabb.maxY, aabb.maxZ);
    }
    
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
    public static boolean collidedAlongVector(AABB box, Vec3 origin, List<AABB> obstacles) {
        Vec3 center = box.getCenter();
        Vec3 distance = center.add(origin);

        for (AABB obstacle : obstacles) {
            AABB inflated = obstacle.inflate(box.getXsize() * 0.5 - 1.0E-7, box.getYsize() * 0.5 - 1.0E-7, box.getZsize() * 0.5 - 1.0E-7);
            if (inflated.contains(distance) || inflated.contains(center)) {
                return true;
            }

            if (inflated.clip(center, distance).isPresent()) {
                return true;
            }
        }

        return false;
    }

    public static CollisionContext positionContext(double y) {
        return new PositionCollisionContext(y);
    }

    private static class PositionCollisionContext extends EntityCollisionContext {
        private final double y;

        private PositionCollisionContext(double y) {
            super(false, -Double.MAX_VALUE, ItemStack.EMPTY, fluidState -> false, null);
            this.y = y;
        }

        @Override
        public boolean isDescending() {
            return false;
        }

        @Override
        public boolean isAbove(VoxelShape shape, BlockPos pos, boolean canAscend) {
            return this.y > pos.getY() + shape.max(Direction.Axis.Y) - Mth.EPSILON;
        }

        @Override
        public boolean isHoldingItem(Item item) {
            return false;
        }

        @Override
        public boolean canStandOnFluid(FluidState fluid1, FluidState fluid2) {
            return false;
        }
    }
}
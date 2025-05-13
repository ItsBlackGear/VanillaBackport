package com.blackgear.vanillabackport.core.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockCollisions;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CollisionUtils {
    public static boolean noCollision(CollisionGetter level, Entity entity, AABB box, boolean inLiquid) {
        Iterable<VoxelShape> collisions = inLiquid ? getBlockAndLiquidCollisions(level, entity, box) : level.getBlockCollisions(entity, box);

        for (VoxelShape shape : collisions) {
            if (!shape.isEmpty()) {
                return false;
            }
        }

        // Check for entity collisions
        if (!level.getEntityCollisions(entity, box).isEmpty()) {
            return false;
        }

        // Check for world border collisions
        if (entity != null) {
            VoxelShape shape = borderCollision(level, entity, box);
            return shape == null || !Shapes.joinIsNotEmpty(shape, Shapes.create(box), BooleanOp.AND);
        }

        return true;
    }

    public static boolean noBlockCollision(CollisionGetter level, @Nullable Entity entity, AABB aABB) {
        for (VoxelShape shape : level.getBlockCollisions(entity, aABB)) {
            if (!shape.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    private static Iterable<VoxelShape> getBlockAndLiquidCollisions(CollisionGetter level, Entity entity, AABB box) {
        return () -> new BlockCollisions<>(level, entity, box, false, (pos, shape) -> shape);
    }

    @Nullable
    private static VoxelShape borderCollision(CollisionGetter level, Entity entity, AABB box) {
        WorldBorder border = level.getWorldBorder();
        return border.isInsideCloseToBorder(entity, box) ? border.getCollisionShape() : null;
    }
}
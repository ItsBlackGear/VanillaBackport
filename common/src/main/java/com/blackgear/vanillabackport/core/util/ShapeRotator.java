package com.blackgear.vanillabackport.core.util;

import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.Map;

public class ShapeRotator {
    private static final Vec3 BLOCK_CENTER = new Vec3(0.5, 0.5, 0.5);

    public static VoxelShape rotate(VoxelShape shape, Direction direction) {
        return rotate(shape, direction, BLOCK_CENTER);
    }

    public static VoxelShape rotate(VoxelShape shape, Direction direction, Vec3 rotationPoint) {
        if (direction.getAxis().isVertical()) {
            throw new IllegalArgumentException("Cannot rotate shape to vertical direction: " + direction);
        }

        if (direction == Direction.NORTH) {
            return shape;
        }

        VoxelShape[] buffer = new VoxelShape[]{ Shapes.empty() };
        int rotations = getRotationsFromNorth(direction);

        final double pivotX = rotationPoint.x;
        final double pivotZ = rotationPoint.z;

        shape.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
            double rMinX = minX;
            double rMinZ = minZ;
            double rMaxX = maxX;
            double rMaxZ = maxZ;

            for (int i = 0; i < rotations; i++) {
                double tempMinX = rMinX;
                double tempMinZ = rMinZ;
                double tempMaxX = rMaxX;
                double tempMaxZ = rMaxZ;

                tempMinX -= pivotX;
                tempMaxX -= pivotX;
                tempMinZ -= pivotZ;
                tempMaxZ -= pivotZ;

                rMinX = -tempMaxZ + pivotX;
                rMaxX = -tempMinZ + pivotX;
                rMinZ = tempMinX + pivotZ;
                rMaxZ = tempMaxX + pivotZ;

                if (rMinX > rMaxX) {
                    double temp = rMinX;
                    rMinX = rMaxX;
                    rMaxX = temp;
                }
                if (rMinZ > rMaxZ) {
                    double temp = rMinZ;
                    rMinZ = rMaxZ;
                    rMaxZ = temp;
                }
            }

            buffer[0] = Shapes.or(buffer[0], Shapes.box(rMinX, minY, rMinZ, rMaxX, maxY, rMaxZ));
        });

        return buffer[0];
    }
    
    public static Map<Direction, VoxelShape> rotateHorizontal(VoxelShape northShape) {
        return rotateHorizontal(northShape, BLOCK_CENTER);
    }

    public static Map<Direction, VoxelShape> rotateHorizontal(VoxelShape northShape, Vec3 rotationPoint) {
        Map<Direction, VoxelShape> shapes = new EnumMap<>(Direction.class);

        shapes.put(Direction.NORTH, northShape);
        shapes.put(Direction.EAST, rotate(northShape, Direction.EAST, rotationPoint));
        shapes.put(Direction.SOUTH, rotate(northShape, Direction.SOUTH, rotationPoint));
        shapes.put(Direction.WEST, rotate(northShape, Direction.WEST, rotationPoint));

        return shapes;
    }

    private static int getRotationsFromNorth(Direction direction) {
        return switch (direction) {
            case NORTH -> 0;
            case EAST -> 1;
            case SOUTH -> 2;
            case WEST -> 3;
            default -> throw new IllegalArgumentException("Invalid horizontal direction: " + direction);
        };
    }

    public static VoxelShape rotateBox(
        double minX,
        double minY,
        double minZ,
        double maxX,
        double maxY,
        double maxZ,
        Direction direction
    ) {
        return rotateBox(minX, minY, minZ, maxX, maxY, maxZ, direction, BLOCK_CENTER);
    }

    public static VoxelShape rotateBox(
        double minX,
        double minY,
        double minZ,
        double maxX,
        double maxY,
        double maxZ,
        Direction direction,
        Vec3 rotationPoint
    ) {
        if (direction.getAxis().isVertical()) {
            throw new IllegalArgumentException("Cannot rotate box to vertical direction: " + direction);
        }

        return rotate(Shapes.box(minX, minY, minZ, maxX, maxY, maxZ), direction, rotationPoint);
    }
}
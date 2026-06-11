package com.blackgear.vanillabackport.core.util;

import com.google.common.collect.ImmutableList;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public class DirectionUtils {
    private static final ImmutableList<Direction.Axis> YXZ_AXIS_ORDER = ImmutableList.of(Direction.Axis.Y, Direction.Axis.X, Direction.Axis.Z);
    private static final ImmutableList<Direction.Axis> YZX_AXIS_ORDER = ImmutableList.of(Direction.Axis.Y, Direction.Axis.Z, Direction.Axis.X);
    
    public static ImmutableList<Direction.Axis> axisStepOrder(final Vec3 movement) {
        return Math.abs(movement.x) < Math.abs(movement.z) ? YZX_AXIS_ORDER : YXZ_AXIS_ORDER;
    }
    
    public static Direction getPositive(Direction.Axis axis) {
        return switch (axis) {
            case X -> Direction.EAST;
            case Y -> Direction.UP;
            case Z -> Direction.SOUTH;
        };
    }
    
    public static Direction getNegative(Direction.Axis axis) {
        return switch (axis) {
            case X -> Direction.WEST;
            case Y -> Direction.DOWN;
            case Z -> Direction.NORTH;
        };
    }
    
    public static Direction[] getDirections(Direction.Axis axis) {
        return new Direction[]{getPositive(axis), getNegative(axis)};
    }
}
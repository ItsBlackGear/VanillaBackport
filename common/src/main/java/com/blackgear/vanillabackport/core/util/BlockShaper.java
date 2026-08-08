package com.blackgear.vanillabackport.core.util;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.EnumMap;
import java.util.Map;

public class BlockShaper {
    private static final Vec3 BLOCK_CENTER = new Vec3(0.5, 0.5, 0.5);
    
    public static VoxelShape cube(double size) {
        return cube(size, size, size);
    }
    
    public static VoxelShape cube(double sizeX, double sizeY, double sizeZ) {
        double halfY = sizeY / 2.0;
        return column(sizeX, sizeZ, 8.0 - halfY, 8.0 + halfY);
    }
    
    public static VoxelShape column(double sizeXZ, double minY, double maxY) {
        return column(sizeXZ, sizeXZ, minY, maxY);
    }
    
    public static VoxelShape column(double sizeX, double sizeZ, double minY, double maxY) {
        double halfX = sizeX / 2.0;
        double halfZ = sizeZ / 2.0;
        return Block.box(8.0 - halfX, minY, 8.0 - halfZ, 8.0 + halfX, maxY, 8.0 + halfZ);
    }
    
    public static VoxelShape boxZ(double sizeXY, double minZ, double maxZ) {
        return boxZ(sizeXY, sizeXY, minZ, maxZ);
    }
    
    public static VoxelShape boxZ(double sizeX, double sizeY, double minZ, double maxZ) {
        double halfY = sizeY / 2.0;
        return boxZ(sizeX, 8.0 - halfY, 8.0 + halfY, minZ, maxZ);
    }
    
    public static VoxelShape boxZ(double sizeX, double minY, double maxY, double minZ, double maxZ) {
        double halfX = sizeX / 2.0;
        return Block.box(8.0 - halfX, minY, minZ, 8.0 + halfX, maxY, maxZ);
    }
    
    public static VoxelShape rotate(VoxelShape shape, Direction direction) {
        return rotate(shape, direction, BLOCK_CENTER);
    }
    
    public static VoxelShape rotate(VoxelShape shape, Direction direction, Vec3 pivot) {
        if (direction == Direction.NORTH) {
            return shape;
        }
        
        VoxelShape[] buffer = new VoxelShape[]{ Shapes.empty() };
        
        shape.forAllBoxes((minX, minY, minZ, maxX, maxY, maxZ) -> {
            AABB rotatedBox = rotateBox(minX, minY, minZ, maxX, maxY, maxZ, direction, pivot);
            buffer[0] = Shapes.or(buffer[0], Shapes.create(rotatedBox));
        });
        
        return buffer[0];
    }
    
    private static AABB rotateBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ, Direction direction, Vec3 pivot) {
        double px = pivot.x;
        double py = pivot.y;
        double pz = pivot.z;
        
        return switch (direction) {
            case NORTH -> new AABB(minX, minY, minZ, maxX, maxY, maxZ);
            case SOUTH -> new AABB(2 * px - maxX, minY, 2 * pz - maxZ, 2 * px - minX, maxY, 2 * pz - minZ);
            case EAST  -> new AABB(px + pz - maxZ, minY, pz - px + minX, px + pz - minZ, maxY, pz - px + maxX);
            case WEST  -> new AABB(px - pz + minZ, minY, pz + px - maxX, px - pz + maxZ, maxY, pz + px - minX);
            case UP    -> new AABB(minX, py + pz - maxZ, pz - py + minY, maxX, py + pz - minZ, pz - py + maxY);
            case DOWN  -> new AABB(minX, py - pz + minZ, pz + py - maxY, maxX, py - pz + maxZ, pz + py - minY);
        };
    }
    
    public static Map<Direction, VoxelShape> rotateHorizontal(VoxelShape northShape) {
        return rotateHorizontal(northShape, BLOCK_CENTER);
    }
    
    public static Map<Direction, VoxelShape> rotateHorizontal(VoxelShape northShape, Vec3 pivot) {
        Map<Direction, VoxelShape> map = new EnumMap<>(Direction.class);
        for (Direction dir : Direction.Plane.HORIZONTAL) {
            map.put(dir, rotate(northShape, dir, pivot));
        }
        
        return map;
    }
    
    public static Map<Direction, VoxelShape> rotateAll(VoxelShape northShape) {
        return rotateAll(northShape, BLOCK_CENTER);
    }
    
    public static Map<Direction, VoxelShape> rotateAll(VoxelShape northShape, Vec3 pivot) {
        Map<Direction, VoxelShape> map = new EnumMap<>(Direction.class);
        for (Direction dir : Direction.values()) {
            map.put(dir, rotate(northShape, dir, pivot));
        }
        return map;
    }
    
    public static Map<AttachFace, Map<Direction, VoxelShape>> rotateAttachFace(VoxelShape northShape) {
        return rotateAttachFace(northShape, BLOCK_CENTER);
    }
    
    public static Map<AttachFace, Map<Direction, VoxelShape>> rotateAttachFace(VoxelShape northShape, Vec3 pivot) {
        Map<AttachFace, Map<Direction, VoxelShape>> map = new EnumMap<>(AttachFace.class);
        
        VoxelShape floorShape = rotate(northShape, Direction.UP, pivot);
        VoxelShape ceilingShape = rotate(rotate(northShape, Direction.DOWN, pivot), Direction.SOUTH, pivot);
        
        map.put(AttachFace.WALL, rotateHorizontal(northShape, pivot));
        map.put(AttachFace.FLOOR, rotateHorizontal(floorShape, pivot));
        map.put(AttachFace.CEILING, rotateHorizontal(ceilingShape, pivot));
        
        return map;
    }
}
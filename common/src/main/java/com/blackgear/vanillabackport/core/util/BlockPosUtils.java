package com.blackgear.vanillabackport.core.util;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class BlockPosUtils {
    /**
     * Returns an Iterable of all BlockPos contained within the given bounding box.
     */
    public static Iterable<BlockPos> betweenClosed(AABB box) {
        BlockPos min = BlockPos.containing(box.minX, box.minY, box.minZ);
        BlockPos max = BlockPos.containing(box.maxX, box.maxY, box.maxZ);
        return BlockPos.betweenClosed(min, max);
    }

    /**
     * Traverses all block positions between two points that intersect with a bounding box.
     * Visits each position with a visitor function that can control traversal.
     */
    public static boolean forEachBlockIntersectedBetween(Vec3 start, Vec3 end, AABB aabb, BlockStepVisitor visitor) {
        Vec3 direction = end.subtract(start);

        // For very small movements, just check blocks in the current bounding box
        if (direction.lengthSqr() < (double) Mth.square(0.99999F)) {
            for (BlockPos pos : betweenClosed(aabb)) {
                if (!visitor.visit(pos, 0)) {
                    return false;
                }
            }

            return true;
        } else { // For longer movements, trace along the path
            LongSet visitedPositions = new LongOpenHashSet();
            Vec3 minPosition = new Vec3(aabb.minX, aabb.minY, aabb.minZ);
            Vec3 startOffset = minPosition.subtract(direction);

            int stepCount = addCollisionsAlongTravel(visitedPositions, startOffset, minPosition, aabb, visitor);
            if (stepCount < 0) {
                return false;
            } else { // Check any remaining positions in the final bounding box
                for (BlockPos pos : betweenClosed(aabb)) {
                    if (!visitedPositions.contains(pos.asLong()) && !visitor.visit(pos, stepCount + 1)) {
                        return false;
                    }
                }

                return true;
            }
        }
    }

    /**
     * Traces collisions along a path and tracks visited block positions.
     */
    private static int addCollisionsAlongTravel(
        LongSet visitedPositions,
        Vec3 startPos,
        Vec3 endPos,
        AABB box,
        BlockStepVisitor visitor
    ) {
        Vec3 travel = endPos.subtract(startPos);

        // Current block coordinates
        int startX = Mth.floor(startPos.x);
        int startY = Mth.floor(startPos.y);
        int startZ = Mth.floor(startPos.z);

        // Direction signs for each axis
        int signX = Mth.sign(travel.x);
        int signY = Mth.sign(travel.y);
        int signZ = Mth.sign(travel.z);

        // Calculate inverse directions for DDA algorithm
        double invDirX = signX == 0 ? Double.MAX_VALUE : (double)signX / travel.x;
        double invDirY = signY == 0 ? Double.MAX_VALUE : (double)signY / travel.y;
        double invDirZ = signZ == 0 ? Double.MAX_VALUE : (double)signZ / travel.z;

        // Calculate initial intersection distances
        double targetMaxX = invDirX * (signX > 0 ? 1.0 - Mth.frac(startPos.x) : Mth.frac(startPos.x));
        double targetMaxY = invDirY * (signY > 0 ? 1.0 - Mth.frac(startPos.y) : Mth.frac(startPos.y));
        double targetMaxZ = invDirZ * (signZ > 0 ? 1.0 - Mth.frac(startPos.z) : Mth.frac(startPos.z));

        int stepsTaken = 0;
        BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos();

        // Digital Differential Analysis (DDA) algorithm for 3D line traversal
        while (targetMaxX <= 1.0 || targetMaxY <= 1.0 || targetMaxZ <= 1.0) {
            // Find axis with the closest intersection
            if (targetMaxX < targetMaxY) {
                if (targetMaxX < targetMaxZ) {
                    // X-axis is closest
                    startX += signX;
                    targetMaxX += invDirX;
                } else {
                    // Z-axis is closest
                    startZ += signZ;
                    targetMaxZ += invDirZ;
                }
            } else if (targetMaxY < targetMaxZ) {
                // Y-axis is closest
                startY += signY;
                targetMaxY += invDirY;
            } else {
                // Z-axis is closest
                startZ += signZ;
                targetMaxZ += invDirZ;
            }

            // Limit max steps to avoid infinite loops
            if (stepsTaken++ > 16) break;

            Optional<Vec3> intersectionPoint = clip(
                startX, startY, startZ,
                startX + 1, startY + 1, startZ + 1,
                startPos, endPos
            );

            if (intersectionPoint.isEmpty()) continue;

            Vec3 hitPoint = intersectionPoint.get();
            // Ensure hit point is slightly inside the block
            double x = Mth.clamp(hitPoint.x, (double)startX + (double)1.0E-5f, (double)startX + 1.0 - (double)1.0E-5f);
            double y = Mth.clamp(hitPoint.y, (double)startY + (double)1.0E-5f, (double)startY + 1.0 - (double)1.0E-5f);
            double z = Mth.clamp(hitPoint.z, (double)startZ + (double)1.0E-5f, (double)startZ + 1.0 - (double)1.0E-5f);

            // Calculate bounds of potential overlapping blocks
            int maxX = Mth.floor(x + box.getXsize());
            int maxY = Mth.floor(y + box.getYsize());
            int maxZ = Mth.floor(z + box.getZsize());

            // Visit all overlapping blocks
            for (int localX = startX; localX <= maxX; ++localX) {
                for (int localY = startY; localY <= maxY; ++localY) {
                    for (int localZ = startZ; localZ <= maxZ; ++localZ) {
                        if (visitedPositions.add(BlockPos.asLong(localX, localY, localZ)) && !visitor.visit(mutable.set(localX, localY, localZ), stepsTaken)) {
                            return -1;
                        }
                    }
                }
            }
        }

        return stepsTaken;
    }

    /**
     * Finds the intersection point between a ray and an axis-aligned box.
     */
    public static Optional<Vec3> clip(
        double minX, double minY, double minZ,
        double maxX, double maxY, double maxZ,
        Vec3 start, Vec3 end
    ) {
        double[] distances = new double[]{1.0};
        double dirX = end.x - start.x;
        double dirY = end.y - start.y;
        double dirZ = end.z - start.z;

        Direction direction = getDirection(
            minX, minY, minZ,
            maxX, maxY, maxZ,
            start,
            distances,
            null,
            dirX, dirY, dirZ
        );

        if (direction == null) return Optional.empty();

        double distance = distances[0];
        return Optional.of(start.add(distance * dirX, distance * dirY, distance * dirZ));
    }

    /**
     * Determines which face of a box is hit first by a ray.
     */
    @Nullable
    private static Direction getDirection(
        double minX, double minY, double minZ,
        double maxX, double maxY, double maxZ,
        Vec3 start,
        double[] minDistance,
        @Nullable Direction prevDirection,
        double dirX, double dirY, double dirZ
    ) {
        // Check X-axis faces
        if (dirX > 1.0E-7) {
            prevDirection = clipPoint(minDistance, prevDirection, dirX, dirY, dirZ, minX, minY, maxY, minZ, maxZ, Direction.WEST, start.x, start.y, start.z);
        } else if (dirX < -1.0E-7) {
            prevDirection = clipPoint(minDistance, prevDirection, dirX, dirY, dirZ, maxX, minY, maxY, minZ, maxZ, Direction.EAST, start.x, start.y, start.z);
        }

        // Check Y-axis faces
        if (dirY > 1.0E-7) {
            prevDirection = clipPoint(minDistance, prevDirection, dirY, dirZ, dirX, minY, minZ, maxZ, minX, maxX, Direction.DOWN, start.y, start.z, start.x);
        } else if (dirY < -1.0E-7) {
            prevDirection = clipPoint(minDistance, prevDirection, dirY, dirZ, dirX, maxY, minZ, maxZ, minX, maxX, Direction.UP, start.y, start.z, start.x);
        }

        // Check Z-axis faces
        if (dirZ > 1.0E-7) {
            prevDirection = clipPoint(minDistance, prevDirection, dirZ, dirX, dirY, minZ, minX, maxX, minY, maxY, Direction.NORTH, start.z, start.x, start.y);
        } else if (dirZ < -1.0E-7) {
            prevDirection = clipPoint(minDistance, prevDirection, dirZ, dirX, dirY, maxZ, minX, maxX, minY, maxY, Direction.SOUTH, start.z, start.x, start.y);
        }

        return prevDirection;
    }

    /**
     * Checks if a ray intersects with a specific face of a box.
     */
    @Nullable
    private static Direction clipPoint(
        double[] minDistance,
        @Nullable Direction prevDirection,
        double mainAxisDir, double secondAxisDir, double thirdAxisDir,
        double facePosition,
        double secondAxisMin, double secondAxisMax,
        double thirdAxisMin, double thirdAxisMax,
        Direction hitDirection,
        double startMain, double startSecond, double startThird
    ) {
        double intersectionTime = (facePosition - startMain) / mainAxisDir;
        double secondAxisPos = startSecond + intersectionTime * secondAxisDir;
        double thirdAxisPos = startThird + intersectionTime * thirdAxisDir;

        // Check if intersection is closer than previous ones and within face bounds
        if (0.0 < intersectionTime && intersectionTime < minDistance[0]
            && secondAxisMin - 1.0E-7 < secondAxisPos && secondAxisPos < secondAxisMax + 1.0E-7
            && thirdAxisMin - 1.0E-7 < thirdAxisPos && thirdAxisPos < thirdAxisMax + 1.0E-7
        ) {
            minDistance[0] = intersectionTime;
            return hitDirection;
        }

        return prevDirection;
    }

    @FunctionalInterface
    public interface BlockStepVisitor {
        /**
         * Called for each block position visited during traversal.
         *
         * @param pos The block position being visited
         * @param step The step count in the traversal
         * @return true to continue traversal, false to interrupt
         */
        boolean visit(BlockPos pos, int step);
    }
}
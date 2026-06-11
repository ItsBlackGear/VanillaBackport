package com.blackgear.vanillabackport.core.util;

import com.google.common.collect.AbstractIterator;
import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;
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

    public static boolean forEachBlockIntersectedBetween(Vec3 from, Vec3 to, AABB aabbAtTarget, BlockStepVisitor visitor) {
        Vec3 travel = to.subtract(from);
        if (travel.lengthSqr() < Mth.square(Mth.EPSILON)) {
            for (BlockPos pos : betweenClosed(aabbAtTarget)) {
                if (!visitor.visit(pos, 0)) {
                    return false;
                }
            }

            return true;
        } else {
            LongSet visitedBlocks = new LongOpenHashSet();
            for (BlockPos pos : betweenCornersInDirection(aabbAtTarget.move(travel.scale(-1.0)), travel)) {
                if (!visitor.visit(pos, 0)) {
                    return false;
                }
                
                visitedBlocks.add(pos.asLong());
            }

            int iterations = addCollisionsAlongTravel(visitedBlocks, travel, aabbAtTarget, visitor);
            if (iterations < 0) {
                return false;
            } else {
                for (BlockPos pos : betweenCornersInDirection(aabbAtTarget, travel)) {
                    if (!visitedBlocks.contains(pos.asLong()) && !visitor.visit(pos, iterations + 1)) {
                        return false;
                    }
                }

                return true;
            }
        }
    }
    
    public static Iterable<BlockPos> betweenCornersInDirection(AABB aabb, Vec3 direction) {
        Vec3 minCorner = CollisionUtils.getMinPosition(aabb);
        int firstCornerX = Mth.floor(minCorner.x());
        int firstCornerY = Mth.floor(minCorner.y());
        int firstCornerZ = Mth.floor(minCorner.z());
        Vec3 maxCorner = CollisionUtils.getMaxPosition(aabb);
        int secondCornerX = Mth.floor(maxCorner.x());
        int secondCornerY = Mth.floor(maxCorner.y());
        int secondCornerZ = Mth.floor(maxCorner.z());
        return betweenCornersInDirection(firstCornerX, firstCornerY, firstCornerZ, secondCornerX, secondCornerY, secondCornerZ, direction);
    }
    
    public static Iterable<BlockPos> betweenCornersInDirection(
        int firstCornerX,
        int firstCornerY,
        int firstCornerZ,
        int secondCornerX,
        int secondCornerY,
        int secondCornerZ,
        Vec3 direction
    ) {
        int minCornerX = Math.min(firstCornerX, secondCornerX);
        int minCornerY = Math.min(firstCornerY, secondCornerY);
        int minCornerZ = Math.min(firstCornerZ, secondCornerZ);
        int maxCornerX = Math.max(firstCornerX, secondCornerX);
        int maxCornerY = Math.max(firstCornerY, secondCornerY);
        int maxCornerZ = Math.max(firstCornerZ, secondCornerZ);
        int diffX = maxCornerX - minCornerX;
        int diffY = maxCornerY - minCornerY;
        int diffZ = maxCornerZ - minCornerZ;
        int startCornerX = direction.x >= 0.0 ? minCornerX : maxCornerX;
        int startCornerY = direction.y >= 0.0 ? minCornerY : maxCornerY;
        int startCornerZ = direction.z >= 0.0 ? minCornerZ : maxCornerZ;
        List<Direction.Axis> axes = DirectionUtils.axisStepOrder(direction);
        Direction.Axis firstVisitAxis = axes.get(0);
        Direction.Axis secondVisitAxis = axes.get(1);
        Direction.Axis thirdVisitAxis = axes.get(2);
        Direction firstVisitDir = direction.get(firstVisitAxis) >= 0.0 ? DirectionUtils.getPositive(firstVisitAxis) : DirectionUtils.getNegative(firstVisitAxis);
        Direction secondVisitDir = direction.get(secondVisitAxis) >= 0.0 ? DirectionUtils.getPositive(secondVisitAxis) : DirectionUtils.getNegative(secondVisitAxis);
        Direction thirdVisitDir = direction.get(thirdVisitAxis) >= 0.0 ? DirectionUtils.getPositive(thirdVisitAxis) : DirectionUtils.getNegative(thirdVisitAxis);
        int firstMax = firstVisitAxis.choose(diffX, diffY, diffZ);
        int secondMax = secondVisitAxis.choose(diffX, diffY, diffZ);
        int thirdMax = thirdVisitAxis.choose(diffX, diffY, diffZ);
        return () -> new AbstractIterator<>() {
            private final BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();
            private int firstIndex;
            private int secondIndex;
            private int thirdIndex;
            private boolean end;
            private final int firstDirX = firstVisitDir.getStepX();
            private final int firstDirY = firstVisitDir.getStepY();
            private final int firstDirZ = firstVisitDir.getStepZ();
            private final int secondDirX = secondVisitDir.getStepX();
            private final int secondDirY = secondVisitDir.getStepY();
            private final int secondDirZ = secondVisitDir.getStepZ();
            private final int thirdDirX = thirdVisitDir.getStepX();
            private final int thirdDirY = thirdVisitDir.getStepY();
            private final int thirdDirZ = thirdVisitDir.getStepZ();
            
            protected BlockPos computeNext() {
                if (this.end) {
                    return this.endOfData();
                } else {
                    this.cursor
                        .set(
                            startCornerX + this.firstDirX * this.firstIndex + this.secondDirX * this.secondIndex + this.thirdDirX * this.thirdIndex,
                            startCornerY + this.firstDirY * this.firstIndex + this.secondDirY * this.secondIndex + this.thirdDirY * this.thirdIndex,
                            startCornerZ + this.firstDirZ * this.firstIndex + this.secondDirZ * this.secondIndex + this.thirdDirZ * this.thirdIndex
                        );
                    if (this.thirdIndex < thirdMax) {
                        this.thirdIndex++;
                    } else if (this.secondIndex < secondMax) {
                        this.secondIndex++;
                        this.thirdIndex = 0;
                    } else if (this.firstIndex < firstMax) {
                        this.firstIndex++;
                        this.thirdIndex = 0;
                        this.secondIndex = 0;
                    } else {
                        this.end = true;
                    }
                    
                    return this.cursor;
                }
            }
        };
    }
    
    private static int addCollisionsAlongTravel(
        LongSet visitedBlocks,
        Vec3 deltaMove,
        AABB box,
        BlockStepVisitor visitor
    ) {
        double boxSizeX = box.getXsize();
        double boxSizeY = box.getYsize();
        double boxSizeZ = box.getZsize();
        Vec3i cornerDir = getFurthestCorner(deltaMove);
        Vec3 toCenter = box.getCenter();
        Vec3 toCorner = new Vec3(
            toCenter.x() + boxSizeX * 0.5 * cornerDir.getX(),
            toCenter.y() + boxSizeY * 0.5 * cornerDir.getY(),
            toCenter.z() + boxSizeZ * 0.5 * cornerDir.getZ()
        );
        Vec3 fromCorner = toCorner.subtract(deltaMove);
        int cornerVisitedBlockX = Mth.floor(fromCorner.x);
        int cornerVisitedBlockY = Mth.floor(fromCorner.y);
        int cornerVisitedBlockZ = Mth.floor(fromCorner.z);
        int signX = Mth.sign(deltaMove.x);
        int signY = Mth.sign(deltaMove.y);
        int signZ = Mth.sign(deltaMove.z);
        double tDeltaX = signX == 0 ? Double.MAX_VALUE : signX / deltaMove.x;
        double tDeltaY = signY == 0 ? Double.MAX_VALUE : signY / deltaMove.y;
        double tDeltaZ = signZ == 0 ? Double.MAX_VALUE : signZ / deltaMove.z;
        double tX = tDeltaX * (signX > 0 ? 1.0 - Mth.frac(fromCorner.x) : Mth.frac(fromCorner.x));
        double tY = tDeltaY * (signY > 0 ? 1.0 - Mth.frac(fromCorner.y) : Mth.frac(fromCorner.y));
        double tZ = tDeltaZ * (signZ > 0 ? 1.0 - Mth.frac(fromCorner.z) : Mth.frac(fromCorner.z));
        int iterations = 0;

        while (tX <= 1.0 || tY <= 1.0 || tZ <= 1.0) {
            if (tX < tY) {
                if (tX < tZ) {
                    cornerVisitedBlockX += signX;
                    tX += tDeltaX;
                } else {
                    cornerVisitedBlockZ += signZ;
                    tZ += tDeltaZ;
                }
            } else if (tY < tZ) {
                cornerVisitedBlockY += signY;
                tY += tDeltaY;
            } else {
                cornerVisitedBlockZ += signZ;
                tZ += tDeltaZ;
            }

            Optional<Vec3> hitPointOpt = clip(
                cornerVisitedBlockX,
                cornerVisitedBlockY,
                cornerVisitedBlockZ,
                cornerVisitedBlockX + 1,
                cornerVisitedBlockY + 1,
                cornerVisitedBlockZ + 1,
                fromCorner,
                toCorner
            );

            if (hitPointOpt.isPresent()) {
                iterations++;
                Vec3 hitPoint = hitPointOpt.get();
                double cornerHitX = Mth.clamp(hitPoint.x, cornerVisitedBlockX + Mth.EPSILON, cornerVisitedBlockX + 1.0 - Mth.EPSILON);
                double cornerHitY = Mth.clamp(hitPoint.y, cornerVisitedBlockY + Mth.EPSILON, cornerVisitedBlockY + 1.0 - Mth.EPSILON);
                double cornerHitZ = Mth.clamp(hitPoint.z, cornerVisitedBlockZ + Mth.EPSILON, cornerVisitedBlockZ + 1.0 - Mth.EPSILON);
                int oppositeCornerX = Mth.floor(cornerHitX - boxSizeX * cornerDir.getX());
                int oppositeCornerY = Mth.floor(cornerHitY - boxSizeY * cornerDir.getY());
                int oppositeCornerZ = Mth.floor(cornerHitZ - boxSizeZ * cornerDir.getZ());
                int currentIteration = iterations;
                
                for (BlockPos pos : betweenCornersInDirection(
                    cornerVisitedBlockX, cornerVisitedBlockY, cornerVisitedBlockZ, oppositeCornerX, oppositeCornerY, oppositeCornerZ, deltaMove
                )) {
                    if (visitedBlocks.add(pos.asLong()) && !visitor.visit(pos, currentIteration)) {
                        return -1;
                    }
                }
            }
        }

        return iterations;
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
    
    private static Vec3i getFurthestCorner(final Vec3 direction) {
        double xDot = Math.abs(VecUtils.X_AXIS.dot(direction));
        double yDot = Math.abs(VecUtils.Y_AXIS.dot(direction));
        double zDot = Math.abs(VecUtils.Z_AXIS.dot(direction));
        int xSign = direction.x >= 0.0 ? 1 : -1;
        int ySign = direction.y >= 0.0 ? 1 : -1;
        int zSign = direction.z >= 0.0 ? 1 : -1;
        if (xDot <= yDot && xDot <= zDot) {
            return new Vec3i(-xSign, -zSign, ySign);
        } else {
            return yDot <= zDot ? new Vec3i(zSign, -ySign, -xSign) : new Vec3i(-ySign, xSign, -zSign);
        }
    }

    @FunctionalInterface
    public interface BlockStepVisitor {
        boolean visit(BlockPos pos, int iteration);
    }
}
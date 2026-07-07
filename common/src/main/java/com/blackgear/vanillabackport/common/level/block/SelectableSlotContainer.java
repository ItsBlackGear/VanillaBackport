package com.blackgear.vanillabackport.common.level.block;

import java.util.Optional;
import java.util.OptionalInt;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

public interface SelectableSlotContainer {
    int getRows();

    int getColumns();

    default OptionalInt getHitSlot(BlockHitResult hitResult, Direction blockFacing) {
        return getRelativeHitCoordinatesForBlockFace(hitResult, blockFacing).map(hitCoords -> {
            int row = getSection(1.0F - hitCoords.y, this.getRows());
            int column = getSection(hitCoords.x, this.getColumns());
            return OptionalInt.of(column + row * this.getColumns());
        }).orElseGet(OptionalInt::empty);
    }

    private static Optional<Vec2> getRelativeHitCoordinatesForBlockFace(BlockHitResult hitResult, Direction blockFacing) {
        Direction hitDirection = hitResult.getDirection();
        if (blockFacing != hitDirection) {
            return Optional.empty();
        } else {
            BlockPos hitBlockPos = hitResult.getBlockPos().relative(hitDirection);
            Vec3 relativeHit = hitResult.getLocation().subtract(hitBlockPos.getX(), hitBlockPos.getY(), hitBlockPos.getZ());
            double relativeX = relativeHit.x();
            double relativeY = relativeHit.y();
            double relativeZ = relativeHit.z();

            return switch (hitDirection) {
                case NORTH -> Optional.of(new Vec2((float) (1.0 - relativeX), (float) relativeY));
                case SOUTH -> Optional.of(new Vec2((float) relativeX, (float) relativeY));
                case WEST -> Optional.of(new Vec2((float) relativeZ, (float) relativeY));
                case EAST -> Optional.of(new Vec2((float) (1.0 - relativeZ), (float) relativeY));
                case DOWN, UP -> Optional.empty();
            };
        }
    }

    private static int getSection(float relativeCoordinate, int maxSections) {
        float targetedPixel = relativeCoordinate * 16.0F;
        float sectionSize = 16.0F / maxSections;
        return Mth.clamp(Mth.floor(targetedPixel / sectionSize), 0, maxSections - 1);
    }
}
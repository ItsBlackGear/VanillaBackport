package com.blackgear.vanillabackport.common.level.entities.mob.animal.golem.copper_golem;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.pathfinder.Path;
import org.jetbrains.annotations.Nullable;

public class CopperGolemPathNavigation extends GroundPathNavigation {
    private boolean canPathToTargetsBelowSurface;

    public CopperGolemPathNavigation(Mob mob, Level level) {
        super(mob, level);
    }

    @Override
    public @Nullable Path createPath(BlockPos pos, int reachRange) {
        LevelChunk chunk = this.level.getChunkSource().getChunkNow(SectionPos.blockToSectionCoord(pos.getX()), SectionPos.blockToSectionCoord(pos.getZ()));
        if (chunk == null) {
            return null;
        } else {
            if (!this.canPathToTargetsBelowSurface) {
                pos = this.findSurfacePosition(chunk, pos);
            }

            return super.createPath(pos, reachRange);
        }
    }

    private BlockPos findSurfacePosition(LevelChunk chunk, BlockPos pos) {
        if (chunk.getBlockState(pos).isAir()) {
            BlockPos.MutableBlockPos columnPos = pos.mutable().move(Direction.DOWN);

            while (columnPos.getY() >= this.level.getMinBuildHeight() && chunk.getBlockState(columnPos).isAir()) {
                columnPos.move(Direction.DOWN);
            }

            if (columnPos.getY() >= this.level.getMinBuildHeight()) {
                return columnPos.above();
            }

            columnPos.setY(pos.getY() + 1);

            while (columnPos.getY() <= this.level.getMaxBuildHeight() && chunk.getBlockState(columnPos).isAir()) {
                columnPos.move(Direction.UP);
            }

            pos = columnPos;
        }

        if (!chunk.getBlockState(pos).isSolid()) {
            return pos;
        } else {
            BlockPos.MutableBlockPos columnPos = pos.mutable().move(Direction.UP);

            while (columnPos.getY() >= this.level.getMaxBuildHeight() && chunk.getBlockState(columnPos).isSolid()) {
                columnPos.move(Direction.UP);
            }

            return columnPos.immutable();
        }
    }

    public void setCanPathToTargetsBelowSurface(boolean canPathToTargetsBelowSurface) {
        this.canPathToTargetsBelowSurface = canPathToTargetsBelowSurface;
    }
}
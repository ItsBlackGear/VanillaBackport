package com.blackgear.vanillabackport.common.api.extensions.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;
import java.util.Optional;

public interface SpreadableBonemealableBlock extends BonemealableBlock {
    static boolean hasSpreadableNeighbourPos(LevelReader level, BlockPos pos, BlockState state) {
        return getSpreadableNeighbourPos(Direction.Plane.HORIZONTAL.stream().toList(), level, pos, state).isPresent();
    }

    static Optional<BlockPos> findSpreadableNeighbourPos(Level level, BlockPos pos, BlockState state) {
        return getSpreadableNeighbourPos(Direction.Plane.HORIZONTAL.shuffledCopy(level.random), level, pos, state);
    }

    private static Optional<BlockPos> getSpreadableNeighbourPos(List<Direction> directions, LevelReader level, BlockPos pos, BlockState state) {
        for (Direction direction : directions) {
            BlockPos offset = pos.relative(direction);
            if (level.isEmptyBlock(offset) && state.canSurvive(level, offset)) {
                return Optional.of(offset);
            }
        }

        return Optional.empty();
    }
}
package com.blackgear.vanillabackport.common.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.state.BlockState;

public class SignalUtils {
    public static int getBestOwnOrNeighbourSignal(SignalGetter level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        return Math.max(level.getBestNeighborSignal(pos), state.isSignalSource() ? getOwnSignal(level, state, pos) : 0);
    }

    private static int getOwnSignal(SignalGetter level, BlockState state, BlockPos pos) {
        int best = 0;

        for (Direction direction : Direction.values()) {
            int signal = state.getSignal(level, pos, direction);
            if (signal >= 15) {
                return 15;
            }

            if (signal > best) {
                best = signal;
            }
        }

        return best;
    }
}
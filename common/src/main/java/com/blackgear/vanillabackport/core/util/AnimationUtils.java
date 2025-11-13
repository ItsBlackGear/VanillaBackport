package com.blackgear.vanillabackport.core.util;

import com.blackgear.vanillabackport.core.mixin.access.AnimationStateAccessor;
import net.minecraft.world.entity.AnimationState;

public class AnimationUtils {
    public static void fastForward(AnimationState state, int duration, float speed) {
        if (state.isStarted()) {
            ((AnimationStateAccessor)state).setAccumulatedTime(state.getAccumulatedTime() + (long) ((float) (duration * 1000) * speed));
        }
    }
}
package com.blackgear.vanillabackport.core.util;

import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;

public class MobUtils {
    public static boolean isPanicking(PathfinderMob mob) {
        if (mob.getBrain().hasMemoryValue(MemoryModuleType.IS_PANICKING)) {
            return mob.getBrain().getMemory(MemoryModuleType.IS_PANICKING).isPresent();
        } else {
            for (WrappedGoal goal : mob.goalSelector.getAvailableGoals()) {
                if (goal.isRunning() && goal.getGoal() instanceof PanicGoal) {
                    return true;
                }
            }

            return false;
        }
    }
}
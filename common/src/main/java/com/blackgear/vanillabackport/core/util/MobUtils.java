package com.blackgear.vanillabackport.core.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.WrappedGoal;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import org.jetbrains.annotations.Nullable;

import java.util.List;

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

    @Nullable
    public static <T extends Entity> T getNearestEntity(List<? extends T> entities, double x, double y, double z) {
        double best = -1.0;
        T result = null;

        for (T entity : entities) {
            double dist = entity.distanceToSqr(x, y, z);
            if (best == -1.0 || dist < best) {
                best = dist;
                result = entity;
            }
        }

        return result;
    }
}
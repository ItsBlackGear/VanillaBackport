package com.blackgear.vanillabackport.core.util;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class MobUtils {
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
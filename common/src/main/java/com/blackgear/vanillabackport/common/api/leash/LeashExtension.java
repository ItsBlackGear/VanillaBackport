package com.blackgear.vanillabackport.common.api.leash;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

public interface LeashExtension {
    static boolean getOrDefault(Entity entity, Predicate<LeashExtension> value, boolean defaultValue) {
        if (entity instanceof LeashExtension extension) {
            return value.test(extension);
        }

        return defaultValue;
    }

    static double getOrDefault(Entity entity, ToDoubleFunction<LeashExtension> value, double defaultValue) {
        if (entity instanceof LeashExtension extension) {
            return value.applyAsDouble(extension);
        }

        return defaultValue;
    }

    default double leashSnapDistance() {
        return 12.0;
    }

    default double leashElasticDistance() {
        return 6.0;
    }

    default boolean supportQuadLeash() {
        return false;
    }

    default boolean supportQuadLeashAsHolder() {
        return false;
    }

    default Vec3[] getQuadLeashOffsets() {
        return createQuadLeashOffsets((Entity) this, 0.0, 0.5, 0.5, 0.5);
    }

    static Vec3[] createQuadLeashOffsets(Entity entity, double forwardOffset, double sideOffset, double widthOffset, double heightOffset) {
        float entityWidth = entity.getBbWidth();
        double forward = forwardOffset * (double) entityWidth;
        double side = sideOffset * (double) entityWidth;
        double width = widthOffset * (double) entityWidth;
        double height = heightOffset * (double) entity.getBbHeight();

        return new Vec3[] {
            new Vec3(-width, height, side + forward),
            new Vec3(-width, height, -side + forward),
            new Vec3(width, height, -side + forward),
            new Vec3(width, height, side + forward)
        };
    }
}
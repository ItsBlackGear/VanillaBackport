package com.blackgear.vanillabackport.common.api.leash;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;

public interface LeashAccess {
    static LeashAccess of(Mob entity) {
        if (entity instanceof LeashAccess access) {
            return access;
        }

        throw new IllegalArgumentException("Entity does not implement LeashAccess: " + entity);
    }

    default double angularMomentum() {
        return 0.0;
    }

    default void setAngularMomentum(double angularMomentum) { }

    default void closeRangeLeashBehavior(Entity entity) { }

    default void onElasticLeashPull(Entity entity) { }

    default void leashTooFarBehaviour() { }
}
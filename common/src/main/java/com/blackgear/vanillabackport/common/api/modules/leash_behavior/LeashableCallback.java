package com.blackgear.vanillabackport.common.api.modules.leash_behavior;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public interface LeashableCallback {
    default double vb$leashSnapDistance() { return LeashPhysics.LEASH_TOO_FAR_DIST; }

    default double vb$leashElasticDistance() { return LeashPhysics.LEASH_ELASTIC_DIST; }

    default boolean vb$supportsQuadLeash() {
        Entity entity = (Entity) this;
        for (Predicate<Entity> filter : LeashPhysics.QUAD_LEASH_OFFSETS.keySet()) {
            if (filter.test(entity)) {
                return true;
            }
        }

        return false;
    }

    default Vec3[] vb$getQuadLeashOffsets() {
        Entity entity = (Entity) this;
        for (Predicate<Entity> filter : LeashPhysics.QUAD_LEASH_OFFSETS.keySet()) {
            if (filter.test(entity)) {
                return LeashPhysics.QUAD_LEASH_OFFSETS.get(filter).apply(entity);
            }
        }

        return LeashPhysics.createQuadOffsets((Entity) this, 0.0, 0.5, 0.5, 0.5);
    }

    default void vb$onElasticLeashPull() {
        ((Entity) this).checkSlowFallDistance();
    }

    default boolean vb$supportsQuadLeashAsHolder() { return false; }

    default Vec3[] vb$getQuadLeashHolderOffsets() {
        return LeashPhysics.createQuadOffsets((Entity) this, 0.0, 0.5, 0.5, 0.0);
    }

    default void vb$whenLeashedTo(Entity leashHolder) {
        Entity entity = (Entity) this;
        if (entity instanceof PathfinderMob mob) {
            mob.restrictTo(leashHolder.blockPosition(), (int) this.vb$leashElasticDistance() - 1);
        }

        if (entity instanceof Leashable leashable) {
            ((LeashableCallback) leashHolder).vb$notifyLeashHolder(leashable);
        }
    }

    default void vb$notifyLeashHolder(Leashable leashee) {}
}
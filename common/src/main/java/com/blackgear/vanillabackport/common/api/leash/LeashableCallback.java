package com.blackgear.vanillabackport.common.api.leash;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public interface LeashableCallback {
    default double vb$leashSnapDistance() { return LeashPhysics.LEASH_TOO_FAR_DIST; }

    default double vb$leashElasticDistance() { return LeashPhysics.LEASH_ELASTIC_DIST; }

    default boolean vb$supportsQuadLeash() { return true; }

    default Vec3[] vb$getQuadLeashOffsets() {
        return LeashPhysics.createQuadOffsets((Entity) this, 0.0, 0.5, 0.5, 0.5);
    }

    default void vb$onElasticLeashPull() {
        ((Entity) this).checkSlowFallDistance();
    }
}
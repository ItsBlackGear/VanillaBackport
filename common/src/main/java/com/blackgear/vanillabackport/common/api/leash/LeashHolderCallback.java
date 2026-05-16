package com.blackgear.vanillabackport.common.api.leash;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.phys.Vec3;

public interface LeashHolderCallback {
    default boolean vb$supportsQuadLeashAsHolder() { return false; }

    default Vec3[] vb$getQuadLeashHolderOffsets() {
        return LeashPhysics.createQuadOffsets((Entity) this, 0.0, 0.5, 0.5, 0.0);
    }

    default void vb$notifyLeashHolder(Leashable leashee) {}
}
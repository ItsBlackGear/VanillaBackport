package com.blackgear.vanillabackport.common.api.extensions;

import com.blackgear.vanillabackport.core.mixin.access.EntityAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public interface MotionAwareEntity {
    default Vec3 getHeadLookAngle() {
        Entity self = (Entity) this;
        return ((EntityAccessor) self).callCalculateViewVector(self.getXRot(), self.getYHeadRot());
    }

    Vec3 getKnownSpeed();

    void computeSpeed();
}
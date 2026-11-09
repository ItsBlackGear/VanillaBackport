package com.blackgear.vanillabackport.core.util;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;

public interface IUpdateExplosionCalculator {
    default boolean shouldDamageEntity(Explosion explosion, Entity entity) {
        return true;
    }
    
    default float getKnockbackMultiplier(Entity entity) {
        return 1.0f;
    }

    default float getEntityDamageAmount(Explosion explosion, Entity entity) {
        float f = explosion.radius * 2.0f;
        Vec3 vec3 = new Vec3(explosion.x, explosion.y, explosion.z);
        double d0 = Math.sqrt(entity.distanceToSqr(vec3)) / (double) f;
        double d1 = (1.0d - d0) * (double) WorldUtilities.ExplosionUtils.getSeenPercent(vec3, entity);
        return (float) (((((d1 * d1) + d1) / 2.0d) * 7.0d * (double) f) + 1.0d);
    }
}
package com.blackgear.vanillabackport.common.level.entities.projectile.wind_charge;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public interface AffectedByExplosion {
    AffectedByExplosion DEFAULT = new AffectedByExplosion() {};
    
    static AffectedByExplosion of(Entity entity) {
        return entity instanceof AffectedByExplosion holder ? holder : null;
    }
    
    default void setCurrentExplosionImpactPos(Vec3 pos) { /* NO-OP */ }
    
    default void setCurrentExplosionCause(Entity entity) { /* NO-OP */ }
}
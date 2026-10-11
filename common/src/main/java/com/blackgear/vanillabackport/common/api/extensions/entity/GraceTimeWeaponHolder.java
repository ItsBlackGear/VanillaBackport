package com.blackgear.vanillabackport.common.api.extensions.entity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public interface GraceTimeWeaponHolder {
    GraceTimeWeaponHolder DEFAULT = new GraceTimeWeaponHolder() {};
    
    static GraceTimeWeaponHolder of(Entity entity) {
        return entity instanceof GraceTimeWeaponHolder holder ? holder : null;
    }
    
    default void setIgnoreFallDamageFromCurrentImpulse(boolean ignoreFallDamage, Vec3 newImpulseImpactPos) { /* NO-OP */ }
    
    default void applyPostImpulseGraceTime(int ticks) { /* NO-OP */ }
    
    default boolean isIgnoringFallDamageFromCurrentImpulse() {
        return false;
    }
    
    default void tryResetCurrentImpulseContext() { /* NO-OP */ }
    
    default boolean isInPostImpulseGraceTime() {
        return false;
    }
    
    default void resetCurrentImpulseContext() { /* NO-OP */ }
}
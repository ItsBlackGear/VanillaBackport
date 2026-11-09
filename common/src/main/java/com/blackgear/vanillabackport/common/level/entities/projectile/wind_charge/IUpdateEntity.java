package com.blackgear.vanillabackport.common.level.entities.projectile.wind_charge;

import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.Nullable;

public interface IUpdateEntity {
    default void vb$onExplosionHit(@Nullable Entity explosionCausedBy) {
    }
}

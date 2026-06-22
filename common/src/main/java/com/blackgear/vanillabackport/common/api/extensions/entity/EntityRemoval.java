package com.blackgear.vanillabackport.common.api.extensions.entity;

import net.minecraft.world.entity.Entity;

public interface EntityRemoval {
    default void onRemoval(Entity.RemovalReason reason) { /* NO-OP */ }
}
package com.blackgear.vanillabackport.common.api.extensions.entity.mounts;

import net.minecraft.world.entity.Entity;

public interface ControllableMob {
    ControllableMob DEFAULT = new ControllableMob() { };
    
    static ControllableMob of(Entity entity) {
        return entity instanceof ControllableMob controllable ? controllable : DEFAULT;
    }
    
    default boolean isMobControlled() {
        return false;
    }
    
    default float chargeSpeedModifier() {
        return 1.0F;
    }
}
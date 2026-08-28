package com.blackgear.vanillabackport.common.level.entities.mob.animal.golem.copper_golem;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;

public interface ContainerUser {
    boolean hasContainerOpen(ContainerOpenersCounter container, BlockPos blockPos);
    
    default LivingEntity getLivingEntity() {
        if (this instanceof LivingEntity living) {
            return living;
        } else {
            throw new IllegalStateException("A container user must be a LivingEntity");
        }
    }
}
package com.blackgear.vanillabackport.common.level.entities.mob.animal.golem.copper_golem.container;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public interface ContainerAdapter {
    boolean supports(BlockEntity blockEntity);
    
    void startOpen(BlockEntity blockEntity, ContainerUser user);
    
    void stopOpen(BlockEntity blockEntity, ContainerUser user);
    
    List<LivingEntity> getEntitiesWithContainerOpen(BlockEntity blockEntity);
}
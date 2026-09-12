package com.blackgear.vanillabackport.common.level.entities.mob.animal.golem.copper_golem.container;

import com.blackgear.platform.core.Environment;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class DefaultContainerAdapter implements ContainerAdapter {
    @Override
    public boolean supports(BlockEntity blockEntity) {
        return blockEntity instanceof Container;
    }
    
    @Override
    public void startOpen(BlockEntity blockEntity, ContainerUser user) {
        if (ContainerManager.isValid(blockEntity, user) && blockEntity.getLevel() instanceof ServerLevel server) {
            ((Container) blockEntity).startOpen(Environment.getOrCreateFakePlayer(server));
        }
    }
    
    @Override
    public void stopOpen(BlockEntity blockEntity, ContainerUser user) {
        if (ContainerManager.isValid(blockEntity, user) && blockEntity.getLevel() instanceof ServerLevel server) {
            ((Container) blockEntity).stopOpen(Environment.getOrCreateFakePlayer(server));
        }
    }
    
    @Override
    public List<LivingEntity> getEntitiesWithContainerOpen(BlockEntity blockEntity) {
        return ContainerManager.getEntitiesWithContainerOpen(
            blockEntity,
            ContainerManager.DEFAULT_RANGE,
            (entity, pos) -> entity instanceof ContainerUser user && user.hasContainerOpen(null, pos)
        );
    }
}
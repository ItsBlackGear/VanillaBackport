package com.blackgear.vanillabackport.common.level.entities.mob.animal.golem.copper_golem.container;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiPredicate;

public class ContainerManager {
    private static final List<ContainerAdapter> ADAPTERS = new CopyOnWriteArrayList<>();
    private static final ContainerAdapter DEFAULT = new DefaultContainerAdapter();
    public static final double DEFAULT_RANGE = 7.0;
    
    static {
        registerAdapter(new ContainerOpenersCounterAdapter());
    }
    
    public static void registerAdapter(ContainerAdapter adapter) {
        ADAPTERS.addFirst(adapter);
    }
    
    public static ContainerAdapter getAdapter(BlockEntity blockEntity) {
        if (blockEntity == null) return DEFAULT;
        
        return ADAPTERS.stream()
            .filter(adapter -> adapter.supports(blockEntity))
            .findFirst()
            .orElse(DEFAULT);
    }
    
    public static BlockPos getConnectedBlockPos(BlockPos pos, BlockState state) {
        Direction connectedDirection = ChestBlock.getConnectedDirection(state);
        return pos.relative(connectedDirection);
    }
    
    public static void startOpen(BlockEntity blockEntity, ContainerUser user) {
        getAdapter(blockEntity).startOpen(blockEntity, user);
    }
    
    public static void stopOpen(BlockEntity blockEntity, ContainerUser user) {
        getAdapter(blockEntity).stopOpen(blockEntity, user);
    }
    
    public static List<LivingEntity> getEntitiesWithContainerOpen(BlockEntity container) {
        return getAdapter(container).getEntitiesWithContainerOpen(container);
    }
    
    public static List<LivingEntity> getEntitiesWithContainerOpen(BlockEntity blockEntity, double range, BiPredicate<LivingEntity, BlockPos> isOpenPredicate) {
        Level level = blockEntity.getLevel();
        if (level == null) return List.of();
        
        BlockPos pos = blockEntity.getBlockPos();
        AABB searchBox = new AABB(pos).inflate(range);
        
        return level.getEntitiesOfClass(LivingEntity.class, searchBox, entity -> {
            if (entity.isSpectator()) return false;
            return isOpenPredicate.test(entity, pos);
        });
    }
    
    public static boolean isValid(BlockEntity blockEntity, ContainerUser user) {
        return blockEntity != null && !blockEntity.isRemoved() && !user.getLivingEntity().isSpectator();
    }
}
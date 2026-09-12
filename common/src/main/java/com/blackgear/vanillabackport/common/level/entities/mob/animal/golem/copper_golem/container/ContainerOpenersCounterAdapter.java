package com.blackgear.vanillabackport.common.level.entities.mob.animal.golem.copper_golem.container;

import com.blackgear.vanillabackport.core.mixin.common.access.ContainerOpenersCounterAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ContainerOpenersCounterAdapter implements ContainerAdapter {
    private static final Map<Class<?>, Optional<Field>> COUNTER_FIELD_CACHE = new ConcurrentHashMap<>();
    
    @Override
    public boolean supports(BlockEntity blockEntity) {
        return getCounter(blockEntity).isPresent();
    }
    
    public static Optional<ContainerOpenersCounter> getCounter(BlockEntity container) {
        if (container == null) return Optional.empty();
        
        Field counter = COUNTER_FIELD_CACHE.computeIfAbsent(container.getClass(), ContainerOpenersCounterAdapter::tryFetchCounter).orElse(null);
        if (counter == null) return Optional.empty();
        
        try {
            return Optional.ofNullable((ContainerOpenersCounter) counter.get(container));
        } catch (IllegalAccessException e) {
            return Optional.empty();
        }
    }
    
    private static Optional<Field> tryFetchCounter(Class<?> clazz) {
        Class<?> current = clazz;
        while (current != null && current != BlockEntity.class) {
            for (Field field : current.getDeclaredFields()) {
                if (ContainerOpenersCounter.class.isAssignableFrom(field.getType())) {
                    field.setAccessible(true);
                    return Optional.of(field);
                }
            }
            current = current.getSuperclass();
        }
        return Optional.empty();
    }
    
    @Override
    public void startOpen(BlockEntity blockEntity, ContainerUser user) {
        if (!ContainerManager.isValid(blockEntity, user)) return;
        
        getCounter(blockEntity).ifPresent(counter -> {
            Level level = blockEntity.getLevel();
            incrementOpeners(counter, user.getLivingEntity(), level, blockEntity.getBlockPos(), blockEntity.getBlockState(), user.getContainerInteractionRange());
            
            handleDoubleChest(blockEntity, level, (connectedCounter, connectedPos, connectedState) ->
                incrementOpeners(connectedCounter, user.getLivingEntity(), level, connectedPos, connectedState, user.getContainerInteractionRange())
            );
        });
    }
    
    @Override
    public void stopOpen(BlockEntity blockEntity, ContainerUser user) {
        if (!ContainerManager.isValid(blockEntity, user)) return;
        
        getCounter(blockEntity).ifPresent(counter -> {
            Level level = blockEntity.getLevel();
            decrementOpeners(counter, user.getLivingEntity(), level, blockEntity.getBlockPos(), blockEntity.getBlockState());
            
            handleDoubleChest(blockEntity, level, (connectedCounter, connectedPos, connectedState) ->
                decrementOpeners(connectedCounter, user.getLivingEntity(), level, connectedPos, connectedState)
            );
        });
    }
    
    private static void incrementOpeners(ContainerOpenersCounter counter, LivingEntity entity, Level level, BlockPos pos, BlockState state, double maxInteractionRange) {
        int previous = counter.openCount++;
        if (previous == 0) {
            ((ContainerOpenersCounterAccessor) counter).callOnOpen(level, pos, state);
            level.gameEvent(entity, GameEvent.CONTAINER_OPEN, pos);
        }
        
        ((ContainerOpenersCounterAccessor) counter).callOpenerCountChanged(level, pos, state, previous, counter.openCount);
        counter.maxInteractionRange = Math.max(maxInteractionRange, counter.maxInteractionRange);
    }
    
    private static void decrementOpeners(ContainerOpenersCounter counter, LivingEntity entity, Level level, BlockPos pos, BlockState state) {
        int previous = counter.openCount--;
        if (counter.openCount == 0) {
            ((ContainerOpenersCounterAccessor) counter).callOnClose(level, pos, state);
            level.gameEvent(entity, GameEvent.CONTAINER_CLOSE, pos);
            counter.maxInteractionRange = 0.0;
        }
        
        ((ContainerOpenersCounterAccessor) counter).callOpenerCountChanged(level, pos, state, previous, counter.openCount);
    }
    
    @Override
    public List<LivingEntity> getEntitiesWithContainerOpen(BlockEntity blockEntity) {
        if (blockEntity == null || blockEntity.getLevel() == null) return List.of();
        
        Optional<ContainerOpenersCounter> counterOpt = getCounter(blockEntity);
        if (counterOpt.isEmpty()) return List.of();
        
        ContainerOpenersCounter counter = counterOpt.get();
        double range = counter.maxInteractionRange + 4.0;
        
        return ContainerManager.getEntitiesWithContainerOpen(
            blockEntity,
            range,
            (entity, pos) -> hasContainerOpen(counter, entity, pos)
        );
    }
    
    private static boolean hasContainerOpen(ContainerOpenersCounter counter, Entity entity, BlockPos pos) {
        if (entity instanceof Player player) {
            return ((ContainerOpenersCounterAccessor) counter).callIsOwnContainer(player);
        }
        return entity instanceof ContainerUser user && !user.getLivingEntity().isSpectator() && user.hasContainerOpen(counter, pos);
    }
    
    private void handleDoubleChest(BlockEntity blockEntity, Level level, ConnectedChestConsumer consumer) {
        if (blockEntity instanceof ChestBlockEntity) {
            BlockPos connectedPos = ContainerManager.getConnectedBlockPos(blockEntity.getBlockPos(), blockEntity.getBlockState());
            BlockEntity connectedContainer = level.getBlockEntity(connectedPos);
            
            if (connectedContainer instanceof ChestBlockEntity) {
                getCounter(connectedContainer).ifPresent(counter ->
                    consumer.accept(counter, connectedPos, connectedContainer.getBlockState())
                );
            }
        }
    }
    
    @FunctionalInterface
    private interface ConnectedChestConsumer {
        void accept(ContainerOpenersCounter counter, BlockPos pos, BlockState state);
    }
}
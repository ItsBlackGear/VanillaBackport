package com.blackgear.vanillabackport.common.level.entities.mob.animal.golem.copper_golem;

import com.blackgear.vanillabackport.core.mixin.common.access.BarrelBlockEntityAccessor;
import com.blackgear.vanillabackport.core.mixin.common.access.ChestBlockEntityAccessor;
import com.blackgear.vanillabackport.core.mixin.common.access.ContainerOpenersCounterAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class ContainerHandler {
    @Nullable
    public static ContainerOpenersCounter getCounter(BlockEntity container) {
        if (container instanceof ChestBlockEntity chest) {
            return ((ChestBlockEntityAccessor) chest).getOpenersCounter();
        } else if (container instanceof BarrelBlockEntity barrel) {
            return ((BarrelBlockEntityAccessor) barrel).getOpenersCounter();
        } else {
            return null;
        }
    }
    
    public static BlockPos getConnectedBlockPos(BlockPos pos, BlockState state) {
        Direction connectedDirection = ChestBlock.getConnectedDirection(state);
        return pos.relative(connectedDirection);
    }
    
    public static void startOpen(BlockEntity container, ContainerUser user) {
        if (!container.isRemoved() && !user.getLivingEntity().isSpectator()) {
            ContainerOpenersCounter counter = getCounter(container);
            Level level = container.getLevel();
            if (counter != null && level != null) {
                incrementOpeners(counter, user.getLivingEntity(), level, container.getBlockPos(), container.getBlockState(), user.getContainerInteractionRange());
                
                // Handle connected chest
                if (container instanceof ChestBlockEntity) {
                    BlockPos connectedPos = getConnectedBlockPos(container.getBlockPos(), container.getBlockState());
                    BlockEntity connectedContainer = level.getBlockEntity(connectedPos);
                    if (connectedContainer instanceof ChestBlockEntity) {
                        ContainerOpenersCounter connectedCounter = getCounter(connectedContainer);
                        if (connectedCounter != null) {
                            incrementOpeners(connectedCounter, user.getLivingEntity(), connectedContainer.getLevel(), connectedPos, connectedContainer.getBlockState(), user.getContainerInteractionRange());
                        }
                    }
                }
            }
        }
    }
    
    public static void stopOpen(BlockEntity container, ContainerUser user) {
        if (!container.isRemoved() && !user.getLivingEntity().isSpectator()) {
            ContainerOpenersCounter counter = getCounter(container);
            Level level = container.getLevel();
            if (counter != null && level != null) {
                decrementOpeners(counter, user.getLivingEntity(), level, container.getBlockPos(), container.getBlockState());
                
                // Handle connected chest
                if (container instanceof ChestBlockEntity) {
                    BlockPos connectedPos = getConnectedBlockPos(container.getBlockPos(), container.getBlockState());
                    BlockEntity connectedContainer = level.getBlockEntity(connectedPos);
                    if (connectedContainer instanceof ChestBlockEntity) {
                        ContainerOpenersCounter connectedCounter = getCounter(connectedContainer);
                        if (connectedCounter != null) {
                            decrementOpeners(connectedCounter, user.getLivingEntity(), connectedContainer.getLevel(), connectedPos, connectedContainer.getBlockState());
                        }
                    }
                }
            }
        }
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
    
    public static List<LivingEntity> getEntitiesWithContainerOpen(BlockEntity container) {
        ContainerOpenersCounter counter = getCounter(container);
        Level level = container.getLevel();
        if (counter != null && level != null) {
            return getEntitiesWithContainerOpen(counter, level, container.getBlockPos());
        }
        
        return List.of();
    }
    
    private static List<LivingEntity> getEntitiesWithContainerOpen(ContainerOpenersCounter counter, Level level, BlockPos pos) {
        double range = counter.maxInteractionRange + 4.0;
        AABB searchBox = new AABB(pos).inflate(range);
        return level.getEntities((Entity) null, searchBox, entity -> hasContainerOpen(counter, entity, pos))
            .stream()
            .map(entity -> (LivingEntity) entity)
            .filter(entity -> entity instanceof Player || entity instanceof ContainerUser)
            .collect(Collectors.toList());
    }
    
    private static boolean hasContainerOpen(ContainerOpenersCounter counter, Entity entity, BlockPos pos) {
        if (entity instanceof Player player) {
            return ((ContainerOpenersCounterAccessor) counter).callIsOwnContainer(player);
        }
        
        return entity instanceof ContainerUser user && !user.getLivingEntity().isSpectator() && user.hasContainerOpen(counter, pos);
    }
}
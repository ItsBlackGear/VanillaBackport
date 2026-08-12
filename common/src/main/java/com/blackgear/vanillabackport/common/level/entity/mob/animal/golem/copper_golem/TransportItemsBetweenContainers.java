package com.blackgear.vanillabackport.common.level.entity.mob.animal.golem.copper_golem;

import com.blackgear.vanillabackport.common.registries.entities.ModMemoryModuleTypes;
import com.blackgear.vanillabackport.core.mixin.common.access.BaseContainerBlockEntityAccessor;
import com.google.common.collect.ImmutableMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.GlobalPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.LockCode;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.behavior.BlockPosTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.world.level.block.entity.BarrelBlockEntity;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import org.apache.logging.log4j.util.TriConsumer;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class TransportItemsBetweenContainers extends Behavior<PathfinderMob> {
    private final float speedModifier;
    private final int horizontalSearchDistance;
    private final int verticalSearchDistance;
    private final Predicate<BlockState> sourceBlockType;
    private final Predicate<BlockState> destinationBlockType;
    private final Predicate<TransportItemTarget> shouldQueueForTarget;
    private final Consumer<PathfinderMob> onStartTravelling;
    private final Map<ContainerInteractionState, OnTargetReachedInteraction> onTargetInteractionActions;
    @Nullable private TransportItemTarget target = null;
    private TransportItemState state = TransportItemState.TRAVELLING;
    @Nullable private ContainerInteractionState interactionState;
    private int ticksSinceReachingTarget;

    public TransportItemsBetweenContainers(
        float speedModifier,
        Predicate<BlockState> sourceBlockType,
        Predicate<BlockState> destinationBlockType,
        int horizontalSearchDistance,
        int verticalSearchDistance,
        Map<ContainerInteractionState, OnTargetReachedInteraction> onTargetInteractionActions,
        Consumer<PathfinderMob> onStartTravelling,
        Predicate<TransportItemTarget> shouldQueueForTarget
    ) {
        super(
            ImmutableMap.of(
                ModMemoryModuleTypes.VISITED_BLOCK_POSITIONS.get(), MemoryStatus.REGISTERED,
                ModMemoryModuleTypes.UNREACHABLE_TRANSPORT_BLOCK_POSITIONS.get(), MemoryStatus.REGISTERED,
                ModMemoryModuleTypes.TRANSPORT_ITEMS_COOLDOWN_TICKS.get(), MemoryStatus.VALUE_ABSENT,
                MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT
            )
        );
        this.speedModifier = speedModifier;
        this.horizontalSearchDistance = horizontalSearchDistance;
        this.verticalSearchDistance = verticalSearchDistance;
        this.sourceBlockType = sourceBlockType;
        this.destinationBlockType = destinationBlockType;
        this.shouldQueueForTarget = shouldQueueForTarget;
        this.onStartTravelling = onStartTravelling;
        this.onTargetInteractionActions = onTargetInteractionActions;
    }

    @Override
    protected void start(ServerLevel level, PathfinderMob entity, long gameTime) {
        if (entity.getNavigation() instanceof CopperGolemPathNavigation navigation) {
            navigation.setCanPathToTargetsBelowSurface(true);
        }
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, PathfinderMob entity) {
        return !entity.isLeashed();
    }

    @Override
    protected boolean canStillUse(ServerLevel level, PathfinderMob entity, long gameTime) {
        return entity.getBrain().getMemory(ModMemoryModuleTypes.TRANSPORT_ITEMS_COOLDOWN_TICKS.get()).isEmpty() && !entity.isPanicking() && !entity.isLeashed();
    }

    @Override
    protected boolean timedOut(long gameTime) {
        return false;
    }

    @Override
    protected void tick(ServerLevel level, PathfinderMob entity, long gameTime) {
        boolean updatedInvalidTarget = this.updateInvalidTarget(level, entity);
        if (this.target == null) {
            this.stop(level, entity, gameTime);
        } else if (!updatedInvalidTarget) {
            if (this.state.equals(TransportItemState.QUEUING)) {
                this.onQueuingForTarget(this.target, level, entity);
            }

            if (this.state.equals(TransportItemState.TRAVELLING)) {
                this.onTravelToTarget(this.target, level, entity);
            }

            if (this.state.equals(TransportItemState.INTERACTING)) {
                this.onReachedTarget(this.target, level, entity);
            }
        }
    }

    private boolean updateInvalidTarget(ServerLevel level, PathfinderMob entity) {
        if (!this.hasValidTarget(level, entity)) {
            this.stopTargetingCurrentTarget(entity);
            Optional<TransportItemTarget> targetBlockPosition = this.getTransportTarget(level, entity);
            if (targetBlockPosition.isPresent()) {
                this.target = targetBlockPosition.get();
                this.onStartTravelling(entity);
                this.setVisitedBlockPos(entity, level, this.target.pos);
            } else {
                this.enterCooldownAfterNoMatchingTargetFound(entity);
            }

            return true;
        } else {
            return false;
        }
    }

    private void onQueuingForTarget(TransportItemTarget target, Level level, PathfinderMob entity) {
        if (!this.isAnotherMobInteractingWithTarget(target, level)) {
            this.resumeTravelling(entity);
        }
    }

    private void onTravelToTarget(TransportItemTarget target, Level level, PathfinderMob entity) {
        if (this.isWithinTargetDistance(3.0, target, level, entity, this.getCenterPos(entity)) && this.isAnotherMobInteractingWithTarget(target, level)) {
            this.startQueuing(entity);
        } else if (this.isWithinTargetDistance(getInteractionRange(entity), target, level, entity, this.getCenterPos(entity))) {
            this.startOnReachedTargetInteraction(target, entity);
        } else {
            this.walkTowardsTarget(entity);
        }
    }

    private Vec3 getCenterPos(PathfinderMob entity) {
        return this.setMiddleYPosition(entity, entity.position());
    }

    private void onReachedTarget(TransportItemTarget target, Level level, PathfinderMob entity) {
        if (!this.isWithinTargetDistance(2.0, target, level, entity, this.getCenterPos(entity))) {
            this.onStartTravelling(entity);
        } else {
            this.ticksSinceReachingTarget++;
            this.onTargetInteraction(target, entity);
            if (this.ticksSinceReachingTarget >= 60) {
                this.doReachedTargetInteraction(
                    entity,
                    target.container,
                    this::pickUpItems,
                    (mob, container) -> this.stopTargetingCurrentTarget(entity),
                    this::putDownItem,
                    (mob, container) -> this.stopTargetingCurrentTarget(entity)
                );
                this.onStartTravelling(entity);
            }
        }
    }

    private void startQueuing(PathfinderMob entity) {
        this.stopInPlace(entity);
        this.setTransportingState(TransportItemState.QUEUING);
    }

    private void resumeTravelling(PathfinderMob entity) {
        this.setTransportingState(TransportItemState.TRAVELLING);
        this.walkTowardsTarget(entity);
    }

    private void walkTowardsTarget(PathfinderMob entity) {
        if (this.target != null) {
            BehaviorUtils.setWalkAndLookTargetMemories(entity, this.target.pos, this.speedModifier, 0);
        }
    }

    private void startOnReachedTargetInteraction(TransportItemTarget target, PathfinderMob entity) {
        this.doReachedTargetInteraction(
            entity,
            target.container,
            this.onReachedInteraction(ContainerInteractionState.PICKUP_ITEM),
            this.onReachedInteraction(ContainerInteractionState.PICKUP_NO_ITEM),
            this.onReachedInteraction(ContainerInteractionState.PLACE_ITEM),
            this.onReachedInteraction(ContainerInteractionState.PLACE_NO_ITEM)
        );
        this.setTransportingState(TransportItemState.INTERACTING);
    }

    private void onStartTravelling(PathfinderMob entity) {
        this.onStartTravelling.accept(entity);
        this.setTransportingState(TransportItemState.TRAVELLING);
        this.interactionState = null;
        this.ticksSinceReachingTarget = 0;
    }

    private BiConsumer<PathfinderMob, Container> onReachedInteraction(ContainerInteractionState state) {
        return (mob, container) -> this.setInteractionState(state);
    }

    private void setTransportingState(TransportItemState state) {
        this.state = state;
    }

    private void setInteractionState(ContainerInteractionState state) {
        this.interactionState = state;
    }

    private void onTargetInteraction(TransportItemTarget target, PathfinderMob entity) {
        entity.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new BlockPosTracker(target.pos));
        this.stopInPlace(entity);
        if (this.interactionState != null) {
            Optional.ofNullable(this.onTargetInteractionActions.get(this.interactionState))
                .ifPresent(action -> action.accept(entity, target, this.ticksSinceReachingTarget));
        }
    }

    private void doReachedTargetInteraction(
        PathfinderMob entity,
        Container container,
        BiConsumer<PathfinderMob, Container> onPickupSuccess,
        BiConsumer<PathfinderMob, Container> onPickupFailure,
        BiConsumer<PathfinderMob, Container> onPlaceSuccess,
        BiConsumer<PathfinderMob, Container> onPlaceFailure
    ) {
        if (isPickingUpItems(entity)) {
            if (matchesGettingItemsRequirement(container)) {
                onPickupSuccess.accept(entity, container);
            } else {
                onPickupFailure.accept(entity, container);
            }
        } else if (matchesLeavingItemsRequirement(entity, container)) {
            onPlaceSuccess.accept(entity, container);
        } else {
            onPlaceFailure.accept(entity, container);
        }
    }

    private Optional<TransportItemTarget> getTransportTarget(ServerLevel level, PathfinderMob entity) {
        AABB targetBlockSearchArea = this.getTargetSearchArea(entity);
        Set<GlobalPos> visitedPositions = getVisitedPositions(entity);
        Set<GlobalPos> unreachablePositions = getUnreachablePositions(entity);
        List<ChunkPos> chunkPositions = ChunkPos.rangeClosed(new ChunkPos(entity.blockPosition()), Math.floorDiv(this.getHorizontalSearchDistance(entity), 16) + 1).toList();
        TransportItemTarget target = null;
        double closestDistance = Float.MAX_VALUE;

        for (ChunkPos chunkPos : chunkPositions) {
            LevelChunk levelChunk = level.getChunkSource().getChunkNow(chunkPos.x, chunkPos.z);
            if (levelChunk != null) {
                for (BlockEntity potentialTarget : levelChunk.getBlockEntities().values()) {
                    if (potentialTarget instanceof Container) {
                        double distance = potentialTarget.getBlockPos().distToCenterSqr(entity.position());
                        if (distance < closestDistance) {
                            TransportItemTarget targetValidToPick = this.isTargetValidToPick(entity, level, potentialTarget, visitedPositions, unreachablePositions, targetBlockSearchArea);
                            if (targetValidToPick != null) {
                                target = targetValidToPick;
                                closestDistance = distance;
                            }
                        }
                    }
                }
            }
        }

        return target == null ? Optional.empty() : Optional.of(target);
    }

    @Nullable
    private TransportItemTarget isTargetValidToPick(
        PathfinderMob entity,
        Level level,
        BlockEntity blockEntity,
        Set<GlobalPos> visitedPositions,
        Set<GlobalPos> unreachablePositions,
        AABB targetBlockSearchArea
    ) {
        BlockPos blockPos = blockEntity.getBlockPos();
        boolean isWithinSearchArea = targetBlockSearchArea.contains(blockPos.getX(), blockPos.getY(), blockPos.getZ());
        if (!isWithinSearchArea) {
            return null;
        } else {
            TransportItemTarget transportItemTarget = TransportItemTarget.tryCreatePossibleTarget(blockEntity, level);
            if (transportItemTarget == null) {
                return null;
            } else {
                boolean isValidTarget = this.isWantedBlock(entity, transportItemTarget.state)
                    && !this.isPositionAlreadyVisited(visitedPositions, unreachablePositions, transportItemTarget, level)
                    && !this.isContainerLocked(transportItemTarget);
                return isValidTarget ? transportItemTarget : null;
            }
        }
    }

    private boolean isContainerLocked(TransportItemTarget target) {
        return target.blockEntity instanceof BaseContainerBlockEntity container && !((BaseContainerBlockEntityAccessor) container).getLockKey().equals(LockCode.NO_LOCK);
    }

    private boolean hasValidTarget(Level level, PathfinderMob entity) {
        boolean targetIsOfValidType = this.target != null && this.isWantedBlock(entity, this.target.state) && this.targetHasNotChanged(level, this.target);
        if (targetIsOfValidType && !this.isTargetBlocked(level, this.target)) {
            if (!this.state.equals(TransportItemState.TRAVELLING)) {
                return true;
            }

            if (this.hasValidTravellingPath(level, this.target, entity)) {
                return true;
            }

            this.markVisitedBlockPosAsUnreachable(entity, level, this.target.pos);
        }

        return false;
    }

    private boolean hasValidTravellingPath(Level level, TransportItemTarget target, PathfinderMob entity) {
        Path path = entity.getNavigation().getPath() != null ? entity.getNavigation().getPath() : entity.getNavigation().createPath(target.pos, 0);
        Vec3 posFromWhichToReachTarget = this.getPositionToReachTargetFrom(path, entity);
        boolean canReachTarget = this.isWithinTargetDistance(getInteractionRange(entity), target, level, entity, posFromWhichToReachTarget);
        boolean hasNotYetCreatedPathToTarget = path == null && !canReachTarget;
        return hasNotYetCreatedPathToTarget || this.targetIsReachableFromPosition(level, canReachTarget, posFromWhichToReachTarget, target, entity);
    }

    private Vec3 getPositionToReachTargetFrom(@Nullable Path path, PathfinderMob entity) {
        boolean haveNoValidPath = path == null || path.getEndNode() == null;
        Vec3 bottomCenter = haveNoValidPath ? entity.position() : path.getEndNode().asBlockPos().getBottomCenter();
        return this.setMiddleYPosition(entity, bottomCenter);
    }

    private Vec3 setMiddleYPosition(PathfinderMob entity, Vec3 pos) {
        return pos.add(0.0, entity.getBoundingBox().getYsize() / 2.0, 0.0);
    }

    private boolean isTargetBlocked(Level level, TransportItemTarget target) {
        return target.blockEntity() instanceof ChestBlockEntity && ChestBlock.isChestBlockedAt(level, target.pos);
    }

    private boolean targetHasNotChanged(Level level, TransportItemTarget target) {
        return target.blockEntity.equals(level.getBlockEntity(target.pos));
    }

    private Stream<TransportItemTarget> getConnectedTargets(TransportItemTarget target, Level level) {
        if (target.state.getBlock() instanceof ChestBlock && target.state.getOptionalValue(ChestBlock.TYPE).orElse(ChestType.SINGLE) != ChestType.SINGLE) {
            TransportItemTarget connectedTarget = TransportItemTarget.tryCreatePossibleTarget(ContainerHandler.getConnectedBlockPos(target.pos, target.state), level);
            return connectedTarget != null ? Stream.of(target, connectedTarget) : Stream.of(target);
        } else {
            return Stream.of(target);
        }
    }

    private AABB getTargetSearchArea(PathfinderMob entity) {
        int horizontalSearchDistance = this.getHorizontalSearchDistance(entity);
        return new AABB(entity.blockPosition()).inflate(horizontalSearchDistance, this.getVerticalSearchDistance(entity), horizontalSearchDistance);
    }

    private int getHorizontalSearchDistance(PathfinderMob entity) {
        return entity.isPassenger() ? 1 : this.horizontalSearchDistance;
    }

    private int getVerticalSearchDistance(PathfinderMob entity) {
        return entity.isPassenger() ? 1 : this.verticalSearchDistance;
    }

    private static Set<GlobalPos> getVisitedPositions(PathfinderMob entity) {
        return entity.getBrain().getMemory(ModMemoryModuleTypes.VISITED_BLOCK_POSITIONS.get()).orElse(Set.of());
    }

    private static Set<GlobalPos> getUnreachablePositions(PathfinderMob entity) {
        return entity.getBrain().getMemory(ModMemoryModuleTypes.UNREACHABLE_TRANSPORT_BLOCK_POSITIONS.get()).orElse(Set.of());
    }

    private boolean isPositionAlreadyVisited(
        Set<GlobalPos> visitedPositions,
        Set<GlobalPos> unreachablePositions,
        TransportItemTarget target,
        Level level
    ) {
        return this.getConnectedTargets(target, level)
            .map(transportItemTarget -> GlobalPos.of(level.dimension(), transportItemTarget.pos))
            .anyMatch(pos -> visitedPositions.contains(pos) || unreachablePositions.contains(pos));
    }

    private static boolean hasFinishedPath(PathfinderMob entity) {
        return entity.getNavigation().getPath() != null && entity.getNavigation().getPath().isDone();
    }

    private void setVisitedBlockPos(PathfinderMob entity, Level level, BlockPos target) {
        Set<GlobalPos> visitedPositions = new HashSet<>(getVisitedPositions(entity));
        visitedPositions.add(GlobalPos.of(level.dimension(), target));
        if (visitedPositions.size() > 10) {
            this.enterCooldownAfterNoMatchingTargetFound(entity);
        } else {
            entity.getBrain().setMemoryWithExpiry(ModMemoryModuleTypes.VISITED_BLOCK_POSITIONS.get(), visitedPositions, 6000L);
        }
    }

    private void markVisitedBlockPosAsUnreachable(PathfinderMob entity, Level level, BlockPos target) {
        Set<GlobalPos> visitedPositions = new HashSet<>(getVisitedPositions(entity));
        visitedPositions.remove(GlobalPos.of(level.dimension(), target));
        Set<GlobalPos> unreachablePositions = new HashSet<>(getUnreachablePositions(entity));
        unreachablePositions.add(GlobalPos.of(level.dimension(), target));
        if (unreachablePositions.size() > 50) {
            this.enterCooldownAfterNoMatchingTargetFound(entity);
        } else {
            entity.getBrain().setMemoryWithExpiry(ModMemoryModuleTypes.VISITED_BLOCK_POSITIONS.get(), visitedPositions, 6000L);
            entity.getBrain().setMemoryWithExpiry(ModMemoryModuleTypes.UNREACHABLE_TRANSPORT_BLOCK_POSITIONS.get(), unreachablePositions, 6000L);
        }
    }

    private boolean isWantedBlock(PathfinderMob entity, BlockState block) {
        return isPickingUpItems(entity) ? this.sourceBlockType.test(block) : this.destinationBlockType.test(block);
    }

    private static double getInteractionRange(PathfinderMob entity) {
        return hasFinishedPath(entity) ? 1.0 : 0.5;
    }

    private boolean isWithinTargetDistance(double distance, TransportItemTarget target, Level level, PathfinderMob entity, Vec3 fromPos) {
        AABB boundingBox = entity.getBoundingBox();
        AABB movedBoundBox = AABB.ofSize(fromPos, boundingBox.getXsize(), boundingBox.getYsize(), boundingBox.getZsize());
        return target.state.getCollisionShape(level, target.pos).bounds().inflate(distance, 0.5, distance).move(target.pos).intersects(movedBoundBox);
    }

    private boolean targetIsReachableFromPosition(Level level, boolean canReachTarget, Vec3 pos, TransportItemTarget target, PathfinderMob entity) {
        return canReachTarget && this.canSeeAnyTargetSide(target, level, entity, pos);
    }

    private boolean canSeeAnyTargetSide(TransportItemTarget target, Level level, PathfinderMob entity, Vec3 eyePosition) {
        Vec3 center = target.pos.getCenter();
        return Direction.stream()
            .map(direction -> center.add(0.5 * direction.getStepX(), 0.5 * direction.getStepY(), 0.5 * direction.getStepZ()))
            .map(hitTarget -> level.clip(new ClipContext(eyePosition, hitTarget, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity)))
            .anyMatch(hitResult -> hitResult.getType() == HitResult.Type.BLOCK && hitResult.getBlockPos().equals(target.pos));
    }

    private boolean isAnotherMobInteractingWithTarget(TransportItemTarget target, Level level) {
        return this.getConnectedTargets(target, level).anyMatch(this.shouldQueueForTarget);
    }

    private static boolean isPickingUpItems(PathfinderMob entity) {
        return entity.getMainHandItem().isEmpty();
    }

    private static boolean matchesGettingItemsRequirement(Container container) {
        return !container.isEmpty();
    }

    private static boolean matchesLeavingItemsRequirement(PathfinderMob entity, Container container) {
        return container.isEmpty() || hasItemMatchingHandItem(entity, container);
    }

    private static boolean hasItemMatchingHandItem(PathfinderMob entity, Container container) {
        ItemStack mainHandItem = entity.getMainHandItem();

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack itemStack = container.getItem(i);
            if (ItemStack.isSameItem(itemStack, mainHandItem)) {
                return true;
            }
        }

        return false;
    }

    private void pickUpItems(PathfinderMob entity, Container container) {
        entity.setItemSlot(EquipmentSlot.MAINHAND, pickupItemFromContainer(container));
        entity.setGuaranteedDrop(EquipmentSlot.MAINHAND);
        container.setChanged();
        this.clearMemoriesAfterMatchingTargetFound(entity);
    }

    private void putDownItem(PathfinderMob entity, Container container) {
        ItemStack itemsLeftAfterVisitingChest = addItemsToContainer(entity, container);
        container.setChanged();
        entity.setItemSlot(EquipmentSlot.MAINHAND, itemsLeftAfterVisitingChest);
        if (itemsLeftAfterVisitingChest.isEmpty()) {
            this.clearMemoriesAfterMatchingTargetFound(entity);
        } else {
            this.stopTargetingCurrentTarget(entity);
        }
    }

    private ItemStack pickupItemFromContainer(Container container) {
        int slot = 0;

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack itemStack = container.getItem(i);
            if (!itemStack.isEmpty()) {
                int itemCount = Math.min(itemStack.getCount(), 16);
                return container.removeItem(slot, itemCount);
            }

            slot++;
        }

        return ItemStack.EMPTY;
    }

    private ItemStack addItemsToContainer(PathfinderMob entity, Container container) {
        int slot = 0;
        ItemStack itemStack = entity.getMainHandItem();

        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack containerItemStack = container.getItem(i);
            if (containerItemStack.isEmpty()) {
                container.setItem(slot, itemStack);
                return ItemStack.EMPTY;
            }

            if (ItemStack.isSameItemSameComponents(containerItemStack, itemStack) && containerItemStack.getCount() < containerItemStack.getMaxStackSize()) {
                int countThatCanBeAdded = containerItemStack.getMaxStackSize() - containerItemStack.getCount();
                int countToAdd = Math.min(countThatCanBeAdded, itemStack.getCount());
                containerItemStack.setCount(containerItemStack.getCount() + countToAdd);
                itemStack.setCount(itemStack.getCount() - countThatCanBeAdded);
                container.setItem(slot, containerItemStack);
                if (itemStack.isEmpty()) {
                    return ItemStack.EMPTY;
                }
            }

            slot++;
        }

        return itemStack;
    }

    private void stopTargetingCurrentTarget(PathfinderMob entity) {
        this.ticksSinceReachingTarget = 0;
        this.target = null;
        entity.getNavigation().stop();
        entity.getBrain().eraseMemory(MemoryModuleType.WALK_TARGET);
    }

    private void clearMemoriesAfterMatchingTargetFound(PathfinderMob entity) {
        this.stopTargetingCurrentTarget(entity);
        entity.getBrain().eraseMemory(ModMemoryModuleTypes.VISITED_BLOCK_POSITIONS.get());
        entity.getBrain().eraseMemory(ModMemoryModuleTypes.UNREACHABLE_TRANSPORT_BLOCK_POSITIONS.get());
    }

    private void enterCooldownAfterNoMatchingTargetFound(PathfinderMob entity) {
        this.stopTargetingCurrentTarget(entity);
        entity.getBrain().setMemory(ModMemoryModuleTypes.TRANSPORT_ITEMS_COOLDOWN_TICKS.get(), 140);
        entity.getBrain().eraseMemory(ModMemoryModuleTypes.VISITED_BLOCK_POSITIONS.get());
        entity.getBrain().eraseMemory(ModMemoryModuleTypes.UNREACHABLE_TRANSPORT_BLOCK_POSITIONS.get());
    }

    @Override
    protected void stop(ServerLevel level, PathfinderMob entity, long gameTime) {
        this.onStartTravelling(entity);
        if (entity.getNavigation() instanceof CopperGolemPathNavigation navigation) {
            navigation.setCanPathToTargetsBelowSurface(false);
        }
    }

    private void stopInPlace(PathfinderMob mob) {
        mob.getNavigation().stop();
        mob.setXxa(0.0F);
        mob.setYya(0.0F);
        mob.setSpeed(0.0F);
        mob.setDeltaMovement(0.0, mob.getDeltaMovement().y, 0.0);
    }

    public enum ContainerInteractionState {
        PICKUP_ITEM, PICKUP_NO_ITEM, PLACE_ITEM, PLACE_NO_ITEM
    }

    public interface OnTargetReachedInteraction extends TriConsumer<PathfinderMob, TransportItemTarget, Integer> {}

    public enum TransportItemState {
        TRAVELLING, QUEUING, INTERACTING
    }

    public record TransportItemTarget(BlockPos pos, Container container, BlockEntity blockEntity, BlockState state) {
        @Nullable
        public static TransportItemTarget tryCreatePossibleTarget(BlockEntity blockEntity, Level level) {
            BlockPos blockPos = blockEntity.getBlockPos();
            BlockState blockState = blockEntity.getBlockState();
            Container container = getBlockEntityContainer(blockEntity, blockState, level, blockPos);
            return container != null ? new TransportItemTarget(blockPos, container, blockEntity, blockState) : null;
        }

        @Nullable
        public static TransportItemTarget tryCreatePossibleTarget(BlockPos blockPos, Level level) {
            BlockEntity blockEntity = level.getBlockEntity(blockPos);
            return blockEntity == null ? null : tryCreatePossibleTarget(blockEntity, level);
        }

        @Nullable
        private static Container getBlockEntityContainer(BlockEntity blockEntity, BlockState blockState, Level level, BlockPos blockPos) {
            if (blockState.getBlock() instanceof ChestBlock chestBlock) {
                return ChestBlock.getContainer(chestBlock, blockState, level, blockPos, false);
            } else {
                return blockEntity instanceof Container container ? container : null;
            }
        }
    }
}

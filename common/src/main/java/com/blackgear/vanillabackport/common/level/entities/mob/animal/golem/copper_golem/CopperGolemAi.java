package com.blackgear.vanillabackport.common.level.entities.mob.animal.golem.copper_golem;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.registries.entities.ModMemoryModuleTypes;
import com.blackgear.vanillabackport.core.data.tags.ModBlockTags;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import static com.blackgear.vanillabackport.common.level.entities.mob.animal.golem.copper_golem.TransportItemsBetweenContainers.*;

public class CopperGolemAi {
    private static final Predicate<BlockState> TRANSPORT_ITEM_SOURCE_BLOCK = block -> block.is(ModBlockTags.COPPER_CHESTS);
    private static final Predicate<BlockState> TRANSPORT_ITEM_DESTINATION_BLOCK = block -> block.is(ModBlockTags.COPPER_GOLEM_DESTINATION_TARGETS);
    private static final ImmutableList<SensorType<? extends Sensor<? super CopperGolem>>> SENSOR_TYPES = ImmutableList.of(
        SensorType.NEAREST_LIVING_ENTITIES,
        SensorType.HURT_BY
    );
    private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
        MemoryModuleType.IS_PANICKING,
        MemoryModuleType.HURT_BY,
        MemoryModuleType.HURT_BY_ENTITY,
        MemoryModuleType.NEAREST_LIVING_ENTITIES,
        MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
        MemoryModuleType.WALK_TARGET,
        MemoryModuleType.LOOK_TARGET,
        MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
        MemoryModuleType.PATH,
        MemoryModuleType.GAZE_COOLDOWN_TICKS,
        ModMemoryModuleTypes.TRANSPORT_ITEMS_COOLDOWN_TICKS.get(),
        ModMemoryModuleTypes.VISITED_BLOCK_POSITIONS.get(),
        ModMemoryModuleTypes.UNREACHABLE_TRANSPORT_BLOCK_POSITIONS.get(),
        MemoryModuleType.DOORS_TO_CLOSE
    );

    public static Brain.Provider<CopperGolem> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    public static Brain<?> makeBrain(Brain<CopperGolem> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain);
        brain.setCoreActivities(Set.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    public static void updateActivity(CopperGolem golem) {
        golem.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.IDLE));
    }

    public static void initCoreActivity(Brain<CopperGolem> brain) {
        brain.addActivity(
            Activity.CORE,
            0,
            ImmutableList.of(
                new AnimalPanic<>(1.5F),
                new LookAtTargetSink(45, 90),
                new MoveToTargetSink(),
                InteractWithDoor.create(),
                new CountDownCooldownTicks(MemoryModuleType.GAZE_COOLDOWN_TICKS),
                new CountDownCooldownTicks(ModMemoryModuleTypes.TRANSPORT_ITEMS_COOLDOWN_TICKS.get()))
        );
    }

    public static void initIdleActivity(Brain<CopperGolem> brain) {
        brain.addActivity(
            Activity.IDLE,
            ImmutableList.of(
                Pair.of(0,
                    new TransportItemsBetweenContainers(
                        1.0F,
                        TRANSPORT_ITEM_SOURCE_BLOCK,
                        TRANSPORT_ITEM_DESTINATION_BLOCK,
                        32,
                        8,
                        getTargetReachedInteractions(),
                        onTravelling(),
                        shouldQueueForTarget()
                    )
                ),
                Pair.of(1, SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(40, 80))),
                Pair.of(2,
                    new RunOne<>(
                        ImmutableMap.of(
                            MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT,
                            ModMemoryModuleTypes.TRANSPORT_ITEMS_COOLDOWN_TICKS.get(), MemoryStatus.VALUE_PRESENT
                        ),
                        ImmutableList.of(
                            Pair.of(RandomStroll.stroll(1.0F, 2, 2), 1),
                            Pair.of(new DoNothing(30, 60), 1)
                        )
                    )
                )
            )
        );
    }

    private static Map<ContainerInteractionState, OnTargetReachedInteraction> getTargetReachedInteractions() {
        return Map.of(
            ContainerInteractionState.PICKUP_ITEM, onTargetReachedInteraction(CopperGolemState.GETTING_ITEM, ModSoundEvents.COPPER_GOLEM_ITEM_GET.get()),
            ContainerInteractionState.PICKUP_NO_ITEM, onTargetReachedInteraction(CopperGolemState.GETTING_NO_ITEM, ModSoundEvents.COPPER_GOLEM_ITEM_NO_GET.get()),
            ContainerInteractionState.PLACE_ITEM, onTargetReachedInteraction(CopperGolemState.DROPPING_ITEM, ModSoundEvents.COPPER_GOLEM_ITEM_DROP.get()),
            ContainerInteractionState.PLACE_NO_ITEM, onTargetReachedInteraction(CopperGolemState.DROPPING_NO_ITEM, ModSoundEvents.COPPER_GOLEM_ITEM_NO_DROP.get())
        );
    }

    private static OnTargetReachedInteraction onTargetReachedInteraction(CopperGolemState state, @Nullable SoundEvent sound) {
        return (entity, target, tickSinceReachingTarget) -> {
            if (entity instanceof CopperGolem golem) {
                if (tickSinceReachingTarget == 1) {
                    ContainerHandler.startOpen(target.blockEntity(), golem);
                    golem.setOpenedChestPos(target.pos());
                    golem.setState(state);
                }

                if (tickSinceReachingTarget == 9 && sound != null) {
                    golem.playSound(sound);
                }

                if (tickSinceReachingTarget == 60) {
                    if (ContainerHandler.getEntitiesWithContainerOpen(target.blockEntity()).contains(golem)) {
                        ContainerHandler.stopOpen(target.blockEntity(), golem);
                    }

                    golem.clearOpenedChestPos();
                }
            }
        };
    }

    private static Consumer<PathfinderMob> onTravelling() {
        return entity -> {
            if (entity instanceof CopperGolem golem) {
                golem.clearOpenedChestPos();
                golem.setState(CopperGolemState.IDLE);
            }
        };
    }

    private static Predicate<TransportItemTarget> shouldQueueForTarget() {
        return target -> !ContainerHandler.getEntitiesWithContainerOpen(target.blockEntity()).isEmpty();
    }
}
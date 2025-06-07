package com.blackgear.vanillabackport.common.level.entities.happyghast;

import com.blackgear.vanillabackport.common.registries.ModSensorTypes;
import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.schedule.Activity;

import java.util.Set;

public class HappyGhastAi {
    private static final float IDLE_SPEED = 1.0F;
    private static final float TEMPTED_SPEED = 1.25F;
    private static final float FOLLOWING_ADULT_SPEED = 1.1F;
    private static final double CLOSE_ENOUGH_DIST = 5.0;
    private static final UniformInt ADULT_FOLLOW_RANGE = UniformInt.of(5, 16);
    private static final ImmutableList<SensorType<? extends Sensor<? super HappyGhast>>> SENSOR_TYPES = ImmutableList.of(
        SensorType.NEAREST_LIVING_ENTITIES,
        SensorType.HURT_BY,
        ModSensorTypes.HAPPY_GHAST_TEMPTATIONS.get(),
        ModSensorTypes.NEAREST_ADULT_ANY_TYPE.get(),
        SensorType.NEAREST_PLAYERS
    );
    private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
        MemoryModuleType.WALK_TARGET,
        MemoryModuleType.LOOK_TARGET,
        MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
        MemoryModuleType.PATH,
        MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
        MemoryModuleType.TEMPTING_PLAYER,
        MemoryModuleType.TEMPTATION_COOLDOWN_TICKS,
        MemoryModuleType.IS_TEMPTED,
        MemoryModuleType.BREED_TARGET,
        MemoryModuleType.IS_PANICKING,
        MemoryModuleType.HURT_BY,
        MemoryModuleType.NEAREST_VISIBLE_ADULT,
        MemoryModuleType.NEAREST_PLAYERS,
        MemoryModuleType.NEAREST_VISIBLE_PLAYER,
        MemoryModuleType.NEAREST_VISIBLE_ATTACKABLE_PLAYER
    );

    public static Brain.Provider<HappyGhast> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    protected static Brain<?> makeBrain(Brain<HappyGhast> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain);
        initPanicActivity(brain);
        brain.setCoreActivities(Set.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    private static void initCoreActivity(Brain<HappyGhast> brain) {
        brain.addActivity(
            Activity.CORE,
            0,
            ImmutableList.of(
                new Swim(0.8F),
                new AnimalPanic<>(2.0F),
                new LookAtTargetSink(45, 90),
                new MoveToTargetSink(),
                new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS)
            )
        );
    }

    private static void initIdleActivity(Brain<HappyGhast> brain) {
        brain.addActivity(
            Activity.IDLE,
            ImmutableList.of(
                Pair.of(1, new FollowTemptation(entity -> TEMPTED_SPEED, entity -> CLOSE_ENOUGH_DIST)),
                Pair.of(2, GhastlingFollowAdult.create(ADULT_FOLLOW_RANGE, entity -> FOLLOWING_ADULT_SPEED, MemoryModuleType.NEAREST_VISIBLE_PLAYER)),
                Pair.of(3, GhastlingFollowAdult.create(ADULT_FOLLOW_RANGE, entity -> FOLLOWING_ADULT_SPEED, MemoryModuleType.NEAREST_VISIBLE_ADULT)),
                Pair.of(4, new RunOne<>(ImmutableList.of(Pair.of(RandomStroll.fly(IDLE_SPEED), 1), Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), 1))))
            )
        );
    }

    private static void initPanicActivity(Brain<HappyGhast> brain) {
        brain.addActivityWithConditions(
            Activity.PANIC,
            ImmutableList.of(),
            Set.of(Pair.of(MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_PRESENT))
        );
    }

    public static void updateActivity(HappyGhast happyGhast) {
        happyGhast.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.PANIC, Activity.IDLE));
    }
}
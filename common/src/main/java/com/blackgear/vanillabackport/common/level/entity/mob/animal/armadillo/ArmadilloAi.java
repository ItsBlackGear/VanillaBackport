package com.blackgear.vanillabackport.common.level.entity.mob.animal.armadillo;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import com.blackgear.vanillabackport.common.registries.entities.ModMemoryModuleTypes;
import com.blackgear.vanillabackport.common.registries.entities.ModSensorTypes;
import com.blackgear.vanillabackport.core.mixin.common.access.AnimalMakeLoveAccessor;
import com.blackgear.vanillabackport.core.util.Utilities.*;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.schedule.Activity;

import java.util.Map;
import java.util.Set;

public class ArmadilloAi {
    private static final UniformInt ADULT_FOLLOW_RANGE = UniformInt.of(5, 16);
    private static final ImmutableList<SensorType<? extends Sensor<? super Armadillo>>> SENSOR_TYPES = ImmutableList.of(
        SensorType.NEAREST_LIVING_ENTITIES,
        SensorType.HURT_BY,
        ModSensorTypes.ARMADILLO_TEMPTATIONS.get(),
        SensorType.NEAREST_ADULT,
        ModSensorTypes.ARMADILLO_SCARE_DETECTED.get()
    );
    private static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
        MemoryModuleType.IS_PANICKING,
        MemoryModuleType.HURT_BY,
        MemoryModuleType.HURT_BY_ENTITY,
        MemoryModuleType.WALK_TARGET,
        MemoryModuleType.LOOK_TARGET,
        MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
        MemoryModuleType.PATH,
        MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
        MemoryModuleType.TEMPTING_PLAYER,
        MemoryModuleType.TEMPTATION_COOLDOWN_TICKS,
        MemoryModuleType.GAZE_COOLDOWN_TICKS,
        MemoryModuleType.IS_TEMPTED,
        MemoryModuleType.BREED_TARGET,
        MemoryModuleType.NEAREST_VISIBLE_ADULT,
        ModMemoryModuleTypes.DANGER_DETECTED_RECENTLY.get()
    );

    private static final OneShot<Armadillo> ARMADILLO_ROLLING_OUT = BehaviorBuilder.create(instance ->
        instance.group(instance.absent(ModMemoryModuleTypes.DANGER_DETECTED_RECENTLY.get())).apply(instance, location -> (level, armadillo, timestamp) -> {
            if (armadillo.isScared()) {
                armadillo.rollOut();
                return true;
            } else {
                return false;
            }
        })
    );

    public static Brain.Provider<Armadillo> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }

    protected static Brain<?> makeBrain(Brain<Armadillo> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain);
        initScaredActivity(brain);
        brain.setCoreActivities(Set.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }

    private static void initCoreActivity(Brain<Armadillo> brain) {
        brain.addActivity(
            Activity.CORE,
            0,
            ImmutableList.of(
                new Swim(0.8F),
                new ArmadilloPanic(2.0F),
                new LookAtTargetSink(45, 90),
                new MoveToTargetSink() {
                    @Override
                    protected boolean checkExtraStartConditions(ServerLevel level, Mob mob) {
                        return (!(mob instanceof Armadillo armadillo) || !armadillo.isScared()) && super.checkExtraStartConditions(level, mob);
                    }
                },
                new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS),
                new CountDownCooldownTicks(MemoryModuleType.GAZE_COOLDOWN_TICKS),
                ARMADILLO_ROLLING_OUT
            )
        );
    }

    private static void initIdleActivity(Brain<Armadillo> brain) {
        brain.addActivity(
            Activity.IDLE,
            ImmutableList.of(
                Pair.of(0, SetEntityLookTargetSometimes.create(EntityType.PLAYER, 6.0F, UniformInt.of(30, 60))),
                Pair.of(1, new ArmadilloMakeLove(ModEntityTypes.ARMADILLO.get(), 1.0F, 1)),
                Pair.of(2, new RunOne<>(
                    ImmutableList.of(
                        Pair.of(new FollowTemptation(armadillo -> 1.25F, armadillo -> armadillo.isBaby() ? 1.0 : 2.0), 1),
                        Pair.of(BabyFollowAdult.create(ADULT_FOLLOW_RANGE, 1.25F), 1)
                    )
                )),
                Pair.of(3, new RandomLookAround(UniformInt.of(150, 250), 30.0F, 0.0F, 0.0F)),
                Pair.of(4, new RunOne<>(
                    ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
                    ImmutableList.of(
                        Pair.of(RandomStroll.stroll(1.0F), 1),
                        Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), 1),
                        Pair.of(new DoNothing(30, 60), 1)
                    )
                ))
            )
        );
    }

    private static void initScaredActivity(Brain<Armadillo> brain) {
        brain.addActivityWithConditions(
            Activity.PANIC,
            ImmutableList.of(Pair.of(0, new ArmadilloBallUp())),
            Set.of(
                Pair.of(ModMemoryModuleTypes.DANGER_DETECTED_RECENTLY.get(), MemoryStatus.VALUE_PRESENT),
                Pair.of(MemoryModuleType.IS_PANICKING, MemoryStatus.VALUE_ABSENT)
            )
        );
    }

    public static void updateActivity(Armadillo armadillo) {
        armadillo.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.PANIC, Activity.IDLE));
    }

    public static class ArmadilloBallUp extends Behavior<Armadillo> {
        static final int BALL_UP_STAY_IN_STATE = 5 * TimeUtils.SECONDS_PER_MINUTE * 20;
        int nextPeekTimer = 0;
        boolean dangerWasAround;

        public ArmadilloBallUp() {
            super(Map.of(), BALL_UP_STAY_IN_STATE);
        }

        @Override
        protected void tick(ServerLevel level, Armadillo armadillo, long timestamp) {
            super.tick(level, armadillo, timestamp);
            if (this.nextPeekTimer > 0) {
                this.nextPeekTimer--;
            }

            if (armadillo.shouldSwitchToScaredState()) {
                armadillo.switchToState(ArmadilloState.SCARED);
                if (armadillo.onGround()) {
                    armadillo.playSound(ModSoundEvents.ARMADILLO_LAND.get());
                }
            } else {
                ArmadilloState state = armadillo.getState();
                long dangerTickCounter = armadillo.getBrain().getTimeUntilExpiry(ModMemoryModuleTypes.DANGER_DETECTED_RECENTLY.get());
                boolean dangerIsAround = dangerTickCounter > 75L;
                if (dangerIsAround != this.dangerWasAround) {
                    this.nextPeekTimer = this.pickNextPeekTimer(armadillo);
                }

                this.dangerWasAround = dangerIsAround;
                if (state == ArmadilloState.SCARED) {
                    if (this.nextPeekTimer == 0 && armadillo.onGround() && dangerIsAround) {
                        level.broadcastEntityEvent(armadillo, (byte) 64);
                        this.nextPeekTimer = this.pickNextPeekTimer(armadillo);
                    }

                    if (dangerTickCounter < ArmadilloState.UNROLLING.animationDuration()) {
                        armadillo.playSound(ModSoundEvents.ARMADILLO_UNROLL_START.get());
                        armadillo.switchToState(ArmadilloState.UNROLLING);
                    }
                } else if (state == ArmadilloState.UNROLLING && dangerTickCounter > ArmadilloState.UNROLLING.animationDuration()) {
                    armadillo.switchToState(ArmadilloState.SCARED);
                }
            }
        }

        private int pickNextPeekTimer(Armadillo armadillo) {
            return ArmadilloState.SCARED.animationDuration() + armadillo.getRandom().nextIntBetweenInclusive(100, 400);
        }

        @Override
        protected boolean checkExtraStartConditions(ServerLevel level, Armadillo armadillo) {
            return armadillo.onGround();
        }

        @Override
        protected boolean canStillUse(ServerLevel level, Armadillo armadillo, long gameTime) {
            return armadillo.getState().isThreatened();
        }

        @Override
        protected void start(ServerLevel level, Armadillo armadillo, long gameTime) {
            armadillo.rollUp();
        }

        @Override
        protected void stop(ServerLevel level, Armadillo armadillo, long gameTime) {
            if (!armadillo.canStayRolledUp()) armadillo.rollOut();
        }
    }

    public static class ArmadilloPanic extends AnimalPanic {
        public ArmadilloPanic(float speedMultiplier) {
            super(speedMultiplier, mob -> mob.isOnFire() || mob.isFreezing());
        }

        @Override
        protected void start(ServerLevel level, PathfinderMob entity, long gameTime) {
            ((Armadillo) entity).rollOut();
            super.start(level, entity, gameTime);
        }
    }

    public static class ArmadilloMakeLove extends AnimalMakeLove {
        private final int closeEnoughDistance;

        public ArmadilloMakeLove(EntityType<? extends Armadillo> entityType, float speedModifier, int closeEnoughDistance) {
            super(entityType, speedModifier);
            this.closeEnoughDistance = closeEnoughDistance;
        }

        @Override
        protected void start(ServerLevel level, Animal entity, long gameTime) {
            ((AnimalMakeLoveAccessor) this).callFindValidBreedPartner(entity).ifPresent(partner -> {
                entity.getBrain().setMemory(MemoryModuleType.BREED_TARGET, partner);
                partner.getBrain().setMemory(MemoryModuleType.BREED_TARGET, entity);
                lockGazeAndWalkToEachOther(entity, partner, ((AnimalMakeLoveAccessor) this).getSpeedModifier(), this.closeEnoughDistance);
                int duration = 60 + entity.getRandom().nextInt(50);
                ((AnimalMakeLoveAccessor) this).setSpawnChildAtTime(gameTime + (long) duration);
            });
        }
        
        @Override
        protected void tick(ServerLevel level, Animal owner, long gameTime) {
            Animal animal = ((AnimalMakeLoveAccessor) this).callGetBreedTarget(owner);
            lockGazeAndWalkToEachOther(owner, animal, ((AnimalMakeLoveAccessor) this).getSpeedModifier(), this.closeEnoughDistance);
            if (owner.closerThan(animal, 3.0F)) {
                if (gameTime >= ((AnimalMakeLoveAccessor) this).getSpawnChildAtTime()) {
                    owner.spawnChildFromBreeding(level, animal);
                    owner.getBrain().eraseMemory(MemoryModuleType.BREED_TARGET);
                    animal.getBrain().eraseMemory(MemoryModuleType.BREED_TARGET);
                }
            }
        }

        private static void lockGazeAndWalkToEachOther(LivingEntity entity1, LivingEntity entity2, float speedModifier, int closeEnoughDistance) {
            lookAtEachOther(entity1, entity2);
            setWalkAndLookTargetMemoriesToEachOther(entity1, entity2, speedModifier, closeEnoughDistance);
        }

        private static void lookAtEachOther(LivingEntity entity1, LivingEntity entity2) {
            BehaviorUtils.lookAtEntity(entity1, entity2);
            BehaviorUtils.lookAtEntity(entity2, entity1);
        }

        private static void setWalkAndLookTargetMemoriesToEachOther(LivingEntity entity1, LivingEntity entity2, float speedModifier, int closeEnoughDistance) {
            BehaviorUtils.setWalkAndLookTargetMemories(entity1, entity2, speedModifier, closeEnoughDistance);
            BehaviorUtils.setWalkAndLookTargetMemories(entity2, entity1, speedModifier, closeEnoughDistance);
        }
    }
}
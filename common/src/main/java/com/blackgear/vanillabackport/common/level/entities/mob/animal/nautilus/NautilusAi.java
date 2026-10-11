package com.blackgear.vanillabackport.common.level.entities.mob.animal.nautilus;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.level.entities.ai.behavior.ChargeAttack;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import com.blackgear.vanillabackport.common.registries.entities.ModMemoryModuleTypes;
import com.blackgear.vanillabackport.common.registries.entities.ModSensorTypes;
import com.blackgear.vanillabackport.core.data.tags.ModEntityTypeTags;
import com.blackgear.vanillabackport.core.data.tags.ModItemTags;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.entity.ai.sensing.Sensor;
import net.minecraft.world.entity.ai.sensing.SensorType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.schedule.Activity;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.GameRules;

import java.util.Optional;
import java.util.Set;

public class NautilusAi {
    private static final UniformInt TIME_BETWEEN_NON_PLAYER_ATTACKS = UniformInt.of(2400, 3600);
    static final TargetingConditions ATTACK_TARGET_CONDITIONS = TargetingConditions.forCombat()
        .selector(target -> {
            return (target.level().getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING) || target.getType() != EntityType.ARMOR_STAND)
                && target.level().getWorldBorder().isWithinBounds(target.getBoundingBox());
        });
    static final ImmutableList<SensorType<? extends Sensor<? super AbstractNautilus>>> SENSOR_TYPES = ImmutableList.of(
        SensorType.NEAREST_LIVING_ENTITIES,
        SensorType.NEAREST_ADULT,
        SensorType.NEAREST_PLAYERS,
        SensorType.HURT_BY,
        ModSensorTypes.NAUTILUS_TEMPTATIONS.get()
    );
    static final ImmutableList<MemoryModuleType<?>> MEMORY_TYPES = ImmutableList.of(
        MemoryModuleType.IS_PANICKING,
        MemoryModuleType.HURT_BY,
        MemoryModuleType.HURT_BY_ENTITY,
        MemoryModuleType.NEAREST_LIVING_ENTITIES,
        MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES,
        MemoryModuleType.WALK_TARGET,
        MemoryModuleType.LOOK_TARGET,
        MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE,
        MemoryModuleType.PATH,
        MemoryModuleType.ANGRY_AT,
        MemoryModuleType.BREED_TARGET,
        MemoryModuleType.ATTACK_TARGET,
        MemoryModuleType.TEMPTATION_COOLDOWN_TICKS,
        ModMemoryModuleTypes.CHARGE_COOLDOWN_TICKS.get(),
        ModMemoryModuleTypes.ATTACK_TARGET_COOLDOWN.get()
    );
    
    public static Brain.Provider<Nautilus> brainProvider() {
        return Brain.provider(MEMORY_TYPES, SENSOR_TYPES);
    }
    
    public static void initMemories(AbstractNautilus nautilus, RandomSource random) {
        nautilus.getBrain().setMemory(ModMemoryModuleTypes.ATTACK_TARGET_COOLDOWN.get(), TIME_BETWEEN_NON_PLAYER_ATTACKS.sample(random));
    }
    
    public static Brain<?> makeBrain(Brain<Nautilus> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain);
        initFightActivity(brain);
        brain.setCoreActivities(Set.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }
    
    public static void initCoreActivity(Brain<Nautilus> brain) {
        brain.addActivity(
            Activity.CORE,
            0,
            ImmutableList.of(
                new AnimalPanic(1.6F),
                new LookAtTargetSink(45, 90),
                new MoveToTargetSink(),
                new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS),
                new CountDownCooldownTicks(ModMemoryModuleTypes.CHARGE_COOLDOWN_TICKS.get()),
                new CountDownCooldownTicks(ModMemoryModuleTypes.ATTACK_TARGET_COOLDOWN.get())
            )
        );
    }
    
    public static void initIdleActivity(Brain<Nautilus> brain) {
        brain.addActivity(
            Activity.IDLE,
            ImmutableList.of(
                Pair.of(1, new AnimalMakeLove(ModEntityTypes.NAUTILUS.get(), 0.4F)),
                Pair.of(2, new FollowTemptation(mob -> 1.3F, mob -> mob.isBaby() ? 2.5 : 3.5)),
                Pair.of(3, StartAttacking.create(NautilusAi::findNearestValidAttackTarget)),
                Pair.of(4, new GateBehavior<>(
                    ImmutableMap.of(MemoryModuleType.WALK_TARGET, MemoryStatus.VALUE_ABSENT),
                    ImmutableSet.of(),
                    GateBehavior.OrderPolicy.ORDERED,
                    GateBehavior.RunningPolicy.TRY_ALL,
                    ImmutableList.of(
                        Pair.of(RandomStroll.swim(1.0F), 2),
                        Pair.of(SetWalkTargetFromLookTarget.create(1.0F, 3), 3)
                    )
                ))
            )
        );
    }
    
    public static void initFightActivity(Brain<Nautilus> brain) {
        brain.addActivityWithConditions(
            Activity.FIGHT,
            ImmutableList.of(
                Pair.of(0, new ChargeAttack(80, ATTACK_TARGET_CONDITIONS, 0.6F, 2.0F, 12.0, 11.0, ModSoundEvents.NAUTILUS_DASH.get()))
            ),
            ImmutableSet.of(
                Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT),
                Pair.of(MemoryModuleType.TEMPTING_PLAYER, MemoryStatus.VALUE_ABSENT),
                Pair.of(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT),
                Pair.of(ModMemoryModuleTypes.CHARGE_COOLDOWN_TICKS.get(), MemoryStatus.VALUE_ABSENT)
            )
        );
    }
    
    protected static Optional<? extends LivingEntity> findNearestValidAttackTarget(AbstractNautilus nautilus) {
        if (!(nautilus.level() instanceof ServerLevel level)) return Optional.empty();
        
        if (!BehaviorUtils.isBreeding(nautilus) && nautilus.isInWater() && !nautilus.isBaby() && !nautilus.isTame()) {
            Optional<LivingEntity> angryAt = BehaviorUtils.getLivingEntityFromUUIDMemory(nautilus, MemoryModuleType.ANGRY_AT)
                .filter(target -> target.isInWater() && Sensor.isEntityAttackableIgnoringLineOfSight(nautilus, target));
            
            if (angryAt.isPresent()) {
                return angryAt;
            } else if (nautilus.getBrain().hasMemoryValue(ModMemoryModuleTypes.ATTACK_TARGET_COOLDOWN.get())) {
                return Optional.empty();
            } else {
                RandomSource random = level.getRandom();
                nautilus.getBrain().setMemory(ModMemoryModuleTypes.ATTACK_TARGET_COOLDOWN.get(), TIME_BETWEEN_NON_PLAYER_ATTACKS.sample(random));
                return random.nextFloat() < 0.5F
                    ? Optional.empty()
                    : nautilus.getBrain().getMemory(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES).orElse(NearestVisibleLivingEntities.empty()).findClosest(NautilusAi::isHostileTarget);
            }
        } else {
            return Optional.empty();
        }
    }
    
    protected static void setAngerTarget(AbstractNautilus nautilus, LivingEntity target) {
        if (Sensor.isEntityAttackableIgnoringLineOfSight(nautilus, target)) {
            nautilus.getBrain().eraseMemory(MemoryModuleType.CANT_REACH_WALK_TARGET_SINCE);
            nautilus.getBrain().setMemoryWithExpiry(MemoryModuleType.ANGRY_AT, target.getUUID(), 400L);
        }
    }
    
    private static boolean isHostileTarget(LivingEntity target) {
        return target.isInWater() && target.getType().is(ModEntityTypeTags.NAUTILUS_HOSTILES);
    }
    
    public static void updateActivity(AbstractNautilus nautilus) {
        nautilus.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
    }

    public static Ingredient getTemptations() {
        return Ingredient.of(ModItemTags.NAUTILUS_FOOD);
    }
}
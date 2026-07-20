package com.blackgear.vanillabackport.common.level.entity.mob.animal.nautilus;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.level.entity.ai.behavior.ChargeAttack;
import com.blackgear.vanillabackport.common.registries.entities.ModMemoryModuleTypes;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.schedule.Activity;

import java.util.Set;

public class ZombieNautilusAi {
    public static Brain.Provider<ZombieNautilus> brainProvider() {
        return Brain.provider(NautilusAi.MEMORY_TYPES, NautilusAi.SENSOR_TYPES);
    }
    
    public static Brain<?> makeBrain(Brain<ZombieNautilus> brain) {
        initCoreActivity(brain);
        initIdleActivity(brain);
        initFightActivity(brain);
        brain.setCoreActivities(Set.of(Activity.CORE));
        brain.setDefaultActivity(Activity.IDLE);
        brain.useDefaultActivity();
        return brain;
    }
    
    public static void initCoreActivity(Brain<ZombieNautilus> brain) {
        brain.addActivity(
            Activity.CORE,
            0,
            ImmutableList.of(
                new LookAtTargetSink(45, 90),
                new MoveToTargetSink(),
                new CountDownCooldownTicks(MemoryModuleType.TEMPTATION_COOLDOWN_TICKS),
                new CountDownCooldownTicks(ModMemoryModuleTypes.CHARGE_COOLDOWN_TICKS.get()),
                new CountDownCooldownTicks(ModMemoryModuleTypes.ATTACK_TARGET_COOLDOWN.get())
            )
        );
    }
    
    public static void initIdleActivity(Brain<ZombieNautilus> brain) {
        brain.addActivity(
            Activity.IDLE,
            ImmutableList.of(
                Pair.of(1, new FollowTemptation(mob -> 1.3F, mob -> mob.isBaby() ? 2.5 : 3.5)),
                Pair.of(2, StartAttacking.create(NautilusAi::findNearestValidAttackTarget)),
                Pair.of(3, new GateBehavior<>(
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
    
    public static void initFightActivity(Brain<ZombieNautilus> brain) {
        brain.addActivityWithConditions(
            Activity.FIGHT,
            ImmutableList.of(
                Pair.of(0, new ChargeAttack(80, NautilusAi.ATTACK_TARGET_CONDITIONS, 0.6F, 2.0F, 12.0, 11.0, ModSoundEvents.ZOMBIE_NAUTILUS_DASH.get()))
            ),
            ImmutableSet.of(
                Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT),
                Pair.of(MemoryModuleType.TEMPTING_PLAYER, MemoryStatus.VALUE_ABSENT),
                Pair.of(MemoryModuleType.BREED_TARGET, MemoryStatus.VALUE_ABSENT),
                Pair.of(ModMemoryModuleTypes.CHARGE_COOLDOWN_TICKS.get(), MemoryStatus.VALUE_ABSENT)
            )
        );
    }
    
    public static void updateActivity(AbstractNautilus nautilus) {
        nautilus.getBrain().setActiveActivityToFirstValid(ImmutableList.of(Activity.FIGHT, Activity.IDLE));
    }
}
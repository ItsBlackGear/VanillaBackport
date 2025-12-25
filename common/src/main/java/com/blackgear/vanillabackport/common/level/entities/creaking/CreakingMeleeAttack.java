package com.blackgear.vanillabackport.common.level.entities.creaking;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.behavior.OneShot;
import net.minecraft.world.entity.ai.behavior.declarative.BehaviorBuilder;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.NearestVisibleLivingEntities;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ProjectileWeaponItem;

import java.util.function.Predicate;

public class CreakingMeleeAttack {
    public static <T extends Mob> OneShot<T> create(Predicate<T> canAttackPredicate, int cooldownBetweenAttacks) {
        return BehaviorBuilder.create(
            instance -> instance.group(
                    instance.registered(MemoryModuleType.LOOK_TARGET),
                    instance.present(MemoryModuleType.ATTACK_TARGET),
                    instance.absent(MemoryModuleType.ATTACK_COOLING_DOWN),
                    instance.present(MemoryModuleType.NEAREST_VISIBLE_LIVING_ENTITIES)
                )
                .apply(
                    instance,
                    (lookTarget, attackTarget, attackCoolingDown, nearestVisibleLivingEntities) -> (level, body, timestamp) -> {
                        LivingEntity target = instance.get(attackTarget);
                        if (canAttackPredicate.test(body)
                            && !isHoldingUsableProjectileWeapon(body)
                            && body.isWithinMeleeAttackRange(target)
                            && instance.<NearestVisibleLivingEntities>get(nearestVisibleLivingEntities).contains(target)) {
                            lookTarget.set(new EntityTracker(target, true));
                            body.swing(InteractionHand.MAIN_HAND);
                            body.doHurtTarget(target);
                            attackCoolingDown.setWithExpiry(true, cooldownBetweenAttacks);
                            return true;
                        } else {
                            return false;
                        }
                    }
                )
        );
    }

    private static boolean isHoldingUsableProjectileWeapon(Mob body) {
        return body.isHolding(stack -> {
            Item item = stack.getItem();
            return item instanceof ProjectileWeaponItem projectile && body.canFireProjectileWeapon(projectile);
        });
    }
}
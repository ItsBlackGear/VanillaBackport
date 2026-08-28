package com.blackgear.vanillabackport.common.level.entities.ai.behavior;

import com.blackgear.vanillabackport.common.registries.entities.ModMemoryModuleTypes;
import com.google.common.collect.ImmutableMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.entity.EntityTypeTest;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ChargeAttack extends Behavior<Animal> {
    private final int timeBetweenAttacks;
    private final TargetingConditions chargeTargeting;
    private final float speed;
    private final float knockbackForce;
    private final double maxTargetDetectionDistance;
    private final double maxChargeDistance;
    private final SoundEvent chargeSound;
    private Vec3 chargeVelocityVector;
    private Vec3 startPosition;
    
    public ChargeAttack(
        int timeBetweenAttacks,
        TargetingConditions chargeTargeting,
        float speed,
        float knockbackForce,
        double maxTargetDetectionDistance,
        double maxChargeDistance,
        SoundEvent chargeSound
    ) {
        super(ImmutableMap.of(
            ModMemoryModuleTypes.CHARGE_COOLDOWN_TICKS.get(), MemoryStatus.VALUE_ABSENT,
            MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT
        ));
        this.timeBetweenAttacks = timeBetweenAttacks;
        this.chargeTargeting = chargeTargeting;
        this.speed = speed;
        this.knockbackForce = knockbackForce;
        this.maxTargetDetectionDistance = maxTargetDetectionDistance;
        this.maxChargeDistance = maxChargeDistance;
        this.chargeSound = chargeSound;
        this.chargeVelocityVector = Vec3.ZERO;
        this.startPosition = Vec3.ZERO;
    }
    
    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, Animal owner) {
        return owner.getBrain().hasMemoryValue(MemoryModuleType.ATTACK_TARGET);
    }
    
    @Override
    protected boolean canStillUse(ServerLevel level, Animal entity, long gameTime) {
        Brain<?> brain = entity.getBrain();
        Optional<LivingEntity> attackCandidate = brain.getMemory(MemoryModuleType.ATTACK_TARGET);
        if (attackCandidate.isEmpty()) {
            return false;
        } else {
            LivingEntity attackTarget = attackCandidate.get();
            if (entity instanceof TamableAnimal tamable && tamable.isTame()) {
                return false;
            } else if (entity.position().subtract(this.startPosition).lengthSqr() >= this.maxChargeDistance * this.maxChargeDistance) {
                return false;
            } else if (attackTarget.position().subtract(entity.position()).lengthSqr() >= this.maxTargetDetectionDistance * this.maxTargetDetectionDistance) {
                return false;
            } else {
                return entity.hasLineOfSight(attackTarget) && !brain.hasMemoryValue(ModMemoryModuleTypes.CHARGE_COOLDOWN_TICKS.get());
            }
        }
    }
    
    @Override
    protected void start(ServerLevel level, Animal entity, long gameTime) {
        Brain<?> brain = entity.getBrain();
        this.startPosition = entity.position();
        LivingEntity attackCandidate = brain.getMemory(MemoryModuleType.ATTACK_TARGET).get();
        Vec3 direction = attackCandidate.position().subtract(entity.position()).normalize();
        this.chargeVelocityVector = direction.scale(this.speed);
        if (this.canStillUse(level, entity, gameTime)) {
            entity.playSound(this.chargeSound);
        }
    }
    
    @Override
    protected void tick(ServerLevel level, Animal entity, long gameTime) {
        Brain<?> brain = entity.getBrain();
        LivingEntity attackTarget = brain.getMemory(MemoryModuleType.ATTACK_TARGET).orElseThrow();
        entity.lookAt(attackTarget, 360.0F, 360.0F);
        entity.setDeltaMovement(this.chargeVelocityVector);
        List<LivingEntity> collidingEntities = new ArrayList<>(1);
        level.getEntities(EntityTypeTest.forClass(LivingEntity.class), entity.getBoundingBox(), target -> this.chargeTargeting.test(entity, target), collidingEntities, 1);
        if (!collidingEntities.isEmpty()) {
            LivingEntity closestAttackTarget = collidingEntities.get(0);
            if (entity.hasPassenger(closestAttackTarget)) {
                return;
            }
            
            this.dealDamageToTarget(level, entity, closestAttackTarget);
            this.dealKnockback(level, entity, closestAttackTarget);
            this.stop(level, entity, gameTime);
        }
    }
    
    private void dealDamageToTarget(ServerLevel level, Animal entity, LivingEntity target) {
        DamageSource source = level.damageSources().mobAttack(entity);
        float damage = (float) entity.getAttributeValue(Attributes.ATTACK_DAMAGE);
        if (target.hurt(source, damage)) {
            entity.doEnchantDamageEffects(entity, target);
        }
    }
    
    private void dealKnockback(ServerLevel level, Animal entity, LivingEntity target) {
        int movementSpeedLevel = entity.hasEffect(MobEffects.MOVEMENT_SPEED) ? entity.getEffect(MobEffects.MOVEMENT_SPEED).getAmplifier() + 1 : 0;
        int movementSlowdownLevel = entity.hasEffect(MobEffects.MOVEMENT_SLOWDOWN) ? entity.getEffect(MobEffects.MOVEMENT_SLOWDOWN).getAmplifier() + 1 : 0;
        float speedBoostPower = 0.25F * (movementSpeedLevel - movementSlowdownLevel);
        float speedFactor = Mth.clamp(this.speed * (float) entity.getAttributeValue(Attributes.MOVEMENT_SPEED), 0.2F, 2.0F) + speedBoostPower;
        float knockback = speedFactor * this.knockbackForce;
        if (knockback > 0.0F) {
            target.knockback(knockback, Mth.sin(entity.getYRot() * Mth.DEG_TO_RAD), -Mth.cos(entity.getYRot() * Mth.DEG_TO_RAD));
            entity.setDeltaMovement(entity.getDeltaMovement().multiply(0.6, 4.0, 0.6));
        }
    }
    
    @Override
    protected void stop(ServerLevel level, Animal entity, long gameTime) {
        entity.getBrain().setMemory(ModMemoryModuleTypes.CHARGE_COOLDOWN_TICKS.get(), this.timeBetweenAttacks);
        entity.getBrain().eraseMemory(MemoryModuleType.ATTACK_TARGET);
    }
}
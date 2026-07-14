package com.blackgear.vanillabackport.common.level.item.spear;

import com.blackgear.vanillabackport.common.api.extensions.SoundExtension;
import com.blackgear.vanillabackport.common.api.extensions.entity.MotionAwareEntity;
import com.blackgear.vanillabackport.common.api.extensions.entity.spear.MobSpearHandler;
import com.blackgear.vanillabackport.core.util.ProjectileUtils;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public record KineticWeapon(
    int contactCooldownTicks,
    int delayTicks,
    Optional<Condition> dismountConditions,
    Optional<Condition> knockbackConditions,
    Optional<Condition> damageConditions,
    float forwardMovement,
    float damageMultiplier,
    Optional<Holder<SoundEvent>> sound,
    Optional<Holder<SoundEvent>> hitSound
) {
    public static Vec3 getMotion(Entity entity) {
        if (!(entity instanceof Player) && entity.isPassenger()) {
            entity = entity.getRootVehicle();
        }

        return ((MotionAwareEntity) entity).getKnownSpeed().scale(20.0);
    }
    
    public void makeSound(Entity causer) {
        this.sound.ifPresent(sound -> ((SoundExtension) causer.level()).playSound(causer, causer.getX(), causer.getY(), causer.getZ(), sound, causer.getSoundSource(), 1.0F, 1.0F));
    }
    
    public void makeLocalHitSound(Entity causer) {
        this.hitSound.ifPresent(sound -> ((SoundExtension) causer.level()).playLocalSound(causer, sound.value(), causer.getSoundSource(), 1.0F, 1.0F));
    }
    
    public int computeDamageUseDuration() {
        return this.delayTicks + this.damageConditions.map(Condition::maxDurationTicks).orElse(0);
    }
    
    public void damageEntities(ItemStack stack, int ticksRemaining, LivingEntity attacker, EquipmentSlot equipmentSlot) {
        MobSpearHandler handler = (MobSpearHandler) attacker;
        
        int ticksUsed = stack.getUseDuration() - ticksRemaining;
        if (ticksUsed >= this.delayTicks) {
            ticksUsed -= this.delayTicks;
            Vec3 attackerLookVector = attacker.getLookAngle();
            double attackerSpeedProjection = attackerLookVector.dot(getMotion(attacker));
            float actionFactor = attacker instanceof Player ? 1.0F : 0.2F;
            AttackRange attackRange = handler.getAttackRangeWith(stack);
            double baseMobDamage = attacker.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
            boolean affected = false;
            
            Collection<EntityHitResult> hitResults = ProjectileUtils.getHitEntitiesAlong(attacker, attackRange, e -> PiercingWeapon.canHitEntity(attacker, e), ClipContext.Block.COLLIDER).map(a -> List.of(), e -> e);
            for (EntityHitResult hitResult : hitResults) {
                Entity otherEntity = hitResult.getEntity();
                if (otherEntity instanceof EnderDragonPart part) {
                    otherEntity = part.parentMob;
                }
                
                boolean wasStabbed = handler.wasRecentlyStabbed(otherEntity, this.contactCooldownTicks);
                if (!wasStabbed) {
                    handler.rememberStabbedEntity(otherEntity);
                    double targetSpeedProjection = attackerLookVector.dot(getMotion(otherEntity));
                    double relativeSpeed = Math.max(0.0, attackerSpeedProjection - targetSpeedProjection);
                    
                    boolean dealsDismount = this.dismountConditions.isPresent() && this.dismountConditions.get().test(ticksUsed, attackerSpeedProjection, relativeSpeed, actionFactor);
                    boolean dealsKnockback = this.knockbackConditions.isPresent() && this.knockbackConditions.get().test(ticksUsed, attackerSpeedProjection, relativeSpeed, actionFactor);
                    boolean dealsDamage = this.damageConditions.isPresent() && this.damageConditions.get().test(ticksUsed, attackerSpeedProjection, relativeSpeed, actionFactor);
                    
                    if (dealsDismount || dealsKnockback || dealsDamage) {
                        float damageDealt = (float) baseMobDamage + Mth.floor(relativeSpeed * this.damageMultiplier);
                        affected |= handler.stabAttack(equipmentSlot, otherEntity, damageDealt, dealsDamage, dealsKnockback, dealsDismount);;
                    }
                }
            }
            
            if (affected) {
                attacker.level().broadcastEntityEvent(attacker, (byte) 2);
                if (attacker instanceof ServerPlayer player) {
                    //TODO: add criteria trigger
                }
            }
        }
    }
    
    public static boolean hasKineticWeapon(ItemStack stack) {
        return getKineticWeapon(stack) != null;
    }
    
    public static @Nullable KineticWeapon getKineticWeapon(ItemStack stack) {
        return stack.getItem() instanceof SpearItem spear ? spear.getKineticWeapon() : null;
    }
    
    public record Condition(
        int maxDurationTicks,
        float minSpeed,
        float minRelativeSpeed
    ) {
        public boolean test(int ticksUsed, double attackerSpeed, double relativeSpeed, double entityFactor) {
            return ticksUsed <= this.maxDurationTicks && attackerSpeed >= this.minSpeed * entityFactor && relativeSpeed >= this.minRelativeSpeed * entityFactor;
        }
        
        public static Optional<Condition> ofAttackerSpeed(int untilTicks, float minAttackerSpeed) {
            return Optional.of(new Condition(untilTicks, minAttackerSpeed, 0.0F));
        }
        
        public static Optional<Condition> ofRelativeSpeed(int untilTicks, float minRelativeSpeed) {
            return Optional.of(new Condition(untilTicks, 0.0F, minRelativeSpeed));
        }
    }
}
package com.blackgear.vanillabackport.common.level.item.spear;

import com.blackgear.platform.common.entity.ReachAttributes;
import com.blackgear.vanillabackport.common.api.extensions.entity.MotionAwareEntity;
import com.blackgear.vanillabackport.core.util.ProjectileUtils;
import com.blackgear.vanillabackport.core.util.Utilities;
import com.blackgear.vanillabackport.core.util.Utilities.DirectionUtils;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

public record AttackRange(
    float minReach,
    float maxReach,
    float minCreativeReach,
    float maxCreativeReach,
    float hitboxMargin,
    float mobFactor
) {
    public static AttackRange defaultFor(LivingEntity entity) {
        return new AttackRange(
            0.0F,
            (float) entity.getAttributeValue(ReachAttributes.getEntityInteractionReachAttribute().get()),
            0.0F,
            (float) entity.getAttributeValue(ReachAttributes.getEntityInteractionReachAttribute().get()),
            0.0F,
            1.0F
        );
    }
    
    public HitResult getClosestHit(Entity attacker, float partial, Predicate<Entity> matching) {
        Either<BlockHitResult, Collection<EntityHitResult>> result = ProjectileUtils.getHitEntitiesAlong(attacker, this, matching, ClipContext.Block.OUTLINE);
        if (result.left().isPresent()) {
            return result.left().get();
        } else {
            EntityHitResult entity = null;
            Vec3 attackerPos = attacker.getEyePosition(partial);
            double closestDistance = Double.MAX_VALUE;
            
            Collection<EntityHitResult> targets = result.right().get();
            for (EntityHitResult target : targets) {
                double distance = attackerPos.distanceToSqr(target.getLocation());
                if (distance < closestDistance) {
                    closestDistance = distance;
                    entity = target;
                }
            }
            
            if (entity != null) {
                return entity;
            } else {
                Vec3 eyeGaze = ((MotionAwareEntity) attacker).getHeadLookAngle();
                Vec3 missPosition = attacker.getEyePosition(partial).add(eyeGaze);
                return BlockHitResult.miss(missPosition, DirectionUtils.getApproximateNearest(eyeGaze), BlockPos.containing(missPosition));
            }
        }
    }
    
    public float effectiveMinRange(Entity entity) {
        if (entity instanceof Player player) {
            return player.isCreative() ? this.minCreativeReach : this.minReach;
        } else {
            return this.minReach * this.mobFactor;
        }
    }
    
    public float effectiveMaxRange(Entity entity) {
        if (entity instanceof Player player) {
            return player.isCreative() ? this.maxCreativeReach : this.maxReach;
        } else {
            return this.maxReach * this.mobFactor;
        }
    }
    
    public boolean isInRange(LivingEntity attacker, Vec3 location) {
        return this.isInRange(attacker, location::distanceToSqr, 0.0);
    }
    
    public boolean isInRange(LivingEntity attacker, AABB boundingBox, double extraBuffer) {
        return this.isInRange(attacker, boundingBox::distanceToSqr, extraBuffer);
    }
    
    private boolean isInRange(LivingEntity attacker, ToDoubleFunction<Vec3> distanceFunction, double extraBuffer) {
        double distance = Math.sqrt(distanceFunction.applyAsDouble(attacker.getEyePosition()));
        double minReach = this.effectiveMinRange(attacker) - this.hitboxMargin - extraBuffer;
        double maxReach = this.effectiveMaxRange(attacker) + this.hitboxMargin + extraBuffer;
        return distance >= minReach && distance <= maxReach;
    }
    
    public static boolean hasAttackRange(ItemStack stack) {
        return getAttackRange(stack) != null;
    }
    
    public static @Nullable AttackRange getAttackRange(ItemStack stack) {
        return stack.getItem() instanceof SpearItem spear ? spear.getAttackRange() : null;
    }
}
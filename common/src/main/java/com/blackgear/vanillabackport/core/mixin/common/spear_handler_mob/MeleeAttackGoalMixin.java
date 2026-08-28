package com.blackgear.vanillabackport.core.mixin.common.spear_handler_mob;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(MeleeAttackGoal.class)
public abstract class MeleeAttackGoalMixin {
    @Shadow @Final protected PathfinderMob mob;
    
    @WrapOperation(
        method = "canUse",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/ai/goal/MeleeAttackGoal;getAttackReachSqr(Lnet/minecraft/world/entity/LivingEntity;)D"
        )
    )
    private double vb$redirectCanUseReach(MeleeAttackGoal instance, LivingEntity target, Operation<Double> original) {
        return this.mob.isWithinMeleeAttackRange(target) ? Double.MAX_VALUE : Double.NEGATIVE_INFINITY;
    }
    
    @WrapOperation(
        method = "checkAndPerformAttack",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/ai/goal/MeleeAttackGoal;getAttackReachSqr(Lnet/minecraft/world/entity/LivingEntity;)D"
        )
    )
    private double vb$redirectCheckAndPerformAttackReach(MeleeAttackGoal instance, LivingEntity target, Operation<Double> original) {
        boolean withinRange = this.mob.isWithinMeleeAttackRange(target);
        boolean hasLineOfSight = this.mob.getSensing().hasLineOfSight(target);
        
        return (withinRange && hasLineOfSight) ? Double.MAX_VALUE : Double.NEGATIVE_INFINITY;
    }
}
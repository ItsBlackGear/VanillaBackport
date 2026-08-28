package com.blackgear.vanillabackport.common.level.entities.ai.behavior;

import com.blackgear.vanillabackport.common.api.extensions.entity.ControllableMob;
import com.blackgear.vanillabackport.common.registries.entities.ModMemoryModuleTypes;
import com.blackgear.vanillabackport.common.registries.items.ModDataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class SpearApproach extends Behavior<PathfinderMob> {
    private final double speedModifierWhenRepositioning;
    private final float approachDistanceSq;

    public SpearApproach(double speedModifierWhenRepositioning, float approachDistance) {
        super(Map.of(ModMemoryModuleTypes.SPEAR_STATUS.get(), MemoryStatus.VALUE_ABSENT));
        this.speedModifierWhenRepositioning = speedModifierWhenRepositioning;
        this.approachDistanceSq = approachDistance * approachDistance;
    }

    private boolean ableToAttack(PathfinderMob mob) {
        return this.getTarget(mob) != null && mob.getMainHandItem().has(ModDataComponents.KINETIC_WEAPON.get());
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, PathfinderMob mob) {
        return this.ableToAttack(mob) && !mob.isUsingItem();
    }

    @Override
    protected void start(ServerLevel level, PathfinderMob mob, long gameTime) {
        mob.setAggressive(true);
        mob.getBrain().setMemory(ModMemoryModuleTypes.SPEAR_STATUS.get(), SpearStatus.APPROACH);
        super.start(level, mob, gameTime);
    }

    @Nullable
    private LivingEntity getTarget(PathfinderMob mob) {
        return mob.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, PathfinderMob mob, long gameTime) {
        return this.ableToAttack(mob) && this.farEnough(mob);
    }

    private boolean farEnough(PathfinderMob mob) {
        LivingEntity target = this.getTarget(mob);
        double targetDistSqr = mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
        return targetDistSqr > this.approachDistanceSq;
    }

    @Override
    protected void tick(ServerLevel level, PathfinderMob mob, long gameTime) {
        LivingEntity target = this.getTarget(mob);
        Entity mount = mob.getRootVehicle();
        float speedModifier = ControllableMob.of(mount).chargeSpeedModifier();

        mob.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
        mob.getNavigation().moveTo(target, speedModifier * this.speedModifierWhenRepositioning);
    }

    @Override
    protected void stop(ServerLevel level, PathfinderMob mob, long gameTime) {
        mob.getNavigation().stop();
        mob.getBrain().setMemory(ModMemoryModuleTypes.SPEAR_STATUS.get(), SpearStatus.CHARGING);
    }

    @Override
    protected boolean timedOut(long gameTime) {
        return false;
    }
}
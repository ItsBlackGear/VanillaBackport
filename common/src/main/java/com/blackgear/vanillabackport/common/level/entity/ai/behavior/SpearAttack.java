package com.blackgear.vanillabackport.common.level.entity.ai.behavior;

import com.blackgear.vanillabackport.common.api.extensions.entity.ControllableMob;
import com.blackgear.vanillabackport.common.level.item.spear.KineticWeapon;
import com.blackgear.vanillabackport.common.registries.entities.ModMemoryModuleTypes;
import com.blackgear.vanillabackport.core.util.WorldUtilities.PathfindingUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Optional;

public class SpearAttack extends Behavior<PathfinderMob> {
    private final double speedModifierWhenCharging;
    private final double speedModifierWhenRepositioning;
    private final float targetInRangeRadiusSq;

    public SpearAttack(double speedModifierWhenCharging, double speedModifierWhenRepositioning, float targetInRangeRadius) {
        super(Map.of(ModMemoryModuleTypes.SPEAR_STATUS.get(), MemoryStatus.VALUE_PRESENT));
        this.speedModifierWhenCharging = speedModifierWhenCharging;
        this.speedModifierWhenRepositioning = speedModifierWhenRepositioning;
        this.targetInRangeRadiusSq = targetInRangeRadius * targetInRangeRadius;
    }

    @Nullable
    private LivingEntity getTarget(PathfinderMob mob) {
        return mob.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
    }

    private boolean ableToAttack(PathfinderMob mob) {
        return this.getTarget(mob) != null && KineticWeapon.hasKineticWeapon(mob.getMainHandItem());
    }

    private int getKineticWeaponUseDuration(PathfinderMob mob) {
        return Optional.ofNullable(KineticWeapon.getKineticWeapon(mob.getMainHandItem())).map(KineticWeapon::computeDamageUseDuration).orElse(0);
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, PathfinderMob mob) {
        return mob.getBrain().getMemory(ModMemoryModuleTypes.SPEAR_STATUS.get()).orElse(SpearStatus.APPROACH) == SpearStatus.CHARGING
            && this.ableToAttack(mob)
            && !mob.isUsingItem();
    }

    @Override
    protected void start(ServerLevel level, PathfinderMob mob, long gameTime) {
        mob.setAggressive(true);
        mob.getBrain().setMemory(ModMemoryModuleTypes.SPEAR_ENGAGE_TIME.get(), this.getKineticWeaponUseDuration(mob));
        mob.getBrain().eraseMemory(ModMemoryModuleTypes.SPEAR_CHARGE_POSITION.get());
        mob.startUsingItem(InteractionHand.MAIN_HAND);
        super.start(level, mob, gameTime);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, PathfinderMob mob, long gameTime) {
        return mob.getBrain().getMemory(ModMemoryModuleTypes.SPEAR_ENGAGE_TIME.get()).orElse(0) > 0 && this.ableToAttack(mob);
    }

    @Override
    protected void tick(ServerLevel level, PathfinderMob mob, long gameTime) {
        LivingEntity target = this.getTarget(mob);
        double targetDistSqr = mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
        Entity mount = mob.getRootVehicle();
        float speedModifier = ControllableMob.of(mount).chargeSpeedModifier();

        int mountDistance = mob.isPassenger() ? 2 : 0;
        mob.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
        mob.getBrain().setMemory(ModMemoryModuleTypes.SPEAR_ENGAGE_TIME.get(), mob.getBrain().getMemory(ModMemoryModuleTypes.SPEAR_ENGAGE_TIME.get()).orElse(0) - 1);
        Vec3 awayPos = mob.getBrain().getMemory(ModMemoryModuleTypes.SPEAR_CHARGE_POSITION.get()).orElse(null);
        if (awayPos != null) {
            mob.getNavigation().moveTo(awayPos.x, awayPos.y, awayPos.z, speedModifier * this.speedModifierWhenRepositioning);
            if (mob.getNavigation().isDone()) {
                mob.getBrain().eraseMemory(ModMemoryModuleTypes.SPEAR_CHARGE_POSITION.get());
            }
        } else {
            mob.getNavigation().moveTo(target, speedModifier * this.speedModifierWhenCharging);
            if (targetDistSqr < this.targetInRangeRadiusSq || mob.getNavigation().isDone()) {
                double distance = Math.sqrt(targetDistSqr);
                Vec3 newAwayPos = PathfindingUtils.getPosAway(mob, 6 + mountDistance - distance, 7 + mountDistance - distance, 7, target.position());
                mob.getBrain().setMemory(ModMemoryModuleTypes.SPEAR_CHARGE_POSITION.get(), newAwayPos);
            }
        }
    }

    @Override
    protected void stop(ServerLevel level, PathfinderMob mob, long gameTime) {
        mob.getNavigation().stop();
        mob.stopUsingItem();
        mob.getBrain().eraseMemory(ModMemoryModuleTypes.SPEAR_CHARGE_POSITION.get());
        mob.getBrain().eraseMemory(ModMemoryModuleTypes.SPEAR_ENGAGE_TIME.get());
        mob.getBrain().setMemory(ModMemoryModuleTypes.SPEAR_STATUS.get(), SpearStatus.RETREAT);
    }

    @Override
    protected boolean timedOut(long gameTime) {
        return false;
    }
}
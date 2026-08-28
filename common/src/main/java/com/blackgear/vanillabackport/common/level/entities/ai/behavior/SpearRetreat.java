package com.blackgear.vanillabackport.common.level.entities.ai.behavior;

import com.blackgear.vanillabackport.common.api.extensions.entity.ControllableMob;
import com.blackgear.vanillabackport.common.registries.entities.ModMemoryModuleTypes;
import com.blackgear.vanillabackport.common.registries.items.ModDataComponents;
import com.blackgear.vanillabackport.core.util.WorldUtilities.PathfindingUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.Behavior;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class SpearRetreat extends Behavior<PathfinderMob> {
    private final double speedModifierWhenRepositioning;

    public SpearRetreat(double speedModifierWhenRepositioning) {
        super(Map.of(ModMemoryModuleTypes.SPEAR_STATUS.get(), MemoryStatus.VALUE_PRESENT), 100);
        this.speedModifierWhenRepositioning = speedModifierWhenRepositioning;
    }

    @Nullable
    private LivingEntity getTarget(PathfinderMob mob) {
        return mob.getBrain().getMemory(MemoryModuleType.ATTACK_TARGET).orElse(null);
    }

    private boolean ableToAttack(PathfinderMob mob) {
        return this.getTarget(mob) != null && mob.getMainHandItem().has(ModDataComponents.KINETIC_WEAPON.get());
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, PathfinderMob mob) {
        if (this.ableToAttack(mob) && !mob.isUsingItem()) {
            if (mob.getBrain().getMemory(ModMemoryModuleTypes.SPEAR_STATUS.get()).orElse(SpearStatus.APPROACH) != SpearStatus.RETREAT) {
                return false;
            } else {
                LivingEntity target = this.getTarget(mob);
                double targetDistSqr = mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
                int mountDistance = mob.isPassenger() ? 2 : 0;
                double distance = Math.sqrt(targetDistSqr);
                Vec3 awayPos = PathfindingUtils.getPosAway(mob, Math.max(0.0, 9 + mountDistance - distance), Math.max(1.0, 11 + mountDistance - distance), 7, target.position());
                if (awayPos == null) {
                    return false;
                } else {
                    mob.getBrain().setMemory(ModMemoryModuleTypes.SPEAR_FLEEING_POSITION.get(), awayPos);
                    return true;
                }
            }
        } else {
            return false;
        }
    }

    @Override
    protected void start(ServerLevel level, PathfinderMob mob, long gameTime) {
        mob.setAggressive(true);
        mob.getBrain().setMemory(ModMemoryModuleTypes.SPEAR_FLEEING_TIME.get(), 0);
        super.start(level, mob, gameTime);
    }

    @Override
    protected boolean canStillUse(ServerLevel level, PathfinderMob mob, long gameTime) {
        return mob.getBrain().getMemory(ModMemoryModuleTypes.SPEAR_FLEEING_TIME.get()).orElse(100) < 100
            && mob.getBrain().getMemory(ModMemoryModuleTypes.SPEAR_FLEEING_POSITION.get()).isPresent()
            && !mob.getNavigation().isDone()
            && this.ableToAttack(mob);
    }

    @Override
    protected void tick(ServerLevel level, PathfinderMob mob, long gameTime) {
        LivingEntity target = this.getTarget(mob);
        float speedModifier = ControllableMob.of(mob.getRootVehicle()).chargeSpeedModifier();
        mob.getBrain().setMemory(MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
        mob.getBrain().setMemory(ModMemoryModuleTypes.SPEAR_FLEEING_TIME.get(), mob.getBrain().getMemory(ModMemoryModuleTypes.SPEAR_FLEEING_TIME.get()).orElse(0) + 1);
        mob.getBrain().getMemory(ModMemoryModuleTypes.SPEAR_FLEEING_POSITION.get()).ifPresent(pos -> mob.getNavigation().moveTo(pos.x, pos.y, pos.z, speedModifier * this.speedModifierWhenRepositioning));
    }

    @Override
    protected void stop(ServerLevel level, PathfinderMob mob, long gameTime) {
        mob.getNavigation().stop();
        mob.setAggressive(false);
        mob.stopUsingItem();
        mob.getBrain().eraseMemory(ModMemoryModuleTypes.SPEAR_FLEEING_TIME.get());
        mob.getBrain().eraseMemory(ModMemoryModuleTypes.SPEAR_FLEEING_POSITION.get());
        mob.getBrain().eraseMemory(ModMemoryModuleTypes.SPEAR_STATUS.get());
    }
}

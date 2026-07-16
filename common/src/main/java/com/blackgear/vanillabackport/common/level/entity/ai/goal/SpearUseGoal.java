package com.blackgear.vanillabackport.common.level.entity.ai.goal;

import com.blackgear.vanillabackport.common.api.extensions.entity.ControllableMob;
import com.blackgear.vanillabackport.common.level.item.spear.KineticWeapon;
import com.blackgear.vanillabackport.core.util.WorldUtilities.PathfindingUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.Optional;

public class SpearUseGoal<T extends Monster> extends Goal {
    private static final double MAX_FLEEING_TIME = reducedTickDelay(100);
    private final T mob;
    @Nullable private SpearUseGoal.SpearUseState state;
    private final double speedModifierWhenCharging;
    private final double speedModifierWhenRepositioning;
    private final float approachDistanceSq;
    private final float targetInRangeRadiusSq;

    public SpearUseGoal(
        T mob,
        double speedModifierWhenCharging,
        double speedModifierWhenRepositioning,
        float approachDistance,
        float targetInRangeRadius
    ) {
        this.mob = mob;
        this.speedModifierWhenCharging = speedModifierWhenCharging;
        this.speedModifierWhenRepositioning = speedModifierWhenRepositioning;
        this.approachDistanceSq = approachDistance * approachDistance;
        this.targetInRangeRadiusSq = targetInRangeRadius * targetInRangeRadius;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.ableToAttack() && !this.mob.isUsingItem();
    }

    private boolean ableToAttack() {
        return this.mob.getTarget() != null && KineticWeapon.hasKineticWeapon(this.mob.getMainHandItem());
    }

    private int getKineticWeaponUseDuration() {
        int durationTicks = Optional.ofNullable(KineticWeapon.getKineticWeapon(this.mob.getMainHandItem()))
            .map(KineticWeapon::computeDamageUseDuration)
            .orElse(0);
        return reducedTickDelay(durationTicks);
    }

    @Override
    public boolean canContinueToUse() {
        return this.state != null && !this.state.done && this.ableToAttack();
    }

    @Override
    public void start() {
        super.start();
        this.mob.setAggressive(true);
        this.state = new SpearUseState();
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.getNavigation().stop();
        this.mob.setAggressive(false);
        this.state = null;
        this.mob.stopUsingItem();
    }

    @Override
    public void tick() {
        if (this.state != null) {
            LivingEntity target = this.mob.getTarget();
            if (target == null) return;

            double targetDistSqr = this.mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
            Entity mount = this.mob.getRootVehicle();
            
            float speedModifier = ControllableMob.of(mount).chargeSpeedModifier();

            int mountDistance = this.mob.isPassenger() ? 2 : 0;
            
            this.mob.lookAt(target, 30.0F, 30.0F);
            this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
            
            Vec3 targetPos = new Vec3(target.getX(), target.getY(), target.getZ());

            if (this.state.notEngagedYet()) {
                if (targetDistSqr > this.approachDistanceSq) {
                    this.mob.getNavigation().moveTo(target, speedModifier * this.speedModifierWhenRepositioning);
                    return;
                }

                int duration = this.getKineticWeaponUseDuration();
                this.state.startEngagement(duration);
                this.mob.startUsingItem(InteractionHand.MAIN_HAND);
            }

            if (this.state.tickAndCheckEngagement()) {
                this.mob.stopUsingItem();
                double distance = Math.sqrt(targetDistSqr);
                this.state.awayPos = PathfindingUtils.getPosAway(
                    this.mob,
                    Math.max(0.0, 9 + mountDistance - distance),
                    Math.max(1.0, 11 + mountDistance - distance),
                    7,
                    targetPos
                );
                this.state.fleeingTime = 1;
            }

            if (!this.state.tickAndCheckFleeing()) {
                if (this.state.awayPos != null) {
                    this.mob.getNavigation().moveTo(this.state.awayPos.x, this.state.awayPos.y, this.state.awayPos.z, speedModifier * this.speedModifierWhenRepositioning);
                    if (this.mob.getNavigation().isDone()) {
                        if (this.state.fleeingTime > 0) {
                            this.state.done = true;
                            return;
                        }

                        this.state.awayPos = null;
                    }
                } else {
                    this.mob.getNavigation().moveTo(target, speedModifier * this.speedModifierWhenCharging);
                    if (targetDistSqr < this.targetInRangeRadiusSq || this.mob.getNavigation().isDone()) {
                        double distance = Math.sqrt(targetDistSqr);
                        this.state.awayPos = PathfindingUtils.getPosAway(this.mob, 6 + mountDistance - distance, 7 + mountDistance - distance, 7, targetPos);
                    }
                }
            }
        }
    }

    public static class SpearUseState {
        private int engageTime = -1;
        private int fleeingTime = -1;
        @Nullable private Vec3 awayPos;
        private boolean done = false;

        public boolean notEngagedYet() {
            return this.engageTime < 0;
        }

        public void startEngagement(int spearDownTime) {
            this.engageTime = spearDownTime;
        }

        public boolean tickAndCheckEngagement() {
            if (this.engageTime > 0) {
                this.engageTime--;
                return this.engageTime == 0;
            }

            return false;
        }

        public boolean tickAndCheckFleeing() {
            if (this.fleeingTime > 0) {
                this.fleeingTime++;
                if (this.fleeingTime > MAX_FLEEING_TIME) {
                    this.done = true;
                    return true;
                }
            }

            return false;
        }
    }
}
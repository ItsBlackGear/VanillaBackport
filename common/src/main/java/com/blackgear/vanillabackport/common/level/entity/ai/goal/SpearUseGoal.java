package com.blackgear.vanillabackport.common.level.entity.ai.goal;

import com.blackgear.vanillabackport.common.api.extensions.entity.ControllableMob;
import com.blackgear.vanillabackport.common.level.components.KineticWeapon;
import com.blackgear.vanillabackport.common.registries.items.ModDataComponents;
import com.blackgear.vanillabackport.core.VanillaBackport;
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
        return this.mob.getTarget() != null && this.mob.getMainHandItem().has(ModDataComponents.KINETIC_WEAPON.get());
    }

    private int getKineticWeaponUseDuration() {
        int durationTicks = Optional.ofNullable(this.mob.getMainHandItem().get(ModDataComponents.KINETIC_WEAPON.get()))
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
        VanillaBackport.LOGGER.debug("SpearUseGoal [{}]: Goal STARTED.", this.mob.getName().getString());
        this.mob.setAggressive(true);
        this.state = new SpearUseState();
    }

    @Override
    public void stop() {
        super.stop();
        VanillaBackport.LOGGER.debug("SpearUseGoal [{}]: Goal STOPPED.", this.mob.getName().getString());
        this.mob.getNavigation().stop();
        this.mob.setAggressive(false);
        this.state = null;
        this.mob.stopUsingItem();
    }

    @Override
    public void tick() {
        if (this.state != null) {
            LivingEntity target = this.mob.getTarget();
            if (target == null) {
                VanillaBackport.LOGGER.warn("SpearUseGoal [{}]: Tick executed but target is null!", this.mob.getName().getString());
                return;
            }

            double targetDistSqr = this.mob.distanceToSqr(target.getX(), target.getY(), target.getZ());
            Entity mount = this.mob.getRootVehicle();
            
            float speedModifier = 1.0F;
            if (mount != null) {
                speedModifier = ControllableMob.of(mount).chargeSpeedModifier();
            }

            int mountDistance = this.mob.isPassenger() ? 2 : 0;
            
            this.mob.lookAt(target, 30.0F, 30.0F);
            this.mob.getLookControl().setLookAt(target, 30.0F, 30.0F);
            
            Vec3 targetPos = new Vec3(target.getX(), target.getY(), target.getZ());

            // 1. INICIO DEL ENFRENTAMIENTO
            if (this.state.notEngagedYet()) {
                if (targetDistSqr > this.approachDistanceSq) {
                    this.mob.getNavigation().moveTo(target, speedModifier * this.speedModifierWhenRepositioning);
                    return;
                }

                int duration = this.getKineticWeaponUseDuration();
                VanillaBackport.LOGGER.debug("SpearUseGoal [{}]: Target in range. Starting engagement (using spear) for {} ticks. Target distance: {}",
                    this.mob.getName().getString(), duration, Math.sqrt(targetDistSqr));
                
                this.state.startEngagement(duration);
                this.mob.startUsingItem(InteractionHand.MAIN_HAND);
            }

            // 2. FIN DE CARGA -> PREPARAR HUIDA
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
                
                VanillaBackport.LOGGER.debug("SpearUseGoal [{}]: Engagement duration elapsed. Moving away. Calculated escape pos: {}",
                    this.mob.getName().getString(), this.state.awayPos);
            }

            // 3. MOVIMIENTO / HUIDA ACTIVA
            if (!this.state.tickAndCheckFleeing()) {
                if (this.state.awayPos != null) {
                    this.mob.getNavigation().moveTo(this.state.awayPos.x, this.state.awayPos.y, this.state.awayPos.z, speedModifier * this.speedModifierWhenRepositioning);
                    if (this.mob.getNavigation().isDone()) {
                        if (this.state.fleeingTime > 0) {
                            VanillaBackport.LOGGER.debug("SpearUseGoal [{}]: Successfully reached escape position. Goal complete.", this.mob.getName().getString());
                            this.state.done = true;
                            return;
                        }

                        this.state.awayPos = null;
                        VanillaBackport.LOGGER.debug("SpearUseGoal [{}]: Temporary awayPos cleared.", this.mob.getName().getString());
                    }
                } else {
                    this.mob.getNavigation().moveTo(target, speedModifier * this.speedModifierWhenCharging);
                    if (targetDistSqr < this.targetInRangeRadiusSq || this.mob.getNavigation().isDone()) {
                        double distance = Math.sqrt(targetDistSqr);
                        this.state.awayPos = PathfindingUtils.getPosAway(this.mob, 6 + mountDistance - distance, 7 + mountDistance - distance, 7, targetPos);
                        
                        VanillaBackport.LOGGER.debug("SpearUseGoal [{}]: Charge finished or close enough to target. Target distance: {}. Retargeting escape position: {}",
                            this.mob.getName().getString(), distance, this.state.awayPos);
                    }
                }
            } else {
                VanillaBackport.LOGGER.debug("SpearUseGoal [{}]: Max fleeing time reached ({} ticks). Aborting goal.",
                    this.mob.getName().getString(), MAX_FLEEING_TIME);
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
package com.blackgear.vanillabackport.core.mixin.common.entities;

import com.blackgear.vanillabackport.common.api.leash.LeashAccess;
import com.blackgear.vanillabackport.common.api.leash.LeashExtension;
import com.blackgear.vanillabackport.common.api.leash.LeashPhysics;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PathfinderMob.class)
public abstract class LeashBehaviorMixin extends Mob implements LeashAccess {
    @Shadow protected abstract void onLeashDistance(float distance);
    @Shadow protected abstract boolean shouldStayCloseToLeashHolder();
    @Shadow protected abstract double followLeashSpeed();

    @Unique private double angularMomentum;

    protected LeashBehaviorMixin(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public double angularMomentum() {
        return this.angularMomentum;
    }

    @Override
    public void setAngularMomentum(double angularMomentum) {
        this.angularMomentum = angularMomentum;
    }

    @Inject(method = "tickLeash", at = @At("HEAD"), cancellable = true)
    private void vb$onTickLeash(CallbackInfo ci) {
        ci.cancel();
        super.tickLeash();

        Entity holder = this.getLeashHolder();
        if (holder != null && holder.level() == this.level()) {
            this.restrictTo(holder.blockPosition(), 5);
            double leashDistance = LeashPhysics.leashDistanceTo(this, holder);
            if (((Mob) this) instanceof TamableAnimal pet && pet.isInSittingPose()) {
                if (leashDistance > LeashExtension.getOrDefault(this, LeashExtension::leashSnapDistance, 10.0)) {
                    this.dropLeash(true, true);
                }

                return;
            }

            if (leashDistance > LeashExtension.getOrDefault(this, LeashExtension::leashSnapDistance, 10.0)) {
                this.leashTooFarBehaviour();
            } else if (leashDistance > LeashExtension.getOrDefault(this, LeashExtension::leashElasticDistance, 6.0) - (double) holder.getBbWidth() - (double) this.getBbWidth() && LeashPhysics.checkElasticInteractions(this, holder)) {
                this.onElasticLeashPull(holder);
            } else {
                this.closeRangeLeashBehavior(holder);
            }

            LeashAccess access = LeashAccess.of(this);
            this.setYRot((float) ((double) this.getYRot() - access.angularMomentum()));
            access.setAngularMomentum(access.angularMomentum() * (double) LeashPhysics.angularFriction(this));
        }
    }

    @Override
    public void leashTooFarBehaviour() {
        this.dropLeash(true, true);
        this.goalSelector.disableControlFlag(Goal.Flag.MOVE);
    }

    @Override
    public void onElasticLeashPull(Entity entity) {
        this.onLeashDistance(this.distanceTo(entity));
        this.checkSlowFallDistance();
    }

    @Override
    public void closeRangeLeashBehavior(Entity entity) {
        if (this.shouldStayCloseToLeashHolder()) {
            this.goalSelector.enableControlFlag(Goal.Flag.MOVE);
            float distanceFromHolder = this.distanceTo(entity);
            Vec3 movement = new Vec3(entity.getX() - this.getX(), entity.getY() - this.getY(), entity.getZ() - this.getZ()).normalize().scale(Math.max(distanceFromHolder - 2.0F, 0.0F));
            this.getNavigation().moveTo(this.getX() + movement.x, this.getY() + movement.y, this.getZ() + movement.z, this.followLeashSpeed());
        }
    }
}
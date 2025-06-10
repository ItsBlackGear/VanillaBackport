package com.blackgear.vanillabackport.common.api.leash;

import com.blackgear.vanillabackport.core.mixin.access.PathfinderMobAccessor;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public interface LeashExtension {
    default double angularMomentum() {
        return 0.0;
    }

    default void setAngularMomentum(double angularMomentum) { }

    default void onTickLeash() {
        if (this instanceof PathfinderMob mob) {
            Entity holder = mob.getLeashHolder();
            if (holder != null && holder.level() == mob.level()) {
                mob.restrictTo(holder.blockPosition(), 5);
                double leashDistance = LeashPhysics.leashDistanceTo(mob, holder);
                if (mob instanceof TamableAnimal pet && pet.isInSittingPose()) {
                    if (leashDistance > this.leashSnapDistance()) {
                        mob.dropLeash(true, true);
                    }

                    return;
                }

                if (leashDistance > this.leashSnapDistance()) {
                    this.leashTooFarBehaviour();
                } else if (leashDistance > this.leashElasticDistance() - (double) holder.getBbWidth() - (double) mob.getBbWidth() && LeashPhysics.checkElasticInteractions(mob, holder)) {
                    this.onElasticLeashPull(holder);
                } else {
                    this.closeRangeLeashBehavior(holder);
                }

                mob.setYRot((float) ((double) mob.getYRot() - this.angularMomentum()));
                this.setAngularMomentum(this.angularMomentum() * (double) LeashPhysics.angularFriction(mob));
            }
        }
    }

    default void leashTooFarBehaviour() {
        if (this instanceof PathfinderMob mob) {
            mob.dropLeash(true, true);
            mob.goalSelector.disableControlFlag(Goal.Flag.MOVE);
        }
    }

    default void onElasticLeashPull(Entity entity) {
        if (this instanceof PathfinderMob mob) {
            ((PathfinderMobAccessor) this).callOnLeashDistance(mob.distanceTo(entity));
            mob.checkSlowFallDistance();
        }
    }

    default void closeRangeLeashBehavior(Entity entity) {
        if (this instanceof PathfinderMob mob) {
            if (((PathfinderMobAccessor) this).callShouldStayCloseToLeashHolder()) {
                mob.goalSelector.enableControlFlag(Goal.Flag.MOVE);
                float distanceFromHolder = mob.distanceTo(entity);
                Vec3 movement = new Vec3(entity.getX() - mob.getX(), entity.getY() - mob.getY(), entity.getZ() - mob.getZ()).normalize().scale(Math.max(distanceFromHolder - 2.0F, 0.0F));
                mob.getNavigation().moveTo(mob.getX() + movement.x, mob.getY() + movement.y, mob.getZ() + movement.z, ((PathfinderMobAccessor) this).callFollowLeashSpeed());
            }
        }
    }

    default double leashSnapDistance() {
        return 12.0;
    }

    default double leashElasticDistance() {
        return 6.0;
    }

    default boolean supportQuadLeash() {
        return false;
    }

    default boolean supportQuadLeashAsHolder() {
        return false;
    }

    default Vec3[] getQuadLeashOffsets() {
        return createQuadLeashOffsets((Entity) this, 0.0, 0.5, 0.5, 0.5);
    }

    static Vec3[] createQuadLeashOffsets(Entity entity, double forwardOffset, double sideOffset, double widthOffset, double heightOffset) {
        float entityWidth = entity.getBbWidth();
        double forward = forwardOffset * (double) entityWidth;
        double side = sideOffset * (double) entityWidth;
        double width = widthOffset * (double) entityWidth;
        double height = heightOffset * (double) entity.getBbHeight();

        return new Vec3[] {
            new Vec3(-width, height, side + forward),
            new Vec3(-width, height, -side + forward),
            new Vec3(width, height, -side + forward),
            new Vec3(width, height, side + forward)
        };
    }
}
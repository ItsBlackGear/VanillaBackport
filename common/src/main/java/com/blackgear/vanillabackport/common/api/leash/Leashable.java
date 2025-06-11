package com.blackgear.vanillabackport.common.api.leash;

import com.blackgear.vanillabackport.core.mixin.access.PathfinderMobAccessor;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.monster.Enemy;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public interface Leashable {
    default double angularMomentum() {
        return 0.0;
    }

    default void setAngularMomentum(double angularMomentum) { }

    default Entity getLeashHolder() {
        if (this instanceof Mob mob) {
            return mob.getLeashHolder();
        }

        return null;
    }

    default void dropLeash(boolean broadcast, boolean dropItem) {
        if (this instanceof Mob mob) {
            mob.dropLeash(broadcast, dropItem);
        }
    }

    default boolean isLeashed() {
        if (this instanceof Mob mob) {
            return mob.isLeashed();
        }

        return false;
    }

    default void setLeashedTo(Entity entity, boolean sendAttachPacket) {
        if (this instanceof Mob mob) {
            mob.setLeashedTo(entity, sendAttachPacket);
        }
    }

    default boolean canBeLeashed(Player entity) {
        if (this instanceof Mob mob) {
            return mob.canBeLeashed(entity);
        }

        return !(this instanceof Enemy);
    }

    default double leashDistanceTo(Entity entity) {
        return entity.getBoundingBox().getCenter().distanceTo(((Entity) this).getBoundingBox().getCenter());
    }

    default void onTickLeash() {
        if (this instanceof Entity entity) {
            Entity holder = this.getLeashHolder();
            if (holder != null && holder.level() == entity.level()) {
                double leashDistance = LeashPhysics.leashDistanceTo(entity, holder);

                if (this instanceof Mob) {
                    if (entity instanceof TamableAnimal pet && pet.isInSittingPose()) {
                        if (leashDistance > this.leashSnapDistance()) {
                            this.dropLeash(true, true);
                        }

                        return;
                    }
                }

                this.whenLeashedTo(holder);
                if (leashDistance > this.leashSnapDistance()) {
                    entity.level().playSound(null, holder, SoundEvents.LEASH_KNOT_BREAK, SoundSource.NEUTRAL, 1.0F, 1.0F);
                    this.leashTooFarBehaviour();
                } else if (leashDistance > this.leashElasticDistance() - (double) holder.getBbWidth() - (double) entity.getBbWidth() && LeashPhysics.checkElasticInteractions(entity, holder)) {
                    this.onElasticLeashPull(holder);
                } else {
                    this.closeRangeLeashBehavior(holder);
                }

                entity.setYRot((float) ((double) entity.getYRot() - this.angularMomentum()));
                this.setAngularMomentum(this.angularMomentum() * (double) LeashPhysics.angularFriction(entity));
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
        if (this instanceof PathfinderMob mob) ((PathfinderMobAccessor) this).callOnLeashDistance(mob.distanceTo(entity));
        ((Entity) this).checkSlowFallDistance();
    }

    default void closeRangeLeashBehavior(Entity entity) {
        if (this instanceof PathfinderMob mob) {
            if (this.iShouldStayCloseToLeashHolder()) {
                mob.goalSelector.enableControlFlag(Goal.Flag.MOVE);
                float distanceFromHolder = mob.distanceTo(entity);
                Vec3 movement = new Vec3(entity.getX() - mob.getX(), entity.getY() - mob.getY(), entity.getZ() - mob.getZ()).normalize().scale(Math.max(distanceFromHolder - 2.0F, 0.0F));
                mob.getNavigation().moveTo(mob.getX() + movement.x, mob.getY() + movement.y, mob.getZ() + movement.z, this.iFollowLeashSpeed());
            }
        }
    }

    default void whenLeashedTo(Entity entity) {
        if (this instanceof PathfinderMob mob) {
            mob.restrictTo(entity.blockPosition(), (int) this.leashElasticDistance() - 1);
        }

        if (entity instanceof Leashable ext) {
            ext.notifyLeashHolder(this);
        }
    }

    default void notifyLeashHolder(Leashable entity) {

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

    default Vec3[] getQuadLeashHolderOffsets() {
        return createQuadLeashOffsets((Entity) this, 0.0, 0.5, 0.5, 0.0);
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

    default boolean iShouldStayCloseToLeashHolder() {
        if (this instanceof PathfinderMob) {
            return ((PathfinderMobAccessor) this).callShouldStayCloseToLeashHolder();
        }

        return true;
    }

    default double iFollowLeashSpeed() {
        if (this instanceof PathfinderMob) {
            return ((PathfinderMobAccessor) this).callFollowLeashSpeed();
        }

        return 1.0;
    }

    default void setBoatDelayedLeashHolderId(int leashHolderId) {
    }
}
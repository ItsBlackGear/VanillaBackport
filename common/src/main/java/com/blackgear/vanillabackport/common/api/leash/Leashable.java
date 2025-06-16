package com.blackgear.vanillabackport.common.api.leash;

import com.blackgear.vanillabackport.core.mixin.access.EntityAccessor;
import com.blackgear.vanillabackport.core.mixin.access.PathfinderMobAccessor;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.Util;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public interface Leashable {
    Map<Predicate<Entity>, Function<Entity, Vec3[]>> QUAD_LEASH_OFFSETS = Util.make(() -> {
        ImmutableMap.Builder<Predicate<Entity>, Function<Entity, Vec3[]>> offsets = new ImmutableMap.Builder<>();
        offsets.put(entity -> entity instanceof Camel, entity -> Leashable.createQuadLeashOffsets(entity, 0.02, 0.48, 0.25, 0.82));
        offsets.put(entity -> entity instanceof AbstractHorse, entity -> Leashable.createQuadLeashOffsets(entity, 0.04, 0.52, 0.23, 0.87));
        offsets.put(entity -> entity instanceof AbstractChestedHorse, entity -> Leashable.createQuadLeashOffsets(entity, 0.04, 0.41, 0.18, 0.73));
        offsets.put(entity -> entity instanceof Sniffer, entity -> Leashable.createQuadLeashOffsets(entity, -0.01, 0.63, 0.38, 1.15));
        return offsets.build();
    });

    Vec3 AXIS_SPECIFIC_ELASTICITY = new Vec3(0.8, 0.2, 0.8);
    List<Vec3> ENTITY_ATTACHMENT_POINT = ImmutableList.of(new Vec3(0.0, 0.5, 0.5));
    List<Vec3> LEASHER_ATTACHMENT_POINT = ImmutableList.of(new Vec3(0.0, 0.5, 0.0));
    List<Vec3> SHARED_QUAD_ATTACHMENT_POINTS = ImmutableList.of(
        new Vec3(-0.5, 0.5, 0.5),
        new Vec3(-0.5, 0.5, -0.5),
        new Vec3(0.5, 0.5, -0.5),
        new Vec3(0.5, 0.5, 0.5)
    );

    default boolean isLeashed() {
        if (this instanceof Mob mob) return mob.isLeashed();
        return false;
    }

    default boolean canHaveALeashAttachedTo(Entity target) {
        if (this == target) {
            return false;
        } else {
            return this.leashDistanceTo(target) <= this.leashSnapDistance() && this.canBeLeashed(target instanceof Player player ? player : null);
        }
    }

    default void setLeashedTo(Entity entity, boolean sendAttachPacket) {
        if (this instanceof Mob mob) mob.setLeashedTo(entity, sendAttachPacket);
    }

    default double leashDistanceTo(Entity entity) {
        return entity.getBoundingBox().getCenter().distanceTo(((Entity) this).getBoundingBox().getCenter());
    }

    default boolean canBeLeashed(Player entity) {
        if (this instanceof Mob mob) return mob.canBeLeashed(entity);
        return true;
    }

    default void dropLeash(boolean broadcast, boolean dropItem) {
        if (this instanceof Mob mob) mob.dropLeash(broadcast, dropItem);
    }

    static <E extends Entity & Leashable> void onTickLeash(E entity) {
        Entity holder = entity.getLeashHolder();
        if (holder != null && holder.level() == entity.level()) {
            double leashDistance = entity.leashDistanceTo(holder);

            if (entity instanceof TamableAnimal pet && pet.isInSittingPose()) {
                if (leashDistance > entity.leashSnapDistance()) {
                    entity.dropLeash(true, true);
                }

                return;
            }

            entity.whenLeashedTo(holder);
            if (leashDistance > entity.leashSnapDistance()) {
                entity.level().playSound(null, holder, SoundEvents.LEASH_KNOT_BREAK, SoundSource.NEUTRAL, 1.0F, 1.0F);
                entity.leashTooFarBehaviour();
            } else if (leashDistance > entity.leashElasticDistance() - (double) holder.getBbWidth() - (double) entity.getBbWidth() && entity.checkElasticInteractions(holder)) {
                entity.onElasticLeashPull(holder);
            } else {
                entity.closeRangeLeashBehavior(holder);
            }

            entity.setYRot((float) (entity.getYRot() - entity.angularMomentum()));
            entity.setAngularMomentum(entity.angularMomentum() * (double) angularFriction(entity));
        }
    }

    default void onElasticLeashPull(Entity entity) {
        if (this instanceof PathfinderMob mob) ((PathfinderMobAccessor) this).callOnLeashDistance(mob.distanceTo(entity));
        ((Entity) this).checkSlowFallDistance();
    }

    default double leashSnapDistance() {
        return 12.0;
    }

    default double leashElasticDistance() {
        return 6.0;
    }

    static <E extends Entity & Leashable> float angularFriction(E entity) {
        if (entity.onGround()) {
            return entity.level().getBlockState(((EntityAccessor) entity).callGetBlockPosBelowThatAffectsMyMovement()).getBlock().getFriction() * 0.91F;
        } else {
            return (entity.isInWater() || entity.isInLava()) ? 0.8F : 0.91F;
        }
    }

    default void whenLeashedTo(Entity entity) {
        if (this instanceof PathfinderMob mob) mob.restrictTo(entity.blockPosition(), (int) this.leashElasticDistance() - 1);
        if (entity instanceof Leashable ext) ext.notifyLeashHolder(this);
    }

    default void notifyLeashHolder(Leashable entity) {

    }

    default void leashTooFarBehaviour() {
        if (this instanceof PathfinderMob mob) mob.goalSelector.disableControlFlag(Goal.Flag.MOVE);
        this.dropLeash(true, true);
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

    default boolean checkElasticInteractions(Entity entity) {
        if (((Entity) this).getControllingPassenger() instanceof Player) return false;

        boolean supportQuadLeash = entity instanceof Leashable holder && holder.supportQuadLeashAsHolder() && this.supportQuadLeash();
        List<Wrench> wrenches = computeElasticInteraction(
            (Entity & Leashable) this,
            entity,
            supportQuadLeash ? SHARED_QUAD_ATTACHMENT_POINTS : ENTITY_ATTACHMENT_POINT,
            supportQuadLeash ? SHARED_QUAD_ATTACHMENT_POINTS : LEASHER_ATTACHMENT_POINT
        );

        if (wrenches.isEmpty()) {
            return false;
        } else {
            Wrench wrench = Wrench.accumulate(wrenches).scale(supportQuadLeash ? 0.25 : 1.0);
            this.setAngularMomentum(this.angularMomentum() + 10.0 * wrench.torque());
            Vec3 offset = getHolderMovement(entity).subtract(getKnownMovement((Entity) this));
            ((Entity) this).addDeltaMovement(wrench.force().multiply(AXIS_SPECIFIC_ELASTICITY).add(offset.scale(0.11)));
            return true;
        }
    }

    static Vec3 getHolderMovement(Entity entity) {
        return entity instanceof Mob mob && mob.isNoAi() ? Vec3.ZERO : getKnownMovement(entity);
    }

    static Vec3 getKnownMovement(Entity entity) {
        Entity passenger = entity.getControllingPassenger();
        if (passenger instanceof Player player) {
            if (entity.isAlive()) {
                return player.getDeltaMovement();
            }
        }

        return entity.getDeltaMovement();
    }

    static <E extends Entity & Leashable> List<Wrench> computeElasticInteraction(E entity, Entity holder, List<Vec3> attachmentPoints, List<Vec3> holderAttachmentPoints) {
        double elasticDistance = entity.leashElasticDistance();
        Vec3 entityMovement = getHolderMovement(entity);
        float entityYaw = entity.getYRot() * (float) (Math.PI / 180.0);
        Vec3 entityDimensions = new Vec3(entity.getBbWidth(), entity.getBbHeight(), entity.getBbWidth());
        float holderYaw = holder.getYRot() * (float) (Math.PI / 180.0);
        Vec3 holderDimensions = new Vec3(holder.getBbWidth(), holder.getBbHeight(), holder.getBbWidth());
        List<Wrench> wrenches = new ArrayList<>();

        for (int i = 0; i < attachmentPoints.size(); i++) {
            Vec3 entityOffset = attachmentPoints.get(i).multiply(entityDimensions).yRot(-entityYaw);
            Vec3 entityPosition = entity.position().add(entityOffset);
            Vec3 holderOffset = holderAttachmentPoints.get(i).multiply(holderDimensions).yRot(-holderYaw);
            Vec3 holderPosition = holder.position().add(holderOffset);
            computeDampenedSpringInteraction(holderPosition, entityPosition, elasticDistance, entityMovement, entityOffset).ifPresent(wrenches::add);
        }

        return wrenches;
    }

    private static Optional<Wrench> computeDampenedSpringInteraction(Vec3 holderPos, Vec3 entityPos, double threshold, Vec3 movement, Vec3 offset) {
        double distance = entityPos.distanceTo(holderPos);
        if (distance < threshold) {
            return Optional.empty();
        } else {
            Vec3 force = holderPos.subtract(entityPos).normalize().scale(distance - threshold);
            double torque = Wrench.torqueFromForce(offset, force);
            boolean movingWithForce = movement.dot(force) >= 0.0;
            if (movingWithForce) {
                force = force.scale(0.3F);
            }

            return Optional.of(new Wrench(force, torque));
        }
    }

    default boolean supportQuadLeash() {
        Entity entity = (Entity) this;
        for (Predicate<Entity> filter : QUAD_LEASH_OFFSETS.keySet()) {
            if (filter.test(entity)) {
                return true;
            }
        }

        return false;
    }

    default boolean supportQuadLeashAsHolder() {
        return false;
    }

    default Vec3[] getQuadLeashOffsets() {
        Entity entity = (Entity) this;
        for (Predicate<Entity> filter : QUAD_LEASH_OFFSETS.keySet()) {
            if (filter.test(entity)) {
                return QUAD_LEASH_OFFSETS.get(filter).apply(entity);
            }
        }

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

    default Entity getLeashHolder() {
        if (this instanceof Mob mob) return mob.getLeashHolder();
        return null;
    }

    static List<Leashable> leashableLeashedTo(Entity entity) {
        return leashableInArea(entity, leashable -> leashable.getLeashHolder() == entity);
    }

    static List<Leashable> leashableInArea(Entity entity, Predicate<Leashable> filter) {
        return leashableInArea(entity.level(), entity.getBoundingBox().getCenter(), filter);
    }

    static List<Leashable> leashableInArea(Level level, Vec3 pos, Predicate<Leashable> filter) {
        AABB area = AABB.ofSize(pos, 32.0, 32.0, 32.0);
        return level.getEntitiesOfClass(Entity.class, area, entity -> entity instanceof Leashable leashable && filter.test(leashable))
            .stream()
            .map(Leashable.class::cast)
            .toList();
    }

    default void setBoatDelayedLeashHolderId(int leashHolderId) {
    }

    default double angularMomentum() {
        return 0.0;
    }

    default void setAngularMomentum(double angularMomentum) { }

    static float getPreciseBodyRotation(Entity entity, float partialTicks) {
        if (entity instanceof LivingEntity living) {
            return Mth.lerp(partialTicks, living.yBodyRotO, living.yBodyRot);
        } else {
            return Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
        }
    }

    record Wrench(Vec3 force, double torque) {
        static final Wrench ZERO = new Wrench(Vec3.ZERO, 0.0);

        /**
         * Calculates torque from two vectors.
         */
        static double torqueFromForce(Vec3 position, Vec3 force) {
            return position.z * force.x - position.x * force.z;
        }

        /**
         * Combines multiple wrenches into a single resultant wrench.
         */
        public static Wrench accumulate(List<Wrench> wrenches) {
            if (wrenches.isEmpty()) return ZERO;

            double x = 0.0;
            double y = 0.0;
            double z = 0.0;
            double torque = 0.0;

            for (Wrench wrench : wrenches) {
                Vec3 force = wrench.force;
                x += force.x;
                y += force.y;
                z += force.z;
                torque += wrench.torque;
            }
            return new Wrench(new Vec3(x, y, z), torque);
        }

        /**
         * Creates a new wrench by scaling the current one by the given factor.
         */
        public Wrench scale(double factor) {
            return new Wrench(this.force.scale(factor), this.torque * factor);
        }
    }
}
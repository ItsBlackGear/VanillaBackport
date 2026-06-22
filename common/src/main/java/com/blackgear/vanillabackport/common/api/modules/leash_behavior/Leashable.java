package com.blackgear.vanillabackport.common.api.modules.leash_behavior;

import com.blackgear.vanillabackport.core.mixin.access.EntityAccessor;
import com.blackgear.vanillabackport.core.mixin.access.PathfinderMobAccessor;
import com.blackgear.vanillabackport.core.util.WorldUtilities.*;
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
        offsets.put(entity -> entity instanceof Camel, entity -> Leashable.vb$createQuadLeashOffsets(entity, 0.02, 0.48, 0.25, 0.82));
        offsets.put(entity -> entity instanceof AbstractHorse, entity -> Leashable.vb$createQuadLeashOffsets(entity, 0.04, 0.52, 0.23, 0.87));
        offsets.put(entity -> entity instanceof AbstractChestedHorse, entity -> Leashable.vb$createQuadLeashOffsets(entity, 0.04, 0.41, 0.18, 0.73));
        offsets.put(entity -> entity instanceof Sniffer, entity -> Leashable.vb$createQuadLeashOffsets(entity, -0.01, 0.63, 0.38, 1.15));
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

    default boolean vb$isLeashed() {
        if (this instanceof Mob mob) return mob.isLeashed();
        return false;
    }

    default boolean vb$canHaveALeashAttachedTo(Entity target) {
        if (this == target) {
            return false;
        } else {
            return this.vb$leashDistanceTo(target) <= this.vb$leashSnapDistance() && this.vb$canBeLeashed(target);
        }
    }

    default void vb$setLeashedTo(Entity entity, boolean sendAttachPacket) {
        if (this instanceof Mob mob) mob.setLeashedTo(entity, sendAttachPacket);
    }

    default double vb$leashDistanceTo(Entity entity) {
        return entity.getBoundingBox().getCenter().distanceTo(((Entity) this).getBoundingBox().getCenter());
    }

    default boolean vb$canBeLeashed(Entity entity) {
        if (this instanceof Mob mob) return mob.canBeLeashed(entity instanceof Player player ? player : null);
        return true;
    }

    default void vb$dropLeash(boolean broadcast, boolean dropItem) {
        if (this instanceof Mob mob) mob.dropLeash(broadcast, dropItem);
    }

    default void vb$dropLeash() {
        this.vb$dropLeash(true, true);
    }

    static <E extends Entity & Leashable> void vb$onTickLeash(E entity) {
        Entity leashHolder = entity.vb$getLeashHolder();
        if (leashHolder != null && leashHolder.level() == entity.level()) {
            double distanceTo = entity.vb$leashDistanceTo(leashHolder);

            if (entity instanceof TamableAnimal pet && pet.isInSittingPose()) {
                if (distanceTo > entity.vb$leashSnapDistance()) {
                    entity.vb$dropLeash(true, true);
                }

                return;
            }

            entity.vb$whenLeashedTo(leashHolder);
            if (distanceTo > entity.vb$leashSnapDistance()) {
                entity.level().playSound(null, leashHolder.getX(), leashHolder.getY(), leashHolder.getZ(), SoundEvents.LEASH_KNOT_BREAK, SoundSource.NEUTRAL, 1.0F, 1.0F);
                entity.vb$leashTooFarBehaviour();
            } else if (distanceTo > entity.vb$leashElasticDistance() - leashHolder.getBbWidth() - entity.getBbWidth() && entity.vb$checkElasticInteractions(leashHolder)) {
                entity.vb$onElasticLeashPull(leashHolder);
            } else {
                entity.vb$closeRangeLeashBehavior(leashHolder);
            }

            entity.setYRot((float) (entity.getYRot() - entity.vb$angularMomentum()));
            entity.vb$setAngularMomentum(entity.vb$angularMomentum() * vb$angularFriction(entity));
        }
    }

    default void vb$onElasticLeashPull(Entity entity) {
        if (this instanceof PathfinderMob mob) ((PathfinderMobAccessor) this).callOnLeashDistance(mob.distanceTo(entity));
        ((Entity) this).checkSlowFallDistance();
    }

    default double vb$leashSnapDistance() {
        return 12.0;
    }

    default double vb$leashElasticDistance() {
        return 6.0;
    }

    static <E extends Entity & Leashable> float vb$angularFriction(E entity) {
        if (entity.onGround()) {
            return entity.level().getBlockState(((EntityAccessor) entity).callGetBlockPosBelowThatAffectsMyMovement()).getBlock().getFriction() * 0.91F;
        } else {
            return EntityUtils.isInLiquid(entity) ? 0.8F : 0.91F;
        }
    }

    default void vb$whenLeashedTo(Entity entity) {
        if (this instanceof Mob mob) mob.restrictTo(entity.blockPosition(), (int) this.vb$leashElasticDistance() - 1);
        if (entity instanceof Leashable ext) ext.vb$notifyLeashHolder(this);
    }

    default void vb$notifyLeashHolder(Leashable entity) {

    }

    default void vb$leashTooFarBehaviour() {
        if (this instanceof Mob mob) mob.goalSelector.disableControlFlag(Goal.Flag.MOVE);
        this.vb$dropLeash(true, true);
    }

    default void vb$closeRangeLeashBehavior(Entity entity) {
        if (this instanceof Mob mob) {
            if (((PathfinderMobAccessor) this).callShouldStayCloseToLeashHolder()) {
                mob.goalSelector.enableControlFlag(Goal.Flag.MOVE);
                float distanceFromHolder = mob.distanceTo(entity);
                Vec3 movement = new Vec3(entity.getX() - mob.getX(), entity.getY() - mob.getY(), entity.getZ() - mob.getZ()).normalize().scale(Math.max(distanceFromHolder - 2.0F, 0.0F));
                mob.getNavigation().moveTo(mob.getX() + movement.x, mob.getY() + movement.y, mob.getZ() + movement.z, ((PathfinderMobAccessor) this).callFollowLeashSpeed());
            }
        }
    }

    default boolean vb$checkElasticInteractions(Entity entity) {
        if (((Entity) this).getControllingPassenger() instanceof Player) return false;

        boolean quadConnection = entity instanceof Leashable holder && holder.vb$supportQuadLeashAsHolder() && this.vb$supportQuadLeash();
        List<Wrench> wrenches = vb$computeElasticInteraction(
            (Entity & Leashable) this,
            entity,
            quadConnection ? SHARED_QUAD_ATTACHMENT_POINTS : ENTITY_ATTACHMENT_POINT,
            quadConnection ? SHARED_QUAD_ATTACHMENT_POINTS : LEASHER_ATTACHMENT_POINT
        );

        if (wrenches.isEmpty()) {
            return false;
        } else {
            Wrench result = Wrench.accumulate(wrenches).scale(quadConnection ? 0.25 : 1.0);
            this.vb$setAngularMomentum(this.vb$angularMomentum() + 10.0 * result.torque());
            Vec3 offset = vb$getHolderMovement(entity).subtract(vb$getKnownMovement((Entity) this));
            ((Entity) this).addDeltaMovement(result.force().multiply(AXIS_SPECIFIC_ELASTICITY).add(offset.scale(0.11)));
            return true;
        }
    }

    static Vec3 vb$getHolderMovement(Entity entity) {
        return entity instanceof Mob mob && mob.isNoAi() ? Vec3.ZERO : vb$getKnownMovement(entity);
    }

    static Vec3 vb$getKnownMovement(Entity entity) {
        Entity passenger = entity.getControllingPassenger();
        if (passenger instanceof Player player) {
            if (entity.isAlive()) {
                return player.getDeltaMovement();
            }
        }

        return entity.getDeltaMovement();
    }

    static <E extends Entity & Leashable> List<Wrench> vb$computeElasticInteraction(E entity, Entity holder, List<Vec3> attachmentPoints, List<Vec3> holderAttachmentPoints) {
        double slackDistance = entity.vb$leashElasticDistance();
        Vec3 currentMovement = vb$getHolderMovement(entity);
        float entityYRot = entity.getYRot() * Mth.DEG_TO_RAD;
        Vec3 entityDimensions = new Vec3(entity.getBbWidth(), entity.getBbHeight(), entity.getBbWidth());
        float leashHolderYRot = holder.getYRot() * Mth.DEG_TO_RAD;
        Vec3 leasherDimensions = new Vec3(holder.getBbWidth(), holder.getBbHeight(), holder.getBbWidth());
        List<Wrench> wrenches = new ArrayList<>();

        for (int i = 0; i < attachmentPoints.size(); i++) {
            Vec3 entityAttachVector = attachmentPoints.get(i).multiply(entityDimensions).yRot(-entityYRot);
            Vec3 entityAttachPos = entity.position().add(entityAttachVector);
            Vec3 leasherAttachVector = holderAttachmentPoints.get(i).multiply(leasherDimensions).yRot(-leashHolderYRot);
            Vec3 leasherAttachPos = holder.position().add(leasherAttachVector);
            vb$computeDampenedSpringInteraction(leasherAttachPos, entityAttachPos, slackDistance, currentMovement, entityAttachVector).ifPresent(wrenches::add);
        }

        return wrenches;
    }

    private static Optional<Wrench> vb$computeDampenedSpringInteraction(Vec3 pivotPoint, Vec3 objectPosition, double springSlack, Vec3 movement, Vec3 offset) {
        double distance = objectPosition.distanceTo(pivotPoint);
        if (distance < springSlack) {
            return Optional.empty();
        } else {
            Vec3 displacement = pivotPoint.subtract(objectPosition).normalize().scale(distance - springSlack);
            double torque = Wrench.torqueFromForce(offset, displacement);
            boolean sameDirectionToMovement = movement.dot(displacement) >= 0.0;
            if (sameDirectionToMovement) {
                displacement = displacement.scale(0.3F);
            }

            return Optional.of(new Wrench(displacement, torque));
        }
    }

    default boolean vb$supportQuadLeash() {
        Entity entity = (Entity) this;
        for (Predicate<Entity> filter : QUAD_LEASH_OFFSETS.keySet()) {
            if (filter.test(entity)) {
                return true;
            }
        }

        return false;
    }

    default boolean vb$supportQuadLeashAsHolder() {
        return false;
    }

    default Vec3[] vb$getQuadLeashOffsets() {
        Entity entity = (Entity) this;
        for (Predicate<Entity> filter : QUAD_LEASH_OFFSETS.keySet()) {
            if (filter.test(entity)) {
                return QUAD_LEASH_OFFSETS.get(filter).apply(entity);
            }
        }

        return vb$createQuadLeashOffsets((Entity) this, 0.0, 0.5, 0.5, 0.5);
    }

    default Vec3[] vb$getQuadLeashHolderOffsets() {
        return vb$createQuadLeashOffsets((Entity) this, 0.0, 0.5, 0.5, 0.0);
    }

    static Vec3[] vb$createQuadLeashOffsets(Entity entity, double frontOffset, double frontBack, double leftRight, double height) {
        float width = entity.getBbWidth();
        double frontOffsetScaled = frontOffset * width;
        double frontBackScaled = frontBack * width;
        double leftRightScaled = leftRight * width;
        double heightScaled = height * entity.getBbHeight();

        return new Vec3[] {
            new Vec3(-leftRightScaled, heightScaled, frontBackScaled + frontOffsetScaled),
            new Vec3(-leftRightScaled, heightScaled, -frontBackScaled + frontOffsetScaled),
            new Vec3(leftRightScaled, heightScaled, -frontBackScaled + frontOffsetScaled),
            new Vec3(leftRightScaled, heightScaled, frontBackScaled + frontOffsetScaled)
        };
    }

    default Entity vb$getLeashHolder() {
        if (this instanceof Mob mob) return mob.getLeashHolder();
        return null;
    }

    static List<Leashable> vb$leashableLeashedTo(Entity entity) {
        return vb$leashableInArea(entity, leashable -> leashable.vb$getLeashHolder() == entity);
    }

    static List<Leashable> vb$leashableInArea(Entity entity, Predicate<Leashable> filter) {
        return vb$leashableInArea(entity.level(), entity.getBoundingBox().getCenter(), filter);
    }

    static List<Leashable> vb$leashableInArea(Level level, Vec3 pos, Predicate<Leashable> filter) {
        AABB area = AABB.ofSize(pos, 32.0, 32.0, 32.0);
        return level.getEntitiesOfClass(Entity.class, area, entity -> entity instanceof Leashable leashable && filter.test(leashable))
            .stream()
            .map(Leashable.class::cast)
            .toList();
    }

    default void vb$setBoatDelayedLeashHolderId(int leashHolderId) {
    }

    default double vb$angularMomentum() {
        return 0.0;
    }

    default void vb$setAngularMomentum(double angularMomentum) { }

    default void vb$resetAngularMomentum() {
        this.vb$setAngularMomentum(0.0);
    }

    static float vb$getPreciseBodyRotation(Entity entity, float partialTicks) {
        if (entity instanceof LivingEntity living) {
            return Mth.lerp(partialTicks, living.yBodyRotO, living.yBodyRot);
        } else {
            return Mth.lerp(partialTicks, entity.yRotO, entity.getYRot());
        }
    }

    record Wrench(Vec3 force, double torque) {
        static final Wrench ZERO = new Wrench(Vec3.ZERO, 0.0);

        static double torqueFromForce(Vec3 position, Vec3 force) {
            return position.z * force.x - position.x * force.z;
        }

        public static Wrench accumulate(List<Wrench> wrenches) {
            if (wrenches.isEmpty()) {
                return ZERO;
            } else {
                double x = 0.0, y = 0.0, z = 0.0, t = 0.0;

                for (Wrench wrench : wrenches) {
                    Vec3 force = wrench.force;
                    x += force.x;
                    y += force.y;
                    z += force.z;
                    t += wrench.torque;
                }

                return new Wrench(new Vec3(x, y, z), t);
            }
        }

        public Wrench scale(double factor) {
            return new Wrench(this.force.scale(factor), this.torque * factor);
        }
    }
}
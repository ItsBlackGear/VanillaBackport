package com.blackgear.vanillabackport.common.api.leash;

import com.blackgear.vanillabackport.core.mixin.access.EntityAccessor;
import com.google.common.collect.ImmutableList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LeashPhysics {
    public static final Vec3 AXIS_SPECIFIC_ELASTICITY = new Vec3(0.8, 0.2, 0.8);
    public static final List<Vec3> ENTITY_ATTACHMENT_POINT = ImmutableList.of(new Vec3(0.0, 0.5, 0.5));
    public static final List<Vec3> LEASHER_ATTACHMENT_POINT = ImmutableList.of(new Vec3(0.0, 0.5, 0.0));
    public static final List<Vec3> SHARED_QUAD_ATTACHMENT_POINTS = ImmutableList.of(new Vec3(-0.5, 0.5, 0.5), new Vec3(-0.5, 0.5, -0.5), new Vec3(0.5, 0.5, -0.5), new Vec3(0.5, 0.5, 0.5));

    public static double leashDistanceTo(Entity source, Entity target) {
        return target.getBoundingBox().getCenter().distanceTo(source.getBoundingBox().getCenter());
    }

    public static <E extends Entity> float angularFriction(E entity) {
        if (entity.onGround()) {
            return entity.level().getBlockState(((EntityAccessor) entity).callGetBlockPosBelowThatAffectsMyMovement()).getBlock().getFriction() * 0.91F;
        }

        if (entity.isInWater() || entity.isInLava()) {
            return 0.8F;
        }

        return 0.91F;
    }

    public static boolean checkElasticInteractions(Mob self, Entity holder) {
        boolean handleHolderQuadLeash = LeashExtension.getOrDefault(holder, LeashExtension::supportQuadLeashAsHolder, false);
        boolean handleQuadLeash = LeashExtension.getOrDefault(self, LeashExtension::supportQuadLeash, false);

        boolean supportQuad = handleHolderQuadLeash && handleQuadLeash;
        List<Wrench> wrenches = computeElasticInteraction(self, holder, supportQuad ? SHARED_QUAD_ATTACHMENT_POINTS : ENTITY_ATTACHMENT_POINT, supportQuad ? SHARED_QUAD_ATTACHMENT_POINTS : LEASHER_ATTACHMENT_POINT);
        if (wrenches.isEmpty()) return false;

        Wrench wrench = Wrench.accumulate(wrenches).scale(supportQuad ? 0.25 : 1.0);
        LeashAccess access = LeashAccess.of(self);
        access.setAngularMomentum(access.angularMomentum() + 10.0 * wrench.torque());
        Vec3 offset = getHolderMovement(holder).subtract(getKnownMovement(self));
        self.addDeltaMovement(wrench.force().multiply(AXIS_SPECIFIC_ELASTICITY).add(offset.scale(0.11)));
        return true;
    }

    private static Vec3 getHolderMovement(Entity entity) {
        if (entity instanceof Mob mob && mob.isNoAi()) {
            return Vec3.ZERO;
        }

        return getKnownMovement(entity);
    }

    private static Vec3 getKnownMovement(Entity entity) {
        LivingEntity passenger = entity.getControllingPassenger();
        if (passenger instanceof Player player) {
            if (entity.isAlive()) {
                return player.getDeltaMovement();
            }
        }

        return entity.getDeltaMovement();
    }

    private static <E extends Entity> List<Wrench> computeElasticInteraction(E entity, Entity holder, List<Vec3> attachmentPoints, List<Vec3> holderAttachmentPoints) {
        double elasticDistance = LeashExtension.getOrDefault(entity, LeashExtension::leashElasticDistance, 6.0);
        Vec3 entityMovement = getHolderMovement(entity);
        float entityYaw = entity.getYRot() * ((float) Math.PI / 180);
        Vec3 entityDimensions = new Vec3(entity.getBbWidth(), entity.getBbHeight(), entity.getBbWidth());
        float holderYaw = holder.getYRot() * ((float) Math.PI / 180);
        Vec3 holderDimensions = new Vec3(holder.getBbWidth(), holder.getBbHeight(), holder.getBbWidth());
        ArrayList<Wrench> wrenches = new ArrayList<>();

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
        if (distance < threshold) return Optional.empty();

        Vec3 force = holderPos.subtract(entityPos).normalize().scale(distance - threshold);
        double torque = Wrench.torqueFromForce(offset, force);
        boolean movingWithForce = movement.dot(force) >= 0.0;

        if (movingWithForce) force = force.scale(0.3F);

        return Optional.of(new Wrench(force, torque));
    }

    public record Wrench(Vec3 force, double torque) {
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
        static Wrench accumulate(List<Wrench> wrenches) {
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
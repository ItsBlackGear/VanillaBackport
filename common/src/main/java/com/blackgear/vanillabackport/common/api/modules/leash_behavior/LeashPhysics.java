package com.blackgear.vanillabackport.common.api.modules.leash_behavior;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.minecraft.Util;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.sniffer.Sniffer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;

public final class LeashPhysics {
    public static final double LEASH_TOO_FAR_DIST = 12.0;
    public static final double LEASH_ELASTIC_DIST = 6.0;
    public static final Vec3 AXIS_SPECIFIC_ELASTICITY = new Vec3(0.8, 0.2, 0.8);
    private static final double TORSIONAL_ELASTICITY = 10.0;
    private static final double STIFFNESS = 0.11;

    private static final List<Vec3> ENTITY_ATTACHMENT_POINT = ImmutableList.of(new Vec3(0.0, 0.5, 0.5));
    private static final List<Vec3> LEASHER_ATTACHMENT_POINT = ImmutableList.of(new Vec3(0.0, 0.5, 0.0));
    private static final List<Vec3> SHARED_QUAD_ATTACHMENT_POINTS = ImmutableList.of(
        new Vec3(-0.5, 0.5,  0.5), new Vec3(-0.5, 0.5, -0.5),
        new Vec3( 0.5, 0.5, -0.5), new Vec3( 0.5, 0.5,  0.5)
    );

    public static final Map<Predicate<Entity>, Function<Entity, Vec3[]>> QUAD_LEASH_OFFSETS = Util.make(() -> {
        ImmutableMap.Builder<Predicate<Entity>, Function<Entity, Vec3[]>> offsets = new ImmutableMap.Builder<>();
        offsets.put(entity -> entity instanceof Boat, entity -> createQuadOffsets(entity, 0.0, 0.64, 0.382, 0.88));
        offsets.put(entity -> entity instanceof Camel, entity -> createQuadOffsets(entity, 0.02, 0.48, 0.25, 0.82));
        offsets.put(entity -> entity instanceof AbstractChestedHorse, entity -> createQuadOffsets(entity, 0.04, 0.41, 0.18, 0.73));
        offsets.put(entity -> entity instanceof AbstractHorse, entity -> createQuadOffsets(entity, 0.04, 0.52, 0.23, 0.87));
        offsets.put(entity -> entity instanceof Sniffer, entity -> createQuadOffsets(entity, -0.01, 0.63, 0.38, 1.15));
        return offsets.build();
    });

    public static double distanceBetween(Entity target, Entity holder) {
        return holder.getBoundingBox().getCenter().distanceTo(target.getBoundingBox().getCenter());
    }

    public static boolean canAttachLeash(Leashable leashable, Entity holder) {
        if (leashable == holder) return false;
        Entity entity = (Entity) leashable;
        return distanceBetween(entity, holder) <= ((LeashableCallback) entity).vb$leashSnapDistance() && leashable.canBeLeashed();
    }

    public static float angularFriction(Entity entity) {
        if (entity.onGround()) {
            return entity.level()
                .getBlockState(entity.getBlockPosBelowThatAffectsMyMovement())
                .getBlock().getFriction() * 0.91F;
        } else {
            return entity.isInLiquid() ? 0.8F : 0.91F;
        }
    }

    public static Vec3 getHolderMovement(Entity holder) {
        return holder instanceof Mob mob && mob.isNoAi() ? Vec3.ZERO : holder.getKnownMovement();
    }

    public static <E extends Entity & Leashable> boolean checkElasticInteractions(
        E entity,
        Entity holder,
        Leashable.LeashData data
    ) {
        if (entity.getControllingPassenger() instanceof Player) return false;

        boolean quadConnection = ((LeashableCallback) holder).vb$supportsQuadLeashAsHolder() && ((LeashableCallback) entity).vb$supportsQuadLeash();
        List<Wrench> wrenches = computeElasticInteraction(
            entity,
            holder,
            quadConnection ? SHARED_QUAD_ATTACHMENT_POINTS : ENTITY_ATTACHMENT_POINT,
            quadConnection ? SHARED_QUAD_ATTACHMENT_POINTS : LEASHER_ATTACHMENT_POINT
        );

        if (wrenches.isEmpty()) {
            return false;
        } else {
            Wrench result = Wrench.accumulate(wrenches).scale(quadConnection ? 0.25 : 1.0);
            LeashDataExtension leashData = Objects.requireNonNull((LeashDataExtension) (Object) data);
            leashData.setAngularMomentum(leashData.angularMomentum() + TORSIONAL_ELASTICITY * result.torque());
            Vec3 offset = getHolderMovement(holder).subtract(entity.getKnownMovement());
            entity.addDeltaMovement(result.force().multiply(AXIS_SPECIFIC_ELASTICITY).add(offset.scale(STIFFNESS)));
            return true;
        }
    }

    private static <E extends Entity & Leashable> List<Wrench> computeElasticInteraction(
        E entity,
        Entity holder,
        List<Vec3> entityAttachmentPoints,
        List<Vec3> leasherAttachmentPoints
    ) {
        double slackDistance = ((LeashableCallback) entity).vb$leashElasticDistance();
        Vec3 currentMovement = getHolderMovement(entity);
        float entityYRot = entity.getYRot() * Mth.DEG_TO_RAD;
        Vec3 entityDimensions = new Vec3(entity.getBbWidth(), entity.getBbHeight(), entity.getBbWidth());
        float holderYRot = holder.getYRot() * Mth.DEG_TO_RAD;
        Vec3 holderDimensions = new Vec3(holder.getBbWidth(), holder.getBbHeight(), holder.getBbWidth());
        List<Wrench> wrenches = new ArrayList<>();

        for (int i = 0; i < entityAttachmentPoints.size(); i++) {
            Vec3 entityAttachVector = entityAttachmentPoints.get(i).multiply(entityDimensions).yRot(-entityYRot);
            Vec3 entityAttachPos = entity.position().add(entityAttachVector);
            Vec3 holderAttachVector = leasherAttachmentPoints.get(i).multiply(holderDimensions).yRot(-holderYRot);
            Vec3 holderAttachPos = holder.position().add(holderAttachVector);
            computeDampenedSpringInteraction(holderAttachPos, entityAttachPos, slackDistance, currentMovement, entityAttachVector).ifPresent(wrenches::add);
        }

        return wrenches;
    }

    private static Optional<Wrench> computeDampenedSpringInteraction(
        Vec3 pivot,
        Vec3 pos,
        double slack,
        Vec3 motion,
        Vec3 lever
    ) {
        double distance = pos.distanceTo(pivot);
        if (distance < slack) {
            return Optional.empty();
        } else {
            Vec3 displacement = pivot.subtract(pos).normalize().scale(distance - slack);
            double torque = Wrench.torqueFromForce(lever, displacement);
            if (motion.dot(displacement) >= 0.0) {
                displacement = displacement.scale(0.3F);
            }

            return Optional.of(new Wrench(displacement, torque));
        }
    }

    public static Vec3[] createQuadOffsets(Entity entity, double frontOffset, double backOffset, double leftRight, double height) {
        float width = entity.getBbWidth();
        double frontOffsetScaled = frontOffset * width;
        double frontBackScaled = backOffset * width;
        double leftRightScaled = leftRight * width;
        double heightScaled = height * entity.getBbHeight();
        return new Vec3[]{
            new Vec3(-leftRightScaled, heightScaled, frontBackScaled + frontOffsetScaled),
            new Vec3(-leftRightScaled, heightScaled, -frontBackScaled + frontOffsetScaled),
            new Vec3(leftRightScaled, heightScaled, -frontBackScaled + frontOffsetScaled),
            new Vec3(leftRightScaled, heightScaled, frontBackScaled + frontOffsetScaled)
        };
    }

    public static List<Leashable> leashableLeashedTo(Entity entity) {
        return leashableInArea(entity, leashable -> leashable.getLeashHolder() == entity);
    }

    public static List<Leashable> leashableInArea(Entity entity, Predicate<Leashable> test) {
        return leashableInArea(entity.level(), entity.getBoundingBox().getCenter(), test);
    }

    public static List<Leashable> leashableInArea(Level level, Vec3 pos, Predicate<Leashable> test) {
        AABB scanArea = AABB.ofSize(pos, 32.0, 32.0, 32.0);
        return level.getEntitiesOfClass(Entity.class, scanArea, entity -> entity instanceof Leashable leashable && test.test(leashable))
            .stream()
            .map(Leashable.class::cast)
            .toList();
    }

    public static void resetAngularMomentum(Leashable leashable) {
        Leashable.LeashData data = leashable.getLeashData();
        if (data != null) {
            ((LeashDataExtension)(Object) data).setAngularMomentum(0.0);
        }
    }

    public static boolean dropAllLeashConnections(Entity entity, @Nullable Player player) {
        List<Leashable> leashables = leashableLeashedTo(entity);
        boolean dropped = !leashables.isEmpty();
        if (entity instanceof Leashable self && self.isLeashed()) {
            self.dropLeash(true, true);
            dropped = true;
        }

        for (Leashable leashable : leashables) {
            leashable.dropLeash(true, true);
        }

        if (dropped) {
            entity.gameEvent(GameEvent.SHEAR, player);
            return true;
        } else {
            return false;
        }
    }

    public record Wrench(Vec3 force, double torque) {
        public static final Wrench ZERO = new Wrench(Vec3.ZERO, 0.0);

        public static double torqueFromForce(Vec3 lever, Vec3 force) {
            return lever.z * force.x - lever.x * force.z;
        }

        public static Wrench accumulate(List<Wrench> wrenches) {
            if (wrenches.isEmpty()) {
                return ZERO;
            } else {
                double x = 0, y = 0, z = 0, t = 0;
                for (Wrench w : wrenches) {
                    x += w.force.x;
                    y += w.force.y;
                    z += w.force.z;
                    t += w.torque;
                }

                return new Wrench(new Vec3(x, y, z), t);
            }
        }

        public Wrench scale(double f) {
            return new Wrench(force.scale(f), torque * f);
        }
    }
}
package com.blackgear.vanillabackport.common.api.leash;

import com.google.common.collect.ImmutableList;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Leashable;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.*;
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

    public static double snapDistance(Entity entity) {
        return entity instanceof LeashableCallback callback
            ? callback.vb$leashSnapDistance()
            : LEASH_TOO_FAR_DIST;
    }

    public static double elasticDistance(Entity entity) {
        return entity instanceof LeashableCallback callback
            ? callback.vb$leashElasticDistance()
            : LEASH_ELASTIC_DIST;
    }

    public static double distanceBetween(Entity target, Entity holder) {
        return holder.getBoundingBox().getCenter().distanceTo(target.getBoundingBox().getCenter());
    }

    public static boolean canAttachLeash(Leashable leashable, Entity holder) {
        if (leashable == holder) return false;
        return distanceBetween((Entity) leashable, holder) <= snapDistance((Entity) leashable) && leashable.canBeLeashed();
    }

    private static List<Vec3> resolveHolderAttachmentPoints(Entity holder, boolean quadConnections) {
        if (!quadConnections) {
            return LEASHER_ATTACHMENT_POINT;
        } else if (holder instanceof LeashHolderCallback callback) {
            return Arrays.asList(callback.vb$getQuadLeashHolderOffsets());
        } else {
            return SHARED_QUAD_ATTACHMENT_POINTS;
        }
    }

    private static List<Vec3> resolveEntityAttachmentPoints(Entity entity, boolean quadConnections) {
        if (!quadConnections) {
            return ENTITY_ATTACHMENT_POINT;
        } else if (entity instanceof LeashableCallback callback) {
            return Arrays.asList(callback.vb$getQuadLeashOffsets());
        } else {
            Vec3[] offsets = QuadLeashRegistry.getOffsets(entity);
            return offsets != null ? Arrays.asList(offsets) : SHARED_QUAD_ATTACHMENT_POINTS;
        }
    }

    private static boolean isQuadConnection(Entity entity, Entity holder) {
        boolean supportsQuadLeashAsHolder = holder instanceof LeashHolderCallback callback
            ? callback.vb$supportsQuadLeashAsHolder()
            : QuadLeashRegistry.supportsAsHolder(holder);

        boolean supportsQuadLeash = entity instanceof LeashableCallback callback
            ? callback.vb$supportsQuadLeash()
            : QuadLeashRegistry.supports(entity);

        return supportsQuadLeashAsHolder && supportsQuadLeash;
    }

    public static boolean supportsQuadLeash(Entity entity) {
        return entity instanceof LeashableCallback callback
            ? callback.vb$supportsQuadLeash()
            : QuadLeashRegistry.supports(entity);
    }

    public static boolean supportsQuadLeashAsHolder(Entity entity) {
        return entity instanceof LeashHolderCallback callback
            ? callback.vb$supportsQuadLeashAsHolder()
            : QuadLeashRegistry.supportsAsHolder(entity);
    }

    public static Vec3[] getQuadLeashOffsets(Entity entity) {
        if (entity instanceof LeashableCallback callback) {
            return callback.vb$getQuadLeashOffsets();
        } else {
            Vec3[] offsets = QuadLeashRegistry.getOffsets(entity);
            return offsets != null ? offsets : createQuadOffsets(entity, 0.0, 0.5, 0.5, 0.5);
        }
    }

    public static Vec3[] getQuadLeashHolderOffsets(Entity entity) {
        if (entity instanceof LeashHolderCallback callback)
            return callback.vb$getQuadLeashHolderOffsets();

        return createQuadOffsets(entity, 0.0, 0.5, 0.5, 0.0);
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
        boolean quadConnection = isQuadConnection(entity, holder);

        List<Vec3> entityAttachmentPoints = resolveEntityAttachmentPoints(entity, quadConnection);
        List<Vec3> leasherAttachmentPoints = resolveHolderAttachmentPoints(holder, quadConnection);

        List<Wrench> wrenches = computeElasticInteraction(entity, holder, entityAttachmentPoints, leasherAttachmentPoints);
        if (wrenches.isEmpty()) {
            return false;
        } else {
            Wrench result = Wrench.accumulate(wrenches).scale(quadConnection ? 0.25 : 1.0);
            LeashDataAccess access = Objects.requireNonNull((LeashDataAccess) (Object) data);
            access.vb$setAngularMomentum(access.vb$getAngularMomentum() + TORSIONAL_ELASTICITY * result.torque());
            Vec3 relativeVelocityToLeasher = getHolderMovement(holder).subtract(entity.getKnownMovement());
            entity.addDeltaMovement(result.force().multiply(AXIS_SPECIFIC_ELASTICITY).add(relativeVelocityToLeasher.scale(STIFFNESS)));
            return true;
        }
    }

    private static <E extends Entity & Leashable> List<Wrench> computeElasticInteraction(
        E entity,
        Entity holder,
        List<Vec3> entityAttachmentPoints,
        List<Vec3> leasherAttachmentPoints
    ) {
        double slackDistance = elasticDistance(entity);
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
                displacement = displacement.scale(0.3);
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
            ((LeashDataAccess)(Object) data).vb$setAngularMomentum(0.0);
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
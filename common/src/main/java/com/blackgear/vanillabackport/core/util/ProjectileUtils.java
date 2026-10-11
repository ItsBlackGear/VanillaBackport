package com.blackgear.vanillabackport.core.util;

import com.blackgear.vanillabackport.common.api.extensions.entity.movement.MotionAwareEntity;
import com.blackgear.vanillabackport.common.level.items.spear.AttackRange;
import com.blackgear.vanillabackport.core.util.Utilities.DirectionUtils;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class ProjectileUtils {
    public static Either<BlockHitResult, Collection<EntityHitResult>> getHitEntitiesAlong(
        Entity attacker,
        AttackRange attackRange,
        Predicate<Entity> matching,
        ClipContext.Block blockClipType
    ) {
        Vec3 look = ((MotionAwareEntity) attacker).getHeadLookAngle();
        Vec3 eyePosition = attacker.getEyePosition();
        Vec3 from = eyePosition.add(look.scale(attackRange.effectiveMinRange(attacker)));
        double movementComponent = ((MotionAwareEntity) attacker).getKnownMovement().dot(look);
        Vec3 to = eyePosition.add(look.scale(attackRange.effectiveMaxRange(attacker) + Math.max(0.0, movementComponent)));
        return getHitEntitiesAlong(attacker, eyePosition, from, matching, to, attackRange.hitboxMargin(), blockClipType);
    }

    private static Either<BlockHitResult, Collection<EntityHitResult>> getHitEntitiesAlong(
        Entity source,
        Vec3 origin,
        Vec3 from,
        Predicate<Entity> matching,
        Vec3 to,
        float entityMargin,
        ClipContext.Block clipType
    ) {
        Level level = source.level();
        BlockHitResult hitResult = clipIncludingBorder(level, new ClipContext(origin, to, clipType, ClipContext.Fluid.NONE, source));
        if (hitResult.getType() != HitResult.Type.MISS) {
            to = hitResult.getLocation();
            if (origin.distanceToSqr(to) < origin.distanceToSqr(from)) {
                return Either.left(hitResult);
            }
        }

        AABB searchArea = AABB.ofSize(from, entityMargin, entityMargin, entityMargin).expandTowards(to.subtract(from)).inflate(1.0);
        Collection<EntityHitResult> entityHit = getManyEntityHitResult(level, source, from, to, searchArea, matching, entityMargin, clipType, true);
        return !entityHit.isEmpty() ? Either.right(entityHit) : Either.left(hitResult);
    }

    public static Collection<EntityHitResult> getManyEntityHitResult(
        Level level,
        Entity source,
        Vec3 from,
        Vec3 to,
        AABB targetSearchArea,
        Predicate<Entity> matching,
        float entityMargin,
        ClipContext.Block clipType,
        boolean includeFromEntity
    ) {
        List<EntityHitResult> collector = new ArrayList<>();

        for (Entity entity : level.getEntities(source, targetSearchArea, matching)) {
            AABB entityBB = entity.getBoundingBox();
            if (includeFromEntity && entityBB.contains(from)) {
                collector.add(new EntityHitResult(entity, from));
            } else {
                Optional<Vec3> exactHit = entityBB.clip(from, to);
                if (exactHit.isPresent()) {
                    collector.add(new EntityHitResult(entity, exactHit.get()));
                } else if (!(entityMargin <= 0.0)) {
                    Optional<Vec3> outsideHit = entityBB.inflate(entityMargin).clip(from, to);
                    if (outsideHit.isPresent()) {
                        Vec3 outsideHitPosition = outsideHit.get();
                        Vec3 towardsTarget = entityBB.getCenter();
                        BlockHitResult hitResult = clipIncludingBorder(level, new ClipContext(outsideHitPosition, towardsTarget, clipType, ClipContext.Fluid.NONE, source));
                        if (hitResult.getType() != HitResult.Type.MISS) {
                            towardsTarget = hitResult.getLocation();
                        }

                        Optional<Vec3> surfaceHit = entity.getBoundingBox().clip(outsideHitPosition, towardsTarget);
                        surfaceHit.ifPresent(vec3 -> collector.add(new EntityHitResult(entity, vec3)));
                    }
                }
            }
        }

        return collector;
    }

    private static BlockHitResult clipIncludingBorder(CollisionGetter level, ClipContext context) {
        BlockHitResult hitResult = level.clip(context);
        WorldBorder worldBorder = level.getWorldBorder();
        if (worldBorder.isWithinBounds(BlockPos.containing(context.getFrom())) && !worldBorder.isWithinBounds(BlockPos.containing(hitResult.getLocation()))) {
            Vec3 delta = hitResult.getLocation().subtract(context.getFrom());
            Direction deltaDirection = DirectionUtils.getApproximateNearest(delta.x, delta.y, delta.z);
            Vec3 hit = clampVec3ToBound(worldBorder, hitResult.getLocation());
            return new BlockHitResult(hit, deltaDirection, BlockPos.containing(hit), false);
        } else {
            return hitResult;
        }
    }

    private static Vec3 clampVec3ToBound(WorldBorder border, Vec3 position) {
        return new Vec3(
            Mth.clamp(position.x, border.getMinX(), border.getMaxX() - 1.0E-5F),
            position.y,
            Mth.clamp(position.z, border.getMinZ(), border.getMaxZ() - 1.0E-5F)
        );
    }
}
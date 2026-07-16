package com.blackgear.vanillabackport.common.level.components;

import com.blackgear.vanillabackport.common.api.extensions.entity.movement.MotionAwareEntity;
import com.blackgear.vanillabackport.core.util.AdditionalCodecs;
import com.blackgear.vanillabackport.core.util.ProjectileUtils;
import com.blackgear.vanillabackport.core.util.Utilities.DirectionUtils;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.*;

import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.ToDoubleFunction;

public record AttackRange(
    float minReach,
    float maxReach,
    float minCreativeReach,
    float maxCreativeReach,
    float hitboxMargin,
    float mobFactor
) {
    public static final Codec<AttackRange> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        AdditionalCodecs.floatRange(0.0F, 64.0F).optionalFieldOf("min_reach", 0.0F).forGetter(AttackRange::minReach),
        AdditionalCodecs.floatRange(0.0F, 64.0F).optionalFieldOf("max_reach", 0.0F).forGetter(AttackRange::maxReach),
        AdditionalCodecs.floatRange(0.0F, 64.0F).optionalFieldOf("min_creative_reach", 0.0F).forGetter(AttackRange::minCreativeReach),
        AdditionalCodecs.floatRange(0.0F, 64.0F).optionalFieldOf("max_creative_reach", 0.0F).forGetter(AttackRange::maxCreativeReach),
        AdditionalCodecs.floatRange(0.0F, 1.0F).optionalFieldOf("hitbox_margin", 0.0F).forGetter(AttackRange::hitboxMargin),
        Codec.floatRange(0.0F, 2.0F).optionalFieldOf("mob_factor", 1.0F).forGetter(AttackRange::mobFactor)
    ).apply(instance, AttackRange::new));

    public static final StreamCodec<ByteBuf, AttackRange> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.FLOAT, AttackRange::minReach,
        ByteBufCodecs.FLOAT, AttackRange::maxReach,
        ByteBufCodecs.FLOAT, AttackRange::minCreativeReach,
        ByteBufCodecs.FLOAT, AttackRange::maxCreativeReach,
        ByteBufCodecs.FLOAT, AttackRange::hitboxMargin,
        ByteBufCodecs.FLOAT, AttackRange::mobFactor,
        AttackRange::new
    );

    public static AttackRange defaultFor(LivingEntity entity) {
        return new AttackRange(
            0.0F,
            (float) entity.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE),
            0.0F,
            (float) entity.getAttributeValue(Attributes.ENTITY_INTERACTION_RANGE),
            0.0F,
            1.0F
        );
    }

    public HitResult getClosestHit(Entity attacker, float partial, Predicate<Entity> matching) {
        Either<BlockHitResult, Collection<EntityHitResult>> result = ProjectileUtils.getHitEntitiesAlong(attacker, this, matching, ClipContext.Block.OUTLINE);
        if (result.left().isPresent()) {
            return result.left().get();
        } else {
            EntityHitResult entity = null;
            Vec3 attackerPos = attacker.getEyePosition(partial);
            double closestDistance = Double.MAX_VALUE;

            Collection<EntityHitResult> targets = result.right().get();
            for (EntityHitResult target : targets) {
                double distance = attackerPos.distanceToSqr(target.getLocation());
                if (distance < closestDistance) {
                    closestDistance = distance;
                    entity = target;
                }
            }

            if (entity != null) {
                return entity;
            } else {
                Vec3 eyeGaze = ((MotionAwareEntity) attacker).getHeadLookAngle();
                Vec3 missPosition = attacker.getEyePosition(partial).add(eyeGaze);
                return BlockHitResult.miss(missPosition, DirectionUtils.getApproximateNearest(eyeGaze), BlockPos.containing(missPosition));
            }
        }
    }

    public float effectiveMinRange(Entity entity) {
        if (entity instanceof Player player) {
            return player.isCreative() ? this.minCreativeReach : this.minReach;
        } else {
            return this.minReach * this.mobFactor;
        }
    }

    public float effectiveMaxRange(Entity entity) {
        if (entity instanceof Player player) {
            return player.isCreative() ? this.maxCreativeReach : this.maxReach;
        } else {
            return this.maxReach * this.mobFactor;
        }
    }

    public boolean isInRange(LivingEntity attacker, Vec3 location) {
        return this.isInRange(attacker, location::distanceToSqr, 0.0);
    }

    public boolean isInRange(LivingEntity attacker, AABB boundingBox, double extraBuffer) {
        return this.isInRange(attacker, boundingBox::distanceToSqr, extraBuffer);
    }

    private boolean isInRange(LivingEntity attacker, ToDoubleFunction<Vec3> distanceFunction, double extraBuffer) {
        double distance = Math.sqrt(distanceFunction.applyAsDouble(attacker.getEyePosition()));
        double minReach = this.effectiveMinRange(attacker) - this.hitboxMargin - extraBuffer;
        double maxReach = this.effectiveMaxRange(attacker) + this.hitboxMargin + extraBuffer;
        return distance >= minReach && distance <= maxReach;
    }
}
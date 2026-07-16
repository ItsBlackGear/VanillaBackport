package com.blackgear.vanillabackport.common.level.components;

import com.blackgear.vanillabackport.common.api.extensions.SoundExtensions;
import com.blackgear.vanillabackport.common.api.extensions.entity.movement.MotionAwareEntity;
import com.blackgear.vanillabackport.common.api.extensions.entity.spear.MobSpearHandler;
import com.blackgear.vanillabackport.core.util.AdditionalCodecs;
import com.blackgear.vanillabackport.core.util.ProjectileUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public record KineticWeapon(
    int contactCooldownTicks,
    int delayTicks,
    Optional<Condition> dismountConditions,
    Optional<Condition> knockbackConditions,
    Optional<Condition> damageConditions,
    float forwardMovement,
    float damageMultiplier,
    Optional<Holder<SoundEvent>> sound,
    Optional<Holder<SoundEvent>> hitSound
) {
    public static final Codec<KineticWeapon> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("contact_cooldown_ticks", 10).forGetter(KineticWeapon::contactCooldownTicks),
        ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("delay_ticks", 0).forGetter(KineticWeapon::delayTicks),
        Condition.CODEC.optionalFieldOf("dismount_conditions").forGetter(KineticWeapon::dismountConditions),
        Condition.CODEC.optionalFieldOf("knockback_conditions").forGetter(KineticWeapon::knockbackConditions),
        Condition.CODEC.optionalFieldOf("damage_conditions").forGetter(KineticWeapon::damageConditions),
        Codec.FLOAT.optionalFieldOf("forward_movement", 0.0F).forGetter(KineticWeapon::forwardMovement),
        Codec.FLOAT.optionalFieldOf("damage_multiplier", 1.0F).forGetter(KineticWeapon::damageMultiplier),
        SoundEvent.CODEC.optionalFieldOf("sound").forGetter(KineticWeapon::sound),
        SoundEvent.CODEC.optionalFieldOf("hit_sound").forGetter(KineticWeapon::hitSound)
    ).apply(instance, KineticWeapon::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, KineticWeapon> STREAM_CODEC = AdditionalCodecs.composite(
        ByteBufCodecs.VAR_INT, KineticWeapon::contactCooldownTicks,
        ByteBufCodecs.VAR_INT, KineticWeapon::delayTicks,
        Condition.STREAM_CODEC.apply(ByteBufCodecs::optional), KineticWeapon::dismountConditions,
        Condition.STREAM_CODEC.apply(ByteBufCodecs::optional), KineticWeapon::knockbackConditions,
        Condition.STREAM_CODEC.apply(ByteBufCodecs::optional), KineticWeapon::damageConditions,
        ByteBufCodecs.FLOAT, KineticWeapon::forwardMovement,
        ByteBufCodecs.FLOAT, KineticWeapon::damageMultiplier,
        SoundEvent.STREAM_CODEC.apply(ByteBufCodecs::optional), KineticWeapon::sound,
        SoundEvent.STREAM_CODEC.apply(ByteBufCodecs::optional), KineticWeapon::hitSound,
        KineticWeapon::new
    );

    public static Vec3 getMotion(Entity entity) {
        if (!(entity instanceof Player) && entity.isPassenger()) {
            entity = entity.getRootVehicle();
        }

        return ((MotionAwareEntity) entity).getKnownSpeed().scale(20.0);
    }

    public void makeSound(Entity causer) {
        this.sound.ifPresent(sound -> ((SoundExtensions) causer.level()).playSound(causer, causer.getX(), causer.getY(), causer.getZ(), sound, causer.getSoundSource(), 1.0F, 1.0F));
    }

    public void makeLocalHitSound(Entity causer) {
        this.hitSound.ifPresent(sound -> causer.level().playLocalSound(causer, sound.value(), causer.getSoundSource(), 1.0F, 1.0F));
    }

    public int computeDamageUseDuration() {
        return this.delayTicks + this.damageConditions.map(Condition::maxDurationTicks).orElse(0);
    }

    public void damageEntities(ItemStack stack, int ticksRemaining, LivingEntity attacker, EquipmentSlot equipmentSlot) {
        MobSpearHandler handler = (MobSpearHandler) attacker;
        int ticksUsed = stack.getUseDuration(attacker) - ticksRemaining;
        if (ticksUsed >= this.delayTicks) {
            ticksUsed -= this.delayTicks;
            Vec3 attackerLookVector = attacker.getLookAngle();
            double attackerSpeedProjection = attackerLookVector.dot(getMotion(attacker));
            float actionFactor = attacker instanceof Player ? 1.0F : 0.2F;
            AttackRange attackRange = handler.getAttackRangeWith(stack);
            double baseMobDamage = attacker.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
            boolean affected = false;

            Collection<EntityHitResult> hitResults = ProjectileUtils.getHitEntitiesAlong(attacker, attackRange, e -> PiercingWeapon.canHitEntity(attacker, e), ClipContext.Block.COLLIDER).map(a -> List.of(), e -> e);
            for (EntityHitResult hitResult : hitResults) {
                Entity otherEntity = hitResult.getEntity();
                if (otherEntity instanceof EnderDragonPart part) {
                    otherEntity = part.parentMob;
                }

                boolean wasStabbed = handler.wasRecentlyStabbed(otherEntity, this.contactCooldownTicks);
                if (!wasStabbed) {
                    handler.rememberStabbedEntity(otherEntity);
                    double targetSpeedProjection = attackerLookVector.dot(getMotion(otherEntity));
                    double relativeSpeed = Math.max(0.0, attackerSpeedProjection - targetSpeedProjection);
                    boolean dealsDismount = this.dismountConditions.isPresent() && this.dismountConditions.get().test(ticksUsed, attackerSpeedProjection, relativeSpeed, actionFactor);
                    boolean dealsKnockback = this.knockbackConditions.isPresent() && this.knockbackConditions.get().test(ticksUsed, attackerSpeedProjection, relativeSpeed, actionFactor);
                    boolean dealsDamage = this.damageConditions.isPresent() && this.damageConditions.get().test(ticksUsed, attackerSpeedProjection, relativeSpeed, actionFactor);
                    if (dealsDismount || dealsKnockback || dealsDamage) {
                        float damageDealt = (float) baseMobDamage + Mth.floor(relativeSpeed * this.damageMultiplier);
                        affected |= handler.stabAttack(equipmentSlot, otherEntity, damageDealt, dealsDamage, dealsKnockback, dealsDismount);
                    }
                }
            }

            if (affected) {
                attacker.level().broadcastEntityEvent(attacker, (byte) 2);
                if (attacker instanceof ServerPlayer player) {
                    //TODO: add criteria trigger
                }
            }
        }
    }

    public record Condition(
        int maxDurationTicks,
        float minSpeed,
        float minRelativeSpeed)
    {
        public static final Codec<Condition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.NON_NEGATIVE_INT.fieldOf("max_duration_ticks").forGetter(Condition::maxDurationTicks),
            Codec.FLOAT.optionalFieldOf("min_speed", 0.0F).forGetter(Condition::minSpeed),
            Codec.FLOAT.optionalFieldOf("min_relative_speed", 0.0F).forGetter(Condition::minRelativeSpeed)
        ).apply(instance, Condition::new));

        public static final StreamCodec<ByteBuf, Condition> STREAM_CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, Condition::maxDurationTicks,
            ByteBufCodecs.FLOAT, Condition::minSpeed,
            ByteBufCodecs.FLOAT, Condition::minRelativeSpeed,
            Condition::new
        );

        public boolean test(int ticksUsed, double attackerSpeed, double relativeSpeed, double entityFactor) {
            return ticksUsed <= this.maxDurationTicks && attackerSpeed >= this.minSpeed * entityFactor && relativeSpeed >= this.minRelativeSpeed * entityFactor;
        }

        public static Optional<Condition> ofAttackerSpeed(int untilTicks, float minAttackerSpeed) {
            return Optional.of(new Condition(untilTicks, minAttackerSpeed, 0.0F));
        }

        public static Optional<Condition> ofRelativeSpeed(int untilTicks, float minRelativeSpeed) {
            return Optional.of(new Condition(untilTicks, 0.0F, minRelativeSpeed));
        }
    }
}
package com.blackgear.vanillabackport.common.level.components;

import com.blackgear.vanillabackport.common.api.extensions.SoundExtensions;
import com.blackgear.vanillabackport.common.api.extensions.entity.spear.MobSpearHandler;
import com.blackgear.vanillabackport.core.util.ProjectileUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Interaction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.EntityHitResult;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public record PiercingWeapon(
    boolean dealsKnockback,
    boolean dismounts,
    Optional<Holder<SoundEvent>> sound,
    Optional<Holder<SoundEvent>> hitSound
) {
    public static final Codec<PiercingWeapon> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.BOOL.optionalFieldOf("deals_knockback", true).forGetter(PiercingWeapon::dealsKnockback),
        Codec.BOOL.optionalFieldOf("dismounts", false).forGetter(PiercingWeapon::dismounts),
        SoundEvent.CODEC.optionalFieldOf("sound").forGetter(PiercingWeapon::sound),
        SoundEvent.CODEC.optionalFieldOf("hit_sound").forGetter(PiercingWeapon::hitSound)
    ).apply(instance, PiercingWeapon::new));

    public static final StreamCodec<RegistryFriendlyByteBuf, PiercingWeapon> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.BOOL, PiercingWeapon::dealsKnockback,
        ByteBufCodecs.BOOL, PiercingWeapon::dismounts,
        SoundEvent.STREAM_CODEC.apply(ByteBufCodecs::optional), PiercingWeapon::sound,
        SoundEvent.STREAM_CODEC.apply(ByteBufCodecs::optional), PiercingWeapon::hitSound,
        PiercingWeapon::new
    );

    public void makeSound(Entity causer) {
        this.sound.ifPresent(sound -> ((SoundExtensions) causer.level()).playSound(causer, causer.getX(), causer.getY(), causer.getZ(), sound, causer.getSoundSource(), 1.0F, 1.0F));
    }

    public void makeHitSound(Entity causer) {
        this.hitSound.ifPresent(sound -> causer.level().playSound(null, causer.getX(), causer.getY(), causer.getZ(), sound, causer.getSoundSource(), 1.0F, 1.0F));
    }

    public static boolean canHitEntity(Entity jabber, Entity target) {
        if (
            (target.isInvulnerable() || !target.isAlive()) ||
            target instanceof Interaction ||
            !target.canBeHitByProjectile()
        ) {
            return false;
        } else {
            return (!(target instanceof Player targetPlayer) || !(jabber instanceof Player jabbingPlayer) || jabbingPlayer.canHarmPlayer(targetPlayer)) && !jabber.isPassengerOfSameVehicle(target);
        }
    }

    public void attack(LivingEntity attacker, EquipmentSlot hand) {
        MobSpearHandler handler = (MobSpearHandler) attacker;
        float damage = (float) attacker.getAttributeValue(Attributes.ATTACK_DAMAGE);
        ItemStack weapon = attacker.getItemBySlot(hand);
        AttackRange attackRange = handler.getAttackRangeWith(weapon);
        boolean hitSomething = false;

        Collection<EntityHitResult> hitResults = ProjectileUtils.getHitEntitiesAlong(attacker, attackRange, target -> canHitEntity(attacker, target), ClipContext.Block.COLLIDER).map(a -> List.of(), e -> e);
        for (EntityHitResult hitResult : hitResults) {
            hitSomething |= handler.stabAttack(hand, hitResult.getEntity(), damage, true, this.dealsKnockback, this.dismounts);
        }

        handler.onAttack();
        handler.postPiercingAttack();
        if (hitSomething) {
            this.makeHitSound(attacker);
        }

        this.makeSound(attacker);
        attacker.swing(InteractionHand.MAIN_HAND, false);
    }
}
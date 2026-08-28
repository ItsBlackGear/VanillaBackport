package com.blackgear.vanillabackport.common.level.items.spear;

import com.blackgear.vanillabackport.common.api.extensions.SoundExtension;
import com.blackgear.vanillabackport.common.api.extensions.entity.spear.MobSpearHandler;
import com.blackgear.vanillabackport.core.util.ProjectileUtils;
import net.minecraft.core.Holder;
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
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public record PiercingWeapon(
    boolean dealsKnockback,
    boolean dismounts,
    Optional<Holder<SoundEvent>> sound,
    Optional<Holder<SoundEvent>> hitSound
) {
    public void makeSound(Entity causer) {
        this.sound.ifPresent(sound -> ((SoundExtension) causer.level()).playSound(causer, causer.getX(), causer.getY(), causer.getZ(), sound, causer.getSoundSource(), 1.0F, 1.0F));
    }
    
    public void makeHitSound(Entity causer) {
        this.hitSound.ifPresent(sound -> causer.level().playSound(null, causer.getX(), causer.getY(), causer.getZ(), sound.value(), causer.getSoundSource(), 1.0F, 1.0F));
    }
    
    public static boolean canHitEntity(Entity jabber, Entity target) {
        if (target.isInvulnerable() || !target.isAlive()) {
            return false;
        } else if (target instanceof Interaction) {
            return true;
        } else if (!target.canBeHitByProjectile()) {
            return false;
        } else {
            return (!(target instanceof Player targetPlayer) || !(jabber instanceof Player jabbingPlayer) || jabbingPlayer.canHarmPlayer(targetPlayer))
                && !jabber.isPassengerOfSameVehicle(target);
        }
    }
    
    public void attack(LivingEntity attacker, EquipmentSlot hand) {
        MobSpearHandler spearHandler = (MobSpearHandler) attacker;
        float damage = (float) attacker.getAttributeValue(Attributes.ATTACK_DAMAGE);
        ItemStack weapon = attacker.getItemBySlot(hand);
        AttackRange attackRange = spearHandler.vb$getAttackRangeWith(weapon);
        boolean hitSomething = false;
        
        Collection<EntityHitResult> hitResults = ProjectileUtils.getHitEntitiesAlong(attacker, attackRange, target -> canHitEntity(attacker, target), ClipContext.Block.COLLIDER).map(a -> List.of(), e -> e);
        for (EntityHitResult hitResult : hitResults) {
            hitSomething |= spearHandler.vb$stabAttack(hand, hitResult.getEntity(), damage, true, this.dealsKnockback, this.dismounts);
        }
        
        spearHandler.vb$onAttack();
        spearHandler.vb$postPiercingAttack();
        if (hitSomething) {
            this.makeHitSound(attacker);
        }
        
        this.makeSound(attacker);
        attacker.swing(InteractionHand.MAIN_HAND, false);
    }
    
    public static boolean hasPiercingWeapon(ItemStack stack) {
        return get(stack) != null;
    }
    
    public static @Nullable PiercingWeapon get(ItemStack stack) {
        return stack.getItem() instanceof SpearItem spear ? spear.getPiercingWeapon() : null;
    }
}
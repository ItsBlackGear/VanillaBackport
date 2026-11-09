package com.blackgear.vanillabackport.common.level.items;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.api.extensions.entity.GraceTimeWeaponHolder;
import com.blackgear.vanillabackport.common.level.entities.projectile.wind_charge.WindBurst;
import com.blackgear.vanillabackport.common.level.items.enchantment.EnchantmentUtils;
import com.blackgear.vanillabackport.common.registries.items.ModEnchantments;
import com.blackgear.vanillabackport.core.network.handlers.LevelEvents;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.function.Predicate;

public class MaceItem extends Item {
    public MaceItem(Properties properties) {
        super(properties);
    }
    
    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot slot) {
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        
        if (slot == EquipmentSlot.MAINHAND) {
            builder.put(Attributes.ATTACK_DAMAGE, new AttributeModifier(BASE_ATTACK_DAMAGE_UUID, "Weapon modifier", 5.0F, AttributeModifier.Operation.ADDITION));
            builder.put(Attributes.ATTACK_SPEED, new AttributeModifier(BASE_ATTACK_SPEED_UUID, "Weapon modifier", -3.4F, AttributeModifier.Operation.ADDITION));
        }
        
        return builder.build();
    }
    
    @Override
    public boolean canAttackBlock(BlockState state, Level level, BlockPos pos, Player player) {
        return !player.isCreative();
    }
    
    @Override
    public int getEnchantmentValue() {
        return 15;
    }

    public void preHurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker.level() instanceof ServerLevel level && canSmashAttack(attacker)) {
            attacker.setDeltaMovement(attacker.getDeltaMovement().with(Direction.Axis.Y, 0.01F));
            GraceTimeWeaponHolder.of(attacker).setIgnoreFallDamageFromCurrentImpulse(true, this.calculateImpactPosition(attacker));
            if (attacker instanceof ServerPlayer player) {
                player.connection.send(new ClientboundSetEntityMotionPacket(player));
            }

            if (target.onGround()) {
                if (attacker instanceof ServerPlayer player) {
                    //TODO
                }

                SoundEvent sound = attacker.fallDistance > 5.0 ? ModSoundEvents.MACE_SMASH_GROUND_HEAVY.get() : ModSoundEvents.MACE_SMASH_GROUND.get();
                level.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), sound, attacker.getSoundSource(), 1.0F, 1.0F);
            } else {
                level.playSound(null, attacker.getX(), attacker.getY(), attacker.getZ(), ModSoundEvents.MACE_SMASH_AIR.get(), attacker.getSoundSource(), 1.0F, 1.0F);
            }

            knockback(level, attacker, target);
            int burst = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.WIND_BURST.get(), stack);
            if (burst > 0) {
                WindBurst.performWindBurstPostAttack(attacker, burst);
            }
        }
    }
    
    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (attacker.level() instanceof ServerLevel && canSmashAttack(attacker)) {
            stack.hurtAndBreak(1, attacker, p -> p.broadcastBreakEvent(EquipmentSlot.MAINHAND));
            attacker.resetFallDistance();
        }

        return true;
    }
    
    private Vec3 calculateImpactPosition(LivingEntity attacker) {
        GraceTimeWeaponHolder holder = GraceTimeWeaponHolder.of(attacker);
        return holder.isIgnoringFallDamageFromCurrentImpulse() && holder.getCurrentImpulseImpactPos().y <= attacker.position().y
            ? holder.getCurrentImpulseImpactPos()
            : attacker.position();
    }
    
    @Override
    public boolean isValidRepairItem(ItemStack stack, ItemStack repairCandidate) {
        return false;
//        return repairCandidate.is(Items.BLAZE_ROD); //TODO
    }
    
    public float getAttackDamageBonus(Entity victim, float ignoredDamage, DamageSource source) {
        if (source.getDirectEntity() instanceof LivingEntity attacker) {
            if (!canSmashAttack(attacker)) {
                return 0.0F;
            } else {
                float fallDistance = attacker.fallDistance;
                float damage;
                if (fallDistance <= 3.0F) {
                    damage = 4.0F * fallDistance;
                } else if (fallDistance <= 8.0F) {
                    damage = 12.0F + 2.0F * (fallDistance - 3.0F);
                } else {
                    damage = 22.0F + (fallDistance - 8.0F);
                }
                
                return attacker.level() instanceof ServerLevel
                    ? damage + EnchantmentUtils.getFallBasedDamageBonus(attacker.getMainHandItem(), victim) * fallDistance
                    : damage;
            }
        } else {
            return 0.0F;
        }
    }
    
    private static void knockback(Level level, Entity attacker, Entity entity) {
        LevelEvents.MACE_SMASH.trigger(level, null, entity.getOnPos(), 750);
        
        level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(3.5), knockbackPredicate(attacker, entity)).forEach(nearby -> {
            Vec3 direction = nearby.position().subtract(entity.position());
            double knockbackPower = getKnockbackPower(attacker, nearby, direction);
            Vec3 knockbackVector = direction.normalize().scale(knockbackPower);
            if (knockbackPower > 0.0) {
                nearby.push(knockbackVector.x, 0.7F, knockbackVector.z);
                if (nearby instanceof ServerPlayer player) {
                    player.connection.send(new ClientboundSetEntityMotionPacket(player));
                }
            }
        });
    }
    
    private static Predicate<LivingEntity> knockbackPredicate(Entity attacker, Entity entity) {
        return nearby -> {
            boolean notSpectator = !nearby.isSpectator();
            boolean notPlayer = nearby != attacker && nearby != entity;
            boolean notAlliedToPlayer = !attacker.isAlliedTo(nearby);
            boolean notTamedBy = !(nearby instanceof TamableAnimal animal && entity instanceof  LivingEntity living && animal.isTame() && animal.isOwnedBy(living));
            boolean notArmorStand = !(nearby instanceof ArmorStand stand && stand.isMarker());
            boolean withinRange = entity.distanceToSqr(nearby) <= Math.pow(3.5, 2.0);
            boolean notFlyingInCreative = !(nearby instanceof Player player && player.isCreative() && player.getAbilities().flying);
            return notSpectator && notPlayer && notAlliedToPlayer && notTamedBy && notArmorStand && withinRange && notFlyingInCreative;
        };
    }
    
    private static double getKnockbackPower(Entity attacker, LivingEntity nearby, Vec3 direction) {
        return ((double)3.5F - direction.length()) * (double)0.7F * (double)(attacker.fallDistance > 5.0F ? 2 : 1) * ((double)1.0F - nearby.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
    }
    
    public static boolean canSmashAttack(LivingEntity attacker) {
        return attacker.fallDistance > 1.5F && !attacker.isFallFlying();
    }
}
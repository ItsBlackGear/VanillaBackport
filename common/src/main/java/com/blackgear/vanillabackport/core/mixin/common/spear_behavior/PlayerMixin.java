package com.blackgear.vanillabackport.core.mixin.common.spear_behavior;

import com.blackgear.vanillabackport.common.api.extensions.entity.spear.PlayerSpearHandler;
import com.blackgear.vanillabackport.common.api.extensions.entity.spear.MobSpearHandler;
import com.blackgear.vanillabackport.common.registries.items.ModDataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.protocol.game.ClientboundSetEntityMotionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Player.class)
public abstract class PlayerMixin extends LivingEntity implements PlayerSpearHandler, MobSpearHandler {
    @Shadow public abstract void playSound(SoundEvent sound, float volume, float pitch);
    @Shadow public abstract void awardStat(ResourceLocation stat, int count);
    @Shadow public abstract float getAttackStrengthScale(float adjustTicks);
    @Shadow public abstract ItemStack getItemBySlot(EquipmentSlot slot);
    @Shadow public abstract void causeFoodExhaustion(float exhaustion);
    @Shadow public abstract float getCurrentItemAttackStrengthDelay();
    @Shadow public abstract void magicCrit(Entity entityHit);
    
    @Shadow protected abstract float getEnchantedDamage(Entity entity, float damage, DamageSource damageSource);
    
    @Unique private int itemSwapTicker;

    protected PlayerMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "attack", at = @At("TAIL"))
    private void vb$onAttack(Entity target, CallbackInfo ci) {
        if (target.isAttackable()) {
            this.vb$onAttack();
            if (!target.skipAttackInteraction(this)) {
                this.vb$postPiercingAttack();
            }
        }
    }

    @Override
    public boolean vb$cannotAttackWithItem(ItemStack stack, int tolerance) {
        float requiredStrength = stack.getOrDefault(ModDataComponents.MINIMUM_ATTACK_CHARGE.get(), 0.0F);
        float optimisticStrength = (this.attackStrengthTicker + tolerance) / this.getCurrentItemAttackStrengthDelay();
        return requiredStrength > 0.0F && optimisticStrength < requiredStrength;
    }

    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/Player;getMainHandItem()Lnet/minecraft/world/item/ItemStack;", shift = At.Shift.BEFORE))
    private void vb$onTick(CallbackInfo ci) {
        this.itemSwapTicker++;
    }
    
    @Override
    public float vb$getItemSwapScale(float scale) {
        return Mth.clamp((this.itemSwapTicker + scale) / this.getCurrentItemAttackStrengthDelay(), 0.0F, 1.0F);
    }

    @Inject(method = "resetAttackStrengthTicker", at = @At("TAIL"))
    private void vb$resetAttackStrengthTicker(CallbackInfo ci) {
        this.itemSwapTicker = 0;
    }

    @Override
    public void vb$onAttack() {
        this.attackStrengthTicker = 0;
    }

    @Override
    public boolean vb$stabAttack(EquipmentSlot weaponSlot, Entity target, float baseDamage, boolean dealsDamage, boolean dealsKnockback, boolean dismounts) {
        Player self = (Player) (Object) this;
        if (!target.isAttackable() || target.skipAttackInteraction(this)) {
            return false;
        } else {
            ItemStack weaponItem = this.getItemBySlot(weaponSlot);
            DamageSource damageSource = this.damageSources().playerAttack(self);
            float magicBoost = this.getEnchantedDamage(target, baseDamage, damageSource) - baseDamage;
            EquipmentSlot handSlot = this.getUsedItemHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
            
            if (!this.isUsingItem() || handSlot != weaponSlot) {
                magicBoost *= this.getAttackStrengthScale(0.5F);
                baseDamage *= this.vb$baseDamageScaleFactor();
            }

            if (dealsKnockback && this.vb$deflectProjectile(target)) {
                return true;
            } else {
                float totalDamage = dealsDamage ? baseDamage + magicBoost : 0.0F;
                float oldTargetHealth = target instanceof LivingEntity living ? living.getHealth() : 0.0F;
                Vec3 oldMovement = target.getDeltaMovement();
                boolean wasHurt = dealsDamage && target.level() instanceof ServerLevel && target.hurt(damageSource, totalDamage);
                
                if (dealsKnockback) {
                    this.vb$causeExtraKnockback(target, 0.4F + this.getKnockback(target, damageSource), oldMovement);
                }

                boolean dismounted = false;
                if (dismounts && target.isPassenger()) {
                    dismounted = true;
                    target.stopRiding();
                }

                if (!wasHurt && !dealsKnockback && !dismounted) {
                    return false;
                } else {
                    this.vb$attackVisualEffects(target, dealsDamage, magicBoost);
                    this.setLastHurtMob(target);
                    this.vb$itemAttackInteraction(target, weaponItem, damageSource, wasHurt);
                    this.vb$damageStatsAndHearts(target, oldTargetHealth);
                    this.causeFoodExhaustion(0.1F);
                    return true;
                }
            }
        }
    }

    @Unique
    private void vb$attackVisualEffects(Entity target, boolean dealsDamage, float magicBoost) {
        this.level().playSound(null, this.getX(), this.getY(), this.getZ(), dealsDamage ? SoundEvents.PLAYER_ATTACK_STRONG : SoundEvents.PLAYER_ATTACK_WEAK, this.getSoundSource(), 1.0F, 1.0F);

        if (magicBoost > 0.0F) {
            this.magicCrit(target);
        }
    }

    @Unique
    private void vb$damageStatsAndHearts(Entity target, float oldTargetHealth) {
        if (target instanceof LivingEntity living) {
            float actualDamage = oldTargetHealth - living.getHealth();
            this.awardStat(Stats.DAMAGE_DEALT, Math.round(actualDamage * 10.0F));
            if (this.level() instanceof ServerLevel level && actualDamage > 2.0F) {
                int count = (int) (actualDamage * 0.5);
                level.sendParticles(ParticleTypes.DAMAGE_INDICATOR, living.getX(), living.getY(0.5), living.getZ(), count, 0.1, 0.0, 0.1, 0.2);
            }
        }
    }
    
    @Override
    public void vb$causeExtraKnockback(Entity target, float knockback, Vec3 oldMovement) {
        if (knockback > 0.0F) {
            if (target instanceof LivingEntity living) {
                living.knockback(knockback, Mth.sin(this.getYRot() * Mth.DEG_TO_RAD), -Mth.cos(this.getYRot() * Mth.DEG_TO_RAD));
            } else {
                target.push(-Mth.sin(this.getYRot() * Mth.DEG_TO_RAD) * knockback, 0.1, Mth.cos(this.getYRot() * Mth.DEG_TO_RAD) * knockback);
            }
            
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, 1.0, 0.6));
            this.setSprinting(false);
        }
        
        if (target instanceof ServerPlayer player && player.hurtMarked) {
            player.connection.send(new ClientboundSetEntityMotionPacket(target));
            target.hurtMarked = false;
            target.setDeltaMovement(oldMovement);
        }
    }

    @Unique
    private void vb$itemAttackInteraction(Entity target, ItemStack weapon, DamageSource damageSource, boolean wasHurt) {
        Player self = (Player)(Object) this;
        Entity hurtTarget = target;
        if (target instanceof EnderDragonPart part) {
            hurtTarget = part.parentMob;
        }

        boolean enemyHurt = false;
        if (this.level() instanceof ServerLevel level) {
            if (hurtTarget instanceof LivingEntity living) {
                enemyHurt = weapon.hurtEnemy(living, self);
            }

            if (wasHurt) {
                EnchantmentHelper.doPostAttackEffectsWithItemSource(level, hurtTarget, damageSource, weapon);
            }
        }

        if (!this.level().isClientSide() && !weapon.isEmpty() && hurtTarget instanceof LivingEntity living) {
            if (enemyHurt) {
                weapon.postHurtEnemy(living, self);
            }

            if (weapon.isEmpty()) {
                if (weapon == this.getMainHandItem()) {
                    this.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                } else {
                    this.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                }
            }
        }
    }

    @Unique
    private boolean vb$deflectProjectile(Entity entity) {
        if (entity.getType().is(EntityTypeTags.REDIRECTABLE_PROJECTILE)
            && entity instanceof Projectile projectile
            && projectile.deflect(ProjectileDeflection.AIM_DEFLECT, this, this, true)) {
            this.level().playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.PLAYER_ATTACK_NODAMAGE, this.getSoundSource());
            return true;
        } else {
            return false;
        }
    }
    
    @Unique
    private float vb$baseDamageScaleFactor() {
        float attackStrengthScale = this.getAttackStrengthScale(0.5F);
        return 0.2F + attackStrengthScale * attackStrengthScale * 0.8F;
    }
}
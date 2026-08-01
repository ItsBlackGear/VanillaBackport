package com.blackgear.vanillabackport.core.mixin.common.spear_behavior;

import com.blackgear.vanillabackport.common.api.extensions.entity.spear.MobSpearHandler;
import com.blackgear.vanillabackport.common.level.item.enchantment.EnchantmentUtils;
import com.blackgear.vanillabackport.common.level.item.spear.AttackRange;
import com.blackgear.vanillabackport.common.level.item.spear.KineticWeapon;
import com.blackgear.vanillabackport.common.level.item.spear.SwingAnimation;
import com.blackgear.vanillabackport.core.util.WorldUtilities.EntityUtils;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Predicate;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity implements MobSpearHandler {
    @Shadow public abstract boolean isUsingItem();
    @Shadow public abstract int getTicksUsingItem();
    @Shadow public abstract ItemStack getItemBySlot(EquipmentSlot slot);
    @Shadow public abstract void setLastHurtMob(Entity entity);
    @Shadow public abstract ItemStack getItemInHand(InteractionHand hand);
    
    @Shadow protected ItemStack useItem;
    
    @Shadow
    public abstract ItemStack getUseItem();
    
    @Unique @Nullable protected Object2LongMap<Entity> recentKineticEnemies;
    @Unique private long lastKineticHitFeedbackTime = -2147483648L;
    
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }
    
    @Override
    public AttackRange getAttackRangeWith(ItemStack weapon) {
        AttackRange range = AttackRange.getAttackRange(weapon);
        return range != null ? range : AttackRange.defaultFor((LivingEntity) (Object) this);
    }
    
    @Inject(
        method = "startUsingItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;gameEvent(Lnet/minecraft/world/level/gameevent/GameEvent;)V"
        ))
    private void vb$startUsingItem(InteractionHand hand, CallbackInfo ci) {
        if (!this.getUseItem().isEmpty() && KineticWeapon.hasKineticWeapon(this.getUseItem())) {
            this.recentKineticEnemies = new Object2LongOpenHashMap<>();
        }
    }
    
    @Inject(
        method = "stopUsingItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;setLivingEntityFlag(IZ)V"
        ))
    private void vb$stopUsingItem(CallbackInfo ci) {
        this.recentKineticEnemies = null;
    }
    
    @ModifyExpressionValue(
        method = "getCurrentSwingDuration",
        at = @At(
            value = "CONSTANT",
            args = "intValue=6"
        ))
    private int vb$getCurrentSwingDuration(int original) {
        ItemStack heldItem = this.getItemInHand(InteractionHand.MAIN_HAND);
        SwingAnimation animation = SwingAnimation.getSwingAnimation(heldItem);
        if (animation == null) return original;
        return animation.duration();
    }
    
    @Override
    public boolean wasRecentlyStabbed(Entity target, int allowedTime) {
        if (this.recentKineticEnemies == null) {
            return false;
        } else if (this.recentKineticEnemies.containsKey(target)) {
            return this.level().getGameTime() - this.recentKineticEnemies.getLong(target) < (long) allowedTime;
        } else {
            return false;
        }
    }
    
    @Override
    public void rememberStabbedEntity(Entity target) {
        if (this.recentKineticEnemies != null)
            this.recentKineticEnemies.put(target, this.level().getGameTime());
    }
    
    @Override
    public int stabbedEntities(Predicate<Entity> filter) {
        return this.recentKineticEnemies == null ? 0 : (int) this.recentKineticEnemies.keySet().stream().filter(filter).count();
    }
    
    @Override
    public boolean stabAttack(EquipmentSlot weaponSlot, Entity target, float baseDamage, boolean dealsDamage, boolean dealsKnockback, boolean dismounts) {
        LivingEntity self = (LivingEntity)(Object)this;
        
        if (!(this.level() instanceof ServerLevel)) return false;
        
        ItemStack weapon = this.getItemBySlot(weaponSlot);
        DamageSource damageSource = this.damageSources().mobAttack(self);
        MobType type = target instanceof LivingEntity living ? living.getMobType() : MobType.UNDEFINED;
        
        float postEnchantmentDamage = baseDamage + EnchantmentHelper.getDamageBonus(weapon, type);
        Vec3 oldMovement = target.getDeltaMovement();
        
        boolean dealtDamage = false;
        if (dealsDamage) {
            dealtDamage = target.hurt(damageSource, postEnchantmentDamage);
        }

        boolean affected = dealsKnockback | dealtDamage;
        
        if (dealsKnockback) {
            this.causeExtraKnockback(target, 0.4F, oldMovement);
            this.causeExtraKnockback(target, EntityUtils.getKnockback(self), oldMovement);
        }
        
        if (dismounts && target.isPassenger()) {
            affected = true;
            target.stopRiding();
        }
        
        if (target instanceof LivingEntity living && self instanceof Player player) {
            weapon.hurtEnemy(living, player);
        }
        
        if (dealtDamage) {
            this.doEnchantDamageEffects(self, target);
        }
        
        if (!affected) {
            return false;
        } else {
            this.setLastHurtMob(target);
            return true;
        }
    }
    
    @Override
    public void causeExtraKnockback(Entity target, float knockback, Vec3 oldMovement) {
        if (knockback > 0.0F && target instanceof LivingEntity living) {
            living.knockback(knockback, Mth.sin(this.getYRot() * Mth.DEG_TO_RAD), -Mth.cos(this.getYRot() * Mth.DEG_TO_RAD));
            this.setDeltaMovement(this.getDeltaMovement().multiply(0.6, 1.0, 0.6));
        }
    }
    
    @Override
    public void postPiercingAttack() {
        if (this.level() instanceof ServerLevel level) {
            EnchantmentUtils.doPostPiercingAttack(level, (LivingEntity)(Object)this);
        }
    }
    
    @Inject(method = "handleEntityEvent", at = @At("HEAD"))
    private void vb$handleEntityEvent(byte id, CallbackInfo ci) {
        if (id == 2) this.onKineticHit();
    }
    
    @Override
    public float getTicksUsingItem(float partial) {
        return !this.isUsingItem() ? 0.0F : this.getTicksUsingItem() + partial;
    }
    
    @Override
    public float getTicksSinceLastKineticHitFeedback(float partial) {
        return this.lastKineticHitFeedbackTime < 0L
            ? 0.0F
            : (float) (this.level().getGameTime() - this.lastKineticHitFeedbackTime) + partial;
    }
    
    @Unique
    private void onKineticHit() {
        if (this.level().getGameTime() - this.lastKineticHitFeedbackTime > 10L) {
            this.lastKineticHitFeedbackTime = this.level().getGameTime();
            KineticWeapon kineticWeapon = KineticWeapon.getKineticWeapon(this.useItem);
            if (kineticWeapon != null) {
                kineticWeapon.makeLocalHitSound(this);
            }
        }
    }
}
package com.blackgear.vanillabackport.common.level.entities.mob.monster.zombie;

import com.blackgear.vanillabackport.common.level.entities.ai.goal.RangedAttackGoal;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.ZombieAttackGoal;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TieredItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;

import java.util.function.IntSupplier;

public abstract class RangedZombie extends Zombie implements RangedAttackMob {
    private float rangedAttackUncertainty = -1.0F;
    private boolean isRanged;
    
    public RangedZombie(EntityType<? extends Zombie> entityType, Level level) {
        super(entityType, level);
    }
    
    public boolean isRanged() {
        return this.isRanged;
    }
    
    public float rangedAttackUncertainty(Level level) {
        return this.rangedAttackUncertainty < 0.0F
            ? 14 - level.getDifficulty().getId() * 4
            : this.rangedAttackUncertainty;
    }
    
    public void setRangedAttackUncertainty(float rangedAttackUncertainty) {
        this.rangedAttackUncertainty = rangedAttackUncertainty;
    }
    
    public abstract Item getRangedWeaponType();
    
    protected abstract Projectile getProjectile(Level level, LivingEntity owner, ItemStack stack);
    
    protected abstract SoundEvent getRangedAttackSound();
    
    @Override
    public void performRangedAttack(LivingEntity target, float power) {
        ItemStack heldItem = this.getMainHandItem();
        ItemStack weapon = heldItem.is(this.getRangedWeaponType()) ? heldItem : new ItemStack(this.getRangedWeaponType());
        Projectile projectile = this.getProjectile(this.level(), this, weapon);
        double xd = target.getX() - this.getX();
        double yd = target.getY(0.333) - projectile.getY();
        double zd = target.getZ() - this.getZ();
        double distanceToTarget = Math.sqrt(xd * xd + zd * zd);
        if (this.level() instanceof ServerLevel level) {
            projectile.shoot(
                xd,
                yd + distanceToTarget * (projectile.getGravity() * 4.0),
                zd,
                1.6F,
                this.rangedAttackUncertainty(level)
            );
            this.level().addFreshEntity(projectile);
        }
        
        this.playSound(this.getRangedAttackSound(), 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
    }
    
    protected boolean shouldDoRangedAttack() {
        if (!this.getMainHandItem().is(this.getRangedWeaponType())) {
            return false;
        } else {
            LivingEntity target = this.getTarget();
            if (target != null && target.isAlive()) {
                float rangeThreshold = this.isRanged ? 9.0F : 25.0F;
                if (this.distanceToSqr(target) >= rangeThreshold) {
                    return true;
                } else if (this.isWithinMeleeAttackRange(target)) {
                    return false;
                } else {
                    Path path = this.navigation.getPath() != null ? this.navigation.getPath() : this.navigation.createPath(target, 0);
                    return path != null && path.getTarget().equals(target.blockPosition()) && !path.canReach();
                }
            } else {
                return false;
            }
        }
    }

    protected static class RangedZombieMeleeAttackGoal extends ZombieAttackGoal {
        private final RangedZombie zombie;
        private final boolean chargeWeapon;
        
        public RangedZombieMeleeAttackGoal(Zombie zombie, double speedModifier, boolean followingTargetEvenIfNotSeen, boolean chargeWeapon) {
            super(zombie, speedModifier, followingTargetEvenIfNotSeen);
            this.zombie = (RangedZombie) zombie;
            this.chargeWeapon = chargeWeapon;
        }
        
        @Override
        public void start() {
            super.start();
            if (this.chargeWeapon && this.zombie.getMainHandItem().getItem() instanceof TieredItem) {
                this.zombie.startUsingItem(InteractionHand.MAIN_HAND);
            }
        }
        
        @Override
        public void stop() {
            super.stop();
            if (this.chargeWeapon && this.zombie.getMainHandItem().getItem() instanceof TieredItem) {
                this.zombie.stopUsingItem();
            }
        }
    }
    
    protected static class RangedZombieRangedAttackGoal extends RangedAttackGoal {
        private final RangedZombie zombie;
        private final boolean chargeWeapon;
        private final boolean stopWhenInRange;
        
        public RangedZombieRangedAttackGoal(
            RangedAttackMob mob,
            double speedModifier,
            IntSupplier attackIntervalMin,
            IntSupplier attackIntervalMax,
            float attackRadius,
            boolean chargeWeapon,
            boolean stopWhenInRange
        ) {
            super(mob, speedModifier, attackIntervalMin, attackIntervalMax, attackRadius);
            this.zombie = (RangedZombie)mob;
            this.chargeWeapon = chargeWeapon;
            this.stopWhenInRange = stopWhenInRange;
        }
        
        @Override
        public boolean canUse() {
            return super.canUse() && this.zombie.shouldDoRangedAttack();
        }
        
        @Override
        public boolean canContinueToUse() {
            return super.canContinueToUse() && this.zombie.shouldDoRangedAttack();
        }
        
        @Override
        public void start() {
            super.start();
            this.zombie.setAggressive(true);
            if (this.chargeWeapon) {
                this.zombie.startUsingItem(InteractionHand.MAIN_HAND);
            }
            
            this.zombie.isRanged = true;
        }
        
        @Override
        public void stop() {
            super.stop();
            this.zombie.setAggressive(false);
            if (this.chargeWeapon) {
                this.zombie.stopUsingItem();
            }
            
            this.zombie.isRanged = false;
        }
        
        @Override
        protected boolean stopWhenInRange() {
            return this.stopWhenInRange;
        }
    }
}
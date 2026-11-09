package com.blackgear.vanillabackport.common.level.entities.projectile.wind_charge;

import net.minecraft.core.Vec3i;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractHurtingProjectile;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.*;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractWindCharge extends AbstractHurtingProjectile implements ItemSupplier {
    public AbstractWindCharge(EntityType<? extends AbstractHurtingProjectile> entityType, Level level) {
        super(entityType, level);
        this.xPower = 0.0;
        this.yPower = 0.0;
        this.zPower = 0.0;
    }
    
    public AbstractWindCharge(EntityType<? extends AbstractWindCharge> entityType, Level level, Entity owner, double x, double y, double z) {
        super(entityType, x, y, z, 0, 0, 0, level);
        this.setOwner(owner);
        this.xPower = 0.0;
        this.yPower = 0.0;
        this.zPower = 0.0;
    }
    
    public AbstractWindCharge(EntityType<? extends AbstractWindCharge> entityType, double x, double y, double z, Vec3 movement, Level level) {
        super(entityType, x, y, z, movement.x, movement.y, movement.z, level);
        this.xPower = 0.0;
        this.yPower = 0.0;
        this.zPower = 0.0;
    }
    
    @Override
    protected AABB makeBoundingBox() {
        EntityDimensions dimensions = this.getType().getDimensions();
        float width = dimensions.width / 2.0F;
        float height = dimensions.height;
        return new AABB(
            this.getX() - width, this.getY() - 0.15F, this.getZ() - width,
            this.getX() + width, this.getY() + 0.15F + height, this.getZ() + width
        );
    }
    
    @Override
    public boolean canCollideWith(Entity entity) {
        return !(entity instanceof AbstractWindCharge) && super.canCollideWith(entity);
    }
    
    @Override
    protected boolean canHitEntity(Entity target) {
        if (target instanceof AbstractWindCharge) {
            return false;
        } else {
            return target.getType() != EntityType.END_CRYSTAL && super.canHitEntity(target);
        }
    }
    
    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        if (!this.level().isClientSide()) {
            LivingEntity owner = this.getOwner() instanceof LivingEntity living ? living : null;
            Entity entity = result.getEntity();
            if (owner != null) owner.setLastHurtMob(entity);
            
            DamageSource source = this.level().damageSources().explosion(this, owner);
            if (entity.hurt(source, 1.0F) && entity instanceof LivingEntity target) {
                this.doEnchantDamageEffects(owner, target);
            }
            
            this.explode(this.position());
        }
    }
    
    @Override
    public void push(double x, double y, double z) { /* NO-OP */ }
    
    protected abstract void explode(Vec3 pos);
    
    @Override
    protected void onHitBlock(BlockHitResult result) {
        super.onHitBlock(result);
        if (!this.level().isClientSide()) {
            Vec3i collisionNormal = result.getDirection().getNormal();
            Vec3 scaledNormal = Vec3.atLowerCornerOf(collisionNormal).scale(0.25);
            Vec3 explosionPos = result.getLocation().add(scaledNormal);
            this.explode(explosionPos);
            this.discard();
        }
    }
    
    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide()) {
            this.discard();
        }
    }
    
    @Override
    protected boolean shouldBurn() {
        return false;
    }
    
    @Override
    public ItemStack getItem() {
        return ItemStack.EMPTY;
    }
    
    @Override
    protected float getInertia() {
        return 1.0F;
    }
    
    @Override
    protected @Nullable ParticleOptions getTrailParticle() {
        return null;
    }
    
    @Override
    public void tick() {
        if (!this.level().isClientSide() && this.getBlockY() > this.level().getMaxBuildHeight() + 30) {
            this.explode(this.position());
            this.discard();
        } else {
            super.tick();
        }
    }
    
    @Override
    public boolean hurt(DamageSource source, float amount) {
        return false;
    }
}
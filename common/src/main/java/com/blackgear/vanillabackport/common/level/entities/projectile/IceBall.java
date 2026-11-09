package com.blackgear.vanillabackport.common.level.entities.projectile;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import com.blackgear.vanillabackport.common.registries.items.ModItems;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class IceBall extends ThrowableItemProjectile {
    public IceBall(EntityType<? extends ThrowableItemProjectile> entityType, Level level) {
        super(entityType, level);
    }
    
    public IceBall(Level level, LivingEntity mob) {
        super(ModEntityTypes.ICE_BALL.get(), mob, level);
    }
    
    public IceBall(Level level, double x, double y, double z) {
        super(ModEntityTypes.ICE_BALL.get(), x, y, z, level);
    }
    
    @Override
    protected Item getDefaultItem() {
        return ModItems.ICE_BALL.get();
    }
    
    private ParticleOptions getParticle() {
        return new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(this.getDefaultItem()));
    }
    
    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            ParticleOptions particle = this.getParticle();
            
            for (int i = 0; i < 8; i++) {
                this.level().addParticle(particle, this.getX(), this.getY(), this.getZ(), 0.0, 0.0, 0.0);
            }
        }
    }
    
    @Override
    protected void onHitEntity(EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        float pow = (float) this.getDeltaMovement().length();
        int damage = Mth.ceil(Mth.clamp(pow * 1.395F, 0.0F, Float.MAX_VALUE));
        entity.hurt(this.damageSources().thrown(this, this.getOwner()), damage);
    }
    
    @Override
    protected void onHit(HitResult result) {
        super.onHit(result);
        if (!this.level().isClientSide) {
            this.level().broadcastEntityEvent(this, (byte) 3);
            Vec3 hitLocation = result.getLocation();
            this.level().playSound(
                null,
                hitLocation.x,
                hitLocation.y,
                hitLocation.z,
                ModSoundEvents.ICE_BALL_BREAK.get(),
                SoundSource.NEUTRAL,
                0.5F,
                0.4F / (this.level().getRandom().nextFloat() * 0.4F + 0.8F)
            );
            this.discard();
        }
    }
}
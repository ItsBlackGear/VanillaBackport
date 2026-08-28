package com.blackgear.vanillabackport.common.level.entities.mob.monster.skeleton;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.api.extensions.entity.modifiers.SkeletonAttackIntervalModifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class Parched extends AbstractSkeleton implements SkeletonAttackIntervalModifier {
    public Parched(EntityType<? extends AbstractSkeleton> entityType, Level level) {
        super(entityType, level);
    }
    
    @Override
    protected boolean isSunBurnTick() {
        return false;
    }
    
    @Override
    protected AbstractArrow getArrow(ItemStack arrowStack, float velocity) {
        AbstractArrow arrow = super.getArrow(arrowStack, velocity);
        if (arrow instanceof Arrow instance) {
            instance.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 600));
        }
        
        return arrow;
    }
    
    public static AttributeSupplier.Builder createAttributes() {
        return AbstractSkeleton.createAttributes().add(Attributes.MAX_HEALTH, 16.0);
    }
    
    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return ModSoundEvents.PARCHED_AMBIENT.get();
    }
    
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return ModSoundEvents.PARCHED_HURT.get();
    }
    
    @Override
    protected SoundEvent getDeathSound() {
        return ModSoundEvents.PARCHED_DEATH.get();
    }
    
    @Override
    protected SoundEvent getStepSound() {
        return ModSoundEvents.PARCHED_STEP.get();
    }
    
    @Override
    public int getHardAttackInterval() {
        return 50;
    }
    
    @Override
    public int getAttackInterval() {
        return 70;
    }
    
    @Override
    public boolean canBeAffected(MobEffectInstance effect) {
        return effect.getEffect() == MobEffects.WEAKNESS || super.canBeAffected(effect);
    }
}
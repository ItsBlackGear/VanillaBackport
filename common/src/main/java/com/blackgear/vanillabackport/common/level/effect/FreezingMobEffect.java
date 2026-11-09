package com.blackgear.vanillabackport.common.level.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class FreezingMobEffect extends MobEffect {
    public FreezingMobEffect(MobEffectCategory category, int color) {
        super(category, color);
    }
    
    @Override
    public boolean applyEffectTick(LivingEntity mob, int amplifier) {
        if (mob.canFreeze()) {
            mob.setTicksFrozen(Math.min(mob.getTicksRequiredToFreeze(), mob.getTicksFrozen() + 1));
        }
        
        return true;
    }
    
    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        return true;
    }
}
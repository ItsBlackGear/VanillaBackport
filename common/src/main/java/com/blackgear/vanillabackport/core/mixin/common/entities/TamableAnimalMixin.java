package com.blackgear.vanillabackport.core.mixin.common.entities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(TamableAnimal.class)
public abstract class TamableAnimalMixin extends MobMixin {
    @Shadow public abstract boolean isTame();

    protected TamableAnimalMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }
}
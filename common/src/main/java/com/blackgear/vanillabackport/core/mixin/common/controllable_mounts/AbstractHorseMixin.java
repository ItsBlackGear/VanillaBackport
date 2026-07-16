package com.blackgear.vanillabackport.core.mixin.common.controllable_mounts;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractHorse.class)
public abstract class AbstractHorseMixin extends Animal {
    protected AbstractHorseMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }
    
    @ModifyReturnValue(method = "getControllingPassenger", at = @At("RETURN"))
    private LivingEntity vb$getControllingPassenger(LivingEntity original) {
        return original != null ? original : super.getControllingPassenger();
    }
}
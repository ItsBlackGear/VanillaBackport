package com.blackgear.vanillabackport.core.mixin.common.frozen_mobs;

import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import com.blackgear.vanillabackport.common.registries.entities.ModMobEffects;
import com.blackgear.vanillabackport.core.data.tags.ModEntityTypeTags;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }
    
    @Inject(method = "canBeAffected", at = @At("HEAD"), cancellable = true)
    private void vb$canBeAffected(MobEffectInstance effectInstance, CallbackInfoReturnable<Boolean> cir) {
        if (effectInstance.is(ModMobEffects.FREEZING) && this.getType().is(ModEntityTypeTags.IMMUNE_TO_FREEZING)) {
            cir.setReturnValue(false);
        }
    }
    
    @Inject(method = "canFreeze", at = @At("HEAD"), cancellable = true)
    private void vb$canFreeze(CallbackInfoReturnable<Boolean> cir) {
        if ((LivingEntity)(Object) this instanceof Zombie zombie) {
            if (zombie.getType().is(ModEntityTypeTags.ZOMBIES_THAT_CONVERT_ON_POWDER_SNOW) || zombie.getType() == ModEntityTypes.FROSTBITE.get()) {
                cir.setReturnValue(false);
            }
        }
    }
    
    @ModifyExpressionValue(
        method = "aiStep",
        at = @At(
            value = "FIELD",
            target = "Lnet/minecraft/world/entity/LivingEntity;isInPowderSnow:Z",
            opcode = Opcodes.GETFIELD
        )
    )
    private boolean freezing$effectCountsAsPowderSnow(boolean original) {
        return original || ((LivingEntity) (Object) this).hasEffect(ModMobEffects.FREEZING);
    }
    
    @WrapOperation(
        method = "aiStep",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;hurt(Lnet/minecraft/world/damagesource/DamageSource;F)Z"
        )
    )
    private boolean freezing$scaleDamage(LivingEntity instance, DamageSource source, float amount, Operation<Boolean> original) {
        if (source.is(DamageTypes.FREEZE) && instance.hasEffect(ModMobEffects.FREEZING)) {
            amount = 1.5F;
        }
        return original.call(instance, source, amount);
    }
}
package com.blackgear.vanillabackport.core.mixin.common.entities;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.api.effect.AdvanceMobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity{
    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
            method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z",
            at = @At(
                    value = "RETURN",
                    ordinal = 1
            )
    )
    public void vb$onAddEffect(MobEffectInstance effectInstance, Entity entity, CallbackInfoReturnable<Boolean> cir){
        if(effectInstance.getEffect() instanceof AdvanceMobEffect advanceMobEffect){
            // Not recommended, but use it for now - Echo2craft.
            var vEntity = (Entity)this;
            if(vEntity instanceof LivingEntity livingEntity){
                advanceMobEffect.onEffectAdded(livingEntity,effectInstance.getAmplifier());
            }
        }
        else if(effectInstance.getEffect().equals(MobEffects.BAD_OMEN)){
            // ResourceLocation vEffect = BuiltInRegistries.MOB_EFFECT.getKey(effectInstance.getEffect());
            this.level().playSound(
                    null,
                    this.getX(),
                    this.getY(),
                    this.getZ(),
                    ModSoundEvents.APPLY_EFFECT_BAD_OMEN.get(),
                    this.getSoundSource(),
                    1.0F,
                    1.0F
            );
        }
    }
}

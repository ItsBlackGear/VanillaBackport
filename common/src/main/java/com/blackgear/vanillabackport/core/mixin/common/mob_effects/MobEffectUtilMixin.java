package com.blackgear.vanillabackport.core.mixin.common.mob_effects;

import com.blackgear.vanillabackport.common.registries.entities.ModMobEffects;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEffectUtil.class)
public class MobEffectUtilMixin {
    @Inject(method = "hasWaterBreathing", at = @At("HEAD"), cancellable = true)
    private static void vb$handleWaterBreathing(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity.hasEffect(ModMobEffects.BREATH_OF_THE_NAUTILUS)) cir.setReturnValue(true);
    }
}
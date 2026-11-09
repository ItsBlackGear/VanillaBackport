package com.blackgear.vanillabackport.core.mixin.client.entity_rendering;

import com.blackgear.vanillabackport.common.api.extensions.entity.conversion.FrostbiteConversion;
import net.minecraft.client.renderer.entity.AbstractZombieRenderer;
import net.minecraft.world.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractZombieRenderer.class)
public class AbstractZombieRendererMixin {
    @Inject(method = "isShaking(Lnet/minecraft/world/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
    private void vb$isShaking(LivingEntity entity, CallbackInfoReturnable<Boolean> cir) {
        if (((FrostbiteConversion) entity).vb$isFreezeConverting()) {
            cir.setReturnValue(true);
        }
    }
}
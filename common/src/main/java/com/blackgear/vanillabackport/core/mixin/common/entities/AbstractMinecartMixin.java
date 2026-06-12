package com.blackgear.vanillabackport.core.mixin.common.entities;

import com.blackgear.vanillabackport.common.api.extensions.entities.MotionAwareEntity;
import net.minecraft.world.entity.vehicle.AbstractMinecart;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AbstractMinecart.class)
public class AbstractMinecartMixin {
    @Inject(method = "tick", at = @At("TAIL"))
    private void vb$handleMinecartTick(CallbackInfo ci) {
        ((MotionAwareEntity) this).computeSpeed();
    }
}
package com.blackgear.vanillabackport.core.mixin.client;

import com.blackgear.vanillabackport.common.level.entities.happyghast.HappyGhast;
import net.minecraft.client.Camera;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Camera.class)
public abstract class CameraMixin {
    @Shadow protected abstract double getMaxZoom(double startingDistance);
    @Shadow protected abstract void move(double distanceOffset, double verticalOffset, double horizontalOffset);

    @Inject(
        method = "setup",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/Camera;getMaxZoom(D)D"
        ),
        cancellable = true
    )
    private void onCameraSetup(BlockGetter level, Entity entity, boolean detached, boolean thirdPersonReverse, float partialTick, CallbackInfo ci) {
        if (entity.isPassenger() && entity.getVehicle() instanceof HappyGhast) {
            this.move(-this.getMaxZoom(8.0), 0.0F, 0.0F);
            ci.cancel();
        }
    }
}
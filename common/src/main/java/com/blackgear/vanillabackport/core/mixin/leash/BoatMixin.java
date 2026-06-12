package com.blackgear.vanillabackport.core.mixin.leash;

import com.blackgear.vanillabackport.common.api.leash.InterpolationHandler;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Boat.class)
public abstract class BoatMixin extends Entity {
    @Unique private final InterpolationHandler interpolation = new InterpolationHandler(this, 3);

    public BoatMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "lerpTo", at = @At("HEAD"), cancellable = true)
    private void vb$lerpTo(double x, double y, double z, float yRot, float xRot, int steps, CallbackInfo ci) {
        this.interpolation.interpolateTo(new Vec3(x, y, z), yRot, xRot);
        ci.cancel();
    }

    @Inject(method = "tickLerp", at = @At("HEAD"), cancellable = true)
    private void vb$tickLerp(CallbackInfo ci) {
        if (this.isControlledByLocalInstance()) {
            this.interpolation.cancel();
            this.syncPacketPositionCodec(this.getX(), this.getY(), this.getZ());
        }

        this.interpolation.interpolate();
        ci.cancel();
    }

    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;startRiding(Lnet/minecraft/world/entity/Entity;)Z"))
    private boolean vb$skipMobsOnBoat(Entity entity, Entity vehicle, Operation<Boolean> original) {
        if (entity.getType() == ModEntityTypes.SULFUR_CUBE.get()) return false;
        return original.call(entity, vehicle);
    }
}
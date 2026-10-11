package com.blackgear.vanillabackport.core.mixin.common.boat_blacklist;

import com.blackgear.vanillabackport.common.level.entities.mob.animal.nautilus.AbstractNautilus;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.vehicle.Boat;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Boat.class)
public abstract class BoatMixin {
    @WrapOperation(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;startRiding(Lnet/minecraft/world/entity/Entity;)Z"))
    private boolean vb$skipMobsOnBoat(Entity entity, Entity vehicle, Operation<Boolean> original) {
        if (entity.getType() == ModEntityTypes.SULFUR_CUBE.get() || entity instanceof AbstractNautilus) return false;
        return original.call(entity, vehicle);
    }
}
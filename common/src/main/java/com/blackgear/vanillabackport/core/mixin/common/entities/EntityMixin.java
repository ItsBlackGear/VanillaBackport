package com.blackgear.vanillabackport.core.mixin.common.entities;

import com.blackgear.vanillabackport.common.level.entities.happyghast.HappyGhast;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {
    @Shadow public abstract boolean isPassengerOfSameVehicle(Entity entity);

    @Inject(method = "canCollideWith", at = @At("HEAD"), cancellable = true)
    private void vb$canCollideWith(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof HappyGhast ghast) {
            cir.setReturnValue(ghast.canBeCollidedWith((Entity) (Object) this) && !this.isPassengerOfSameVehicle(entity));
        }
    }
}
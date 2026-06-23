package com.blackgear.vanillabackport.core.mixin.common.leash_behavior;

import com.blackgear.vanillabackport.common.api.modules.leash_behavior.Leashable;
import net.minecraft.world.entity.*;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PathfinderMob.class)
public abstract class LeashBehaviorMixin extends Mob implements Leashable {
    @Unique private double angularMomentum;

    protected LeashBehaviorMixin(EntityType<? extends Mob> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    public double vb$angularMomentum() {
        return this.angularMomentum;
    }

    @Override
    public void vb$setAngularMomentum(double angularMomentum) {
        this.angularMomentum = angularMomentum;
    }

    @Inject(method = "tickLeash", at = @At("HEAD"), cancellable = true)
    private void vb$onTickLeash(CallbackInfo ci) {
        ci.cancel();
        super.tickLeash();
        Leashable.vb$onTickLeash(this);
    }
}
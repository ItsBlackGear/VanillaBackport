package com.blackgear.vanillabackport.core.mixin.common.entity_movement;

import com.blackgear.vanillabackport.common.api.extensions.entity.MotionAwareEntity;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Entity.class)
public abstract class EntityMixin implements MotionAwareEntity {
    @Shadow public abstract Vec3 position();
    @Shadow @Nullable public abstract LivingEntity getControllingPassenger();
    @Shadow public abstract boolean isAlive();
    @Shadow public abstract Vec3 getDeltaMovement();

    @Unique private Vec3 lastKnownSpeed = Vec3.ZERO;
    @Unique @Nullable private Vec3 lastKnownPosition;

    @Inject(
        method = "reapplyPosition",
        at = @At("TAIL")
    )
    private void vb$reapplyPosition(CallbackInfo ci) {
        this.lastKnownPosition = null;
    }

    @Inject(
        method = "baseTick",
        at = @At("TAIL")
    )
    private void vb$baseTick(CallbackInfo ci) {
        this.computeSpeed();
    }

    @Override
    public void computeSpeed() {
        if (this.lastKnownPosition == null) {
            this.lastKnownPosition = this.position();
        }

        this.lastKnownSpeed = this.position().subtract(this.lastKnownPosition);
        this.lastKnownPosition = this.position();
    }

    @Override
    public Vec3 getKnownSpeed() {
        return this.getControllingPassenger() instanceof Player player && this.isAlive()
            ? ((MotionAwareEntity) player).getKnownSpeed()
            : this.lastKnownSpeed;
    }
}
package com.blackgear.vanillabackport.core.mixin.common.entities;

import com.blackgear.vanillabackport.common.level.entities.bat.BatAnimator;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ambient.AmbientCreature;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Bat.class)
public abstract class BatMixin extends AmbientCreature implements BatAnimator {
    @Unique private final AnimationState flyAnimationState = new AnimationState();
    @Unique private final AnimationState restAnimationState = new AnimationState();

    @Shadow public abstract boolean isResting();

    protected BatMixin(EntityType<? extends AmbientCreature> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        this.setupAnimationStates();
    }

    @Unique
    private void setupAnimationStates() {
        if (this.isResting()) {
            this.flyAnimationState.stop();
            this.restAnimationState.startIfStopped(this.tickCount);
        } else {
            this.restAnimationState.stop();
            this.flyAnimationState.startIfStopped(this.tickCount);
        }
    }

    @Override
    public AnimationState flyAnimationState() {
        return this.flyAnimationState;
    }

    @Override
    public AnimationState restAnimationState() {
        return this.restAnimationState;
    }
}
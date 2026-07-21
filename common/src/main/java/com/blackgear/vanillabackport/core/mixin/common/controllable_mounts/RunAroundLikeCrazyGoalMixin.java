package com.blackgear.vanillabackport.core.mixin.common.controllable_mounts;

import com.blackgear.vanillabackport.common.api.extensions.entity.mounts.ControllableMob;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.ai.goal.RunAroundLikeCrazyGoal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(RunAroundLikeCrazyGoal.class)
public class RunAroundLikeCrazyGoalMixin {
    @Shadow @Final private AbstractHorse horse;
    
    @ModifyExpressionValue(
        method = "canUse",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/animal/horse/AbstractHorse;isVehicle()Z",
            ordinal = 0
        )
    )
    private boolean vb$canUse(boolean original) {
        return original && !ControllableMob.of(this.horse).isMobControlled();
    }
}
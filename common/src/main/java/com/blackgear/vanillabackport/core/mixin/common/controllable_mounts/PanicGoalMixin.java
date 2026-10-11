package com.blackgear.vanillabackport.core.mixin.common.controllable_mounts;

import com.blackgear.vanillabackport.common.api.extensions.entity.mounts.ControllableMob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PanicGoal.class)
public class PanicGoalMixin {
    @Shadow @Final protected PathfinderMob mob;
    
    @Inject(method = "shouldPanic", at = @At("RETURN"), cancellable = true)
    private void vb$shouldPanic(CallbackInfoReturnable<Boolean> cir) {
        if (ControllableMob.of(this.mob).isMobControlled()) {
            cir.setReturnValue(false);
        }
    }
}
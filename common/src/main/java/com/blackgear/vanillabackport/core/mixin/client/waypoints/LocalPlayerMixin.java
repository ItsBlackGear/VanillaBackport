package com.blackgear.vanillabackport.core.mixin.client.waypoints;

import com.blackgear.vanillabackport.client.api.modules.waypoints.ExperienceDisplay;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin implements ExperienceDisplay {
    @Unique private int experienceDisplayStartTick;
    
    @Override
    public int getExperienceDisplayStartTick() {
        return this.experienceDisplayStartTick;
    }
    
    @Override
    public void setExperienceDisplayStartTick(int ticks) {
        this.experienceDisplayStartTick = ticks;
    }
    
    @Inject(method = "setExperienceValues", at = @At("TAIL"))
    private void vb$setExperienceValues(float currentXP, int maxXP, int level, CallbackInfo ci) {
        this.setExperienceDisplayStartTick(((LocalPlayer)(Object) this).tickCount);
    }
}
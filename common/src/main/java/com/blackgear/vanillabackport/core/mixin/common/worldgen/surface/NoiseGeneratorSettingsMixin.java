package com.blackgear.vanillabackport.core.mixin.common.worldgen.surface;

import com.blackgear.vanillabackport.common.worldgen.ModSurfaceRuleData;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = NoiseGeneratorSettings.class, priority = 100)
public class NoiseGeneratorSettingsMixin {
    @Shadow @Final @Mutable
    private SurfaceRules.RuleSource surfaceRule;
    @Unique private boolean vb$appliedRules = false;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void vb$injectSurfaceRules(CallbackInfo ci) {
        if (this.vb$appliedRules) return;

        this.surfaceRule = SurfaceRules.sequence(ModSurfaceRuleData.makeRules(), this.surfaceRule);
        this.vb$appliedRules = true;
    }
}
package com.blackgear.vanillabackport.core.mixin.common.worldgen.sulfur_caves;

import com.blackgear.vanillabackport.common.worldgen.surface.ModSurfaceRuleData;
import com.blackgear.vanillabackport.core.mixin.common.access.NoiseGeneratorSettingsAccessor;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NoiseBasedChunkGenerator.class)
public class NoiseBasedChunkGeneratorMixin {
    @Unique private boolean patched = false;
    @Shadow @Final private Holder<NoiseGeneratorSettings> settings;
    
    @Inject(method = "generatorSettings", at = @At("RETURN"))
    private void vb$injectSurfaceRules(CallbackInfoReturnable<Holder<NoiseGeneratorSettings>> cir) {
        if (!this.patched && this.settings.is(NoiseGeneratorSettings.OVERWORLD)) {
            NoiseGeneratorSettings settings = cir.getReturnValue().value();
            ((NoiseGeneratorSettingsAccessor) (Object) settings).setSurfaceRule(SurfaceRules.sequence(ModSurfaceRuleData.makeRules(), settings.surfaceRule()));
            this.patched = true;
        }
    }
}
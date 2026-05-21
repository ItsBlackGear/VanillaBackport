package com.blackgear.vanillabackport.core.mixin.common.worldgen.surface;

import com.blackgear.vanillabackport.common.worldgen.ModSurfaceRuleData;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraft.world.level.levelgen.SurfaceRules;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = NoiseGeneratorSettings.class, priority = 100)
public class NoiseGeneratorSettingsMixin {
    @Shadow @Final @Mutable private SurfaceRules.RuleSource surfaceRule;
    @Unique private boolean vb$appliedRules = false;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void vb$injectSurfaceRules(CallbackInfo ci) {
        if (this.vb$appliedRules) return;

        if (vb$canLoad()) {
            this.surfaceRule = SurfaceRules.sequence(ModSurfaceRuleData.makeRules(), this.surfaceRule);
            this.vb$appliedRules = true;
        }
    }

    @Unique // Hacky workaround, at least until 1.2
    private static boolean vb$canLoad() {
        Registry<Block> registry = BuiltInRegistries.BLOCK;
        return registry.containsKey(ResourceLocation.withDefaultNamespace("sulfur"))
            && registry.containsKey(ResourceLocation.withDefaultNamespace("cinnabar"));
    }
}
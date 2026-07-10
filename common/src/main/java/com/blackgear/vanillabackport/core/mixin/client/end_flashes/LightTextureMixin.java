package com.blackgear.vanillabackport.core.mixin.client.end_flashes;

import com.blackgear.vanillabackport.client.api.modules.end_flashes.EndFlashRenderer;
import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = LightTexture.class, priority = 1100)
public class LightTextureMixin {
    @Shadow @Final private Minecraft minecraft;
    @Shadow @Final private NativeImage lightPixels;
    
    @Unique private float vb$intensity;
    
    @Inject(method = "updateLightTexture", at = @At("HEAD"))
    private void vb$calculateLightIntensity(float partialTicks, CallbackInfo ci) {
        this.vb$intensity = EndFlashRenderer.calculateLightIntensity(this.minecraft, partialTicks);
    }
    
    @Inject(method = "updateLightTexture", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/texture/DynamicTexture;upload()V"))
    private void vb$applyEndFlashTint(float partialTicks, CallbackInfo ci) {
        ClientLevel level = this.minecraft.level;
        if (level == null || level.effects().skyType() != DimensionSpecialEffects.SkyType.END) {
            return;
        }
        
        EndFlashRenderer.applyLightTint(this.lightPixels, this.vb$intensity);
    }
}
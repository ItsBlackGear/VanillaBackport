package com.blackgear.vanillabackport.core.mixin.client.end_flashes;

import com.blackgear.vanillabackport.client.api.modules.end_flashes.EndFlashRenderer;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Shadow @Final private Minecraft minecraft;
    
    @Inject(method = "renderEndSky", at = @At("RETURN"))
    private void vb$renderEndFlash(PoseStack poseStack, CallbackInfo ci) {
        EndFlashRenderer.render(poseStack, this.minecraft);
    }
}
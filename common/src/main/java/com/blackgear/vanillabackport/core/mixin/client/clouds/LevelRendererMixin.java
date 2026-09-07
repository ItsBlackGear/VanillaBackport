package com.blackgear.vanillabackport.core.mixin.client.clouds;

import com.blackgear.vanillabackport.core.VanillaBackport;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Shadow private int lastViewDistance;
    
    @Unique
    private int vb$getCloudRenderDistance() {
        return Math.max(1, this.lastViewDistance);
    }
    
    @ModifyExpressionValue(method = "buildClouds",
        at = @At(value = "CONSTANT", args = "intValue=-3"))
    private int vb$modifyFancyStart(int original) {
        if (!VanillaBackport.CLIENT_CONFIG.extendedCloudReach.get()) {
            return original;
        }
        
        return 1 - this.vb$getCloudRenderDistance();
    }
    
    @ModifyExpressionValue(method = "buildClouds",
        at = @At(value = "CONSTANT", args = "intValue=4"))
    private int vb$modifyFancyEnd(int original) {
        if (!VanillaBackport.CLIENT_CONFIG.extendedCloudReach.get()) {
            return original;
        }
        
        return this.vb$getCloudRenderDistance();
    }
    
    @ModifyExpressionValue(method = "buildClouds",
        at = @At(value = "CONSTANT", args = "intValue=-32"))
    private int vb$modifyFastStart(int original) {
        if (!VanillaBackport.CLIENT_CONFIG.extendedCloudReach.get()) {
            return original;
        }
        
        return -this.vb$getCloudRenderDistance() * 4;
    }
    
    @ModifyExpressionValue(method = "buildClouds",
        at = @At(value = "CONSTANT", args = "intValue=32"))
    private int vb$modifyFastEnd(int original) {
        if (!VanillaBackport.CLIENT_CONFIG.extendedCloudReach.get()) {
            return original;
        }
        
        return this.vb$getCloudRenderDistance() * 4;
    }
}
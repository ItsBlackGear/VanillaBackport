package com.blackgear.vanillabackport.core.mixin.client.clouds;

import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.client.renderer.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Shadow private int lastViewDistance;
    
    @Unique
    private int vb$getCloudRenderDistance() {
        return Math.max(1, this.lastViewDistance);
    }
    
    @ModifyConstant(method = "buildClouds", constant = @Constant(intValue = -3))
    private int vb$modifyFancyStart(int original) {
        if (!VanillaBackport.CLIENT_CONFIG.extendedCloudReach.get()) return original;
        return 1 - vb$getCloudRenderDistance();
    }
    
    @ModifyConstant(method = "buildClouds", constant = @Constant(intValue = 4))
    private int vb$modifyFancyEnd(int original) {
        if (!VanillaBackport.CLIENT_CONFIG.extendedCloudReach.get()) return original;
        return vb$getCloudRenderDistance();
    }
    
    @ModifyConstant(method = "buildClouds", constant = @Constant(intValue = -32))
    private int vb$modifyFastStart(int original) {
        if (!VanillaBackport.CLIENT_CONFIG.extendedCloudReach.get()) return original;
        return -vb$getCloudRenderDistance() * 4;
    }
    
    @ModifyConstant(method = "buildClouds", constant = @Constant(intValue = 32))
    private int vb$modifyFastEnd(int original) {
        if (!VanillaBackport.CLIENT_CONFIG.extendedCloudReach.get()) return original;
        return vb$getCloudRenderDistance() * 4;
    }
}
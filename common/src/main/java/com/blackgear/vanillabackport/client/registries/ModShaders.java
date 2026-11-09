package com.blackgear.vanillabackport.client.registries;

import com.blackgear.platform.client.GameRendering;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class ModShaders {
    public static ShaderInstance rendertypeBreezeWindShader;
    
    @Nullable
    public static ShaderInstance getRendertypeBreezeWindShader() {
        return rendertypeBreezeWindShader;
    }
    
    public static void bootstrap(GameRendering.CoreShaderRegisterEvent event) {
        event.register(
            new ResourceLocation("rendertype_breeze_wind"), DefaultVertexFormat.NEW_ENTITY,
            instance -> rendertypeBreezeWindShader = instance
        );
    }
}
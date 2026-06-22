package com.blackgear.vanillabackport.client.api.extensions.chunk_render;

import com.mojang.blaze3d.vertex.VertexBuffer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;

import java.util.Map;
import java.util.Set;

@Environment(EnvType.CLIENT)
public interface RenderChunkStaleAccess {
    Set<RenderType> vb$getStaleBuffers();

    Map<RenderType, VertexBuffer> vb$getBuffers();
}
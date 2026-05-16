package com.blackgear.vanillabackport.client.util;

import com.mojang.blaze3d.vertex.VertexBuffer;
import net.minecraft.client.renderer.RenderType;

import java.util.Map;
import java.util.Set;

public interface RenderChunkStaleAccess {
    Set<RenderType> vb$getStaleBuffers();

    Map<RenderType, VertexBuffer> vb$getBuffers();
}
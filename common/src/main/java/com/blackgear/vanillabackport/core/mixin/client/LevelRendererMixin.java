package com.blackgear.vanillabackport.core.mixin.client;

import com.blackgear.vanillabackport.client.util.RenderChunkStaleAccess;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexBuffer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Set;

// Memory leak fix - MC-170134
@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    /**
     * After compilation confirms which layers the new section actually uses,
     * free GPU memory for any layer that had stale data but is now empty.
     * Only fires when a real compile task completes, not on reset().
     */
    @Inject(method = "addRecentlyCompiledChunk", at = @At("HEAD"))
    private void onChunkCompiled(ChunkRenderDispatcher.RenderChunk chunk, CallbackInfo ci) {
        if (!(chunk instanceof RenderChunkStaleAccess accessor)) return;

        Set<RenderType> stale = accessor.vb$getStaleBuffers();
        if (stale.isEmpty()) return;

        ChunkRenderDispatcher.CompiledChunk compiled = chunk.getCompiledChunk();
        Map<RenderType, VertexBuffer> buffers = accessor.vb$getBuffers();

            RenderSystem.recordRenderCall(() -> {
                stale.forEach(renderType -> {
                    if (compiled.isEmpty(renderType)) {
                        VertexBuffer buf = buffers.get(renderType);
                        if (buf != null && !buf.isInvalid()) {
                            buf.close();
                            buffers.put(renderType, new VertexBuffer(VertexBuffer.Usage.STATIC));
                        }
                    }
                });
                stale.clear();
            });
    }
}
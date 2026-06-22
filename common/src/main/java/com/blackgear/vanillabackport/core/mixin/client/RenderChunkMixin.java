package com.blackgear.vanillabackport.core.mixin.client;

import com.blackgear.vanillabackport.client.api.extensions.chunk_render.RenderChunkStaleAccess;
import com.mojang.blaze3d.vertex.VertexBuffer;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.chunk.ChunkRenderDispatcher;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;
import java.util.Set;

// Memory leak fix - MC-170134
@Mixin(ChunkRenderDispatcher.RenderChunk.class)
public abstract class RenderChunkMixin implements RenderChunkStaleAccess {
    @Shadow @Final private Map<RenderType, VertexBuffer> buffers;
    @Shadow public abstract ChunkRenderDispatcher.CompiledChunk getCompiledChunk();
    @Unique private final Set<RenderType> staleBuffers = new ObjectArraySet<>();

    /**
     * When a section is repositioned, record which layers currently have
     * GPU data. After the new compilation finishes, we'll know which of
     * those are now empty and can be freed.
     */
    @Inject(method = "setOrigin", at = @At("HEAD"))
    private void trackStaleBuffers(int x, int y, int z, CallbackInfo ci) {
        staleBuffers.clear();
        ChunkRenderDispatcher.CompiledChunk current = this.getCompiledChunk();
        if (current == ChunkRenderDispatcher.CompiledChunk.UNCOMPILED) return;

        RenderType.chunkBufferLayers().forEach(renderType -> {
            if (!current.isEmpty(renderType)) {
                staleBuffers.add(renderType);
            }
        });
    }

    @Override
    public Map<RenderType, VertexBuffer> vb$getBuffers() {
        return this.buffers;
    }

    @Override
    public Set<RenderType> vb$getStaleBuffers() {
        return this.staleBuffers;
    }
}
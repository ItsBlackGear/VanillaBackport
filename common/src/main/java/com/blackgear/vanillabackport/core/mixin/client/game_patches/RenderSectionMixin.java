package com.blackgear.vanillabackport.core.mixin.client.game_patches;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.VertexBuffer;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.chunk.SectionRenderDispatcher;
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
@Mixin(SectionRenderDispatcher.RenderSection.class)
public abstract class RenderSectionMixin {
    @Shadow @Final private Map<RenderType, VertexBuffer> buffers;
    @Shadow public abstract SectionRenderDispatcher.CompiledSection getCompiled();
    @Unique private final Set<RenderType> staleBuffers = new ObjectArraySet<>();

    /**
     * When a section is repositioned, record which layers currently have
     * GPU data. After the new compilation finishes, we'll know which of
     * those are now empty and can be freed.
     */
    @Inject(method = "setOrigin", at = @At("HEAD"))
    private void vb$trackStaleBuffers(int x, int y, int z, CallbackInfo ci) {
        this.staleBuffers.clear();
        SectionRenderDispatcher.CompiledSection current = this.getCompiled();
        RenderType.chunkBufferLayers().forEach(renderType -> {
            if (!current.isEmpty(renderType)) {
                this.staleBuffers.add(renderType);
            }
        });
    }

    /**
     * After compilation confirms which layers the new section actually uses,
     * free GPU memory for any layer that had stale data but is now empty.
     * Only fires when a real compile task completes, not on reset().
     */
    @Inject(method = "setCompiled", at = @At("HEAD"))
    private void vb$clearStaleBuffers(SectionRenderDispatcher.CompiledSection compiled, CallbackInfo ci) {
        if (this.staleBuffers.isEmpty()) return;

        // Capture what needs freeing before clearing the set
        Set<RenderType> toFree = new ObjectArraySet<>();
        this.staleBuffers.forEach(renderType -> {
            if (compiled.isEmpty(renderType)) {
                toFree.add(renderType);
            }
        });
        this.staleBuffers.clear();

        if (toFree.isEmpty()) return;

        // Defer GL calls to the render thread
        RenderSystem.recordRenderCall(() -> toFree.forEach(renderType -> {
            VertexBuffer buf = this.buffers.get(renderType);
            if (buf != null && !buf.isInvalid()) {
                buf.close();
                this.buffers.put(renderType, new VertexBuffer(VertexBuffer.Usage.STATIC));
            }
        }));
    }
}
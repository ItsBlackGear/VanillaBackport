package com.blackgear.vanillabackport.core.mixin.common.end_flashes;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.chunk.storage.ChunkSerializer;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkSerializer.class)
public class ChunkSerializerMixin {
    @Inject(method = "read", at = @At("RETURN"))
    private static void vb$readEndSkyLightPatch(ServerLevel level, PoiManager poiManager, ChunkPos pos, CompoundTag tag, CallbackInfoReturnable<ProtoChunk> cir) {
        ChunkAccess chunk = cir.getReturnValue();
        
        if (chunk != null && level.dimensionTypeRegistration().is(BuiltinDimensionTypes.END)) {
            if (!tag.contains("EndSkyLightPatch"))
                chunk.setLightCorrect(false);
        }
    }
    
    @Inject(method = "write", at = @At("RETURN"))
    private static void vb$writeEndSkyLightPatch(ServerLevel level, ChunkAccess chunk, CallbackInfoReturnable<CompoundTag> cir) {
        if (chunk != null && level.dimensionTypeRegistration().is(BuiltinDimensionTypes.END)) {
            CompoundTag tag = cir.getReturnValue();
            if (tag != null)
                tag.putBoolean("EndSkyLightPatch", true);
        }
    }
}
package com.blackgear.vanillabackport.core.mixin.common.level;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ProtoChunk;
import net.minecraft.world.level.chunk.storage.ChunkSerializer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkSerializer.class)
public class ChunkSerializerMixin {
    @Inject(method = "read", at = @At("HEAD"))
    private static void remapProperties(ServerLevel level, PoiManager poiManager, ChunkPos pos, CompoundTag tag, CallbackInfoReturnable<ProtoChunk> cir) {
        updateLeafLitterProperties(tag);
    }

    @Unique
    private static void updateLeafLitterProperties(CompoundTag tag) {
        if (!tag.contains("sections")) return;

        ListTag sections = tag.getList("sections", Tag.TAG_COMPOUND);
        for (int i = 0; i < sections.size(); i++) {
            CompoundTag section = sections.getCompound(i);
            if (!section.contains("block_states")) continue;

            CompoundTag blockStates = section.getCompound("block_states");
            if (!blockStates.contains("palette")) continue;

            ListTag palette = blockStates.getList("palette", Tag.TAG_COMPOUND);
            for (int j = 0; j < palette.size(); j++) {
                CompoundTag entry = palette.getCompound(j);
                if (!"minecraft:leaf_litter".equals(entry.getString("Name"))) continue;

                CompoundTag properties = entry.getCompound("Properties");
                if (!properties.contains("flower_amount")) continue;

                String value = properties.getString("flower_amount");
                properties.remove("flower_amount");
                properties.putString("segment_amount", value);
            }
        }
    }
}
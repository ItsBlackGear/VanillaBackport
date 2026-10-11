package com.blackgear.vanillabackport.core.mixin.compat.backports;

import com.google.common.collect.ImmutableMap;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.model.geom.LayerDefinitions;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Mixin(LayerDefinitions.class)
public abstract class LayerDefinitionsMixin {
    @WrapOperation(
        method = "createRoots",
        at = @At(
            value = "INVOKE",
            target = "Lcom/google/common/collect/ImmutableMap$Builder;build()Lcom/google/common/collect/ImmutableMap;",
            remap = false
        )
    )
    private static ImmutableMap<ModelLayerLocation, LayerDefinition> vb$safeBuild(ImmutableMap.Builder<ModelLayerLocation, LayerDefinition> builder, Operation<ImmutableMap<ModelLayerLocation, LayerDefinition>> original) {
        try {
            return original.call(builder);
        } catch (IllegalArgumentException e) {
            return vb$buildFilteredMap(builder);
        }
    }
    
    @Unique
    private static ImmutableMap<ModelLayerLocation, LayerDefinition> vb$buildFilteredMap(ImmutableMap.Builder<ModelLayerLocation, LayerDefinition> builder) {
        Map<ModelLayerLocation, LayerDefinition> safeMap = new HashMap<>();
        
        try {
            Field entriesField = ImmutableMap.Builder.class.getDeclaredField("entries");
            entriesField.setAccessible(true);
            Object[] entries = (Object[]) entriesField.get(builder);
            
            Field sizeField = ImmutableMap.Builder.class.getDeclaredField("size");
            sizeField.setAccessible(true);
            int size = sizeField.getInt(builder);
            
            for (int i = 0; i < size; i++) {
                if (entries[i] != null) {
                    @SuppressWarnings("unchecked")
                    Map.Entry<ModelLayerLocation, LayerDefinition> entry = (Map.Entry<ModelLayerLocation, LayerDefinition>) entries[i];
                    safeMap.putIfAbsent(entry.getKey(), entry.getValue());
                }
            }
        } catch (Exception ignored) { }
        
        return ImmutableMap.copyOf(safeMap);
    }
}
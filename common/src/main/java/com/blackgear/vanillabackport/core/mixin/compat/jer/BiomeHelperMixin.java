package com.blackgear.vanillabackport.core.mixin.compat.jer;

import jeresources.api.util.BiomeHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Pseudo @Mixin(BiomeHelper.class)
public class BiomeHelperMixin {
    @Inject(method = "getAllBiomes", at = @At("HEAD"), cancellable = true, remap = false)
    private static void vb$getAllBiomes(CallbackInfoReturnable<List<Biome>> cir) {
        List<Biome> biomes = new ArrayList<>();
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            Optional<Registry<Biome>> registry = level.registryAccess().registry(Registries.BIOME);
            if (registry.isPresent()) {
                registry.get().forEach(biomes::add);
                cir.setReturnValue(biomes);
            }
        }
    }

    @Inject(method = "getBiome", at = @At("HEAD"), cancellable = true, remap = false)
    private static void vb$getBiome(ResourceKey<Biome> key, CallbackInfoReturnable<Biome> cir) {
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            Optional<Registry<Biome>> registry = level.registryAccess().registry(Registries.BIOME);
            if (registry.isPresent()) {
                Biome biome = registry.get().get(key);
                cir.setReturnValue(biome);
            }
        }
    }

    @Inject(method = "getBiomes", at = @At("HEAD"), cancellable = true, remap = false)
    private static void vb$getBiomes(ResourceKey<Biome> category, CallbackInfoReturnable<List<Biome>> cir) {
        List<Biome> biomes = new ArrayList<>();
        Level level = Minecraft.getInstance().level;
        if (level != null) {
            Optional<Registry<Biome>> registry = level.registryAccess().registry(Registries.BIOME);
            if (registry.isPresent()) {
                registry.get().asLookup()
                    .listElements()
                    .filter(reference -> reference.key().equals(category))
                    .forEach(entry -> biomes.add(entry.value()));
                cir.setReturnValue(biomes);
            }
        }
    }
}
package com.blackgear.vanillabackport.core.mixin.access;

import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(OverworldBiomes.class)
public interface OverworldBiomesAccessor {
    @Invoker
    static void callGlobalOverworldGeneration(BiomeGenerationSettings.Builder builder) {}
}
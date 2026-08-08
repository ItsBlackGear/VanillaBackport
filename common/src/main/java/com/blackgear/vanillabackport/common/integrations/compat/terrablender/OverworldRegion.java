package com.blackgear.vanillabackport.common.integrations.compat.terrablender;

import com.blackgear.vanillabackport.common.integrations.worldgen.BiomeGeneration;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.mojang.datafixers.util.Pair;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import terrablender.api.ParameterUtils.*;
import terrablender.api.Region;
import terrablender.api.RegionType;
import terrablender.api.VanillaParameterOverlayBuilder;

import java.util.function.Consumer;

public class OverworldRegion extends Region {
    public OverworldRegion(ResourceLocation name, RegionType type, int weight) {
        super(name, type, weight);
    }

    @Override
    public void addBiomes(Registry<Biome> registry, Consumer<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> mapper) {
        VanillaParameterOverlayBuilder builder = new VanillaParameterOverlayBuilder();

        if (VanillaBackport.COMMON_CONFIG.hasPaleGarden.get())
            BiomeGeneration.PALE_GARDEN.forEach(pair -> builder.add(pair.getFirst(), pair.getSecond()));
        
        if (VanillaBackport.COMMON_CONFIG.hasSulfurCaves.get())
            builder.add(BiomeGeneration.SULFUR_CAVES.getFirst(), BiomeGeneration.SULFUR_CAVES.getSecond());
        
        builder.build().forEach(mapper);
    }
}
package com.blackgear.vanillabackport.common.integrations.worldgen;

import com.blackgear.platform.common.worldgen.placement.BiomePlacement;
import com.blackgear.platform.common.worldgen.placement.Placement;
import com.blackgear.platform.common.worldgen.placement.parameters.*;
import com.blackgear.vanillabackport.common.registries.worldgen.ModBiomes;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.mojang.datafixers.util.Pair;
import net.minecraft.Util;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;

import java.util.ArrayList;
import java.util.List;

public class BiomeGeneration {
    public static final List<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> PALE_GARDEN = Util.make(() -> {
        List<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> parameters = new ArrayList<>();
        
        addPaleGarden(parameters, Placement.MID_SLICE, Continentalness.FAR_INLAND.parameter(), Erosion.EROSION_1);
        addPaleGarden(parameters, Placement.MID_SLICE, Continentalness.FAR_INLAND.parameter(), Erosion.EROSION_2);
        
        addPaleGarden(parameters, Placement.HIGH_SLICE, Continentalness.span(Continentalness.MID_INLAND, Continentalness.FAR_INLAND), Erosion.EROSION_2);
        addPaleGarden(parameters, Placement.HIGH_SLICE, Continentalness.FAR_INLAND.parameter(), Erosion.EROSION_3);
        
        addPaleGarden(parameters, Placement.PEAK, Continentalness.span(Continentalness.MID_INLAND, Continentalness.FAR_INLAND), Erosion.EROSION_2);
        addPaleGarden(parameters, Placement.PEAK, Continentalness.FAR_INLAND.parameter(), Erosion.EROSION_3);
        
        return List.copyOf(parameters);
    });
    
    public static final Pair<Climate.ParameterPoint, ResourceKey<Biome>> SULFUR_CAVES = Pair.of(
        Climate.parameters(
            Temperature.FULL_RANGE.parameter(),
            Humidity.FULL_RANGE.parameter(),
            Continentalness.span(Continentalness.COAST, Continentalness.INLAND),
            Erosion.span(Erosion.EROSION_5, Erosion.EROSION_6),
            Depth.UNDERGROUND.parameter(),
            Climate.Parameter.span(-1.1F, -0.85F),
            0.0F
        ),
        ModBiomes.SULFUR_CAVES
    );
    
    public static void bootstrap(BiomePlacement.Event event) {
        if (VanillaBackport.COMMON_CONFIG.hasPaleGarden.get())
            PALE_GARDEN.forEach(event::add);
        
        if (VanillaBackport.COMMON_CONFIG.hasSulfurCaves.get())
            event.add(SULFUR_CAVES);
    }
    
    private static void addPaleGarden(List<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> parameters, Placement placement, Climate.Parameter continentalness, Erosion erosion) {
        for (Weirdness weirdness : placement.getWeirdnesses()) {
            parameters.add(Pair.of(
                Climate.parameters(
                    Temperature.NEUTRAL.parameter(),
                    Humidity.HUMID.parameter(),
                    continentalness,
                    erosion.parameter(),
                    Depth.SURFACE.parameter(),
                    weirdness.parameter(),
                    0.0F
                ),
                ModBiomes.PALE_GARDEN
            ));
        }
    }
}
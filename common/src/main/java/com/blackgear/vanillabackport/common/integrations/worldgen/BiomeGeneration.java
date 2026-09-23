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
        
        addBiome(parameters,
            ModBiomes.PALE_GARDEN,
            Placement.MID_SLICE_VARIANT,
            Temperature.NEUTRAL,
            Humidity.HUMID,
            Continentalness.FAR_INLAND.parameter(),
            Erosion.EROSION_1.parameter()
        );
        
        addBiome(parameters,
            ModBiomes.PALE_GARDEN,
            Placement.MID_SLICE_VARIANT,
            Temperature.NEUTRAL,
            Humidity.HUMID,
            Continentalness.FAR_INLAND.parameter(),
            Erosion.EROSION_2.parameter()
        );
        
        addBiome(parameters,
            ModBiomes.PALE_GARDEN,
            Placement.HIGH_SLICE_VARIANT,
            Temperature.NEUTRAL,
            Humidity.HUMID,
            Continentalness.span(Continentalness.MID_INLAND, Continentalness.FAR_INLAND),
            Erosion.EROSION_2.parameter()
        );
        
        addBiome(parameters,
            ModBiomes.PALE_GARDEN,
            Placement.HIGH_SLICE_VARIANT,
            Temperature.NEUTRAL,
            Humidity.HUMID,
            Continentalness.FAR_INLAND.parameter(),
            Erosion.EROSION_3.parameter()
        );
        
        addBiome(parameters,
            ModBiomes.PALE_GARDEN,
            Placement.PEAK_VARIANT,
            Temperature.NEUTRAL,
            Humidity.HUMID,
            Continentalness.span(Continentalness.MID_INLAND, Continentalness.FAR_INLAND),
            Erosion.EROSION_2.parameter()
        );
        
        addBiome(parameters,
            ModBiomes.PALE_GARDEN,
            Placement.PEAK_VARIANT,
            Temperature.NEUTRAL,
            Humidity.HUMID,
            Continentalness.FAR_INLAND.parameter(),
            Erosion.EROSION_3.parameter()
        );
        
        return List.copyOf(parameters);
    });
    
    public static final List<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> DAPPLED_FOREST = Util.make(() -> {
        List<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> parameters = new ArrayList<>();
        
        addBiome(parameters,
            ModBiomes.DAPPLED_FOREST,
            Placement.MID_SLICE_VARIANT,
            Temperature.COOL,
            Humidity.ARID,
            Continentalness.span(Continentalness.NEAR_INLAND, Continentalness.FAR_INLAND),
            Erosion.span(Erosion.EROSION_2, Erosion.EROSION_4)
        );
        
        addBiome(parameters,
            ModBiomes.DAPPLED_FOREST,
            Placement.HIGH_SLICE_VARIANT,
            Temperature.COOL,
            Humidity.ARID,
            Continentalness.span(Continentalness.COAST, Continentalness.FAR_INLAND),
            Erosion.span(Erosion.EROSION_2, Erosion.EROSION_4)
        );
        
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
        
        if (VanillaBackport.COMMON_CONFIG.hasDappledForest.get())
            DAPPLED_FOREST.forEach(event::add);
    }
    
    private static void addBiome(
        List<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> parameters,
        ResourceKey<Biome> biome,
        Placement placement,
        Temperature temperature,
        Humidity humidity,
        Climate.Parameter continentalness,
        Climate.Parameter erosion
    ) {
        for (Weirdness weirdness : placement.getWeirdnesses()) {
            parameters.add(Pair.of(
                Climate.parameters(
                    temperature.parameter(),
                    humidity.parameter(),
                    continentalness,
                    erosion,
                    Depth.SURFACE.parameter(),
                    weirdness.parameter(),
                    0.0F
                ),
                biome
            ));
        }
    }
}
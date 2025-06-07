package com.blackgear.vanillabackport.common.worldgen;

import com.blackgear.platform.common.worldgen.placement.BiomePlacement;
import com.blackgear.platform.common.worldgen.placement.Placement;
import com.blackgear.platform.common.worldgen.placement.parameters.Continentalness;
import com.blackgear.platform.common.worldgen.placement.parameters.Erosion;
import com.blackgear.platform.common.worldgen.placement.parameters.Humidity;
import com.blackgear.platform.common.worldgen.placement.parameters.Temperature;
import com.blackgear.vanillabackport.common.registries.ModBiomes;
import com.blackgear.vanillabackport.core.VanillaBackport;

public class BiomeGeneration {
    public static void bootstrap(BiomePlacement.Event event) {
        if (VanillaBackport.CONFIG.generatePaleGarden.get()) {
            // Peaks
            event.addSurfaceBiome(Placement.PEAK_VARIANT, Temperature.NEUTRAL.parameter(), Humidity.HUMID.parameter(), Continentalness.span(Continentalness.MID_INLAND, Continentalness.FAR_INLAND), Erosion.EROSION_2.parameter(), 0.0F, ModBiomes.PALE_GARDEN);
            event.addSurfaceBiome(Placement.PEAK_VARIANT, Temperature.NEUTRAL.parameter(), Humidity.HUMID.parameter(), Continentalness.FAR_INLAND.parameter(), Erosion.EROSION_3.parameter(), 0.0F, ModBiomes.PALE_GARDEN);

            // High Slice
            event.addSurfaceBiome(Placement.HIGH_SLICE_VARIANT, Temperature.NEUTRAL.parameter(), Humidity.HUMID.parameter(), Continentalness.span(Continentalness.MID_INLAND, Continentalness.FAR_INLAND), Erosion.EROSION_2.parameter(), 0.0F, ModBiomes.PALE_GARDEN);
            event.addSurfaceBiome(Placement.HIGH_SLICE_VARIANT, Temperature.NEUTRAL.parameter(), Humidity.HUMID.parameter(), Continentalness.FAR_INLAND.parameter(), Erosion.EROSION_3.parameter(), 0.0F, ModBiomes.PALE_GARDEN);

            // Mid Slice
            event.addSurfaceBiome(Placement.MID_SLICE_VARIANT, Temperature.NEUTRAL.parameter(), Humidity.HUMID.parameter(), Continentalness.FAR_INLAND.parameter(), Erosion.EROSION_1.parameter(), 0.0F, ModBiomes.PALE_GARDEN);
            event.addSurfaceBiome(Placement.MID_SLICE_VARIANT, Temperature.NEUTRAL.parameter(), Humidity.HUMID.parameter(), Continentalness.FAR_INLAND.parameter(), Erosion.EROSION_2.parameter(), 0.0F, ModBiomes.PALE_GARDEN);
        }
    }
}
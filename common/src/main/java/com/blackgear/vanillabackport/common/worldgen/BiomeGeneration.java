package com.blackgear.vanillabackport.common.worldgen;

import com.blackgear.platform.common.worldgen.placement.BiomePlacement;
import com.blackgear.platform.common.worldgen.placement.Placement;
import com.blackgear.platform.common.worldgen.placement.parameters.*;
import com.blackgear.vanillabackport.common.registries.ModBiomes;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.level.biome.Climate;

public class BiomeGeneration {
    public static void bootstrap(BiomePlacement.Event event) {
        if (VanillaBackport.COMMON_CONFIG.hasPaleGarden.get()) {
            addPaleGarden(event, Placement.PEAK_VARIANT, Continentalness.span(Continentalness.MID_INLAND, Continentalness.FAR_INLAND), Erosion.EROSION_2);
            addPaleGarden(event, Placement.PEAK_VARIANT, Continentalness.FAR_INLAND.parameter(), Erosion.EROSION_3);

            addPaleGarden(event, Placement.HIGH_SLICE_VARIANT, Continentalness.span(Continentalness.MID_INLAND, Continentalness.FAR_INLAND), Erosion.EROSION_2);
            addPaleGarden(event, Placement.HIGH_SLICE_VARIANT, Continentalness.FAR_INLAND.parameter(), Erosion.EROSION_3);

            addPaleGarden(event, Placement.MID_SLICE_VARIANT, Continentalness.FAR_INLAND.parameter(), Erosion.EROSION_1);
            addPaleGarden(event, Placement.MID_SLICE_VARIANT, Continentalness.FAR_INLAND.parameter(), Erosion.EROSION_2);
        }

        event.add(new Pair<>(
            new Climate.ParameterPoint(
                Temperature.FULL_RANGE.parameter(),
                Humidity.FULL_RANGE.parameter(),
                Continentalness.span(Continentalness.COAST, Continentalness.INLAND),
                Erosion.span(Erosion.EROSION_5, Erosion.EROSION_6),
                Depth.UNDERGROUND.parameter(),
                Climate.Parameter.span(-1.1F, -0.85F),
                0L
            ),
            ModBiomes.SULFUR_CAVES
        ));
    }

    private static void addPaleGarden(BiomePlacement.Event event, Placement placement, Climate.Parameter continentalness, Erosion erosion) {
        for (Weirdness weirdness : placement.getWeirdnesses()) {
            event.add(new Pair<>(
                new Climate.ParameterPoint(
                    Temperature.NEUTRAL.parameter(),
                    Humidity.HUMID.parameter(),
                    continentalness,
                    erosion.parameter(),
                    Depth.SURFACE.parameter(),
                    weirdness.parameter(),
                    0L
                ),
                ModBiomes.PALE_GARDEN
            ));
        }
    }
}
package com.blackgear.vanillabackport.common.worldgen.biomes.terrablender;

import com.blackgear.vanillabackport.common.registries.ModBiomes;
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

        if (VanillaBackport.COMMON_CONFIG.hasPaleGarden.get()) addPaleGarden(builder);
        if (VanillaBackport.COMMON_CONFIG.hasSulfurCaves.get()) addSulfurCaves(builder);

        builder.build().forEach(mapper);
    }

    private static void addPaleGarden(VanillaParameterOverlayBuilder builder) {
        addPaleGarden(builder, Placement.PEAK_VARIANT, Continentalness.span(Continentalness.MID_INLAND, Continentalness.FAR_INLAND), Erosion.EROSION_2);
        addPaleGarden(builder, Placement.PEAK_VARIANT, Continentalness.FAR_INLAND.parameter(), Erosion.EROSION_3);

        addPaleGarden(builder, Placement.HIGH_SLICE_VARIANT, Continentalness.span(Continentalness.MID_INLAND, Continentalness.FAR_INLAND), Erosion.EROSION_2);
        addPaleGarden(builder, Placement.HIGH_SLICE_VARIANT, Continentalness.FAR_INLAND.parameter(), Erosion.EROSION_3);

        addPaleGarden(builder, Placement.MID_SLICE_VARIANT, Continentalness.FAR_INLAND.parameter(), Erosion.EROSION_1);
        addPaleGarden(builder, Placement.MID_SLICE_VARIANT, Continentalness.FAR_INLAND.parameter(), Erosion.EROSION_2);
    }

    private static void addSulfurCaves(VanillaParameterOverlayBuilder builder) {
        new ParameterPointListBuilder()
            .temperature(Temperature.FULL_RANGE)
            .humidity(Humidity.FULL_RANGE)
            .continentalness(Continentalness.span(Continentalness.COAST, Continentalness.INLAND))
            .erosion(Erosion.span(Erosion.EROSION_5, Erosion.EROSION_6))
            .depth(Depth.UNDERGROUND)
            .weirdness(Climate.Parameter.span(-1.1F, -0.85F))
            .offset(0L)
            .build()
            .forEach(point -> builder.add(point, ModBiomes.SULFUR_CAVES));
    }

    private static void addPaleGarden(VanillaParameterOverlayBuilder builder, Placement placement, Climate.Parameter continentalness, Erosion erosion) {
        for (Weirdness weirdness : placement.getWeirdnesses()) {
            new ParameterPointListBuilder()
                .temperature(Temperature.NEUTRAL.parameter())
                .humidity(Humidity.HUMID.parameter())
                .continentalness(continentalness)
                .erosion(erosion.parameter())
                .depth(Depth.SURFACE.parameter())
                .weirdness(weirdness.parameter())
                .offset(0L)
                .build()
                .forEach(point -> builder.add(point, ModBiomes.PALE_GARDEN));
        }
    }

    public enum Placement {
        VALLEY(Weirdness.VALLEY),
        LOW_SLICE(Weirdness.LOW_SLICE_NORMAL_DESCENDING, Weirdness.LOW_SLICE_VARIANT_ASCENDING),
        LOW_SLICE_NORMAL(Weirdness.LOW_SLICE_NORMAL_DESCENDING),
        LOW_SLICE_VARIANT(Weirdness.LOW_SLICE_VARIANT_ASCENDING),
        MID_SLICE(Weirdness.MID_SLICE_NORMAL_ASCENDING, Weirdness.MID_SLICE_VARIANT_ASCENDING, Weirdness.MID_SLICE_NORMAL_DESCENDING, Weirdness.MID_SLICE_VARIANT_DESCENDING),
        MID_SLICE_NORMAL(Weirdness.MID_SLICE_NORMAL_ASCENDING, Weirdness.MID_SLICE_NORMAL_DESCENDING),
        MID_SLICE_VARIANT(Weirdness.MID_SLICE_VARIANT_ASCENDING, Weirdness.MID_SLICE_VARIANT_DESCENDING),
        HIGH_SLICE(Weirdness.HIGH_SLICE_NORMAL_ASCENDING, Weirdness.HIGH_SLICE_VARIANT_ASCENDING, Weirdness.HIGH_SLICE_NORMAL_DESCENDING, Weirdness.HIGH_SLICE_VARIANT_DESCENDING),
        HIGH_SLICE_NORMAL(Weirdness.HIGH_SLICE_NORMAL_ASCENDING, Weirdness.HIGH_SLICE_NORMAL_DESCENDING),
        HIGH_SLICE_VARIANT(Weirdness.HIGH_SLICE_VARIANT_ASCENDING, Weirdness.HIGH_SLICE_VARIANT_DESCENDING),
        PEAK(Weirdness.PEAK_NORMAL, Weirdness.PEAK_VARIANT),
        PEAK_NORMAL(Weirdness.PEAK_NORMAL),
        PEAK_VARIANT(Weirdness.PEAK_VARIANT);

        private final Weirdness[] weirdnesses;

        Placement(Weirdness... weirdnesses) {
            this.weirdnesses = weirdnesses;
        }

        public Weirdness[] getWeirdnesses() {
            return this.weirdnesses;
        }
    }
}
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

        if (VanillaBackport.CONFIG.generatePaleGarden.get()) {
            addPaleGarden(builder);
            builder.build().forEach(mapper);
        }
    }

    private static void addPaleGarden(VanillaParameterOverlayBuilder builder) {
        new ParameterPointListBuilder()
            .temperature(Temperature.NEUTRAL)
            .humidity(Humidity.HUMID)
            .continentalness(Continentalness.span(Continentalness.MID_INLAND, Continentalness.FAR_INLAND))
            .erosion(Erosion.EROSION_2)
            .depth(Depth.SURFACE)
            .weirdness(Weirdness.HIGH_SLICE_VARIANT_ASCENDING, Weirdness.HIGH_SLICE_VARIANT_DESCENDING, Weirdness.PEAK_VARIANT)
            .build()
            .forEach(point -> builder.add(point, ModBiomes.PALE_GARDEN));

        new ParameterPointListBuilder()
            .temperature(Temperature.NEUTRAL)
            .humidity(Humidity.HUMID)
            .continentalness(Continentalness.FAR_INLAND)
            .erosion(Erosion.EROSION_3)
            .depth(Depth.SURFACE)
            .weirdness(Weirdness.HIGH_SLICE_VARIANT_ASCENDING, Weirdness.HIGH_SLICE_VARIANT_DESCENDING, Weirdness.PEAK_VARIANT)
            .build()
            .forEach(point -> builder.add(point, ModBiomes.PALE_GARDEN));

        new ParameterPointListBuilder()
            .temperature(Temperature.NEUTRAL)
            .humidity(Humidity.HUMID)
            .continentalness(Continentalness.FAR_INLAND)
            .erosion(Erosion.EROSION_1, Erosion.EROSION_2)
            .depth(Depth.SURFACE)
            .weirdness(Weirdness.MID_SLICE_VARIANT_ASCENDING, Weirdness.MID_SLICE_VARIANT_DESCENDING)
            .build()
            .forEach(point -> builder.add(point, ModBiomes.PALE_GARDEN));
    }
}
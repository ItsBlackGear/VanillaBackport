package com.blackgear.vanillabackport.common.registries;

import com.blackgear.platform.common.worldgen.WorldGenRegistry;
import com.blackgear.vanillabackport.common.worldgen.biomes.TheGardenAwakensBiomes;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class ModBiomes {
    public static final WorldGenRegistry<Biome> BIOMES = WorldGenRegistry.of(Registries.BIOME, VanillaBackport.MOD_ID);

    public static final ResourceKey<Biome> PALE_GARDEN = BIOMES.create("pale_garden");

    public static void bootstrap(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> features = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> carvers = context.lookup(Registries.CONFIGURED_CARVER);

        context.register(PALE_GARDEN, TheGardenAwakensBiomes.paleGarden(features, carvers));
    }
}
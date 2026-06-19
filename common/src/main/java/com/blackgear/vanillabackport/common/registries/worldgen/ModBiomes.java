package com.blackgear.vanillabackport.common.registries.worldgen;

import com.blackgear.platform.core.api.registrar.bootstrap.BootstrapRegistrar;
import com.blackgear.vanillabackport.common.worldgen.biomes.ChaosCubedBiomes;
import com.blackgear.vanillabackport.common.worldgen.biomes.TheGardenAwakensBiomes;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.function.BiFunction;

public class ModBiomes {
    public static final BootstrapRegistrar<Biome> REGISTRIES = BootstrapRegistrar.create(Registries.BIOME, VanillaBackport.NAMESPACE);
    
    public static final ResourceKey<Biome> PALE_GARDEN = register("pale_garden", TheGardenAwakensBiomes::paleGarden);
    public static final ResourceKey<Biome> SULFUR_CAVES = register("sulfur_caves", ChaosCubedBiomes::sulfurCaves);
    
    private static ResourceKey<Biome> register(String key, BiFunction<HolderGetter<PlacedFeature>, HolderGetter<ConfiguredWorldCarver<?>>, Biome> factory) {
        return REGISTRIES.register(key, context -> factory.apply(context.lookup(Registries.PLACED_FEATURE), context.lookup(Registries.CONFIGURED_CARVER)));
    }
}
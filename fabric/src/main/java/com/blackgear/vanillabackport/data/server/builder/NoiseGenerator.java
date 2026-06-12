package com.blackgear.vanillabackport.data.server.builder;

import com.blackgear.vanillabackport.common.registries.worldgen.ModNoises;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.synth.NormalNoise.NoiseParameters;

import java.util.concurrent.CompletableFuture;

public class NoiseGenerator extends FabricDynamicRegistryProvider {
    public NoiseGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        this.add(provider, entries, ModNoises.SULFUR_CAVE_GRADIENT);
    }

    private void add(HolderLookup.Provider provider, Entries entries, ResourceKey<NoiseParameters> key) {
        final HolderLookup.RegistryLookup<NoiseParameters> registry = provider.lookupOrThrow(Registries.NOISE);
        entries.add(key, registry.getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "worldgen/noise";
    }
}
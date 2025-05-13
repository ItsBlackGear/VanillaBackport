package com.blackgear.vanillabackport.data.server.builder;

import com.blackgear.vanillabackport.common.registries.ModBiomes;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;

import java.util.concurrent.CompletableFuture;

public class BiomeGenerator extends FabricDynamicRegistryProvider {
    public BiomeGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        this.add(provider, entries, ModBiomes.PALE_GARDEN);
    }

    private void add(HolderLookup.Provider provider, Entries entries, ResourceKey<Biome> key) {
        final HolderLookup.RegistryLookup<Biome> registry = provider.lookupOrThrow(Registries.BIOME);
        entries.add(key, registry.getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "worldgen/biome";
    }
}
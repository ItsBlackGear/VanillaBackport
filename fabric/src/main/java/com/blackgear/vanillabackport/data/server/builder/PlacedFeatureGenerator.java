package com.blackgear.vanillabackport.data.server.builder;

import com.blackgear.vanillabackport.common.worldgen.placements.TheGardenAwakensPlacements;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.concurrent.CompletableFuture;

public class PlacedFeatureGenerator extends FabricDynamicRegistryProvider {
    public PlacedFeatureGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        this.add(provider, entries, TheGardenAwakensPlacements.PALE_OAK_CHECKED);
        this.add(provider, entries, TheGardenAwakensPlacements.PALE_OAK_CREAKING_CHECKED);

        this.add(provider, entries, TheGardenAwakensPlacements.FLOWER_PALE_GARDEN);
        this.add(provider, entries, TheGardenAwakensPlacements.PALE_GARDEN_VEGETATION);
        this.add(provider, entries, TheGardenAwakensPlacements.PALE_GARDEN_FLOWERS);
        this.add(provider, entries, TheGardenAwakensPlacements.PALE_MOSS_PATCH);
    }

    private void add(HolderLookup.Provider provider, Entries entries, ResourceKey<PlacedFeature> key) {
        final HolderLookup.RegistryLookup<PlacedFeature> registry = provider.lookupOrThrow(Registries.PLACED_FEATURE);
        entries.add(key, registry.getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "worldgen/placed_feature";
    }
}
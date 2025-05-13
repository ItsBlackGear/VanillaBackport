package com.blackgear.vanillabackport.data.server.builder;

import com.blackgear.vanillabackport.common.worldgen.features.TheGardenAwakensFeatures;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricDynamicRegistryProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

import java.util.concurrent.CompletableFuture;

public class ConfiguredFeatureGenerator extends FabricDynamicRegistryProvider {
    public ConfiguredFeatureGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registriesFuture) {
        super(output, registriesFuture);
    }

    @Override
    protected void configure(HolderLookup.Provider provider, Entries entries) {
        this.add(provider, entries, TheGardenAwakensFeatures.PALE_OAK);
        this.add(provider, entries, TheGardenAwakensFeatures.PALE_OAK_BONEMEAL);
        this.add(provider, entries, TheGardenAwakensFeatures.PALE_OAK_CREAKING);
        this.add(provider, entries, TheGardenAwakensFeatures.PALE_OAK_CREAKING_BONEMEAL);

        this.add(provider, entries, TheGardenAwakensFeatures.FLOWER_PALE_GARDEN);
        this.add(provider, entries, TheGardenAwakensFeatures.PALE_GARDEN_FLOWERS);
        this.add(provider, entries, TheGardenAwakensFeatures.PALE_GARDEN_VEGETATION);
        this.add(provider, entries, TheGardenAwakensFeatures.PALE_MOSS_VEGETATION);
        this.add(provider, entries, TheGardenAwakensFeatures.PALE_MOSS_PATCH);
        this.add(provider, entries, TheGardenAwakensFeatures.PALE_MOSS_PATCH_BONEMEAL);
    }

    private void add(HolderLookup.Provider provider, Entries entries, ResourceKey<ConfiguredFeature<?, ?>> key) {
        final HolderLookup.RegistryLookup<ConfiguredFeature<?, ?>> registry = provider.lookupOrThrow(Registries.CONFIGURED_FEATURE);
        entries.add(key, registry.getOrThrow(key).value());
    }

    @Override
    public String getName() {
        return "worldgen/configured_feature";
    }
}
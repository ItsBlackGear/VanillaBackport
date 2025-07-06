package com.blackgear.vanillabackport.data.server.builder;

import com.blackgear.vanillabackport.common.worldgen.features.SpringToLifeFeatures;
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
        this.add(provider, entries, SpringToLifeFeatures.EMPTY);

        // The Garden Awakens
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

        // Spring to Life
        this.add(provider, entries, SpringToLifeFeatures.PATCH_BUSH);
        this.add(provider, entries, SpringToLifeFeatures.PATCH_FIREFLY_BUSH);

        this.add(provider, entries, SpringToLifeFeatures.WILDFLOWERS_BIRCH_FOREST);
        this.add(provider, entries, SpringToLifeFeatures.WILDFLOWERS_MEADOW);

        this.add(provider, entries, SpringToLifeFeatures.PATCH_DRY_GRASS);

        this.add(provider, entries, SpringToLifeFeatures.PATCH_LEAF_LITTER);

        this.add(provider, entries, SpringToLifeFeatures.FALLEN_OAK_TREE);
        this.add(provider, entries, SpringToLifeFeatures.FALLEN_BIRCH_TREE);
        this.add(provider, entries, SpringToLifeFeatures.FALLEN_SUPER_BIRCH_TREE);
        this.add(provider, entries, SpringToLifeFeatures.FALLEN_JUNGLE_TREE);
        this.add(provider, entries, SpringToLifeFeatures.FALLEN_SPRUCE_TREE);

        this.add(provider, entries, SpringToLifeFeatures.OAK_BEES_0002_LEAF_LITTER);
        this.add(provider, entries, SpringToLifeFeatures.BIRCH_BEES_0002_LEAF_LITTER);
        this.add(provider, entries, SpringToLifeFeatures.FANCY_OAK_BEES_0002_LEAF_LITTER);

        this.add(provider, entries, SpringToLifeFeatures.OAK_LEAF_LITTER);
        this.add(provider, entries, SpringToLifeFeatures.DARK_OAK_LEAF_LITTER);
        this.add(provider, entries, SpringToLifeFeatures.BIRCH_LEAF_LITTER);
        this.add(provider, entries, SpringToLifeFeatures.FANCY_OAK_LEAF_LITTER);

        this.add(provider, entries, SpringToLifeFeatures.DARK_FOREST_VEGETATION);
        this.add(provider, entries, SpringToLifeFeatures.TREES_BIRCH_AND_OAK_LEAF_LITTER);
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
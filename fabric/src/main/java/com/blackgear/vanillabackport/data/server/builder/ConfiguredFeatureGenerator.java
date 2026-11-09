package com.blackgear.vanillabackport.data.server.builder;

import com.blackgear.vanillabackport.common.worldgen.features.*;
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
        // The Garden Awakens
        this.add(provider, entries, TheGardenAwakensFeatures.PALE_OAK);
        this.add(provider, entries, TheGardenAwakensFeatures.PALE_OAK_BONEMEAL);
        this.add(provider, entries, TheGardenAwakensFeatures.PALE_OAK_CREAKING);

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
        this.add(provider, entries, SpringToLifeFeatures.LEAF_LITTER_SPARSE);
        this.add(provider, entries, SpringToLifeFeatures.LEAF_LITTER_THICK);

        this.add(provider, entries, SpringToLifeFeatures.CACTUS_FLOWER);

        this.add(provider, entries, SpringToLifeFeatures.FALLEN_OAK_TREE);
        this.add(provider, entries, SpringToLifeFeatures.FALLEN_BIRCH_TREE);
        this.add(provider, entries, SpringToLifeFeatures.FALLEN_SUPER_BIRCH_TREE);
        this.add(provider, entries, SpringToLifeFeatures.FALLEN_JUNGLE_TREE);
        this.add(provider, entries, SpringToLifeFeatures.FALLEN_SPRUCE_TREE);

        // Chaos Cubed
        this.add(provider, entries, ChaosCubedFeatures.SULFUR_POOL);
        this.add(provider, entries, ChaosCubedFeatures.SULFUR_SPRING);
        this.add(provider, entries, ChaosCubedFeatures.ROOTED_SULFUR_SPRING);
        this.add(provider, entries, ChaosCubedFeatures.SULFUR_SPIKE_CLUSTER);
        this.add(provider, entries, ChaosCubedFeatures.SULFUR_SPIKE);
        
        // Wilderness Bound
        this.add(provider, entries, WildernessBoundFeatures.RED_POPLAR);
        this.add(provider, entries, WildernessBoundFeatures.ORANGE_POPLAR);
        this.add(provider, entries, WildernessBoundFeatures.YELLOW_POPLAR);
        this.add(provider, entries, WildernessBoundFeatures.FALLEN_POPLAR_TREE);
        this.add(provider, entries, WildernessBoundFeatures.RED_SHRUB);
        this.add(provider, entries, WildernessBoundFeatures.BROWN_MUSHROOM);
        this.add(provider, entries, WildernessBoundFeatures.TREES_DAPPLED_FOREST);
        
        // Miscellaneous
        this.add(provider, entries, MiscellaneousFeatures.ICE_CAVE_ORE_COAL);
        this.add(provider, entries, MiscellaneousFeatures.ICE_CAVE_ORE_COAL_BURIED);
        this.add(provider, entries, MiscellaneousFeatures.ICE_CAVE_ORE_COPPER);
        this.add(provider, entries, MiscellaneousFeatures.ICE_CAVE_ORE_DIAMOND_SMALL);
        this.add(provider, entries, MiscellaneousFeatures.ICE_CAVE_ORE_DIAMOND_MEDIUM);
        this.add(provider, entries, MiscellaneousFeatures.ICE_CAVE_ORE_DIAMOND_LARGE);
        this.add(provider, entries, MiscellaneousFeatures.ICE_CAVE_ORE_DIAMOND_BURIED);
        this.add(provider, entries, MiscellaneousFeatures.ICE_CAVE_ORE_IRON);
        this.add(provider, entries, MiscellaneousFeatures.ICE_CAVE_ORE_IRON_SMALL);
        this.add(provider, entries, MiscellaneousFeatures.ICE_CAVE_ORE_GOLD_BURIED);
        this.add(provider, entries, MiscellaneousFeatures.ICE_CAVE_ORE_LAPIS);
        this.add(provider, entries, MiscellaneousFeatures.ICE_CAVE_ORE_LAPIS_BURIED);
        this.add(provider, entries, MiscellaneousFeatures.ICE_CAVE_ORE_REDSTONE);
        
        this.add(provider, entries, MiscellaneousFeatures.ICE_CAVE_ORE_GRAVEL);
        this.add(provider, entries, MiscellaneousFeatures.ICE_CAVE_ORE_DIORITE);
        this.add(provider, entries, MiscellaneousFeatures.ICE_CAVE_ORE_ANDESITE);
        
        this.add(provider, entries, MiscellaneousFeatures.LARGE_ICICLE);
        this.add(provider, entries, MiscellaneousFeatures.SNOW_LAYERS);
        this.add(provider, entries, MiscellaneousFeatures.ICE_CRYSTAL);
        this.add(provider, entries, MiscellaneousFeatures.ICICLE_CLUSTER);
        this.add(provider, entries, MiscellaneousFeatures.ICE_CREVASSE);
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
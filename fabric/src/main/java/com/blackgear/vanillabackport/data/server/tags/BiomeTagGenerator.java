package com.blackgear.vanillabackport.data.server.tags;

import com.blackgear.vanillabackport.common.registries.ModBiomes;
import com.blackgear.vanillabackport.core.data.tags.ModBiomeTags;
import com.blackgear.vanillabackport.core.data.tags.fabric.FabricBiomeTags;
import com.blackgear.vanillabackport.core.data.tags.forge.ForgeBiomeTags;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;

import java.util.concurrent.CompletableFuture;

public class BiomeTagGenerator extends FabricTagProvider<Biome> {
    public BiomeTagGenerator(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, Registries.BIOME, provider);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        this.addEntityPlacementTags(provider);
        this.addFeaturePlacementTags(provider);

        this.getOrCreateTagBuilder(BiomeTags.IS_FOREST)
            .add(ModBiomes.PALE_GARDEN);

        this.getOrCreateTagBuilder(BiomeTags.IS_OVERWORLD)
            .add(ModBiomes.PALE_GARDEN);

        this.getOrCreateTagBuilder(BiomeTags.HAS_WOODLAND_MANSION)
            .add(ModBiomes.PALE_GARDEN);

        this.getOrCreateTagBuilder(BiomeTags.STRONGHOLD_BIASED_TO)
            .add(ModBiomes.PALE_GARDEN);

        this.getOrCreateTagBuilder(ModBiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS)
            .add(Biomes.SNOWY_PLAINS)
            .add(Biomes.ICE_SPIKES)
            .add(Biomes.FROZEN_PEAKS)
            .add(Biomes.JAGGED_PEAKS)
            .add(Biomes.SNOWY_SLOPES)
            .add(Biomes.FROZEN_OCEAN)
            .add(Biomes.DEEP_FROZEN_OCEAN)
            .add(Biomes.GROVE)
            .add(Biomes.DEEP_DARK)
            .add(Biomes.FROZEN_RIVER)
            .add(Biomes.SNOWY_TAIGA)
            .add(Biomes.SNOWY_BEACH)
            .add(Biomes.COLD_OCEAN)
            .add(Biomes.DEEP_COLD_OCEAN)
            .add(Biomes.OLD_GROWTH_PINE_TAIGA)
            .add(Biomes.OLD_GROWTH_SPRUCE_TAIGA)
            .add(Biomes.TAIGA)
            .add(Biomes.WINDSWEPT_FOREST)
            .add(Biomes.WINDSWEPT_GRAVELLY_HILLS)
            .add(Biomes.STONY_PEAKS)
            .forceAddTag(BiomeTags.IS_END)
            .addOptionalTag(ForgeBiomeTags.IS_COLD)
            .addOptionalTag(ForgeBiomeTags.IS_SNOWY)
            .addOptionalTag(FabricBiomeTags.CLIMATE_COLD)
            .addOptionalTag(FabricBiomeTags.SNOWY)
            .addOptionalTag(FabricBiomeTags.ICY);

        this.getOrCreateTagBuilder(ModBiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS)
            .add(Biomes.DESERT)
            .add(Biomes.WARM_OCEAN)
            .add(Biomes.MANGROVE_SWAMP)
            .add(Biomes.DEEP_LUKEWARM_OCEAN)
            .add(Biomes.LUKEWARM_OCEAN)
            .forceAddTag(BiomeTags.IS_NETHER)
            .forceAddTag(BiomeTags.IS_SAVANNA)
            .forceAddTag(BiomeTags.IS_JUNGLE)
            .forceAddTag(BiomeTags.IS_BADLANDS)
            .addOptionalTag(ForgeBiomeTags.IS_HOT)
            .addOptionalTag(FabricBiomeTags.CLIMATE_HOT);
    }

    protected void addEntityPlacementTags(HolderLookup.Provider provider) {
        this.getOrCreateTagBuilder(ModBiomeTags.SPAWNS_CAMELS)
            .addOptionalTag(FabricBiomeTags.DESERT)
            .addOptionalTag(ForgeBiomeTags.IS_DESERT);
    }

    protected void addFeaturePlacementTags(HolderLookup.Provider provider) {
        this.getOrCreateTagBuilder(ModBiomeTags.SPAWNS_BUSHES)
            .forceAddTag(BiomeTags.IS_HILL)
            .forceAddTag(BiomeTags.IS_RIVER)
            .add(Biomes.FOREST)
            .add(Biomes.BIRCH_FOREST)
            .add(Biomes.OLD_GROWTH_BIRCH_FOREST)
            .addOptionalTag(ForgeBiomeTags.IS_PLAINS)
            .addOptionalTag(FabricBiomeTags.PLAINS);

        this.getOrCreateTagBuilder(ModBiomeTags.SPAWNS_FIREFLY_BUSHES)
            .forceAddTag(BiomeTags.IS_BEACH)
            .forceAddTag(BiomeTags.IS_RIVER)
            .forceAddTag(BiomeTags.IS_TAIGA)
            .forceAddTag(BiomeTags.IS_FOREST)
            .forceAddTag(BiomeTags.IS_OCEAN)
            .forceAddTag(BiomeTags.IS_SAVANNA)
            .forceAddTag(BiomeTags.IS_HILL)
            .forceAddTag(BiomeTags.IS_JUNGLE)
            .forceAddTag(BiomeTags.IS_BADLANDS)
            .addOptionalTag(ForgeBiomeTags.IS_PLAINS)
            .addOptionalTag(FabricBiomeTags.PLAINS)
            .addOptionalTag(ForgeBiomeTags.IS_MUSHROOM)
            .addOptionalTag(FabricBiomeTags.MUSHROOM)
            .add(Biomes.MANGROVE_SWAMP);

        this.getOrCreateTagBuilder(ModBiomeTags.SPAWNS_FIREFLY_BUSHES_SWAMP)
            .addOptionalTag(ForgeBiomeTags.IS_SWAMP)
            .addOptionalTag(FabricBiomeTags.SWAMP);

        this.getOrCreateTagBuilder(ModBiomeTags.SPAWNS_WILDFLOWERS)
            .add(Biomes.BIRCH_FOREST)
            .add(Biomes.OLD_GROWTH_BIRCH_FOREST)
            .addOptionalTag(FabricBiomeTags.BIRCH_FOREST);

        this.getOrCreateTagBuilder(ModBiomeTags.SPAWNS_NOISE_BASED_WILDFLOWERS)
            .add(Biomes.MEADOW);

        this.getOrCreateTagBuilder(ModBiomeTags.SPAWNS_DRY_GRASS)
            .addOptionalTag(FabricBiomeTags.DESERT)
            .addOptionalTag(ForgeBiomeTags.IS_DESERT);

        this.getOrCreateTagBuilder(ModBiomeTags.SPAWNS_DRY_GRASS_RARELY)
            .forceAddTag(BiomeTags.IS_BADLANDS);

        this.getOrCreateTagBuilder(ModBiomeTags.SPAWNS_FALLEN_OAK_TREES)
            .add(Biomes.DARK_FOREST)
            .forceAddTag(BiomeTags.IS_BADLANDS)
            .forceAddTag(BiomeTags.IS_SAVANNA)
            .forceAddTag(BiomeTags.IS_HILL)
            .add(Biomes.FOREST)
            .add(Biomes.FLOWER_FOREST)
            .addOptionalTag(FabricBiomeTags.PLAINS)
            .addOptionalTag(ForgeBiomeTags.IS_PLAINS);

        this.getOrCreateTagBuilder(ModBiomeTags.SPAWNS_FALLEN_BIRCH_TREES_RARELY)
            .forceAddTag(BiomeTags.IS_FOREST);

        this.getOrCreateTagBuilder(ModBiomeTags.SPAWNS_FALLEN_BIRCH_TREES)
            .add(Biomes.BIRCH_FOREST)
            .add(Biomes.OLD_GROWTH_BIRCH_FOREST)
            .addOptionalTag(FabricBiomeTags.BIRCH_FOREST);

        this.getOrCreateTagBuilder(ModBiomeTags.SPAWNS_FALLEN_SUPER_BIRCH_TREES)
            .add(Biomes.OLD_GROWTH_BIRCH_FOREST);

        this.getOrCreateTagBuilder(ModBiomeTags.SPAWNS_FALLEN_JUNGLE_TREES)
            .forceAddTag(BiomeTags.IS_JUNGLE);

        this.getOrCreateTagBuilder(ModBiomeTags.SPAWNS_FALLEN_SPRUCE_TREES)
            .forceAddTag(BiomeTags.IS_TAIGA);

        this.getOrCreateTagBuilder(ModBiomeTags.SPAWNS_FALLEN_SPRUCE_TREES_RARELY)
            .forceAddTag(BiomeTags.IS_HILL);

        this.getOrCreateTagBuilder(ModBiomeTags.SPAWNS_LEAF_LITTER_PATCHES)
            .add(Biomes.DARK_FOREST);
    }
}
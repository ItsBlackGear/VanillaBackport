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
        this.getOrCreateTagBuilder(BiomeTags.IS_FOREST)
            .add(ModBiomes.PALE_GARDEN);
        this.getOrCreateTagBuilder(BiomeTags.HAS_WOODLAND_MANSION)
            .add(ModBiomes.PALE_GARDEN);
        this.getOrCreateTagBuilder(BiomeTags.STRONGHOLD_BIASED_TO)
            .add(ModBiomes.PALE_GARDEN);
        this.getOrCreateTagBuilder(BiomeTags.IS_OVERWORLD)
            .add(ModBiomes.PALE_GARDEN);

        this.getOrCreateTagBuilder(ModBiomeTags.SPAWNS_WARM_VARIANT_FARM_ANIMALS)
            .add(
                Biomes.DESERT,
                Biomes.WARM_OCEAN,
                Biomes.MANGROVE_SWAMP,
                Biomes.DEEP_LUKEWARM_OCEAN,
                Biomes.LUKEWARM_OCEAN
            )
            .forceAddTag(BiomeTags.IS_NETHER)
            .forceAddTag(BiomeTags.IS_SAVANNA)
            .forceAddTag(BiomeTags.IS_JUNGLE)
            .forceAddTag(BiomeTags.IS_BADLANDS)
            .addOptionalTag(ForgeBiomeTags.IS_HOT)
            .addOptionalTag(FabricBiomeTags.CLIMATE_HOT);

        this.getOrCreateTagBuilder(ModBiomeTags.SPAWNS_COLD_VARIANT_FARM_ANIMALS)
            .add(
                Biomes.SNOWY_PLAINS,
                Biomes.ICE_SPIKES,
                Biomes.FROZEN_PEAKS,
                Biomes.JAGGED_PEAKS,
                Biomes.SNOWY_SLOPES,
                Biomes.FROZEN_OCEAN,
                Biomes.DEEP_FROZEN_OCEAN,
                Biomes.GROVE,
                Biomes.DEEP_DARK,
                Biomes.FROZEN_RIVER,
                Biomes.SNOWY_TAIGA,
                Biomes.SNOWY_BEACH,
                Biomes.COLD_OCEAN,
                Biomes.DEEP_COLD_OCEAN,
                Biomes.OLD_GROWTH_PINE_TAIGA,
                Biomes.OLD_GROWTH_SPRUCE_TAIGA,
                Biomes.TAIGA,
                Biomes.WINDSWEPT_FOREST,
                Biomes.WINDSWEPT_GRAVELLY_HILLS,
                Biomes.STONY_PEAKS
            )
            .forceAddTag(BiomeTags.IS_END)
            .addOptionalTag(ForgeBiomeTags.IS_COLD)
            .addOptionalTag(ForgeBiomeTags.IS_SNOWY)
            .addOptionalTag(FabricBiomeTags.CLIMATE_COLD)
            .addOptionalTag(FabricBiomeTags.SNOWY)
            .addOptionalTag(FabricBiomeTags.ICY);

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
    }
}
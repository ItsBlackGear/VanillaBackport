package com.blackgear.vanillabackport.data.server.tags;

import com.blackgear.vanillabackport.common.registries.ModBiomes;
import com.blackgear.vanillabackport.core.data.tags.ModBiomeTags;
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
            .forceAddTag(BiomeTags.IS_BADLANDS);

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
            .forceAddTag(BiomeTags.IS_END);
    }
}
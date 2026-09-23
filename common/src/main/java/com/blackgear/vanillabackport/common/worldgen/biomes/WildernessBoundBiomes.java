package com.blackgear.vanillabackport.common.worldgen.biomes;

import com.blackgear.vanillabackport.common.worldgen.placements.WildernessBoundPlacements;
import com.blackgear.vanillabackport.core.mixin.common.access.OverworldBiomesAccessor;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class WildernessBoundBiomes {
    public static Biome dappledForest(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
        BiomeDefaultFeatures.farmAnimals(mobs);
        BiomeDefaultFeatures.commonSpawns(mobs);
        mobs.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, 4, 2, 4));
        mobs.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.FOX, 4, 2, 4));
        
        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(features, carvers);
        OverworldBiomesAccessor.callGlobalOverworldGeneration(generation);
        BiomeDefaultFeatures.addDefaultOres(generation);
        BiomeDefaultFeatures.addDefaultSoftDisks(generation);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WildernessBoundPlacements.TREES_DAPPLED_FOREST);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WildernessBoundPlacements.BROWN_MUSHROOM_DAPPLED_FOREST);
        generation.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, WildernessBoundPlacements.PATCH_RED_SHRUB);
        BiomeDefaultFeatures.addForestGrass(generation);
        
        return new Biome.BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(0.6F)
            .downfall(0.6F)
            .specialEffects(
                new BiomeSpecialEffects.Builder()
                    .waterColor(3625300)
                    .waterFogColor(3625300)
                    .skyColor(8168447)
                    .fogColor(13424866)
                    .grassColorOverride(14641191)
                    .foliageColorOverride(15109680)
                    .backgroundMusic(Musics.createGameMusic(SoundEvents.MUSIC_BIOME_FOREST))
                    .build()
            )
            .mobSpawnSettings(mobs.build())
            .generationSettings(generation.build())
            .build();
    }
}

package com.blackgear.vanillabackport.common.worldgen.biomes;

import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import com.blackgear.vanillabackport.common.worldgen.placements.MiscellaneousPlacements;
import net.minecraft.core.HolderGetter;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.placement.OrePlacements;
import net.minecraft.sounds.Musics;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class MiscellaneousBiomes {
    public static int calculateSkyColor(float temperature) {
        float temp = temperature / 3.0F;
        temp = Mth.clamp(temp, -1.0F, 1.0F);
        return Mth.hsvToRgb(0.62222224F - temp * 0.05F, 0.5F + temp * 0.1F, 1.0F);
    }
    
    public static Biome iceCaves(HolderGetter<PlacedFeature> features, HolderGetter<ConfiguredWorldCarver<?>> carvers) {
        MobSpawnSettings.Builder mobs = new MobSpawnSettings.Builder();
        mobs.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.CREEPER, 100, 4, 4));
        mobs.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SKELETON, 90, 4, 4));
        mobs.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.STRAY, 10, 4, 4));
        mobs.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(ModEntityTypes.FROSTBITE.get(), 10, 4, 4));
        mobs.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SLIME, 100, 4, 4));
        mobs.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.SPIDER, 100, 4, 4));
        mobs.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 50, 4, 4));
        mobs.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ENDERMAN, 10, 1, 4));
        mobs.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.WITCH, 5, 1, 1));
        mobs.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE_VILLAGER, 5, 1, 1));
        mobs.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.GLOW_SQUID, 10, 2, 4));
        mobs.addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(EntityType.BAT, 10, 8, 8));
        
        BiomeGenerationSettings.Builder generation = new BiomeGenerationSettings.Builder(features, carvers);
        BiomeDefaultFeatures.addDefaultCarversAndLakes(generation);
        BiomeDefaultFeatures.addDefaultCrystalFormations(generation);
        BiomeDefaultFeatures.addDefaultMonsterRoom(generation);
        generation.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, OrePlacements.ORE_TUFF);
        BiomeDefaultFeatures.addDefaultSprings(generation);
        BiomeDefaultFeatures.addSurfaceFreezing(generation);
        BiomeDefaultFeatures.addDefaultSoftDisks(generation);
        addIceCaveOres(generation);
        addIceCaveUndergroundVariety(generation);
        addIceCavesFeatures(generation);
        
        return new Biome.BiomeBuilder()
            .hasPrecipitation(true)
            .temperature(0.0F)
            .downfall(0.0F)
            .specialEffects(
                new BiomeSpecialEffects.Builder()
                    .waterColor(-15444581)
                    .waterFogColor(-15444581)
                    .skyColor(calculateSkyColor(0.0F))
                    .fogColor(-15444581)
                    .backgroundMusic(Musics.createGameMusic(ModSoundEvents.MUSIC_BIOME_SULFUR_CAVES))
                    .build()
            )
            .mobSpawnSettings(mobs.build())
            .generationSettings(generation.build())
            .build();
    }
    
    private static void addIceCaveOres(BiomeGenerationSettings.Builder builder) {
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscellaneousPlacements.ICE_CAVE_ORE_COAL_UPPER);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscellaneousPlacements.ICE_CAVE_ORE_COAL_LOWER);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscellaneousPlacements.ICE_CAVE_ORE_IRON_UPPER);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscellaneousPlacements.ICE_CAVE_ORE_IRON_MIDDLE);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscellaneousPlacements.ICE_CAVE_ORE_IRON_SMALL);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscellaneousPlacements.ICE_CAVE_ORE_GOLD);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscellaneousPlacements.ICE_CAVE_ORE_GOLD_LOWER);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscellaneousPlacements.ICE_CAVE_ORE_REDSTONE);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscellaneousPlacements.ICE_CAVE_ORE_REDSTONE_LOWER);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscellaneousPlacements.ICE_CAVE_ORE_DIAMOND);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscellaneousPlacements.ICE_CAVE_ORE_DIAMOND_MEDIUM);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscellaneousPlacements.ICE_CAVE_ORE_DIAMOND_LARGE);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscellaneousPlacements.ICE_CAVE_ORE_DIAMOND_BURIED);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscellaneousPlacements.ICE_CAVE_ORE_LAPIS);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscellaneousPlacements.ICE_CAVE_ORE_LAPIS_BURIED);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscellaneousPlacements.ICE_CAVE_ORE_COPPER);
    }
    
    public static void addIceCaveUndergroundVariety(BiomeGenerationSettings.Builder builder) {
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscellaneousPlacements.ICE_CAVE_ORE_GRAVEL);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscellaneousPlacements.ICE_CAVE_ORE_DIORITE_UPPER);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscellaneousPlacements.ICE_CAVE_ORE_DIORITE_LOWER);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscellaneousPlacements.ICE_CAVE_ORE_ANDESITE_UPPER);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, MiscellaneousPlacements.ICE_CAVE_ORE_ANDESITE_LOWER);
    }
    
    public static void addIceCavesFeatures(BiomeGenerationSettings.Builder builder) {
        builder.addFeature(GenerationStep.Decoration.LOCAL_MODIFICATIONS, MiscellaneousPlacements.LARGE_ICICLE);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, MiscellaneousPlacements.SNOW_LAYERS);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, MiscellaneousPlacements.ICE_CRYSTALS);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, MiscellaneousPlacements.ICICLE_CLUSTER);
        builder.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, MiscellaneousPlacements.ICE_CREVASSE);
    }
}

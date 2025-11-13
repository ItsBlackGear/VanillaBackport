package com.blackgear.vanillabackport.common.worldgen.generation;

import com.blackgear.platform.common.worldgen.modifier.BiomeContext;
import com.blackgear.platform.common.worldgen.modifier.BiomeWriter;
import com.blackgear.vanillabackport.common.registries.ModEntities;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;

public class ArmoredPawsFeatureManager extends FeatureManager {
    public ArmoredPawsFeatureManager(BiomeContext context, BiomeWriter writer) {
        super(context, writer);
    }

    @Override
    public void bootstrap() {
        this.addIf(VanillaBackport.COMMON_CONFIG.hasArmadillos.get(), (context, writer) -> {
            this.getOrCreateBiomeBuilder(BiomeTags.IS_SAVANNA)
                .add(() -> writer.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(ModEntities.ARMADILLO.get(), 10, 2, 3)));

            this.getOrCreateBiomeBuilder(BiomeTags.IS_BADLANDS)
                .add(() -> writer.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(ModEntities.ARMADILLO.get(), 6, 1, 2)));
        });

        this.addIf(VanillaBackport.COMMON_CONFIG.updatedWolfSpawns.get(), (context, writer) -> {
            this.getOrCreateBiomeBuilder(Biomes.SPARSE_JUNGLE)
                .add(() -> writer.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 8, 2, 4)));

            this.getOrCreateBiomeBuilder(BiomeTags.IS_SAVANNA)
                .add(() -> writer.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 8, 4, 8)));

            this.getOrCreateBiomeBuilder(BiomeTags.IS_BADLANDS)
                .add(() -> writer.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 2, 4, 8)));

            this.getOrCreateBiomeBuilder(Biomes.GROVE)
                .add(() -> writer.replaceSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.WOLF, 1, 1, 1)));
        });
    }
}
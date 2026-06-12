package com.blackgear.vanillabackport.common.worldgen;

import com.blackgear.platform.common.worldgen.modifier.BiomeContext;
import com.blackgear.platform.common.worldgen.modifier.BiomeWriter;
import com.blackgear.platform.common.worldgen.modifier.FeatureManager;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import com.blackgear.vanillabackport.common.registries.worldgen.ModBiomes;
import com.blackgear.vanillabackport.common.worldgen.placements.ChaosCubedPlacements;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;

public class ChaosCubedFeatureManager extends FeatureManager {
    public ChaosCubedFeatureManager(BiomeContext context, BiomeWriter writer) {
        super(context, writer);
    }

    @Override
    public void bootstrap() {
        this.addIf(VanillaBackport.COMMON_CONFIG.hasSulfurSprings.get(), (context, writer) -> {
            this.getOrCreateBiomeBuilder(ModBiomes.SULFUR_CAVES)
                .add(() -> writer.addFeature(GenerationStep.Decoration.LAKES, ChaosCubedPlacements.ROOTED_SULFUR_SPRING));
        });

        this.addIf(VanillaBackport.COMMON_CONFIG.hasSulfurCubes.get(), (context, writer) -> {
            this.getOrCreateBiomeBuilder(ModBiomes.SULFUR_CAVES)
                .add(() -> writer.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(ModEntityTypes.SULFUR_CUBE.get(), 100, 2, 4)));
        });
    }
}
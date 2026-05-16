package com.blackgear.vanillabackport.common.worldgen.generation;

import com.blackgear.platform.common.worldgen.modifier.BiomeContext;
import com.blackgear.platform.common.worldgen.modifier.BiomeWriter;
import com.blackgear.vanillabackport.common.registries.ModBiomes;
import com.blackgear.vanillabackport.common.registries.ModEntityTypes;
import com.blackgear.vanillabackport.common.worldgen.placements.ChaosCubedPlacements;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;

public class ChaosCubedFeatureManager extends FeatureManager {
    public ChaosCubedFeatureManager(BiomeContext context, BiomeWriter writer) {
        super(context, writer);
    }

    @Override
    public void bootstrap() {
        this.addIf(VanillaBackport.COMMON_CONFIG.hasSulfurSprings.get(), (context, writer) -> {
            this.getOrCreateBiomeBuilder(ModBiomes.SULFUR_CAVES)
                .add(() -> this.addVegetation(ChaosCubedPlacements.ROOTED_SULFUR_SPRING));
        });

        this.addIf(VanillaBackport.COMMON_CONFIG.hasSulfurCubes.get(), (context, writer) -> {
            this.getOrCreateBiomeBuilder(ModBiomes.SULFUR_CAVES)
                .add(() -> writer.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(ModEntityTypes.SULFUR_CUBE, 100, 2, 4)));
        });
    }
}
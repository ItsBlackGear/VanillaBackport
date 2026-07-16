package com.blackgear.vanillabackport.common.worldgen;

import com.blackgear.platform.common.worldgen.modifier.BiomeContext;
import com.blackgear.platform.common.worldgen.modifier.BiomeWriter;
import com.blackgear.platform.common.worldgen.modifier.FeatureManager;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModBiomeTags;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;

public class MountsOfMayhemFeatureManager extends FeatureManager {
    public MountsOfMayhemFeatureManager(BiomeContext context, BiomeWriter writer) {
        super(context, writer);
    }

    @Override
    public void bootstrap() {
        this.addIf(true, (context, writer) -> {
            this.getOrCreateBiomeBuilder(BiomeTags.HAS_DESERT_PYRAMID)
                .add(() -> writer.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(ModEntityTypes.PARCHED.get(), 50, 4, 4)));
        });
        this.addIf(true, (context, writer) -> {
            this.getOrCreateBiomeBuilder(ModBiomeTags.SPAWNS_ZOMBIE_HORSES)
                .add(() -> writer.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE, 5, 1, 1)));
        });
    }
}
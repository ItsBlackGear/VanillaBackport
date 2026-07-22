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
import net.minecraft.world.level.biome.MobSpawnSettings;

public class MountsOfMayhemFeatureManager extends FeatureManager {
    public MountsOfMayhemFeatureManager(BiomeContext context, BiomeWriter writer) {
        super(writer, context);
    }

    @Override
    public void bootstrap() {
        this.addIf(VanillaBackport.COMMON_CONFIG.hasParchedSkeletons.get(), (context, writer) -> {
            this.getOrCreateBiomeBuilder(BiomeTags.HAS_DESERT_PYRAMID)
                .add(() -> writer.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(ModEntityTypes.PARCHED.get(), 50, 4, 4)));
        });
        this.addIf(VanillaBackport.COMMON_CONFIG.hasZombieHorses.get(), (context, writer) -> {
            this.getOrCreateBiomeBuilder(ModBiomeTags.SPAWNS_ZOMBIE_HORSES)
                .add(() -> writer.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(EntityType.ZOMBIE_HORSE, 5, 1, 1)));
        });
        
        this.addIf(VanillaBackport.COMMON_CONFIG.hasNautilus.get(), (context, writer) -> {
            this.getOrCreateBiomeBuilder(ModBiomeTags.SPAWNS_NAUTILUS)
                .add(() -> writer.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(ModEntityTypes.NAUTILUS.get(), 2, 1, 1)));
            
            this.getOrCreateBiomeBuilder(ModBiomeTags.SPAWNS_NAUTILUS_FREQUENTLY)
                .add(() -> writer.addSpawn(MobCategory.WATER_CREATURE, new MobSpawnSettings.SpawnerData(ModEntityTypes.NAUTILUS.get(), 5, 1, 1)));
        });
    }
}
package com.blackgear.vanillabackport.common.worldgen;

import com.blackgear.platform.common.worldgen.modifier.BiomeContext;
import com.blackgear.platform.common.worldgen.modifier.BiomeWriter;
import com.blackgear.platform.common.worldgen.modifier.FeatureManager;
import com.blackgear.vanillabackport.common.registries.worldgen.ModBiomes;
import com.blackgear.vanillabackport.common.worldgen.placements.SpringToLifePlacements;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModBiomeTags;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;

public class SpringToLifeFeatureManager extends FeatureManager {
    public SpringToLifeFeatureManager(BiomeContext context, BiomeWriter writer) {
        super(writer, context);
    }

    @Override
    public void bootstrap() {
        this.addIf(VanillaBackport.COMMON_CONFIG.hasCamelSpawns.get(), (context, writer) -> {
            this.getOrCreateBiomeBuilder(ModBiomeTags.SPAWNS_CAMELS)
                .add(() -> writer.addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.CAMEL, 1, 1, 1)));
        });

        this.addIf(VanillaBackport.COMMON_CONFIG.hasBushes.get(), (context, writer) -> {
            this.getOrCreateBiomeBuilder(ModBiomeTags.SPAWNS_BUSHES)
                .add(() -> this.addVegetation(SpringToLifePlacements.PATCH_BUSH));
        });

        this.addIf(VanillaBackport.COMMON_CONFIG.hasFireflyBushes.get(), (context, writer) -> {
            this.getOrCreateBiomeBuilder(ModBiomeTags.SPAWNS_FIREFLY_BUSHES)
                .add(() -> this.addVegetation(SpringToLifePlacements.PATCH_FIREFLY_BUSH_NEAR_WATER));

            this.getOrCreateBiomeBuilder(context.is(ModBiomeTags.SPAWNS_FIREFLY_BUSHES_SWAMP) && !context.is(ModBiomeTags.SPAWNS_FIREFLY_BUSHES))
                .add(() -> this.addVegetation(SpringToLifePlacements.PATCH_FIREFLY_BUSH_SWAMP))
                .add(() -> this.addVegetation(SpringToLifePlacements.PATCH_FIREFLY_BUSH_NEAR_WATER_SWAMP));
        });

        this.addIf(VanillaBackport.COMMON_CONFIG.hasWildflowers.get(), (context, writer) -> {
            this.getOrCreateBiomeBuilder(ModBiomeTags.SPAWNS_NOISE_BASED_WILDFLOWERS)
                .add(() -> this.addVegetation(SpringToLifePlacements.WILDFLOWERS_MEADOW));

            this.getOrCreateBiomeBuilder(ModBiomeTags.SPAWNS_WILDFLOWERS)
                .add(() -> this.addVegetation(SpringToLifePlacements.WILDFLOWERS_BIRCH_FOREST));
        });

        this.addIf(VanillaBackport.COMMON_CONFIG.hasDryGrass.get(), (context, writer) -> {
            this.getOrCreateBiomeBuilder(ModBiomeTags.SPAWNS_DRY_GRASS)
                .add(() -> this.addVegetation(SpringToLifePlacements.PATCH_DRY_GRASS_DESERT));

            this.getOrCreateBiomeBuilder(ModBiomeTags.SPAWNS_DRY_GRASS_RARELY)
                .add(() -> this.addVegetation(SpringToLifePlacements.PATCH_DRY_GRASS_BADLANDS));
        });

        this.addIf(VanillaBackport.COMMON_CONFIG.hasFallenTrees.get(), (context, writer) -> {
            this.getOrCreateBiomeBuilder(ModBiomeTags.SPAWNS_FALLEN_OAK_TREES)
                .add(() -> this.addVegetation(SpringToLifePlacements.PLACED_FALLEN_OAK_TREE));

            this.getOrCreateBiomeBuilder(ctx -> ctx.is(ModBiomeTags.SPAWNS_FALLEN_BIRCH_TREES_RARELY) && !ctx.is(ModBiomes.PALE_GARDEN))
                .add(() -> this.addVegetation(SpringToLifePlacements.PLACED_RARE_FALLEN_BIRCH_TREE));

            this.getOrCreateBiomeBuilder(ModBiomeTags.SPAWNS_FALLEN_BIRCH_TREES)
                .add(() -> this.addVegetation(SpringToLifePlacements.PLACED_FALLEN_BIRCH_TREE));

            this.getOrCreateBiomeBuilder(ModBiomeTags.SPAWNS_FALLEN_SUPER_BIRCH_TREES)
                .add(() -> this.addVegetation(SpringToLifePlacements.PLACED_FALLEN_SUPER_BIRCH_TREE));

            this.getOrCreateBiomeBuilder(ModBiomeTags.SPAWNS_FALLEN_JUNGLE_TREES)
                .add(() -> this.addVegetation(SpringToLifePlacements.PLACED_FALLEN_JUNGLE_TREE));

            this.getOrCreateBiomeBuilder(ModBiomeTags.SPAWNS_FALLEN_SPRUCE_TREES)
                .add(() -> this.addVegetation(SpringToLifePlacements.PLACED_FALLEN_SPRUCE_TREE));

            this.getOrCreateBiomeBuilder(ModBiomeTags.SPAWNS_FALLEN_SPRUCE_TREES_RARELY)
                .add(() -> this.addVegetation(SpringToLifePlacements.PLACED_RARE_FALLEN_SPRUCE_TREE));
        });

        this.addIf(VanillaBackport.COMMON_CONFIG.hasLeafLitter.get(), (context, writer) -> {
            this.getOrCreateBiomeBuilder(ModBiomeTags.SPAWNS_LEAF_LITTER_PATCHES)
                .add(() -> this.addVegetation(SpringToLifePlacements.PATCH_LEAF_LITTER));

            this.getOrCreateBiomeBuilder(ModBiomeTags.SPAWNS_LEAF_LITTER)
                .add(() -> this.addVegetation(SpringToLifePlacements.LEAF_LITTER));
        });

        this.addIf(VanillaBackport.COMMON_CONFIG.hasCactusFlowers.get(), (context, writer) -> {
            if (context.hasFeature(VegetationPlacements.PATCH_CACTUS_DECORATED) || context.hasFeature(VegetationPlacements.PATCH_CACTUS_DESERT)) {
                this.addVegetation(SpringToLifePlacements.CACTUS_FLOWER);
            }
        });
    }
}
package com.blackgear.vanillabackport.common.worldgen.generation;

import com.blackgear.platform.common.worldgen.modifier.BiomeContext;
import com.blackgear.platform.common.worldgen.modifier.BiomeWriter;
import com.blackgear.vanillabackport.common.registries.ModBiomes;
import com.blackgear.vanillabackport.common.worldgen.placements.SpringToLifePlacements;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModBiomeTags;
import net.minecraft.data.worldgen.placement.VegetationPlacements;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;

public class SpringToLifeFeatureManager extends FeatureManager {
    public SpringToLifeFeatureManager(BiomeContext context, BiomeWriter writer) {
        super(context, writer);
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

            if (context.hasFeature(VegetationPlacements.DARK_FOREST_VEGETATION)) {
                writer.replaceFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.DARK_FOREST_VEGETATION, SpringToLifePlacements.TREES_DARK_FOREST_LEAF_LITTER);
            }

            if (context.hasFeature(VegetationPlacements.TREES_BADLANDS)) {
                writer.replaceFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_BADLANDS, SpringToLifePlacements.TREES_BADLANDS_LEAF_LITTER);
            }

            if (context.hasFeature(VegetationPlacements.TREES_BIRCH_AND_OAK)) {
                writer.replaceFeature(GenerationStep.Decoration.VEGETAL_DECORATION, VegetationPlacements.TREES_BIRCH_AND_OAK, SpringToLifePlacements.TREES_BIRCH_AND_OAK_LEAF_LITTER);
            }
        });
    }
}
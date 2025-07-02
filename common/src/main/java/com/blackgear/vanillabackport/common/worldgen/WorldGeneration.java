package com.blackgear.vanillabackport.common.worldgen;

import com.blackgear.platform.common.worldgen.modifier.BiomeContext;
import com.blackgear.platform.common.worldgen.modifier.BiomeWriter;
import com.blackgear.vanillabackport.common.worldgen.placements.SpringToLifePlacements;
import com.blackgear.vanillabackport.core.data.tags.ModBiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;

public class WorldGeneration {
    public static void bootstrap(BiomeWriter writer, BiomeContext context) {
        if (context.is(ModBiomeTags.SPAWNS_BUSHES)) {
            writer.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, SpringToLifePlacements.PATCH_BUSH);
        }

        if (context.is(ModBiomeTags.SPAWNS_FIREFLY_BUSHES)) {
            writer.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, SpringToLifePlacements.PATCH_FIREFLY_BUSH_NEAR_WATER);
        }

        if (context.is(ModBiomeTags.SPAWNS_FIREFLY_BUSHES_SWAMP) && !context.is(ModBiomeTags.SPAWNS_FIREFLY_BUSHES)) {
            writer.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, SpringToLifePlacements.PATCH_FIREFLY_BUSH_SWAMP);
            writer.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, SpringToLifePlacements.PATCH_FIREFLY_BUSH_NEAR_WATER_SWAMP);
        }

        if (context.is(Biomes.MEADOW)) {
            writer.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, SpringToLifePlacements.WILDFLOWERS_MEADOW);
        }

        if (context.is(Biomes.BIRCH_FOREST) || context.is(Biomes.OLD_GROWTH_BIRCH_FOREST)) {
            writer.addFeature(GenerationStep.Decoration.VEGETAL_DECORATION, SpringToLifePlacements.WILDFLOWERS_BIRCH_FOREST);
        }
    }
}
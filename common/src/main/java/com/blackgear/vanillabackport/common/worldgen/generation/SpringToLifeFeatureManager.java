package com.blackgear.vanillabackport.common.worldgen.generation;

import com.blackgear.platform.common.worldgen.modifier.BiomeContext;
import com.blackgear.platform.common.worldgen.modifier.BiomeWriter;
import com.blackgear.vanillabackport.common.worldgen.placements.SpringToLifePlacements;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.data.tags.ModBiomeTags;
import com.blackgear.vanillabackport.core.data.tags.fabric.FabricBiomeTags;
import com.blackgear.vanillabackport.core.data.tags.forge.ForgeBiomeTags;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;

public class SpringToLifeFeatureManager extends FeatureManager {
    public SpringToLifeFeatureManager(BiomeContext context, BiomeWriter writer) {
        super(context, writer);
    }

    @Override
    public void bootstrap() {
        this.addIf(VanillaBackport.CONFIG.generateBushes.get(), (context, writer) -> {
            this.testFor(ModBiomeTags.SPAWNS_BUSHES)
                .add(() -> this.addVegetation(SpringToLifePlacements.PATCH_BUSH));
        });

        this.addIf(VanillaBackport.CONFIG.generateFireflyBushes.get(), (context, writer) -> {
            this.testFor(ModBiomeTags.SPAWNS_FIREFLY_BUSHES)
                .add(() -> this.addVegetation(SpringToLifePlacements.PATCH_FIREFLY_BUSH_NEAR_WATER));

            this.testFor(context.is(ModBiomeTags.SPAWNS_FIREFLY_BUSHES_SWAMP) && !context.is(ModBiomeTags.SPAWNS_FIREFLY_BUSHES))
                .add(() -> this.addVegetation(SpringToLifePlacements.PATCH_FIREFLY_BUSH_SWAMP))
                .add(() -> this.addVegetation(SpringToLifePlacements.PATCH_FIREFLY_BUSH_NEAR_WATER_SWAMP));
        });

        this.addIf(VanillaBackport.CONFIG.generateWildflowers.get(), (context, writer) -> {
            this.testFor(Biomes.MEADOW)
                .add(() -> this.addVegetation(SpringToLifePlacements.WILDFLOWERS_MEADOW));

            this.testFor(context.is(Biomes.BIRCH_FOREST) || context.is(Biomes.OLD_GROWTH_BIRCH_FOREST) || context.is(FabricBiomeTags.BIRCH_FOREST))
                .add(() -> this.addVegetation(SpringToLifePlacements.WILDFLOWERS_BIRCH_FOREST));
        });

        this.addIf(VanillaBackport.CONFIG.generateDryGrass.get(), (context, writer) -> {
            if (context.is(FabricBiomeTags.DESERT) || context.is(ForgeBiomeTags.IS_DESERT)) {
                this.addVegetation(SpringToLifePlacements.PATCH_DRY_GRASS_DESERT);
            }

            if (context.is(BiomeTags.IS_BADLANDS)) {
                this.addVegetation(SpringToLifePlacements.PATCH_DRY_GRASS_BADLANDS);
            }
        });

        this.addIf(VanillaBackport.CONFIG.generateFallenTrees.get(), (context, writer) -> {
            if (
                context.is(Biomes.DARK_FOREST)
                || context.is(BiomeTags.IS_BADLANDS)
                || context.is(BiomeTags.IS_SAVANNA)
                || context.is(BiomeTags.IS_HILL)
                || context.is(Biomes.FOREST)
                || context.is(Biomes.FLOWER_FOREST)
                || context.is(FabricBiomeTags.PLAINS)
                || context.is(ForgeBiomeTags.IS_PLAINS)
            ) {
                this.addVegetation(SpringToLifePlacements.PLACED_FALLEN_OAK_TREE);
            }

            if (context.is(BiomeTags.IS_FOREST)) {
                this.addVegetation(SpringToLifePlacements.PLACED_FALLEN_BIRCH_TREE);
            }

            if (context.is(Biomes.BIRCH_FOREST) || context.is(Biomes.OLD_GROWTH_BIRCH_FOREST) || context.is(FabricBiomeTags.BIRCH_FOREST)) {
                this.addVegetation(SpringToLifePlacements.PLACED_COMMON_FALLEN_BIRCH_TREE);
            }

            if (context.is(Biomes.OLD_GROWTH_BIRCH_FOREST)) {
                this.addVegetation(SpringToLifePlacements.PLACED_FALLEN_SUPER_BIRCH_TREE);
            }

            if (context.is(BiomeTags.IS_JUNGLE)) {
                this.addVegetation(SpringToLifePlacements.PLACED_FALLEN_JUNGLE_TREE);
            }

            if (context.is(BiomeTags.IS_TAIGA)) {
                this.addVegetation(SpringToLifePlacements.PLACED_FALLEN_SPRUCE_TREE);
            }

            if (context.is(BiomeTags.IS_HILL)) {
                this.addVegetation(SpringToLifePlacements.PLACED_RARE_FALLEN_SPRUCE_TREE);
            }
        });

        this.addIf(VanillaBackport.CONFIG.generateLeafLitter.get(), (context, writer) -> {
            if (context.is(Biomes.DARK_FOREST)) {
                this.addVegetation(SpringToLifePlacements.PATCH_LEAF_LITTER);
                this.addVegetation(SpringToLifePlacements.DARK_FOREST_VEGETATION);
            }

            if (context.is(Biomes.WOODED_BADLANDS)) {
                this.addVegetation(SpringToLifePlacements.TREES_BADLANDS);
            }

            if (context.is(Biomes.FOREST) || context.is(Biomes.FLOWER_FOREST)) {
                this.addVegetation(SpringToLifePlacements.TREES_BIRCH_AND_OAK_LEAF_LITTER);
            }
        });
    }
}
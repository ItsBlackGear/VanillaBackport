package com.blackgear.vanillabackport.common.worldgen.surface;

import com.blackgear.vanillabackport.common.registries.worldgen.ModBiomes;
import com.blackgear.vanillabackport.common.registries.blocks.ModBlocks;
import com.blackgear.vanillabackport.common.registries.worldgen.ModNoises;
import com.blackgear.vanillabackport.common.level.worldgen.surface.SpatialNoiseThresholdConditionSource;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class ModSurfaceRuleData {
    public static SurfaceRules.RuleSource makeRules() {
        SurfaceRules.RuleSource cinnabar = makeStateRule(ModBlocks.CINNABAR.get());
        SurfaceRules.RuleSource sulfur = makeStateRule(ModBlocks.SULFUR.get());
        SurfaceRules.RuleSource stone = makeStateRule(Blocks.STONE);

        return SurfaceRules.ifTrue(
            SurfaceRules.isBiome(ModBiomes.SULFUR_CAVES),
            SurfaceRules.sequence(
                SurfaceRules.ifTrue(noiseCondition3d(ModNoises.SULFUR_CAVE_GRADIENT, -0.4F, -0.1F), cinnabar),
                SurfaceRules.ifTrue(noiseCondition3d(ModNoises.SULFUR_CAVE_GRADIENT, 0.0F, 0.4F), sulfur),
                SurfaceRules.ifTrue(noiseCondition3d(ModNoises.SULFUR_CAVE_GRADIENT, 0.4F), cinnabar),
                SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(), stone)
            )
        );
    }

    public static SurfaceRules.ConditionSource noiseCondition3d(ResourceKey<NormalNoise.NoiseParameters> noise, double minRange, double maxRange) {
        return new SpatialNoiseThresholdConditionSource(noise, minRange, maxRange);
    }

    public static SurfaceRules.ConditionSource noiseCondition3d(ResourceKey<NormalNoise.NoiseParameters> noise, double minRange) {
        return noiseCondition3d(noise, minRange, Double.MAX_VALUE);
    }

    private static SurfaceRules.RuleSource makeStateRule(Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }
}
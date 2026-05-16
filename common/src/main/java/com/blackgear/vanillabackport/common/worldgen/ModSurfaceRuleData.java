package com.blackgear.vanillabackport.common.worldgen;

import com.blackgear.vanillabackport.common.registries.ModBiomes;
import com.blackgear.vanillabackport.common.registries.ModBlocks;
import com.blackgear.vanillabackport.common.registries.ModNoises;
import com.blackgear.vanillabackport.common.worldgen.surface.SpatialNoiseThresholdConditionSource;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.synth.NormalNoise;

public class ModSurfaceRuleData {
    private static final SurfaceRules.RuleSource CINNABAR = makeStateRule(ModBlocks.CINNABAR.get());
    private static final SurfaceRules.RuleSource SULFUR = makeStateRule(ModBlocks.SULFUR.get());

    public static SurfaceRules.RuleSource makeRules() {
        return SurfaceRules.ifTrue(
            SurfaceRules.not(SurfaceRules.abovePreliminarySurface()),
            SurfaceRules.ifTrue(
                SurfaceRules.isBiome(ModBiomes.SULFUR_CAVES),
                SurfaceRules.sequence(
                    SurfaceRules.ifTrue(noiseCondition3d(ModNoises.SULFUR_CAVE_GRADIENT, -0.4F, -0.1F), CINNABAR),
                    SurfaceRules.ifTrue(noiseCondition3d(ModNoises.SULFUR_CAVE_GRADIENT, 0.0F, 0.4F), SULFUR),
                    SurfaceRules.ifTrue(noiseCondition3d(ModNoises.SULFUR_CAVE_GRADIENT, 0.4F), CINNABAR)
                )
            )
        );
    }

    public static SurfaceRules.ConditionSource noiseCondition3d(ResourceKey<NormalNoise.NoiseParameters> noise, double minRange, double maxRange) {
        return new SpatialNoiseThresholdConditionSource(noise, minRange, maxRange);
    }

    public static SurfaceRules.ConditionSource noiseCondition3d(ResourceKey<NormalNoise.NoiseParameters> noise, double minRange) {
        return noiseCondition3d(noise, minRange, Double.MAX_VALUE);
    }

    private static SurfaceRules.RuleSource makeStateRule(final Block block) {
        return SurfaceRules.state(block.defaultBlockState());
    }
}
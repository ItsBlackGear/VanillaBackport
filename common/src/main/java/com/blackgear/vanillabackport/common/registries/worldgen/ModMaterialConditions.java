package com.blackgear.vanillabackport.common.registries.worldgen;

import com.blackgear.platform.core.api.registrar.Registrar;
import com.blackgear.vanillabackport.common.level.worldgen.surface.SpatialNoiseThresholdConditionSource;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.mojang.serialization.MapCodec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.SurfaceRules;

public class ModMaterialConditions {
    public static final Registrar<MapCodec<? extends SurfaceRules.ConditionSource>> REGISTRIES = Registrar.create(Registries.MATERIAL_CONDITION, VanillaBackport.NAMESPACE);

    public static final MapCodec<SpatialNoiseThresholdConditionSource> SPATIAL_NOISE_THRESHOLD = REGISTRIES.register("spatial_noise_threshold", SpatialNoiseThresholdConditionSource.CODEC.codec());
}

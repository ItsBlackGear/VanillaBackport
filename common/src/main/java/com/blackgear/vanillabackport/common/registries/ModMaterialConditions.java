package com.blackgear.vanillabackport.common.registries;

import com.blackgear.platform.core.api.registrar.Registrar;
import com.blackgear.vanillabackport.common.worldgen.surface.SpatialNoiseThresholdConditionSource;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.mojang.serialization.Codec;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.SurfaceRules;

public class ModMaterialConditions {
    public static final Registrar<Codec<? extends SurfaceRules.ConditionSource>> MATERIALS = Registrar.create(Registries.MATERIAL_CONDITION, VanillaBackport.NAMESPACE);

    public static final Codec<SpatialNoiseThresholdConditionSource> SPATIAL_NOISE_THRESHOLD = MATERIALS.register("spatial_noise_threshold", SpatialNoiseThresholdConditionSource.CODEC.codec());
}

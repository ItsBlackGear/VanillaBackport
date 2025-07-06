package com.blackgear.vanillabackport.common.registries;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.common.level.features.FallenTreeConfiguration;
import com.blackgear.vanillabackport.common.level.features.FallenTreeFeature;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;

import java.util.function.Supplier;

public class ModFeatures {
    public static final CoreRegistry<Feature<?>> FEATURES = CoreRegistry.create(Registries.FEATURE, VanillaBackport.MOD_ID);

    public static final Supplier<Feature<FallenTreeConfiguration>> FALLEN_TREE = FEATURES.register("fallen_tree", () -> new FallenTreeFeature(FallenTreeConfiguration.CODEC));
}
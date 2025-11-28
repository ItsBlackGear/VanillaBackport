package com.blackgear.vanillabackport.common.registries;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.common.level.features.CactusFlowerFeature;
import com.blackgear.vanillabackport.common.level.features.FallenTreeConfiguration;
import com.blackgear.vanillabackport.common.level.features.FallenTreeFeature;
import com.blackgear.vanillabackport.common.level.features.LeafLitterFeature;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

import java.util.function.Supplier;

public class ModFeatures {
    public static final CoreRegistry<Feature<?>> FEATURES = CoreRegistry.create(Registries.FEATURE, VanillaBackport.NAMESPACE);

    public static final Supplier<Feature<FallenTreeConfiguration>> FALLEN_TREE = FEATURES.register(
        "fallen_tree",
        () -> new FallenTreeFeature(FallenTreeConfiguration.CODEC)
    );

    public static final Supplier<Feature<NoneFeatureConfiguration>> LEAF_LITTER = FEATURES.register(
        "leaf_litter",
        () -> new LeafLitterFeature(NoneFeatureConfiguration.CODEC)
    );

    public static final Supplier<Feature<NoneFeatureConfiguration>> CACTUS_FLOWER = FEATURES.register(
        "cactus_flower",
        () -> new CactusFlowerFeature(NoneFeatureConfiguration.CODEC)
    );
}
package com.blackgear.vanillabackport.common.registries;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.common.level.features.*;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration;

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
    public static final Supplier<Feature<SimpleRandomFeatureConfiguration>> SEQUENCE = FEATURES.register(
        "sequence",
        () -> new SequenceFeature(SimpleRandomFeatureConfiguration.CODEC)
    );
    public static final Supplier<Feature<TemplateFeatureConfiguration>> TEMPLATE = FEATURES.register(
        "template",
        () -> new TemplateFeature(TemplateFeatureConfiguration.CODEC)
    );
    public static final Supplier<Feature<WeightedRandomFeatureConfiguration>> WEIGHTED_RANDOM_SELECTOR = FEATURES.register(
        "weighted_random_selector",
        () -> new WeightedRandomSelectorFeature(WeightedRandomFeatureConfiguration.CODEC)
    );
    public static final Supplier<Feature<SpeleothemConfiguration>> SPELEOTHEM = FEATURES.register(
        "speleothem",
        () -> new SpeleothemFeature(SpeleothemConfiguration.CODEC)
    );
    public static final Supplier<Feature<SpeleothemClusterConfiguration>> SPELEOTHEM_CLUSTER = FEATURES.register(
        "speleothem_cluster",
        () -> new SpeleothemClusterFeature(SpeleothemClusterConfiguration.CODEC)
    );
    public static final Supplier<Feature<SulfurRootSystemConfiguration>> SULFUR_ROOT_SYSTEM = FEATURES.register(
        "sulfur_root_system",
        () -> new SulfurRootSystemFeature(SulfurRootSystemConfiguration.CODEC)
    );
}
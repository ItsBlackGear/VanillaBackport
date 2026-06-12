package com.blackgear.vanillabackport.common.registries.worldgen;

import com.blackgear.platform.core.CoreRegistry;
import com.blackgear.vanillabackport.common.level.worldgen.features.*;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleRandomFeatureConfiguration;

import java.util.function.Supplier;

public class ModFeatures {
    public static final CoreRegistry<Feature<?>> REGISTRIES = CoreRegistry.create(Registries.FEATURE, VanillaBackport.NAMESPACE);

    public static final Supplier<Feature<FallenTreeConfiguration>> FALLEN_TREE = REGISTRIES.register("fallen_tree",
        () -> new FallenTreeFeature(FallenTreeConfiguration.CODEC));
    public static final Supplier<Feature<NoneFeatureConfiguration>> LEAF_LITTER = REGISTRIES.register("leaf_litter",
        () -> new LeafLitterFeature(NoneFeatureConfiguration.CODEC));
    public static final Supplier<Feature<NoneFeatureConfiguration>> CACTUS_FLOWER = REGISTRIES.register("cactus_flower",
        () -> new CactusFlowerFeature(NoneFeatureConfiguration.CODEC));
    public static final Supplier<Feature<SimpleRandomFeatureConfiguration>> SEQUENCE = REGISTRIES.register("sequence",
        () -> new SequenceFeature(SimpleRandomFeatureConfiguration.CODEC));
    public static final Supplier<Feature<TemplateFeatureConfiguration>> TEMPLATE = REGISTRIES.register("template",
        () -> new TemplateFeature(TemplateFeatureConfiguration.CODEC));
    public static final Supplier<Feature<WeightedRandomFeatureConfiguration>> WEIGHTED_RANDOM_SELECTOR = REGISTRIES.register("weighted_random_selector",
        () -> new WeightedRandomSelectorFeature(WeightedRandomFeatureConfiguration.CODEC));
    public static final Supplier<Feature<SpeleothemConfiguration>> SPELEOTHEM = REGISTRIES.register("speleothem",
        () -> new SpeleothemFeature(SpeleothemConfiguration.CODEC));
    public static final Supplier<Feature<SpeleothemClusterConfiguration>> SPELEOTHEM_CLUSTER = REGISTRIES.register("speleothem_cluster",
        () -> new SpeleothemClusterFeature(SpeleothemClusterConfiguration.CODEC));
    public static final Supplier<Feature<SulfurRootSystemConfiguration>> SULFUR_ROOT_SYSTEM = REGISTRIES.register("sulfur_root_system",
        () -> new SulfurRootSystemFeature(SulfurRootSystemConfiguration.CODEC));
}
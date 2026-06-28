package com.blackgear.vanillabackport.common.api.modules.mob_variant;

import com.blackgear.vanillabackport.core.ModChecker;
import com.blackgear.vanillabackport.core.VanillaBackport;

@FunctionalInterface
public interface VariantSpawner {
    VariantSpawner DEFAULT = () -> true;
    VariantSpawner FARM_ANIMALS = () -> VanillaBackport.COMMON_CONFIG.hasFarmAnimalVariants.get() && !ModChecker.MIXED_LITTER;

    boolean apply();
}
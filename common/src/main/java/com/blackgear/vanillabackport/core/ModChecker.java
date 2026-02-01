package com.blackgear.vanillabackport.core;

import com.blackgear.platform.core.Environment;
import com.google.common.base.Supplier;

public class ModChecker {
    public static final Supplier<Boolean> MIXED_LITTER_LOADED = () -> Environment.hasModLoaded("mixed_litter");
    public static final Supplier<Boolean> BEST_BUNDLES_LOADED = () -> Environment.hasModLoaded("best_bundles");
}
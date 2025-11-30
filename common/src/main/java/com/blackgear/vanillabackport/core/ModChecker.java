package com.blackgear.vanillabackport.core;

import com.blackgear.platform.core.Environment;

public class ModChecker {
    public static final boolean MIXED_LITTER_LOADED = Environment.hasModLoaded("mixed_litter");
    public static final boolean BEST_BUNDLES_LOADED = Environment.hasModLoaded("best_bundles");
}
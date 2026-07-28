package com.blackgear.vanillabackport.core;

import com.blackgear.platform.core.Environment;

public class ModChecker {
    public static final boolean MIXED_LITTER = Environment.hasModLoaded("mixed_litter");
    public static final boolean BEST_BUNDLES = Environment.hasModLoaded("best_bundles");
    public static final boolean TERRABLENDER = Environment.hasModLoaded("terrablender");
    public static final boolean SABLE = Environment.hasModLoaded("sable");
    public static final boolean EVERY_COMPAT = Environment.hasModLoaded("everycomp");
}
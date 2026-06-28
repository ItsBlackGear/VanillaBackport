package com.blackgear.vanillabackport.core;

import com.blackgear.platform.core.Environment;

public class ModChecker {
    public static final boolean MIXED_LITTER = Environment.hasModLoaded("mixed_litter");
    public static final boolean TERRABLENDER = Environment.hasModLoaded("terrablender");
    public static final boolean BACKPORTED_WOLVES = Environment.hasModLoaded("backported_wolves");
    public static final boolean COPPER_AGE_BACKPORT = Environment.hasModLoaded("copperagebackport");
}
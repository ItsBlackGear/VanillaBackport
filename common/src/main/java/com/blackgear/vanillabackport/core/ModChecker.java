package com.blackgear.vanillabackport.core;

import com.blackgear.platform.core.Environment;

public class ModChecker {
    public static final boolean MIXED_LITTER = Environment.hasModLoaded("mixed_litter");
    public static final boolean QUARK = Environment.hasModLoaded("quark");
    public static final boolean SABLE = Environment.hasModLoaded("sable");
    public static final boolean EVERY_COMPAT = Environment.hasModLoaded("everycomp");
}
package com.blackgear.vanillabackport.core;

import com.blackgear.platform.core.Environment;

import java.util.function.Supplier;

public class ModChecker {
    public static final Supplier<Boolean> MIXED_LITTER_LOADED = () -> Environment.hasModLoaded("mixed_litter");
    public static final Supplier<Boolean> BACKPORTED_WOLVES_LOADED = () -> Environment.hasModLoaded("backported_wolves");
}
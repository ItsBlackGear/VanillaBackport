package com.blackgear.vanillabackport.client.api.modules.end_flashes;

import java.util.function.BooleanSupplier;

public class BadOptimizationsCommonHook implements BooleanSupplier {
    public BadOptimizationsCommonHook() {}

    @Override
    public boolean getAsBoolean() {
        return EndFlashRenderer.needsLightmapUpdate();
    }
}
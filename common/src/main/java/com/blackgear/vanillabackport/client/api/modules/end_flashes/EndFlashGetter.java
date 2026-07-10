package com.blackgear.vanillabackport.client.api.modules.end_flashes;

import org.jetbrains.annotations.Nullable;

public interface EndFlashGetter {
    default @Nullable EndFlashState endFlashState() {
        return null;
    }
}
package com.blackgear.vanillabackport.common.level.blockentities.decoratedpot;

public enum WobbleStyle{
    POSITIVE(7),
    NEGATIVE(10);

    public final int duration;

    WobbleStyle(final int pDuration) {
        this.duration = pDuration;
    }
}

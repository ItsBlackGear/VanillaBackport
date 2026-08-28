package com.blackgear.vanillabackport.common.level.block_entities.decorated_pot;

public enum WobbleStyle {
    POSITIVE(7),
    NEGATIVE(10);

    public final int duration;

    WobbleStyle(int duration) {
        this.duration = duration;
    }
}
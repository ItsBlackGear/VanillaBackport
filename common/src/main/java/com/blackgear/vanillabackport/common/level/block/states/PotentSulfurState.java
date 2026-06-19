package com.blackgear.vanillabackport.common.level.block.states;

import net.minecraft.util.StringRepresentable;

public enum PotentSulfurState implements StringRepresentable {
    DRY("dry"),
    WET("wet"),
    DORMANT("dormant"),
    ERUPTING("erupting"),
    CONTINUOUS("continuous");

    private final String name;

    PotentSulfurState(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }
}
package com.blackgear.vanillabackport.common.api.extensions.entities;

public interface PositionAwareEntity {
    default boolean getRequiresPrecisePosition() {
        return false;
    }

    default void setRequiresPrecisePosition(boolean requiresPrecisePosition) {

    }
}
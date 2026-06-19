package com.blackgear.vanillabackport.common.api.extensions.entity;

public interface PositionAwareEntity {
    default boolean getRequiresPrecisePosition() {
        return false;
    }

    default void setRequiresPrecisePosition(boolean requiresPrecisePosition) {

    }
}
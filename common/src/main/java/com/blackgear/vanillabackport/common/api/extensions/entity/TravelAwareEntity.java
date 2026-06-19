package com.blackgear.vanillabackport.common.api.extensions.entity;

public interface TravelAwareEntity {
    default boolean omnidirectionalAirMover() {
        return false;
    }

    default void postTravelInFluid() {}
}
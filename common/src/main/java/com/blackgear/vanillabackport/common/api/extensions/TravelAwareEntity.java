package com.blackgear.vanillabackport.common.api.extensions;

public interface TravelAwareEntity {
    default boolean omnidirectionalAirMover() {
        return false;
    }

    default void postTravelInFluid() {}
}
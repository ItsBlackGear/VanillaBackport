package com.blackgear.vanillabackport.common.api.extensions.entity.movement;

public interface TravelAwareEntity {
    default boolean omnidirectionalAirMover() {
        return false;
    }

    default void postTravelInFluid() {}
}

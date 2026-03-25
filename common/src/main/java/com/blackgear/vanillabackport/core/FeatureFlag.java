package com.blackgear.vanillabackport.core;

//NOTES: configs do not work here as they get loaded too late
@FunctionalInterface
public interface FeatureFlag {
    FeatureFlag DEFAULT = () -> true;

    boolean isEnabled();
}
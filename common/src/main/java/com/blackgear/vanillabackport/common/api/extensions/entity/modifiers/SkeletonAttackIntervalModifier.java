package com.blackgear.vanillabackport.common.api.extensions.entity.modifiers;

public interface SkeletonAttackIntervalModifier {
    default int getHardAttackInterval() {
        return 20;
    }
    
    default int getAttackInterval() {
        return 40;
    }
}
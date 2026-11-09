package com.blackgear.vanillabackport.common.api.extensions.entity.conversion;

public interface FrostbiteConversion {
    default boolean vb$isFreezeConverting() {
        return false;
    }
}
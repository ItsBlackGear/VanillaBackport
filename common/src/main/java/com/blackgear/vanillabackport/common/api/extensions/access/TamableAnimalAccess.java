package com.blackgear.vanillabackport.common.api.extensions.access;

public interface TamableAnimalAccess {
    default void applyTamingSideEffects() { /* NO-OP */ }
}
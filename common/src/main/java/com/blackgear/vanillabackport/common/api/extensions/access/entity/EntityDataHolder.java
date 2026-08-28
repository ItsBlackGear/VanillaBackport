package com.blackgear.vanillabackport.common.api.extensions.access.entity;

import net.minecraft.nbt.CompoundTag;

public interface EntityDataHolder {
    default void vb$addAdditionalSaveData(CompoundTag tag) { /* NO-OP */ }
    
    default void vb$readAdditionalSaveData(CompoundTag tag) { /* NO-OP */ }
}
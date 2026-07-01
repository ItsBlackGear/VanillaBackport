package com.blackgear.vanillabackport.common.api.extensions.access.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.SynchedEntityData;

public interface EntityDataHolder {
    default void vb$defineSynchedData(SynchedEntityData.Builder builder) { /* NO-OP */ }
    
    default void vb$addAdditionalSaveData(CompoundTag tag) { /* NO-OP */ }
    
    default void vb$readAdditionalSaveData(CompoundTag tag) { /* NO-OP */ }
}
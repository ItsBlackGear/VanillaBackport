package com.blackgear.vanillabackport.core.mixin.client.access;

import net.minecraft.data.models.blockstates.VariantProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(VariantProperties.Rotation.class)
public interface RotationAccessor {
    @Accessor int getValue();
}

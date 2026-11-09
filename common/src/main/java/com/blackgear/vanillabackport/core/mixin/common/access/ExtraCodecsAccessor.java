package com.blackgear.vanillabackport.core.mixin.common.access;

import com.mojang.serialization.Codec;
import net.minecraft.util.ExtraCodecs;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.function.Function;

@Mixin(ExtraCodecs.class)
public interface ExtraCodecsAccessor {
    @Invoker
    static Codec<Float> callFloatRangeMinExclusiveWithMessage(float min, float max, Function<Float, String> errorMessage) {
        throw new UnsupportedOperationException();
    }
}

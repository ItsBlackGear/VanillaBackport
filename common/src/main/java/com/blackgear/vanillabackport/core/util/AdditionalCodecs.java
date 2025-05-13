package com.blackgear.vanillabackport.core.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.util.ExtraCodecs;

import java.util.function.Function;

public class AdditionalCodecs {
    public static final Codec<Integer> RGB_COLOR_CODEC = withAlternative(
        Codec.INT,
        ExtraCodecs.VECTOR3F,
        vector -> ColorUtils.colorFromFloat(1.0F, vector.x(), vector.y(), vector.z())
    );

    private static <T, U> Codec<T> withAlternative(Codec<T> primary, Codec<U> alternative, Function<U, T> converter) {
        return Codec.either(primary, alternative).xmap(either -> either.map(t -> t, converter), Either::left);
    }
}
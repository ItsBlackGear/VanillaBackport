package com.blackgear.vanillabackport.core.util;

import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector4f;

import java.util.List;
import java.util.function.Function;

public class AdditionalCodecs {
    public static final Codec<Vector4f> VECTOR4F = Codec.FLOAT
        .listOf()
        .comapFlatMap(
            floats -> Util.fixedSize(floats, 4).map(values -> new Vector4f(values.get(0), values.get(1), values.get(2), values.get(3))),
            vector4f -> List.of(vector4f.x(), vector4f.y(), vector4f.z(), vector4f.w())
        );

    public static final Codec<Integer> RGB_COLOR_CODEC = withAlternative(
        Codec.INT,
        ExtraCodecs.VECTOR3F,
        vector -> ColorUtils.colorFromFloat(1.0F, vector.x(), vector.y(), vector.z())
    );

    public static final Codec<Integer> ARGB_COLOR_CODEC = withAlternative(
        Codec.INT,
        VECTOR4F,
        vector -> ColorUtils.colorFromFloat(vector.w(), vector.x(), vector.y(), vector.z())
    );

    private static <T, U> Codec<T> withAlternative(Codec<T> primary, Codec<U> alternative, Function<U, T> converter) {
        return Codec.either(primary, alternative).xmap(either -> either.map(t -> t, converter), Either::left);
    }
}
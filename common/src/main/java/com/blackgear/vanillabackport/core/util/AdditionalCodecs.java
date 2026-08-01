package com.blackgear.vanillabackport.core.util;

import com.blackgear.vanillabackport.core.util.Utilities.ColorUtils;
import com.blackgear.vanillabackport.core.util.Utilities.MthUtils;
import com.mojang.datafixers.util.Either;
import com.mojang.datafixers.util.Function9;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.function.Function;

public class AdditionalCodecs {
    public static final Codec<Integer> RGB_COLOR_CODEC = withAlternative(
        Codec.INT,
        ExtraCodecs.VECTOR3F,
        vector -> ColorUtils.colorFromFloat(1.0F, vector.x(), vector.y(), vector.z())
    );
    public static final StreamCodec<ByteBuf, Vec3> VEC3_STREAM_CODEC = new StreamCodec<>() {
        public Vec3 decode(ByteBuf byteBuf) {
            return new FriendlyByteBuf(byteBuf).readVec3();
        }

        public void encode(ByteBuf byteBuf, Vec3 vec3) {
            new FriendlyByteBuf(byteBuf).writeVec3(vec3);
        }
    };
    public static final StreamCodec<ByteBuf, Float> ROTATION_BYTE = ByteBufCodecs.BYTE.map(MthUtils::unpackDegrees, MthUtils::packDegrees);

    private static <T, U> Codec<T> withAlternative(Codec<T> primary, Codec<U> alternative, Function<U, T> converter) {
        return Codec.either(primary, alternative).xmap(either -> either.map(t -> t, converter), Either::left);
    }
    
    public static <E> Codec<List<E>> compactListCodec(Codec<E> elementCodec, Codec<List<E>> listCodec) {
        return Codec.either(listCodec, elementCodec).xmap(e -> e.map(l -> l, List::of), v -> v.size() == 1 ? Either.right(v.getFirst()) : Either.left(v));
    }
    
    public static Codec<FloatProvider> floatProvider(float minValue) {
        return FloatProvider.CODEC.validate(
            value -> value.getMinValue() < minValue
                ? DataResult.error(() -> "Value provider too low: " + minValue + " [" + value.getMinValue() + "-" + value.getMaxValue() + "]")
                : DataResult.success(value)
        );
    }
    
    public static Codec<Float> floatRange(float minInclusive, float maxInclusive) {
        return floatRangeMinExclusiveWithMessage(minInclusive, maxInclusive, value -> "Value must be within range [" + minInclusive + ";" + maxInclusive + "]: " + value);
    }
    
    private static Codec<Float> floatRangeMinExclusiveWithMessage(float minInclusive, float maxInclusive, Function<Float, String> error) {
        return Codec.FLOAT.validate(
            value -> value.compareTo(minInclusive) > 0 && value.compareTo(maxInclusive) <= 0
                ? DataResult.success(value)
                : DataResult.error(() -> error.apply(value))
        );
    }
    
    public static <B, C, T1, T2, T3, T4, T5, T6, T7, T8, T9> StreamCodec<B, C> composite(
        StreamCodec<? super B, T1> codec1,
        Function<C, T1> getter1,
        StreamCodec<? super B, T2> codec2,
        Function<C, T2> getter2,
        StreamCodec<? super B, T3> codec3,
        Function<C, T3> getter3,
        StreamCodec<? super B, T4> codec4,
        Function<C, T4> getter4,
        StreamCodec<? super B, T5> codec5,
        Function<C, T5> getter5,
        StreamCodec<? super B, T6> codec6,
        Function<C, T6> getter6,
        StreamCodec<? super B, T7> codec7,
        Function<C, T7> getter7,
        StreamCodec<? super B, T8> codec8,
        Function<C, T8> getter8,
        StreamCodec<? super B, T9> codec9,
        Function<C, T9> getter9,
        Function9<T1, T2, T3, T4, T5, T6, T7, T8, T9, C> constructor
    ) {
        return new StreamCodec<B, C>() {
            @Override
            public C decode(final B input) {
                T1 v1 = codec1.decode(input);
                T2 v2 = codec2.decode(input);
                T3 v3 = codec3.decode(input);
                T4 v4 = codec4.decode(input);
                T5 v5 = codec5.decode(input);
                T6 v6 = codec6.decode(input);
                T7 v7 = codec7.decode(input);
                T8 v8 = codec8.decode(input);
                T9 v9 = codec9.decode(input);
                return constructor.apply(v1, v2, v3, v4, v5, v6, v7, v8, v9);
            }
            
            @Override
            public void encode(final B output, final C value) {
                codec1.encode(output, (T1)getter1.apply(value));
                codec2.encode(output, (T2)getter2.apply(value));
                codec3.encode(output, (T3)getter3.apply(value));
                codec4.encode(output, (T4)getter4.apply(value));
                codec5.encode(output, (T5)getter5.apply(value));
                codec6.encode(output, (T6)getter6.apply(value));
                codec7.encode(output, (T7)getter7.apply(value));
                codec8.encode(output, (T8)getter8.apply(value));
                codec9.encode(output, (T9)getter9.apply(value));
            }
        };
    }
}
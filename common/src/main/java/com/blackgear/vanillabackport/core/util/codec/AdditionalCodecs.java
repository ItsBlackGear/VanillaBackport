package com.blackgear.vanillabackport.core.util.codec;

import com.blackgear.vanillabackport.core.util.ColorUtils;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.Util;
import net.minecraft.advancements.critereon.MinMaxBounds;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Registry;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;
import org.joml.Vector4f;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public class AdditionalCodecs {
    public static final Codec<MinMaxBounds.Doubles> DOUBLES = AdditionalCodecs.<Double, MinMaxBounds.Doubles>createCodec(Codec.DOUBLE, (min, max) -> new MinMaxBounds.Doubles(min.orElse(null), max.orElse(null)));

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

    private static <T extends Number, R extends MinMaxBounds<T>> Codec<R> createCodec(Codec<T> codec, BoundsFactory<T, R> factory) {
        Codec<R> builder = RecordCodecBuilder.create(instance -> instance.group(
            codec.optionalFieldOf("min").forGetter(bounds -> Optional.ofNullable(bounds.getMin())),
            codec.optionalFieldOf("max").forGetter(bounds -> Optional.ofNullable(bounds.getMax()))
        ).apply(instance, factory::create));

        return Codec.either(builder, codec)
            .xmap(either -> either.map(bounds -> bounds, number -> factory.create(Optional.of(number), Optional.of(number))), bounds -> {
                Optional<T> min = Optional.ofNullable(bounds.getMin());
                Optional<T> max = Optional.ofNullable(bounds.getMax());
                Optional<T> optional = min.equals(max) ? min : Optional.empty();
                return optional.<Either<R, T>>map(Either::right).orElseGet(() -> Either.left(bounds));
            });
    }

    interface BoundsFactory<T extends Number, R extends MinMaxBounds<T>> {
        R create(Optional<T> min, Optional<T> max);
    }

    public static <E> Codec<HolderSet<E>> homogeneousList(ResourceKey<? extends Registry<E>> resourceKey, Codec<E> codec) {
        return homogeneousList(resourceKey, codec, false);
    }

    public static <E> Codec<HolderSet<E>> homogeneousList(ResourceKey<? extends Registry<E>> registryKey, Codec<E> codec, boolean disallowInline) {
        return HolderSetCodec.create(registryKey, RegistryFileCodec.create(registryKey, codec), disallowInline);
    }

    public static <E> Codec<HolderSet<E>> homogeneousList(ResourceKey<? extends Registry<E>> registryKey) {
        return homogeneousList(registryKey, false);
    }

    public static <E> Codec<HolderSet<E>> homogeneousList(ResourceKey<? extends Registry<E>> registryKey, boolean disallowInline) {
        return HolderSetCodec.create(registryKey, RegistryFixedCodec.create(registryKey), disallowInline);
    }
}
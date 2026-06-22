package com.blackgear.vanillabackport.core.util;

import com.blackgear.vanillabackport.core.util.Utilities.ColorUtils;
import com.blackgear.vanillabackport.core.util.Utilities.MthUtils;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.valueproviders.FloatProvider;
import net.minecraft.world.phys.Vec3;

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

    public static Codec<FloatProvider> floatProvider(float minValue) {
        return FloatProvider.CODEC.validate(
            value -> value.getMinValue() < minValue
                ? DataResult.error(() -> "Value provider too low: " + minValue + " [" + value.getMinValue() + "-" + value.getMaxValue() + "]")
                : DataResult.success(value)
        );
    }
}
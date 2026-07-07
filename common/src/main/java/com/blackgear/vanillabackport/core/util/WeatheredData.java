package com.blackgear.vanillabackport.core.util;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;

import java.util.function.IntFunction;

public interface WeatheredData {
    StringRepresentable.EnumCodec<WeatherState> CODEC = StringRepresentable.fromEnum(WeatherState::values);
    IntFunction<WeatherState> BY_ID = ByIdMap.continuous(Enum::ordinal, WeatherState.values(), ByIdMap.OutOfBoundsStrategy.CLAMP);
    StreamCodec<ByteBuf, WeatherState> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, Enum::ordinal);

    static WeatherState fromName(String name) {
        return CODEC.byName(name, WeatherState.UNAFFECTED);
    }

    static WeatherState next(WeatherState state) {
        return BY_ID.apply(state.ordinal() + 1);
    }

    static WeatherState previous(WeatherState state) {
        return BY_ID.apply(state.ordinal() - 1);
    }
}
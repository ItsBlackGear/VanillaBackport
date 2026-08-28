package com.blackgear.vanillabackport.common.level.entities.mob.animal.golem.copper_golem;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.function.IntFunction;

public enum CopperGolemState implements StringRepresentable {
    IDLE("idle", 0),
    GETTING_ITEM("getting_item", 1),
    GETTING_NO_ITEM("getting_no_item", 2),
    DROPPING_ITEM("dropping_item", 3),
    DROPPING_NO_ITEM("dropping_no_item", 4);

    public static final Codec<CopperGolemState> CODEC = StringRepresentable.fromEnum(CopperGolemState::values);
    private static final IntFunction<CopperGolemState> BY_ID = ByIdMap.continuous(CopperGolemState::id, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
    public static final StreamCodec<ByteBuf, CopperGolemState> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID, CopperGolemState::id);
    private final String name;
    private final int id;

    CopperGolemState(String name, int id) {
        this.name = name;
        this.id = id;
    }

    @Override
    public String getSerializedName() {
        return this.name;
    }

    public int id() {
        return this.id;
    }
}
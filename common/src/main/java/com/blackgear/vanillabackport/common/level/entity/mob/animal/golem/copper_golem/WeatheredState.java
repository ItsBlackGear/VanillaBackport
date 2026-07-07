package com.blackgear.vanillabackport.common.level.entity.mob.animal.golem.copper_golem;

import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.WeatheringCopper.WeatherState;

import java.util.function.IntFunction;

public enum WeatheredState implements StringRepresentable {
    UNAFFECTED("unaffected"),
    EXPOSED("exposed"),
    WEATHERED("weathered"),
    OXIDIZED("oxidized");
    
    public static final EnumCodec<WeatheredState> CODEC = StringRepresentable.fromEnum(WeatheredState::values);
    public static final IntFunction<WeatheredState> BY_ID = ByIdMap.continuous(Enum::ordinal, WeatheredState.values(), ByIdMap.OutOfBoundsStrategy.CLAMP);
    private final String name;
    
    WeatheredState(String name) {
        this.name = name;
    }
    
    public String getSerializedName() {
        return this.name;
    }
    
    static WeatheredState fromName(String name) {        
        return CODEC.byName(name, WeatheredState.UNAFFECTED);
    }
    
    static WeatheredState next(WeatheredState state) {
        return BY_ID.apply(state.ordinal() + 1);
    }
    
    static WeatheredState previous(WeatheredState state) {
        return BY_ID.apply(state.ordinal() - 1);
    }
    
    public static WeatherState parse(WeatheredState state) {
        return switch (state) {
            case UNAFFECTED -> WeatherState.UNAFFECTED;
            case EXPOSED -> WeatherState.EXPOSED;
            case WEATHERED -> WeatherState.WEATHERED;
            case OXIDIZED -> WeatherState.OXIDIZED;
        };
    }
}
package com.blackgear.vanillabackport.common.registries.entities;

import com.blackgear.platform.core.helper.DataSerializerRegistry;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.golem.copper_golem.CopperGolemState;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.util.WeatheredData;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.level.block.WeatheringCopper;

import java.util.function.Supplier;

public class ModEntityDataSerializers {
    public static final DataSerializerRegistry SERIALIZERS = DataSerializerRegistry.create(VanillaBackport.NAMESPACE);
    
    public static final Supplier<EntityDataSerializer<WeatheringCopper.WeatherState>> WEATHERING_COPPER_STATE = SERIALIZERS.register("weathering_copper_state",
        WeatheredData.STREAM_CODEC);
    
    public static final Supplier<EntityDataSerializer<CopperGolemState>> COPPER_GOLEM_STATE = SERIALIZERS.register("copper_golem_state",
        CopperGolemState.STREAM_CODEC);
}
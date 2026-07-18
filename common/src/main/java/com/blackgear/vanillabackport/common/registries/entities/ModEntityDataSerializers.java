package com.blackgear.vanillabackport.common.registries.entities;

import com.blackgear.platform.core.helper.DataSerializerRegistry;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.armadillo.ArmadilloState;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.golem.copper_golem.CopperGolemState;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.golem.copper_golem.WeatheredState;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.item.DyeColor;

import java.util.function.Supplier;

public class ModEntityDataSerializers {
    public static final DataSerializerRegistry SERIALIZERS = DataSerializerRegistry.create(VanillaBackport.NAMESPACE);

    public static final Supplier<EntityDataSerializer<ArmadilloState>> ARMADILLO_STATE = SERIALIZERS.simpleEnum("armadillo_state", ArmadilloState.class);
    public static final Supplier<EntityDataSerializer<WeatheredState>> WEATHERING_COPPER_STATE = SERIALIZERS.simpleEnum("weathering_copper_state", WeatheredState.class);
    public static final Supplier<EntityDataSerializer<CopperGolemState>> COPPER_GOLEM_STATE = SERIALIZERS.simpleEnum("copper_golem_state", CopperGolemState.class);
    public static final Supplier<EntityDataSerializer<DyeColor>> DYE_COLOR = SERIALIZERS.simpleEnum("dye_color", DyeColor.class);
}
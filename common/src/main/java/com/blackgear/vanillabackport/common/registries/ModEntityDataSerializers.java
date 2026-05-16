package com.blackgear.vanillabackport.common.registries;

import com.blackgear.platform.core.helper.DataSerializerRegistry;
import com.blackgear.vanillabackport.common.api.wolf.WolfSoundVariant;
import com.blackgear.vanillabackport.common.level.entities.armadillo.ArmadilloState;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.registries.ModRegistries;
import net.minecraft.core.Holder;
import net.minecraft.network.syncher.EntityDataSerializer;

import java.util.function.Supplier;

public class ModEntityDataSerializers {
    public static final DataSerializerRegistry SERIALIZERS = DataSerializerRegistry.create(VanillaBackport.NAMESPACE);

    public static final Supplier<EntityDataSerializer<ArmadilloState>> ARMADILLO_STATE = SERIALIZERS.simpleEnum("armadillo_state", ArmadilloState.class);
}
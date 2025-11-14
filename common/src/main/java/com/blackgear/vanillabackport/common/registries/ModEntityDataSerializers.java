package com.blackgear.vanillabackport.common.registries;

import com.blackgear.platform.core.helper.DataSerializerRegistry;
import com.blackgear.vanillabackport.common.api.wolf.WolfSoundVariant;
import com.blackgear.vanillabackport.common.level.entities.animal.CowVariant;
import com.blackgear.vanillabackport.common.level.entities.animal.PigVariant;
import com.blackgear.vanillabackport.common.level.entities.armadillo.ArmadilloState;
import com.blackgear.vanillabackport.core.registries.ModRegistries;
import net.minecraft.network.syncher.EntityDataSerializer;

public class ModEntityDataSerializers {
    public static final DataSerializerRegistry SERIALIZERS = DataSerializerRegistry.create();

    public static final EntityDataSerializer<ArmadilloState> ARMADILLO_STATE = SERIALIZERS.simpleEnum(ArmadilloState.class);
}
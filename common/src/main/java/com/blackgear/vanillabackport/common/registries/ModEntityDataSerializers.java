package com.blackgear.vanillabackport.common.registries;

import com.blackgear.platform.core.helper.DataSerializerRegistry;
import com.blackgear.vanillabackport.common.api.wolf.WolfSoundVariant;
import com.blackgear.vanillabackport.common.level.entities.animal.ChickenVariant;
import com.blackgear.vanillabackport.common.level.entities.animal.CowVariant;
import com.blackgear.vanillabackport.common.level.entities.animal.PigVariant;
import com.blackgear.vanillabackport.core.registries.ModRegistries;
import net.minecraft.network.syncher.EntityDataSerializer;

public class ModEntityDataSerializers {
    public static final DataSerializerRegistry SERIALIZERS = DataSerializerRegistry.create();

    public static final EntityDataSerializer<WolfSoundVariant> WOLF_SOUND_VARIANT = SERIALIZERS.simpleId(ModRegistries.WOLF_SOUND_VARIANT.get());
    public static final EntityDataSerializer<CowVariant> COW_VARIANT = SERIALIZERS.simpleId(ModRegistries.COW_VARIANT.get());
    public static final EntityDataSerializer<PigVariant> PIG_VARIANT = SERIALIZERS.simpleId(ModRegistries.PIG_VARIANT.get());
    public static final EntityDataSerializer<ChickenVariant> CHICKEN_VARIANT = SERIALIZERS.simpleId(ModRegistries.CHICKEN_VARIANT.get());
}
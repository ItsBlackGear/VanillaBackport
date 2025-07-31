package com.blackgear.vanillabackport.common.api.variant;

import com.blackgear.vanillabackport.core.registries.ModRegistries;
import com.mojang.serialization.Codec;

public interface SpawnCondition extends PriorityProvider.SelectorCondition<SpawnContext> {
    Codec<SpawnCondition> CODEC = ModRegistries.SPAWN_CONDITION_TYPE.get().byNameCodec().dispatch(SpawnCondition::codec, codec -> codec);

    Codec<? extends SpawnCondition> codec();
}
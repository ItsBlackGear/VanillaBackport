package com.blackgear.vanillabackport.common.api.variants.spawn;

import com.blackgear.vanillabackport.core.registries.ModRegistries;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;

public interface SpawnCondition extends PriorityProvider.SelectorCondition<SpawnContext> {
    Codec<SpawnCondition> CODEC = ModRegistries.SPAWN_CONDITION_TYPE.get().byNameCodec().dispatch(SpawnCondition::codec, codec -> codec);

    MapCodec<? extends SpawnCondition> codec();
}
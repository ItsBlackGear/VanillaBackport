package com.blackgear.vanillabackport.common.api.variant;

import com.mojang.serialization.Codec;

import java.util.List;

public record SpawnPrioritySelectors(List<PriorityProvider.Selector<SpawnContext, SpawnCondition>> selectors) {
    public static final SpawnPrioritySelectors EMPTY = new SpawnPrioritySelectors(List.of());
    public static final Codec<SpawnPrioritySelectors> CODEC = PriorityProvider.Selector.codec(SpawnCondition.CODEC)
        .listOf()
        .xmap(SpawnPrioritySelectors::new, SpawnPrioritySelectors::selectors);

    public static SpawnPrioritySelectors single(SpawnCondition condition, int priority) {
        return new SpawnPrioritySelectors(PriorityProvider.single(condition, priority));
    }

    public static SpawnPrioritySelectors fallback(int priority) {
        return new SpawnPrioritySelectors(PriorityProvider.alwaysTrue(priority));
    }
}

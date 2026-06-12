package com.blackgear.vanillabackport.common.level.entity.mob.animal.frog;

import com.blackgear.vanillabackport.common.api.variants.*;
import com.blackgear.vanillabackport.common.api.variants.spawn.PriorityProvider;
import com.blackgear.vanillabackport.common.api.variants.spawn.SpawnCondition;
import com.blackgear.vanillabackport.common.api.variants.spawn.SpawnContext;
import com.blackgear.vanillabackport.common.api.variants.spawn.SpawnPrioritySelectors;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record FrogDataVariant(ClientAsset assetInfo, SpawnPrioritySelectors spawnConditions) implements PriorityProvider<SpawnContext, SpawnCondition> {
    public static final Codec<FrogDataVariant> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ClientAsset.DEFAULT_FIELD_CODEC.forGetter(FrogDataVariant::assetInfo),
        SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(FrogDataVariant::spawnConditions)
    ).apply(instance, FrogDataVariant::new));

    private FrogDataVariant(ClientAsset clientAsset) {
        this(clientAsset, SpawnPrioritySelectors.EMPTY);
    }

    @Override
    public List<Selector<SpawnContext, SpawnCondition>> selectors() {
        return this.spawnConditions.selectors();
    }
}
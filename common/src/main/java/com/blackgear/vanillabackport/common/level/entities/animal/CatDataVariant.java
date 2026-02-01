package com.blackgear.vanillabackport.common.level.entities.animal;

import com.blackgear.vanillabackport.common.api.variant.ClientAsset;
import com.blackgear.vanillabackport.common.api.variant.spawn.PriorityProvider;
import com.blackgear.vanillabackport.common.api.variant.spawn.SpawnCondition;
import com.blackgear.vanillabackport.common.api.variant.spawn.SpawnContext;
import com.blackgear.vanillabackport.common.api.variant.spawn.SpawnPrioritySelectors;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record CatDataVariant(ClientAsset assetInfo, SpawnPrioritySelectors spawnConditions) implements PriorityProvider<SpawnContext, SpawnCondition> {
    public static final Codec<CatDataVariant> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        ClientAsset.DEFAULT_FIELD_CODEC.forGetter(CatDataVariant::assetInfo),
        SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(CatDataVariant::spawnConditions)
    ).apply(instance, CatDataVariant::new));

    private CatDataVariant(ClientAsset clientAsset) {
        this(clientAsset, SpawnPrioritySelectors.EMPTY);
    }

    @Override
    public List<Selector<SpawnContext, SpawnCondition>> selectors() {
        return this.spawnConditions.selectors();
    }
}
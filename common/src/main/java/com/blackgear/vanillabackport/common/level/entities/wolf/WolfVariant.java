package com.blackgear.vanillabackport.common.level.entities.wolf;

import com.blackgear.vanillabackport.common.api.variant.*;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record WolfVariant(AssetInfo assetInfo, SpawnPrioritySelectors spawnConditions) implements PriorityProvider<SpawnContext, SpawnCondition> {
    public static final Codec<WolfVariant> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        AssetInfo.CODEC.fieldOf("assets").forGetter(WolfVariant::assetInfo),
        SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(WolfVariant::spawnConditions)
    ).apply(instance, WolfVariant::new));

    private WolfVariant(AssetInfo assetInfo) {
        this(assetInfo, SpawnPrioritySelectors.EMPTY);
    }

    @Override
    public List<Selector<SpawnContext, SpawnCondition>> selectors() {
        return this.spawnConditions.selectors();
    }

    public record AssetInfo(ClientAsset wild, ClientAsset tame, ClientAsset angry) {
        public static final Codec<AssetInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ClientAsset.CODEC.fieldOf("wild").forGetter(AssetInfo::wild),
            ClientAsset.CODEC.fieldOf("tame").forGetter(AssetInfo::tame),
            ClientAsset.CODEC.fieldOf("angry").forGetter(AssetInfo::angry)
        ).apply(instance, AssetInfo::new));
    }
}
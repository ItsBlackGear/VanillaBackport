package com.blackgear.vanillabackport.common.level.entities.animal;

import com.blackgear.vanillabackport.common.api.variant.*;
import com.blackgear.vanillabackport.common.api.variant.spawn.PriorityProvider;
import com.blackgear.vanillabackport.common.api.variant.spawn.SpawnCondition;
import com.blackgear.vanillabackport.common.api.variant.spawn.SpawnContext;
import com.blackgear.vanillabackport.common.api.variant.spawn.SpawnPrioritySelectors;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.List;

public record EnhancedWolfVariant(AssetInfo assetInfo, SpawnPrioritySelectors spawnConditions) implements PriorityProvider<SpawnContext, SpawnCondition> {
    public static final Codec<EnhancedWolfVariant> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        AssetInfo.CODEC.fieldOf("assets").forGetter(EnhancedWolfVariant::assetInfo),
        SpawnPrioritySelectors.CODEC.fieldOf("spawn_conditions").forGetter(EnhancedWolfVariant::spawnConditions)
    ).apply(instance, EnhancedWolfVariant::new));

    @Override
    public List<Selector<SpawnContext, SpawnCondition>> selectors() {
        return this.spawnConditions.selectors();
    }

    /**
     * Asset info for custom wolf textures.
     */
    public record AssetInfo(ClientAsset wild, ClientAsset tame, ClientAsset angry) {
        public static final Codec<AssetInfo> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ClientAsset.CODEC.fieldOf("wild").forGetter(AssetInfo::wild),
            ClientAsset.CODEC.fieldOf("tame").forGetter(AssetInfo::tame),
            ClientAsset.CODEC.fieldOf("angry").forGetter(AssetInfo::angry)
        ).apply(instance, AssetInfo::new));
    }
}
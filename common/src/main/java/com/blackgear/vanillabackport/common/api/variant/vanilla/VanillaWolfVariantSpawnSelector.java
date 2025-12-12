package com.blackgear.vanillabackport.common.api.variant.vanilla;

import com.blackgear.vanillabackport.common.api.variant.spawn.PriorityProvider;
import com.blackgear.vanillabackport.common.api.variant.spawn.SpawnCondition;
import com.blackgear.vanillabackport.common.api.variant.spawn.SpawnContext;
import com.blackgear.vanillabackport.common.api.variant.spawn.SpawnPrioritySelectors;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.RegistryFileCodec;
import net.minecraft.world.entity.animal.WolfVariant;

import java.util.List;

/**
 * Associates vanilla WolfVariant entries with custom spawn conditions.
 * This allows the mod to select appropriate vanilla variants based on biome during spawn,
 * while letting vanilla handle the rendering.
 */
public record VanillaWolfVariantSpawnSelector(
    Holder<WolfVariant> vanillaVariant,
    SpawnPrioritySelectors spawnConditions
) implements PriorityProvider<SpawnContext, SpawnCondition> {

    public static final Codec<VanillaWolfVariantSpawnSelector> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        RegistryFileCodec.create(Registries.WOLF_VARIANT, WolfVariant.DIRECT_CODEC)
            .fieldOf("vanilla_variant")
            .forGetter(VanillaWolfVariantSpawnSelector::vanillaVariant),
        SpawnPrioritySelectors.CODEC
            .fieldOf("spawn_conditions")
            .forGetter(VanillaWolfVariantSpawnSelector::spawnConditions)
    ).apply(instance, VanillaWolfVariantSpawnSelector::new));

    /**
     * Creates a selector with a fallback priority (always matches with lowest priority).
     */
    public static VanillaWolfVariantSpawnSelector fallback(Holder<WolfVariant> variant, int priority) {
        return new VanillaWolfVariantSpawnSelector(variant, SpawnPrioritySelectors.fallback(priority));
    }

    /**
     * Creates a selector with a single spawn condition.
     */
    public static VanillaWolfVariantSpawnSelector withCondition(Holder<WolfVariant> variant, SpawnCondition condition, int priority) {
        return new VanillaWolfVariantSpawnSelector(variant, SpawnPrioritySelectors.single(condition, priority));
    }

    @Override
    public List<Selector<SpawnContext, SpawnCondition>> selectors() {
        return this.spawnConditions.selectors();
    }
}


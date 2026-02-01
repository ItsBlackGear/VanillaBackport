package com.blackgear.vanillabackport.common.api.variant.spawn.check.raw;

import com.blackgear.vanillabackport.common.api.variant.spawn.SpawnCondition;
import com.blackgear.vanillabackport.common.api.variant.spawn.SpawnContext;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public record RawBiomeCheck(TagKey<Biome> requiredBiomes) implements SpawnCondition {
    public static final Codec<RawBiomeCheck> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        TagKey.codec(Registries.BIOME).fieldOf("biomes").forGetter(RawBiomeCheck::requiredBiomes)
    ).apply(instance, RawBiomeCheck::new));

    @Override
    public boolean test(SpawnContext context) {
        return context.biome().is(this.requiredBiomes);
    }

    @Override
    public Codec<? extends SpawnCondition> codec() {
        return CODEC;
    }
}

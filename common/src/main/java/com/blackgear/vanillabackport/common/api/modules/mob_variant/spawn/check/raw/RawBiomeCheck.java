package com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.check.raw;

import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.SpawnCondition;
import com.blackgear.vanillabackport.common.api.modules.mob_variant.spawn.SpawnContext;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public record RawBiomeCheck(TagKey<Biome> requiredBiomes) implements SpawnCondition {
    public static final MapCodec<RawBiomeCheck> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        TagKey.codec(Registries.BIOME).fieldOf("biomes").forGetter(RawBiomeCheck::requiredBiomes)
    ).apply(instance, RawBiomeCheck::new));

    @Override
    public boolean test(SpawnContext context) {
        return context.biome().is(this.requiredBiomes);
    }

    @Override
    public MapCodec<? extends SpawnCondition> codec() {
        return CODEC;
    }
}
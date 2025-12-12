package com.blackgear.vanillabackport.common.api.variant.spawn.check.internal;

import com.blackgear.vanillabackport.common.api.variant.spawn.SpawnCondition;
import com.blackgear.vanillabackport.common.api.variant.spawn.SpawnContext;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;

public record IBiomeCheck(TagKey<Biome> requiredBiomes) implements SpawnCondition {
    public static final MapCodec<IBiomeCheck> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        TagKey.codec(Registries.BIOME).fieldOf("biomes").forGetter(IBiomeCheck::requiredBiomes)
    ).apply(instance, IBiomeCheck::new));

    @Override
    public boolean test(SpawnContext context) {
        return context.biome().is(this.requiredBiomes);
    }

    @Override
    public MapCodec<? extends SpawnCondition> codec() {
        return CODEC;
    }
}
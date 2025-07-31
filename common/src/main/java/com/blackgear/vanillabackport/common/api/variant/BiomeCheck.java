package com.blackgear.vanillabackport.common.api.variant;

import com.blackgear.vanillabackport.core.util.codec.AdditionalCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.biome.Biome;

public record BiomeCheck(HolderSet<Biome> requiredBiomes) implements SpawnCondition {
    public static final Codec<BiomeCheck> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        AdditionalCodecs.homogeneousList(Registries.BIOME).fieldOf("biomes").forGetter(BiomeCheck::requiredBiomes)
    ).apply(instance, BiomeCheck::new));

    @Override
    public boolean test(SpawnContext context) {
        return this.requiredBiomes.contains(context.biome());
    }

    @Override
    public Codec<? extends SpawnCondition> codec() {
        return CODEC;
    }
}
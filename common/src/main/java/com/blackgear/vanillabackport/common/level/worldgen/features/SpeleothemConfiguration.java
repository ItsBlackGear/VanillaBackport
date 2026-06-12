package com.blackgear.vanillabackport.common.level.worldgen.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public record SpeleothemConfiguration(
    BlockState baseBlock,
    BlockState pointedBlock,
    TagKey<Block> replaceableBlocks,
    float chanceOfTallerGeneration,
    float chanceOfDirectionalSpread,
    float chanceOfSpreadRadius2,
    float chanceOfSpreadRadius3
) implements FeatureConfiguration {
    public static final Codec<SpeleothemConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        BlockState.CODEC.fieldOf("base_block").forGetter(SpeleothemConfiguration::baseBlock),
        BlockState.CODEC.fieldOf("pointed_block").forGetter(SpeleothemConfiguration::pointedBlock),
        TagKey.hashedCodec(Registries.BLOCK).fieldOf("replaceable_blocks").forGetter(SpeleothemConfiguration::replaceableBlocks),
        Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_taller_generation").orElse(0.2F).forGetter(SpeleothemConfiguration::chanceOfTallerGeneration),
        Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_directional_spread").orElse(0.7F).forGetter(SpeleothemConfiguration::chanceOfDirectionalSpread),
        Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_spread_radius_2").orElse(0.5F).forGetter(SpeleothemConfiguration::chanceOfSpreadRadius2),
        Codec.floatRange(0.0F, 1.0F).fieldOf("chance_of_spread_radius_3").orElse(0.5F).forGetter(SpeleothemConfiguration::chanceOfSpreadRadius3)
    ).apply(instance, SpeleothemConfiguration::new));
}
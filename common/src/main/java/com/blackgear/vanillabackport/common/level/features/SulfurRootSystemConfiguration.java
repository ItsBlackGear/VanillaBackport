package com.blackgear.vanillabackport.common.level.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public record SulfurRootSystemConfiguration(
    Holder<PlacedFeature> feature,
    int requiredVerticalSpaceForTree,
    int levelTestDistance,
    int maxLevelDeviation,
    int rootRadius,
    TagKey<Block> rootReplaceable,
    BlockStateProvider rootStateProvider,
    int rootPlacementAttempts,
    int rootColumnMaxHeight,
    int hangingRootRadius,
    int hangingRootsVerticalSpan,
    BlockStateProvider hangingRootStateProvider,
    int hangingRootPlacementAttempts,
    int allowedVerticalWaterForTree,
    BlockPredicate allowedTreePosition
) implements FeatureConfiguration {
    public static final Codec<SulfurRootSystemConfiguration> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        PlacedFeature.CODEC.fieldOf("feature").forGetter(SulfurRootSystemConfiguration::feature),
        Codec.intRange(1, 64).fieldOf("required_vertical_space_for_tree").forGetter(SulfurRootSystemConfiguration::requiredVerticalSpaceForTree),
        Codec.intRange(0, 16).fieldOf("level_test_distance").forGetter(SulfurRootSystemConfiguration::levelTestDistance),
        Codec.intRange(0, 64).fieldOf("max_level_deviation").forGetter(SulfurRootSystemConfiguration::maxLevelDeviation),
        Codec.intRange(1, 64).fieldOf("root_radius").forGetter(SulfurRootSystemConfiguration::rootRadius),
        TagKey.hashedCodec(Registries.BLOCK).fieldOf("root_replaceable").forGetter(SulfurRootSystemConfiguration::rootReplaceable),
        BlockStateProvider.CODEC.fieldOf("root_state_provider").forGetter(SulfurRootSystemConfiguration::rootStateProvider),
        Codec.intRange(1, 256).fieldOf("root_placement_attempts").forGetter(SulfurRootSystemConfiguration::rootPlacementAttempts),
        Codec.intRange(1, 4096).fieldOf("root_column_max_height").forGetter(SulfurRootSystemConfiguration::rootColumnMaxHeight),
        Codec.intRange(1, 64).fieldOf("hanging_root_radius").forGetter(SulfurRootSystemConfiguration::hangingRootRadius),
        Codec.intRange(0, 16).fieldOf("hanging_roots_vertical_span").forGetter(SulfurRootSystemConfiguration::hangingRootsVerticalSpan),
        BlockStateProvider.CODEC.fieldOf("hanging_root_state_provider").forGetter(SulfurRootSystemConfiguration::hangingRootStateProvider),
        Codec.intRange(1, 256).fieldOf("hanging_root_placement_attempts").forGetter(SulfurRootSystemConfiguration::hangingRootPlacementAttempts),
        Codec.intRange(1, 64).fieldOf("allowed_vertical_water_for_tree").forGetter(SulfurRootSystemConfiguration::allowedVerticalWaterForTree),
        BlockPredicate.CODEC.fieldOf("allowed_tree_position").forGetter(SulfurRootSystemConfiguration::allowedTreePosition)
    ).apply(instance, SulfurRootSystemConfiguration::new));
}

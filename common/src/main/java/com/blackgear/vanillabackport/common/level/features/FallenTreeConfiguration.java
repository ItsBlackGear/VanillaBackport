package com.blackgear.vanillabackport.common.level.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.treedecorators.TreeDecorator;

import java.util.ArrayList;
import java.util.List;

public class FallenTreeConfiguration implements FeatureConfiguration {
    public static final Codec<FallenTreeConfiguration> CODEC = RecordCodecBuilder.create(
        instance -> instance.group(
                BlockStateProvider.CODEC.fieldOf("trunk_provider").forGetter(config -> config.trunkProvider),
                IntProvider.codec(0, 16).fieldOf("log_length").forGetter(config -> config.logLength),
                TreeDecorator.CODEC.listOf().fieldOf("stump_decorators").forGetter(config -> config.stumpDecorators),
                TreeDecorator.CODEC.listOf().fieldOf("log_decorators").forGetter(config -> config.logDecorators)
            )
            .apply(instance, FallenTreeConfiguration::new)
    );
    public final BlockStateProvider trunkProvider;
    public final IntProvider logLength;
    public final List<TreeDecorator> stumpDecorators;
    public final List<TreeDecorator> logDecorators;

    protected FallenTreeConfiguration(BlockStateProvider trunkProvider, IntProvider logLength, List<TreeDecorator> stumpDecorators, List<TreeDecorator> logDecorators) {
        this.trunkProvider = trunkProvider;
        this.logLength = logLength;
        this.stumpDecorators = stumpDecorators;
        this.logDecorators = logDecorators;
    }

    public static class FallenTreeConfigurationBuilder {
        private final BlockStateProvider trunkProvider;
        private final IntProvider logLength;
        private List<TreeDecorator> stumpDecorators = new ArrayList<>();
        private List<TreeDecorator> logDecorators = new ArrayList<>();

        public FallenTreeConfigurationBuilder(BlockStateProvider trunkProvider, IntProvider logLength) {
            this.trunkProvider = trunkProvider;
            this.logLength = logLength;
        }

        public FallenTreeConfigurationBuilder stumpDecorators(List<TreeDecorator> decorators) {
            this.stumpDecorators = decorators;
            return this;
        }

        public FallenTreeConfigurationBuilder logDecorators(List<TreeDecorator> decorators) {
            this.logDecorators = decorators;
            return this;
        }

        public FallenTreeConfiguration build() {
            return new FallenTreeConfiguration(this.trunkProvider, this.logLength, this.stumpDecorators, this.logDecorators);
        }
    }
}
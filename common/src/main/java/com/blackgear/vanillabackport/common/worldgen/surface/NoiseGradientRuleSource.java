package com.blackgear.vanillabackport.common.worldgen.surface;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.SurfaceRules;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public record NoiseGradientRuleSource(ResourceKey<NormalNoise.NoiseParameters> noise, List<Optional<BlockState>> gradient) implements SurfaceRules.RuleSource {
    private static final Codec<Optional<BlockState>> OPTIONAL_STATE_CODEC = BlockState.CODEC.optionalFieldOf("state").codec();
    private static final MapCodec<NoiseGradientRuleSource> MAP_CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        ResourceKey.codec(Registries.NOISE).fieldOf("noise").forGetter(NoiseGradientRuleSource::noise),
        ExtraCodecs.nonEmptyList(OPTIONAL_STATE_CODEC.listOf()).fieldOf("gradient").forGetter(NoiseGradientRuleSource::gradient)
    ).apply(instance, NoiseGradientRuleSource::new));
    public static final KeyDispatchDataCodec<NoiseGradientRuleSource> CODEC = KeyDispatchDataCodec.of(MAP_CODEC);

    @Override
    public KeyDispatchDataCodec<? extends SurfaceRules.RuleSource> codec() {
        return CODEC;
    }

    @Override
    public SurfaceRules.SurfaceRule apply(SurfaceRules.Context context) {
        NormalNoise noise = context.randomState.getOrCreateNoise(this.noise);
        return new NoiseGradientRule(noise, this.gradient.stream().map(state -> state.orElse(null)).toList());
    }

    private record NoiseGradientRule(NormalNoise noise, List<BlockState> gradient) implements SurfaceRules.SurfaceRule {
        @Override @Nullable
        public BlockState tryApply(int x, int y, int z) {
            double noiseValue = this.noise.getValue(x, y, z);
            double normalizedNoiseValue = (noiseValue + 1.0) / 2.0;
            int index = Mth.floor(normalizedNoiseValue * this.gradient.size());
            return this.gradient.get(Mth.clamp(index, 0, this.gradient.size() - 1));
        }
    }
}
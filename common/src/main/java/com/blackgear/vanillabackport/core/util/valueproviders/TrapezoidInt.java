package com.blackgear.vanillabackport.core.util.valueproviders;

import com.blackgear.vanillabackport.common.registries.ModValueProviders;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviderType;

public class TrapezoidInt extends IntProvider {
    public static final MapCodec<TrapezoidInt> CODEC = RecordCodecBuilder.<TrapezoidInt>mapCodec(instance -> instance.group(
            Codec.INT.fieldOf("min").forGetter(p -> p.minInclusive),
            Codec.INT.fieldOf("max").forGetter(provider -> provider.maxInclusive),
            Codec.INT.fieldOf("plateau").forGetter(p -> p.plateau)
        ).apply(instance, TrapezoidInt::new)
        ).validate(provider -> {
            if (provider.maxInclusive < provider.minInclusive) {
                return DataResult.error(() -> "Max must be larger than min: [" + provider.minInclusive + ", " + provider.maxInclusive + "]");
            } else {
                return provider.plateau > provider.maxInclusive - provider.minInclusive
                    ? DataResult.error(() -> "Plateau can at most be the full span: [" + provider.minInclusive + ", " + provider.maxInclusive + "]")
                    : DataResult.success(provider);
            }
        });

    private final int minInclusive;
    private final int maxInclusive;
    private final int plateau;

    private TrapezoidInt(int minInclusive, int maxInclusive, int plateau) {
        this.minInclusive = minInclusive;
        this.maxInclusive = maxInclusive;
        this.plateau = plateau;
    }

    public static TrapezoidInt of(int min, int max, int plateau) {
        return new TrapezoidInt(min, max, plateau);
    }

    public static TrapezoidInt triangle(int range) {
        return of(-range, range, 0);
    }

    @Override
    public int sample(RandomSource random) {
        if (this.plateau == 0 && this.maxInclusive == -this.minInclusive) {
            return random.nextInt(this.maxInclusive + 1) - random.nextInt(this.maxInclusive + 1);
        } else {
            int range = this.maxInclusive - this.minInclusive;
            if (this.plateau == range) {
                return Mth.randomBetweenInclusive(random, this.minInclusive, this.maxInclusive);
            } else {
                int plateauStart = (range - this.plateau) / 2;
                int plateauEnd = range - plateauStart;
                return this.minInclusive + Mth.randomBetweenInclusive(random, 0, plateauEnd) + Mth.randomBetweenInclusive(random, 0, plateauStart);
            }
        }
    }

    @Override
    public int getMinValue() {
        return this.minInclusive;
    }

    @Override
    public int getMaxValue() {
        return this.maxInclusive;
    }

    @Override
    public IntProviderType<?> getType() {
        return ModValueProviders.TRAPEZOID_INT.get();
    }

    @Override
    public String toString() {
        return "trapezoid(" + this.plateau + ") in [" + this.minInclusive + "-" + this.maxInclusive + "]";
    }
}

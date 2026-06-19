package com.blackgear.vanillabackport.common.api.variant.spawn.check;

import com.blackgear.vanillabackport.common.api.variant.spawn.SpawnCondition;
import com.blackgear.vanillabackport.common.api.variant.spawn.SpawnContext;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.MinMaxBounds;

public record MoonBrightnessCheck(MinMaxBounds.Doubles range) implements SpawnCondition {
    public static final MapCodec<MoonBrightnessCheck> CODEC = RecordCodecBuilder.mapCodec(instance -> instance.group(
        MinMaxBounds.Doubles.CODEC.fieldOf("range").forGetter(MoonBrightnessCheck::range)
    ).apply(instance, MoonBrightnessCheck::new));

    @Override
    public boolean test(SpawnContext context) {
        return this.range.matches(context.level().getLevel().getMoonBrightness());
    }

    @Override
    public MapCodec<? extends SpawnCondition> codec() {
        return CODEC;
    }
}
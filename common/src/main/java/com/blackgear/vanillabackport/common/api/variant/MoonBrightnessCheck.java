package com.blackgear.vanillabackport.common.api.variant;

import com.blackgear.vanillabackport.core.util.codec.AdditionalCodecs;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.MinMaxBounds;

public record MoonBrightnessCheck(MinMaxBounds.Doubles range) implements SpawnCondition {
    public static final Codec<MoonBrightnessCheck> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        AdditionalCodecs.DOUBLES.fieldOf("range").forGetter(MoonBrightnessCheck::range)
    ).apply(instance, MoonBrightnessCheck::new));

    @Override
    public boolean test(SpawnContext context) {
        return this.range.matches(context.level().getLevel().getMoonBrightness());
    }

    @Override
    public Codec<? extends SpawnCondition> codec() {
        return CODEC;
    }
}
package com.blackgear.vanillabackport.common.level.components;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record UseEffects(
    boolean canSprint,
    boolean interactVibrations,
    float speedMultiplier
) {
    public static final UseEffects DEFAULT = new UseEffects(false, true, 0.2F);
    public static final Codec<UseEffects> CODEC = RecordCodecBuilder.create(instance -> instance.group(
        Codec.BOOL.optionalFieldOf("can_sprint", DEFAULT.canSprint).forGetter(UseEffects::canSprint),
        Codec.BOOL.optionalFieldOf("interact_vibrations", DEFAULT.interactVibrations).forGetter(UseEffects::interactVibrations),
        Codec.floatRange(0.0F, 1.0F).optionalFieldOf("speed_multiplier", DEFAULT.speedMultiplier).forGetter(UseEffects::speedMultiplier)
    ).apply(instance, UseEffects::new));
    public static final StreamCodec<ByteBuf, UseEffects> STREAM_CODEC = StreamCodec.composite(
        ByteBufCodecs.BOOL, UseEffects::canSprint,
        ByteBufCodecs.BOOL, UseEffects::interactVibrations,
        ByteBufCodecs.FLOAT, UseEffects::speedMultiplier,
        UseEffects::new
    );
}
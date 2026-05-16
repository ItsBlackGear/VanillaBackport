package com.blackgear.vanillabackport.client.level.particles.particleoptions;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

public record GeyserParticleOptions(ParticleType<GeyserParticleOptions> type, int waterBlocks) implements ParticleOptions {
    public static MapCodec<GeyserParticleOptions> codec(ParticleType<GeyserParticleOptions> type) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.POSITIVE_INT.fieldOf("water_blocks").forGetter(GeyserParticleOptions::waterBlocks)
        ).apply(instance, waterBlocks -> new GeyserParticleOptions(type, waterBlocks)));
    }

    public static StreamCodec<? super ByteBuf, GeyserParticleOptions> streamCodec(ParticleType<GeyserParticleOptions> type) {
        return StreamCodec.composite(ByteBufCodecs.INT, options -> options.waterBlocks, waterBlocks -> new GeyserParticleOptions(type, waterBlocks));
    }

    @Override
    public ParticleType<?> getType() {
        return this.type;
    }
}
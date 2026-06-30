package com.blackgear.vanillabackport.client.level.particle.particleoptions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;

public record GeyserBaseParticleOptions(ParticleType<GeyserBaseParticleOptions> type, int waterBlocks, float burstImpulseBase) implements ParticleOptions {
    public static MapCodec<GeyserBaseParticleOptions> codec(ParticleType<GeyserBaseParticleOptions> type) {
        return RecordCodecBuilder.mapCodec(instance -> instance.group(
            ExtraCodecs.POSITIVE_INT.fieldOf("water_blocks").forGetter(GeyserBaseParticleOptions::waterBlocks),
            Codec.FLOAT.fieldOf("burst_impulse_base").forGetter(GeyserBaseParticleOptions::burstImpulseBase)
        ).apply(instance, (waterBlocks, burstImpulseBase) -> new GeyserBaseParticleOptions(type, waterBlocks, burstImpulseBase)));
    }

    public static StreamCodec<? super ByteBuf, GeyserBaseParticleOptions> streamCodec(ParticleType<GeyserBaseParticleOptions> type) {
        return StreamCodec.composite(
            ByteBufCodecs.INT, options -> options.waterBlocks,
            ByteBufCodecs.FLOAT, options -> options.burstImpulseBase,
            (waterBlocks, burstImpulseBase) -> new GeyserBaseParticleOptions(type, waterBlocks, burstImpulseBase)
        );
    }

    @Override
    public ParticleType<?> getType() {
        return this.type;
    }
}
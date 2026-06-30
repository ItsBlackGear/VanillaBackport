package com.blackgear.vanillabackport.client.level.particle.particleoptions;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;

import java.util.Locale;

public record GeyserBaseParticleOptions(ParticleType<GeyserBaseParticleOptions> type, int waterBlocks, float burstImpulseBase) implements ParticleOptions {
    public static Codec<GeyserBaseParticleOptions> codec(ParticleType<GeyserBaseParticleOptions> type) {
        return RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.POSITIVE_INT.fieldOf("water_blocks").forGetter(GeyserBaseParticleOptions::waterBlocks),
            Codec.FLOAT.fieldOf("burst_impulse_base").forGetter(GeyserBaseParticleOptions::burstImpulseBase)
        ).apply(instance, (waterBlocks, burstImpulseBase) -> new GeyserBaseParticleOptions(type, waterBlocks, burstImpulseBase)));
    }

    public static final Deserializer<GeyserBaseParticleOptions> DESERIALIZER = new Deserializer<>() {
        @Override
        public GeyserBaseParticleOptions fromCommand(ParticleType<GeyserBaseParticleOptions> type, StringReader reader) throws CommandSyntaxException {
            return new GeyserBaseParticleOptions(type, reader.readInt(), reader.readFloat());
        }

        @Override
        public GeyserBaseParticleOptions fromNetwork(ParticleType<GeyserBaseParticleOptions> type, FriendlyByteBuf buffer) {
            return new GeyserBaseParticleOptions(type, buffer.readInt(), buffer.readFloat());
        }
    };

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeInt(this.waterBlocks);
        buffer.writeFloat(this.burstImpulseBase);
    }

    @Override
    public ParticleType<?> getType() {
        return this.type;
    }

    @Override
    public String writeToString() {
        return String.format(
            Locale.ROOT,
            "%s %d %f",
            BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()),
            this.waterBlocks,
            this.burstImpulseBase
        );
    }
}
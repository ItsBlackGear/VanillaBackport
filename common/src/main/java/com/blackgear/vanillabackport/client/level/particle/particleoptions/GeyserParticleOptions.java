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

public record GeyserParticleOptions(ParticleType<GeyserParticleOptions> type, int waterBlocks) implements ParticleOptions {
    public static Codec<GeyserParticleOptions> codec(ParticleType<GeyserParticleOptions> type) {
        return RecordCodecBuilder.create(instance -> instance.group(
            ExtraCodecs.POSITIVE_INT.fieldOf("water_blocks").forGetter(GeyserParticleOptions::waterBlocks)
        ).apply(instance, waterBlocks -> new GeyserParticleOptions(type, waterBlocks)));
    }

    public static final Deserializer<GeyserParticleOptions> DESERIALIZER = new Deserializer<>() {
        @Override
        public GeyserParticleOptions fromCommand(ParticleType<GeyserParticleOptions> type, StringReader reader) throws CommandSyntaxException {
            return new GeyserParticleOptions(type, reader.readInt());
        }

        @Override
        public GeyserParticleOptions fromNetwork(ParticleType<GeyserParticleOptions> type, FriendlyByteBuf buffer) {
            return new GeyserParticleOptions(type, buffer.readInt());
        }
    };

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeInt(this.waterBlocks);
    }

    @Override
    public ParticleType<?> getType() {
        return this.type;
    }

    @Override
    public String writeToString() {
        return String.format(
            Locale.ROOT,
            "%s %d",
            BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()),
            this.waterBlocks
        );
    }
}
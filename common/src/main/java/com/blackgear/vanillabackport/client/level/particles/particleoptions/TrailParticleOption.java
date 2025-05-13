package com.blackgear.vanillabackport.client.level.particles.particleoptions;

import com.blackgear.vanillabackport.client.registries.ModParticles;
import com.blackgear.vanillabackport.core.util.AdditionalCodecs;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.phys.Vec3;

public class TrailParticleOption extends TrailParticleOptionsBase {
    public static final Codec<TrailParticleOption> CODEC = RecordCodecBuilder.create(instance ->
        instance.group(
            Vec3.CODEC.fieldOf("target").forGetter(option -> option.target),
            AdditionalCodecs.RGB_COLOR_CODEC.fieldOf("color").forGetter(option -> option.color),
            ExtraCodecs.POSITIVE_INT.fieldOf("duration").forGetter(option -> option.duration)
        ).apply(instance, TrailParticleOption::new)
    );
    public static final Deserializer<TrailParticleOption> DESERIALIZER = new Deserializer<>() {
        @Override
        public TrailParticleOption fromCommand(ParticleType<TrailParticleOption> particleType, StringReader reader) throws CommandSyntaxException {
            Vec3 target = readVec3(reader);
            reader.expect(' ');
            int color = reader.readInt();
            reader.expect(' ');
            int duration = reader.readInt();
            return new TrailParticleOption(target, color, duration);
        }

        @Override
        public TrailParticleOption fromNetwork(ParticleType<TrailParticleOption> particleType, FriendlyByteBuf buffer) {
            return new TrailParticleOption(readVec3(buffer), buffer.readInt(), buffer.readInt());
        }
    };

    public TrailParticleOption(Vec3 target, int color, int duration) {
        super(target, color, duration);
    }

    @Override
    public ParticleType<?> getType() {
        return ModParticles.TRAIL.get();
    }
}
package com.blackgear.vanillabackport.client.level.particles.particleoptions;

import com.blackgear.vanillabackport.client.registries.ModParticles;
import com.blackgear.vanillabackport.core.util.AdditionalCodecs;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.phys.Vec3;

public record TrailParticleOption(Vec3 target, int color, int duration) implements ParticleOptions {
    public static final MapCodec<TrailParticleOption> CODEC = RecordCodecBuilder.mapCodec(instance ->
        instance.group(
            Vec3.CODEC.fieldOf("target").forGetter(TrailParticleOption::target),
            AdditionalCodecs.RGB_COLOR_CODEC.fieldOf("color").forGetter(TrailParticleOption::color),
            ExtraCodecs.POSITIVE_INT.fieldOf("duration").forGetter(TrailParticleOption::duration)
        ).apply(instance, TrailParticleOption::new)
    );
    public static final StreamCodec<RegistryFriendlyByteBuf, TrailParticleOption> STREAM_CODEC = StreamCodec.composite(
        AdditionalCodecs.VEC3_STREAM_CODEC,
        TrailParticleOption::target,
        ByteBufCodecs.INT,
        TrailParticleOption::color,
        ByteBufCodecs.VAR_INT,
        TrailParticleOption::duration,
        TrailParticleOption::new
    );

    @Override
    public ParticleType<?> getType() {
        return ModParticles.TRAIL.get();
    }
}
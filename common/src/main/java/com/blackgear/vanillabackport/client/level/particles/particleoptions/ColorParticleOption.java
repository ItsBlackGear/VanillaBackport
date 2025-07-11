package com.blackgear.vanillabackport.client.level.particles.particleoptions;

import com.blackgear.vanillabackport.core.util.AdditionalCodecs;
import com.blackgear.vanillabackport.core.util.ColorUtils;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.util.FastColor;

import java.util.Locale;

public class ColorParticleOption implements ParticleOptions {
    public static Codec<ColorParticleOption> codec(ParticleType<ColorParticleOption> type) {
        return AdditionalCodecs.ARGB_COLOR_CODEC.xmap(integer -> new ColorParticleOption(type, integer), option -> option.color)
            .fieldOf("color").codec();
    }

    public static final Deserializer<ColorParticleOption> DESERIALIZER = new Deserializer<>() {
        @Override
        public ColorParticleOption fromCommand(ParticleType<ColorParticleOption> type, StringReader reader) throws CommandSyntaxException {
            int color = reader.readInt();
            return new ColorParticleOption(type, color);
        }

        @Override
        public ColorParticleOption fromNetwork(ParticleType<ColorParticleOption> type, FriendlyByteBuf buffer) {
            return new ColorParticleOption(type, buffer.readInt());
        }
    };

    private final ParticleType<ColorParticleOption> type;
    private final int color;

    public ColorParticleOption(ParticleType<ColorParticleOption> type, int color) {
        this.type = type;
        this.color = color;
    }

    @Override
    public ParticleType<?> getType() {
        return this.type;
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeInt(this.color);
    }

    @Override
    public String writeToString() {
        return String.format(
            Locale.ROOT,
            "%s %d",
            BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()),
            this.color
        );
    }

    public float getRed() {
        return FastColor.ARGB32.red(this.color) / 255.0F;
    }

    public float getGreen() {
        return FastColor.ARGB32.green(this.color) / 255.0F;
    }

    public float getBlue() {
        return FastColor.ARGB32.blue(this.color) / 255.0F;
    }

    public float getAlpha() {
        return FastColor.ARGB32.alpha(this.color) / 255.0F;
    }

    public static ColorParticleOption create(ParticleType<ColorParticleOption> type, int color) {
        return new ColorParticleOption(type, color);
    }

    public static ColorParticleOption create(ParticleType<ColorParticleOption> type, float red, float green, float blue) {
        return new ColorParticleOption(type, ColorUtils.colorFromFloat(1.0F, red, green, blue));
    }
}

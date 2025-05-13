package com.blackgear.vanillabackport.client.level.particles.particleoptions;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.phys.Vec3;

import java.util.Locale;

public abstract class TrailParticleOptionsBase implements ParticleOptions {
    protected final Vec3 target;
    protected final int color;
    protected final int duration;

    protected TrailParticleOptionsBase(Vec3 target, int color, int duration) {
        this.target = target;
        this.color = color;
        this.duration = duration;
    }

    public static Vec3 readVec3(StringReader reader) throws CommandSyntaxException {
        reader.expect(' ');
        float x = reader.readFloat();
        reader.expect(' ');
        float y = reader.readFloat();
        reader.expect(' ');
        float z = reader.readFloat();
        return new Vec3(x, y, z);
    }

    public static Vec3 readVec3(FriendlyByteBuf buffer) {
        return new Vec3(buffer.readFloat(), buffer.readFloat(), buffer.readFloat());
    }

    @Override
    public void writeToNetwork(FriendlyByteBuf buffer) {
        buffer.writeFloat((float)this.target.x());
        buffer.writeFloat((float)this.target.y());
        buffer.writeFloat((float)this.target.z());
        buffer.writeInt(this.color);
        buffer.writeInt(this.duration);
    }

    @Override
    public String writeToString() {
        return String.format(
            Locale.ROOT,
            "%s %.2f %.2f %.2f %d %d",
            BuiltInRegistries.PARTICLE_TYPE.getKey(this.getType()),
            this.target.x(),
            this.target.y(),
            this.target.z(),
            this.color,
            this.duration
        );
    }

    public Vec3 target() {
        return this.target;
    }

    public int color() {
        return this.color;
    }

    public int duration() {
        return this.duration;
    }
}
package com.blackgear.vanillabackport.core.network;

import com.blackgear.platform.core.network.base.Packet;
import com.blackgear.platform.core.network.base.PacketContext;
import com.blackgear.platform.core.network.base.PacketHandler;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.network.handlers.ClientboundPacketListener;
import com.blackgear.vanillabackport.core.util.Utilities;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ClientboundWindBurstPacket implements Packet<ClientboundWindBurstPacket> {
    public static final ResourceLocation ID = VanillaBackport.resource("wind_burst");
    public static final ClientboundWindBurstHandler HANDLER = new ClientboundWindBurstHandler();
    public final double x;
    public final double y;
    public final double z;
    public final float power;
    public final List<BlockPos> toBlow;
    public final float knockbackX;
    public final float knockbackY;
    public final float knockbackZ;
    public final ParticleOptions smallExplosionParticles;
    public final ParticleOptions largeExplosionParticles;
    public final Explosion.BlockInteraction blockInteraction;
    public final Holder<SoundEvent> explosionSound;
    
    public ClientboundWindBurstPacket(
        double x,
        double y,
        double z,
        float radius,
        List<BlockPos> toBlow,
        @Nullable Vec3 knockback,
        Explosion.BlockInteraction interaction,
        ParticleOptions smallParticle,
        ParticleOptions largeParticle,
        Holder<SoundEvent> sound
    ) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.power = radius;
        this.toBlow = Lists.newArrayList(toBlow);
        this.explosionSound = sound;
        if (knockback != null) {
            this.knockbackX = (float) knockback.x;
            this.knockbackY = (float) knockback.y;
            this.knockbackZ = (float) knockback.z;
        } else {
            this.knockbackX = 0.0F;
            this.knockbackY = 0.0F;
            this.knockbackZ = 0.0F;
        }
        
        this.blockInteraction = interaction;
        this.smallExplosionParticles = smallParticle;
        this.largeExplosionParticles = largeParticle;
    }
    
    @Override
    public ResourceLocation getId() {
        return ID;
    }
    
    @Override
    public PacketHandler<ClientboundWindBurstPacket> getHandler() {
        return HANDLER;
    }
    
    public static class ClientboundWindBurstHandler implements PacketHandler<ClientboundWindBurstPacket> {
        @Override
        public void encode(ClientboundWindBurstPacket packet, FriendlyByteBuf buf) {
            buf.writeDouble(packet.x);
            buf.writeDouble(packet.y);
            buf.writeDouble(packet.z);
            buf.writeFloat(packet.power);
            int i = Mth.floor(packet.x);
            int j = Mth.floor(packet.y);
            int k = Mth.floor(packet.z);
            buf.writeCollection(packet.toBlow, (buf1, pos) -> {
                int l = pos.getX() - i;
                int i1 = pos.getY() - j;
                int j1 = pos.getZ() - k;
                buf1.writeByte(l);
                buf1.writeByte(i1);
                buf1.writeByte(j1);
            });
            buf.writeFloat(packet.knockbackX);
            buf.writeFloat(packet.knockbackY);
            buf.writeFloat(packet.knockbackZ);
            buf.writeEnum(packet.blockInteraction);
            buf.writeId(BuiltInRegistries.PARTICLE_TYPE, packet.smallExplosionParticles.getType());
            buf.writeId(BuiltInRegistries.PARTICLE_TYPE, packet.largeExplosionParticles.getType());
            buf.writeId(BuiltInRegistries.SOUND_EVENT.asHolderIdMap(), packet.explosionSound, (buf1, sound) -> sound.writeToNetwork(buf1));
        }
        
        @Override
        public ClientboundWindBurstPacket decode(FriendlyByteBuf buf) {
            var x = buf.readDouble();
            var y = buf.readDouble();
            var z = buf.readDouble();
            var power = buf.readFloat();
            int i = Mth.floor(x);
            int j = Mth.floor(y);
            int k = Mth.floor(z);
            var toBlow = buf.readList((p_178850_) -> {
                int l = p_178850_.readByte() + i;
                int i1 = p_178850_.readByte() + j;
                int j1 = p_178850_.readByte() + k;
                return new BlockPos(l, i1, j1);
            });
            var knockbackX = buf.readFloat();
            var knockbackY = buf.readFloat();
            var knockbackZ = buf.readFloat();
            var blockInteraction = buf.readEnum(Explosion.BlockInteraction.class);
            var smallExplosionParticles = Utilities.BufferUtils.readParticle(buf, buf.readById(BuiltInRegistries.PARTICLE_TYPE));
            var largeExplosionParticles = Utilities.BufferUtils.readParticle(buf, buf.readById(BuiltInRegistries.PARTICLE_TYPE));
            var explosionSound = buf.readById(BuiltInRegistries.SOUND_EVENT.asHolderIdMap(), SoundEvent::readFromNetwork);
            
            return new ClientboundWindBurstPacket(x, y, z, power, toBlow, new Vec3(knockbackX, knockbackY, knockbackZ), blockInteraction, smallExplosionParticles, largeExplosionParticles, explosionSound);
        }
        
        @Override
        public PacketContext handle(ClientboundWindBurstPacket packet) {
            return (player, level) -> {
                if (level.isClientSide()) {
                    ClientboundPacketListener.INSTANCE.handleExplosion(packet, player, level);
                }
            };
        }
    }
}
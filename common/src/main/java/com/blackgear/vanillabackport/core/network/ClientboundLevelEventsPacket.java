package com.blackgear.vanillabackport.core.network;

import com.blackgear.platform.core.networking.PayloadContext;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.network.handlers.ClientboundPayloadListener;
import com.blackgear.vanillabackport.core.network.handlers.LevelEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record ClientboundLevelEventsPacket(LevelEvents event, BlockPos pos, int data) implements CustomPacketPayload {
    public static final Type<ClientboundLevelEventsPacket> TYPE = new Type<>(VanillaBackport.resource("level_events"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundLevelEventsPacket> STREAM_CODEC = StreamCodec.ofMember(ClientboundLevelEventsPacket::write, ClientboundLevelEventsPacket::new);
    
    public ClientboundLevelEventsPacket(LevelEvents event, BlockPos pos, int data) {
        this.event = event;
        this.pos = pos.immutable();
        this.data = data;
    }
    
    public ClientboundLevelEventsPacket(RegistryFriendlyByteBuf buf) {
        this(buf.readEnum(LevelEvents.class), buf.readBlockPos(), buf.readInt());
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeEnum(this.event);
        buf.writeBlockPos(this.pos);
        buf.writeInt(this.data);
    }
    
    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handler(ClientboundLevelEventsPacket payload, PayloadContext context) {
        ClientboundPayloadListener.execute(() -> ClientboundPayloadListener.handleLevelEvents(payload, context.player(), context.player().level()), context);
    }
}
package com.blackgear.vanillabackport.core.network;

import com.blackgear.platform.core.network.base.Packet;
import com.blackgear.platform.core.network.base.PacketContext;
import com.blackgear.platform.core.network.base.PacketHandler;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.network.handlers.ClientboundPacketListener;
import com.blackgear.vanillabackport.core.network.handlers.LevelEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record ClientboundLevelEventsPacket(LevelEvents type, BlockPos pos, int data) implements Packet<ClientboundLevelEventsPacket> {
    public static final ResourceLocation ID = VanillaBackport.resource("level_events");
    public static final ClientboundLevelEventsHandler HANDLER = new ClientboundLevelEventsHandler();
    
    public ClientboundLevelEventsPacket(LevelEvents type, BlockPos pos, int data) {
        this.type = type;
        this.pos = pos.immutable();
        this.data = data;
    }
    
    @Override
    public ResourceLocation getId() {
        return ID;
    }
    
    @Override
    public PacketHandler<ClientboundLevelEventsPacket> getHandler() {
        return HANDLER;
    }
    
    public static class ClientboundLevelEventsHandler implements PacketHandler<ClientboundLevelEventsPacket> {
        @Override
        public void encode(ClientboundLevelEventsPacket packet, FriendlyByteBuf buf) {
            buf.writeEnum(packet.type);
            buf.writeBlockPos(packet.pos);
            buf.writeInt(packet.data);
        }
        
        @Override
        public ClientboundLevelEventsPacket decode(FriendlyByteBuf buf) {
            return new ClientboundLevelEventsPacket(buf.readEnum(LevelEvents.class), buf.readBlockPos(), buf.readInt());
        }
        
        @Override
        public PacketContext handle(ClientboundLevelEventsPacket packet) {
            return (player, level) -> {
                if (level.isClientSide()) {
                    ClientboundPacketListener.INSTANCE.handleLevelEvents(packet, player, level);
                }
            };
        }
    }
}
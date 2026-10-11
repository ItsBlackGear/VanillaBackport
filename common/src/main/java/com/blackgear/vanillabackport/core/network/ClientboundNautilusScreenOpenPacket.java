package com.blackgear.vanillabackport.core.network;

import com.blackgear.platform.core.network.base.Packet;
import com.blackgear.platform.core.network.base.PacketContext;
import com.blackgear.platform.core.network.base.PacketHandler;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.network.handlers.ClientboundPacketListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record ClientboundNautilusScreenOpenPacket(int containerId, int size, int entityId) implements Packet<ClientboundNautilusScreenOpenPacket> {
    public static final ResourceLocation ID = VanillaBackport.resource("nautilus_screen_open");
    public static final ClientboundNautilusScreenOpenHandler HANDLER = new ClientboundNautilusScreenOpenHandler();
    
    @Override
    public ResourceLocation getId() {
        return ID;
    }
    
    @Override
    public PacketHandler<ClientboundNautilusScreenOpenPacket> getHandler() {
        return HANDLER;
    }
    
    public static class ClientboundNautilusScreenOpenHandler implements PacketHandler<ClientboundNautilusScreenOpenPacket> {
        @Override
        public void encode(ClientboundNautilusScreenOpenPacket packet, FriendlyByteBuf buf) {
            buf.writeByte(packet.containerId());
            buf.writeVarInt(packet.size());
            buf.writeInt(packet.entityId());
        }
        
        @Override
        public ClientboundNautilusScreenOpenPacket decode(FriendlyByteBuf buf) {
            return new ClientboundNautilusScreenOpenPacket(buf.readByte(), buf.readVarInt(), buf.readInt());
        }
        
        @Override
        public PacketContext handle(ClientboundNautilusScreenOpenPacket packet) {
            return (player, level) -> {
                if (level.isClientSide()) {
                    ClientboundPacketListener.handleNautilusScreenOpen(packet, player, level);
                }
            };
        }
    }
}
package com.blackgear.vanillabackport.core.network;

import com.blackgear.platform.core.network.base.Packet;
import com.blackgear.platform.core.network.base.PacketContext;
import com.blackgear.platform.core.network.base.PacketHandler;
import com.blackgear.vanillabackport.client.level.gui.inventory.NautilusInventoryScreen;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.nautilus.AbstractNautilus;
import com.blackgear.vanillabackport.common.level.inventory.NautilusInventoryMenu;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;

public record ClientboundNautilusScreenOpenPacket(int containerId, int size, int entityId) implements Packet<ClientboundNautilusScreenOpenPacket> {
    public static final ResourceLocation ID = VanillaBackport.resource("nautilus_screen_open");
    public static final Handler HANDLER = new Handler();
    
    @Override
    public ResourceLocation getId() {
        return ID;
    }
    
    @Override
    public PacketHandler<ClientboundNautilusScreenOpenPacket> getHandler() {
        return HANDLER;
    }
    
    public static class Handler implements PacketHandler<ClientboundNautilusScreenOpenPacket> {
        @Override
        public void encode(ClientboundNautilusScreenOpenPacket packet, FriendlyByteBuf buf) {
            buf.writeByte(packet.containerId);
            buf.writeVarInt(packet.size);
            buf.writeInt(packet.entityId);
        }
        
        @Override
        public ClientboundNautilusScreenOpenPacket decode(FriendlyByteBuf buf) {
            return new ClientboundNautilusScreenOpenPacket(buf.readByte(), buf.readVarInt(), buf.readInt());
        }
        
        @Override
        public PacketContext handle(ClientboundNautilusScreenOpenPacket packet) {
            return (player, level) -> {
                Entity entity = level.getEntity(packet.entityId);
                if (entity instanceof AbstractNautilus nautilus) {
                    SimpleContainer container = new SimpleContainer(packet.size);
                    NautilusInventoryMenu menu = new NautilusInventoryMenu(packet.containerId, player.getInventory(), container, nautilus);
                    player.containerMenu = menu;
                    Minecraft.getInstance().setScreen(new NautilusInventoryScreen(menu, player.getInventory(), nautilus));
                }
            };
        }
    }
}
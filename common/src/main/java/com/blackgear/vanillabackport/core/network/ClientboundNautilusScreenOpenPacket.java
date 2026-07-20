package com.blackgear.vanillabackport.core.network;

import com.blackgear.platform.core.networking.PayloadContext;
import com.blackgear.vanillabackport.client.level.gui.inventory.NautilusInventoryScreen;
import com.blackgear.vanillabackport.common.level.entity.mob.animal.nautilus.AbstractNautilus;
import com.blackgear.vanillabackport.common.level.inventory.NautilusInventoryMenu;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public record ClientboundNautilusScreenOpenPacket(int containerId, int size, int entityId) implements CustomPacketPayload {
    public static final Type<ClientboundNautilusScreenOpenPacket> TYPE = new Type<>(VanillaBackport.resource("nautilus_screen_open"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundNautilusScreenOpenPacket> STREAM_CODEC = StreamCodec.ofMember(ClientboundNautilusScreenOpenPacket::write, ClientboundNautilusScreenOpenPacket::new);

    public ClientboundNautilusScreenOpenPacket(RegistryFriendlyByteBuf buf) {
        this(buf.readByte(), buf.readVarInt(), buf.readInt());
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeByte(this.containerId);
        buf.writeVarInt(this.size);
        buf.writeInt(this.entityId);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handler(ClientboundNautilusScreenOpenPacket payload, PayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            Level level = player.level();
            
            Entity entity = level.getEntity(payload.entityId);
            if (entity instanceof AbstractNautilus nautilus) {
                SimpleContainer container = new SimpleContainer(payload.size);
                NautilusInventoryMenu menu = new NautilusInventoryMenu(payload.containerId, player.getInventory(), container, nautilus);
                player.containerMenu = menu;
                Minecraft.getInstance().setScreen(new NautilusInventoryScreen(menu, player.getInventory(), nautilus));
            }
        });
    }
}
package com.blackgear.vanillabackport.core.network;

import com.blackgear.platform.core.network.base.Packet;
import com.blackgear.platform.core.network.base.PacketContext;
import com.blackgear.platform.core.network.base.PacketHandler;
import com.blackgear.vanillabackport.common.api.extensions.entity.spear.SpearSwingTracker;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public record ServerboundUpdateSpearSwingPacket(boolean isAttack) implements Packet<ServerboundUpdateSpearSwingPacket> {
    public static final ResourceLocation ID = VanillaBackport.resource("update_spear_swing");
    public static final Handler HANDLER = new Handler();

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public PacketHandler<ServerboundUpdateSpearSwingPacket> getHandler() {
        return HANDLER;
    }

    public static class Handler implements PacketHandler<ServerboundUpdateSpearSwingPacket> {
        @Override
        public void encode(ServerboundUpdateSpearSwingPacket packet, FriendlyByteBuf buf) {
            buf.writeBoolean(packet.isAttack());
        }

        @Override
        public ServerboundUpdateSpearSwingPacket decode(FriendlyByteBuf buf) {
            return new ServerboundUpdateSpearSwingPacket(buf.readBoolean());
        }

        @Override
        public PacketContext handle(ServerboundUpdateSpearSwingPacket packet) {
            return (player, level) -> {
                ((SpearSwingTracker) player).vb$setAttackSwing(packet.isAttack());
                NetworkHandler.DEFAULT_CHANNEL.sendToPlayersTrackingEntity(new ClientboundUpdateSpearSwingPacket(player.getId(), packet.isAttack()), player);
            };
        }
    }
}
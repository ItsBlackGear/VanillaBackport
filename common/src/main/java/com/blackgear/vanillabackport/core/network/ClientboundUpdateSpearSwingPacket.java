package com.blackgear.vanillabackport.core.network;

import com.blackgear.platform.core.network.base.Packet;
import com.blackgear.platform.core.network.base.PacketContext;
import com.blackgear.platform.core.network.base.PacketHandler;
import com.blackgear.vanillabackport.common.api.extensions.entity.spear.SpearSwingTracker;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;

public record ClientboundUpdateSpearSwingPacket(int entityId, boolean isAttack) implements Packet<ClientboundUpdateSpearSwingPacket> {
    public static final ResourceLocation ID = VanillaBackport.resource("client_update_spear_swing");
    public static final Handler HANDLER = new Handler();

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public PacketHandler<ClientboundUpdateSpearSwingPacket> getHandler() {
        return HANDLER;
    }

    public static class Handler implements PacketHandler<ClientboundUpdateSpearSwingPacket> {
        @Override
        public void encode(ClientboundUpdateSpearSwingPacket packet, FriendlyByteBuf buf) {
            buf.writeInt(packet.entityId());
            buf.writeBoolean(packet.isAttack());
        }

        @Override
        public ClientboundUpdateSpearSwingPacket decode(FriendlyByteBuf buf) {
            return new ClientboundUpdateSpearSwingPacket(buf.readInt(), buf.readBoolean());
        }

        @Override
        public PacketContext handle(ClientboundUpdateSpearSwingPacket packet) {
            return (player, level) -> {
                if (level.isClientSide()) {
                    Entity entity = level.getEntity(packet.entityId());
                    if (entity instanceof LivingEntity living) {
                        ((SpearSwingTracker) living).vb$setAttackSwing(packet.isAttack());
                    }
                }
            };
        }
    }
}
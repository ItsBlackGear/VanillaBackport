package com.blackgear.vanillabackport.core.network;

import com.blackgear.platform.core.network.base.Packet;
import com.blackgear.platform.core.network.base.PacketContext;
import com.blackgear.platform.core.network.base.PacketHandler;
import com.blackgear.vanillabackport.common.api.extensions.entity.movement.MotionAwareEntity;
import com.blackgear.vanillabackport.common.level.block_entities.CrafterBlockEntity;
import com.blackgear.vanillabackport.common.level.inventory.CrafterMenu;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public record ServerboundContainerSlotStateChangedPacket(int slotId, int containerId, boolean newState) implements Packet<ServerboundContainerSlotStateChangedPacket> {
    public static final ResourceLocation ID = VanillaBackport.resource("container_slot_state_changed");
    public static final Handler HANDLER = new Handler();

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public PacketHandler<ServerboundContainerSlotStateChangedPacket> getHandler() {
        return HANDLER;
    }

    public static class Handler implements PacketHandler<ServerboundContainerSlotStateChangedPacket> {
        @Override
        public void encode(ServerboundContainerSlotStateChangedPacket packet, FriendlyByteBuf buf) {
            buf.writeVarInt(packet.slotId);
            buf.writeVarInt(packet.containerId);
            buf.writeBoolean(packet.newState);
        }

        @Override
        public ServerboundContainerSlotStateChangedPacket decode(FriendlyByteBuf buf) {
            return new ServerboundContainerSlotStateChangedPacket(buf.readVarInt(), buf.readVarInt(), buf.readBoolean());
        }

        @Override
        public PacketContext handle(ServerboundContainerSlotStateChangedPacket packet) {
            return (player, level) -> {
                if (!player.isSpectator() && packet.containerId() == player.containerMenu.containerId) {
                    if (player.containerMenu instanceof CrafterMenu menu && menu.getContainer() instanceof CrafterBlockEntity crafter) {
                        crafter.setSlotState(packet.slotId(), packet.newState());
                    }
                }
            };
        }
    }
}

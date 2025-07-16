package com.blackgear.vanillabackport.core.network;

import com.blackgear.platform.core.network.base.Packet;
import com.blackgear.platform.core.network.base.PacketContext;
import com.blackgear.platform.core.network.base.PacketHandler;
import com.blackgear.vanillabackport.common.api.bundle.BundleContents;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public record ServerboundSelectBundleItemPacket(int slotId, int selectedItemIndex) implements Packet<ServerboundSelectBundleItemPacket> {
    public static final ResourceLocation ID = VanillaBackport.resource("select_bundle_item");
    public static final Handler HANDLER = new Handler();

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public PacketHandler<ServerboundSelectBundleItemPacket> getHandler() {
        return HANDLER;
    }

    public static class Handler implements PacketHandler<ServerboundSelectBundleItemPacket> {
        @Override
        public void encode(ServerboundSelectBundleItemPacket packet, FriendlyByteBuf buf) {
            buf.writeVarInt(packet.slotId);
            buf.writeVarInt(packet.selectedItemIndex);
        }

        @Override
        public ServerboundSelectBundleItemPacket decode(FriendlyByteBuf buf) {
            int slotId = buf.readVarInt();
            int selectedItemIndex = buf.readVarInt();

            if (selectedItemIndex < 0 && selectedItemIndex != -1) {
                throw new IllegalArgumentException("Invalid selectedItemIndex: " + selectedItemIndex);
            }

            return new ServerboundSelectBundleItemPacket(slotId, selectedItemIndex);
        }

        @Override
        public PacketContext handle(ServerboundSelectBundleItemPacket packet) {
            return (player, level) -> {
                NonNullList<Slot> slots = player.containerMenu.slots;
                int slotId = packet.slotId;
                int selectedItemIndex = packet.selectedItemIndex;

                if (slotId >= 0 && slotId < slots.size()) {
                    ItemStack stack = slots.get(slotId).getItem();
                    BundleContents.toggleSelectedItem(stack, selectedItemIndex);
                }
            };
        }
    }
}
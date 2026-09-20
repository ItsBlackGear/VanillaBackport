package com.blackgear.vanillabackport.core.network;

import com.blackgear.platform.core.networking.PayloadContext;
import com.blackgear.platform.core.networking.PayloadDistributor;
import com.blackgear.vanillabackport.common.api.extensions.entity.spear.SpearSwingTracker;
import com.blackgear.vanillabackport.common.api.modules.bundle_ui.BundleFeatures;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.core.NonNullList;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public record ServerboundUpdateSpearSwingPacket(boolean isAttack) implements CustomPacketPayload {
    public static final Type<ServerboundUpdateSpearSwingPacket> TYPE = new Type<>(VanillaBackport.resource("update_spear_swing"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ServerboundUpdateSpearSwingPacket> STREAM_CODEC = StreamCodec.ofMember(ServerboundUpdateSpearSwingPacket::write, ServerboundUpdateSpearSwingPacket::new);

    public ServerboundUpdateSpearSwingPacket(RegistryFriendlyByteBuf buf) {
        this(buf.readBoolean());
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeBoolean(this.isAttack);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handler(ServerboundUpdateSpearSwingPacket payload, PayloadContext context) {
        context.enqueueWork(() -> {
            Player player = context.player();
            ((SpearSwingTracker) player).vb$setAttackSwing(payload.isAttack());
            PayloadDistributor.sendToPlayersTrackingEntity(player, new ClientboundUpdateSpearSwingPacket(player.getId(), payload.isAttack()));
        });
    }
}
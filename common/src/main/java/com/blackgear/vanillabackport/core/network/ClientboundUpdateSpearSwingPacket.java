package com.blackgear.vanillabackport.core.network;

import com.blackgear.platform.core.networking.PayloadContext;
import com.blackgear.vanillabackport.common.api.extensions.entity.spear.SpearSwingTracker;
import com.blackgear.vanillabackport.core.VanillaBackport;
import com.blackgear.vanillabackport.core.network.handlers.ClientboundPayloadListener;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public record ClientboundUpdateSpearSwingPacket(int entityId, boolean isAttack) implements CustomPacketPayload {
    public static final Type<ClientboundUpdateSpearSwingPacket> TYPE = new Type<>(VanillaBackport.resource("client_update_spear_swing"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundUpdateSpearSwingPacket> STREAM_CODEC = StreamCodec.ofMember(ClientboundUpdateSpearSwingPacket::write, ClientboundUpdateSpearSwingPacket::new);

    public ClientboundUpdateSpearSwingPacket(RegistryFriendlyByteBuf buf) {
        this(buf.readInt(), buf.readBoolean());
    }

    private void write(FriendlyByteBuf buf) {
        buf.writeInt(this.entityId);
        buf.writeBoolean(this.isAttack);
    }

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handler(ClientboundUpdateSpearSwingPacket payload, PayloadContext context) {
        context.enqueueWork(() -> {
            Level level = context.player().level();
            
            if (level.isClientSide()) {
                Entity entity = level.getEntity(payload.entityId());
                if (entity instanceof LivingEntity living) {
                    ((SpearSwingTracker) living).vb$setAttackSwing(payload.isAttack());
                }
            }
        });
    }
}
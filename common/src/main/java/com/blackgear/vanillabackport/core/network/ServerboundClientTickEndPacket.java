package com.blackgear.vanillabackport.core.network;

import com.blackgear.platform.core.network.base.Packet;
import com.blackgear.platform.core.network.base.PacketContext;
import com.blackgear.platform.core.network.base.PacketHandler;
import com.blackgear.vanillabackport.common.api.extensions.entity.movement.MotionAwareEntity;
import com.blackgear.vanillabackport.core.VanillaBackport;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;

public record ServerboundClientTickEndPacket() implements Packet<ServerboundClientTickEndPacket> {
    public static final ResourceLocation ID = VanillaBackport.resource("client_tick_end");
    public static final Handler HANDLER = new Handler();

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public PacketHandler<ServerboundClientTickEndPacket> getHandler() {
        return HANDLER;
    }

    public static class Handler implements PacketHandler<ServerboundClientTickEndPacket> {
        public boolean receivedMovementThisTick;

        @Override
        public void encode(ServerboundClientTickEndPacket packet, FriendlyByteBuf buf) {
            // No data to encode
        }

        @Override
        public ServerboundClientTickEndPacket decode(FriendlyByteBuf buf) {
            return new ServerboundClientTickEndPacket();
        }

        @Override
        public PacketContext handle(ServerboundClientTickEndPacket packet) {
            return (player, level) -> {
                if (!this.receivedMovementThisTick) {
                    ((MotionAwareEntity) player).setKnownMovement(Vec3.ZERO);
                }

                this.receivedMovementThisTick = false;
            };
        }
    }
}

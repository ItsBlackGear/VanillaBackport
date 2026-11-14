package com.blackgear.vanillabackport.core.network.handlers;

import com.blackgear.platform.core.networking.PayloadDistributor;
import com.blackgear.vanillabackport.core.network.ClientboundLevelEventsPacket;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public enum LevelEvents {
    SOUND_SULFUR_SPIKE_LAND,
    SOUND_ICICLE_LAND,
    CONVERT_ZOMBIE_TO_FROSTBITE,
    CONVERT_FROSTBITE_TO_ZOMBIE,
    CONVERT_DROWNED_TO_ZOMBIE;
    
    public void trigger(Level level, BlockPos pos, int data) {
        this.trigger(level, null, pos, data);
    }
    
    public void trigger(Level level, @Nullable Player exclude, BlockPos pos, int data) {
        if (level instanceof ServerLevel server) {
            PlayerList list = server.getServer().getPlayerList();
            for (int i = 0; i < list.getPlayers().size(); i++) {
                ServerPlayer player = list.getPlayers().get(i);
                if (player != exclude && player.level().dimension() == server.dimension()) {
                    double x = pos.getX() - player.getX();
                    double y = pos.getY() - player.getY();
                    double z = pos.getZ() - player.getZ();
                    if (x * x + y * y + z * z < 64.0 * 64.0) {
                        PayloadDistributor.sendToPlayer(player, new ClientboundLevelEventsPacket(this, pos, data));
                    }
                }
            }
        }
    }
}
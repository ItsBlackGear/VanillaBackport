package com.blackgear.vanillabackport.common.api.modules.ender_pearl_persistance;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.level.ChunkPos;

import java.util.Comparator;

public class EnderPearlLoaderModule {
    public static final TicketType<ChunkPos> ENDER_PEARL = TicketType.create("ender_pearl", Comparator.comparingLong(ChunkPos::toLong), 40);

    public static long registerAndUpdateEnderPearlTicket(ServerPlayer player, ThrownEnderpearl pearl) {
        if (pearl.level() instanceof ServerLevel level) {
            ChunkPos pos = pearl.chunkPosition();
            ((EnderPearlAccess) player).registerEnderPearl(pearl);
            level.resetEmptyTime();
            return placeEnderPearlTicket(level, pos) - 1L;
        } else {
            return 0L;
        }
    }
    
    public static long placeEnderPearlTicket(ServerLevel level, ChunkPos chunk) {
        level.getChunkSource().addRegionTicket(ENDER_PEARL, chunk, 2, chunk);
        return ENDER_PEARL.timeout();
    }
}
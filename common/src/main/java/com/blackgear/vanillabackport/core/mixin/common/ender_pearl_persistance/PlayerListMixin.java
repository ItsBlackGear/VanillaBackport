package com.blackgear.vanillabackport.core.mixin.common.ender_pearl_persistance;

import com.blackgear.vanillabackport.common.api.modules.ender_pearl_persistance.EnderPearlAccess;
import com.blackgear.vanillabackport.common.api.modules.ender_pearl_persistance.EnderPearlLoaderModule;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.Connection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.ThrownEnderpearl;
import net.minecraft.world.level.Level;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(PlayerList.class)
public abstract class PlayerListMixin {
    @Shadow @Final private static Logger LOGGER;
    @Shadow @Final private MinecraftServer server;
    
    @Shadow
    public abstract Optional<CompoundTag> load(ServerPlayer player);
    
    @Inject(
        method = "placeNewPlayer",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerPlayer;loadGameTypes(Lnet/minecraft/nbt/CompoundTag;)V"
        )
    )
    private void vb$loadPlayerEnderPearlsOnLogin(Connection connection, ServerPlayer player, CommonListenerCookie cookie, CallbackInfo ci) {
        Optional<CompoundTag> playerData = this.load(player);
        playerData.ifPresent(this::vb$loadAndSpawnEnderPearls);
    }
    
    @Inject(
        method = "remove",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/server/level/ServerLevel;removePlayerImmediately(Lnet/minecraft/server/level/ServerPlayer;Lnet/minecraft/world/entity/Entity$RemovalReason;)V"
        )
    )
    private void vb$unloadEnderPearlsOnLogout(ServerPlayer player, CallbackInfo ci) {
        for (ThrownEnderpearl pearl : ((EnderPearlAccess) player).getEnderPearls()) {
            pearl.setRemoved(Entity.RemovalReason.UNLOADED_WITH_PLAYER);
        }
    }
    
    @Unique
    private void vb$loadAndSpawnEnderPearls(CompoundTag compound) {
        if (compound.contains("ender_pearls", Tag.TAG_LIST)) {
            ListTag pearlsList = compound.getList("ender_pearls", Tag.TAG_COMPOUND);
            for (int i = 0; i < pearlsList.size(); i++) {
                this.vb$loadAndSpawnEnderPearl(pearlsList.getCompound(i));
            }
        }
    }
    
    @Unique
    private void vb$loadAndSpawnEnderPearl(CompoundTag pearlTag) {
        Level.RESOURCE_KEY_CODEC.parse(NbtOps.INSTANCE, pearlTag.get("ender_pearl_dimension"))
            .result().ifPresent(key -> {
                ServerLevel pearlLevel = this.server.getLevel(key);
                
                if (pearlLevel != null) {
                    Entity pearl = EntityType.loadEntityRecursive(
                        pearlTag,
                        pearlLevel,
                        entity -> !pearlLevel.addWithUUID(entity) ? null : entity
                    );
                    
                    if (pearl != null) {
                        EnderPearlLoaderModule.placeEnderPearlTicket(pearlLevel, pearl.chunkPosition());
                    } else {
                        LOGGER.warn("Failed to spawn player ender pearl in level ({}), skipping", key);
                    }
                } else {
                    LOGGER.warn("Trying to load ender pearl without level ({}) being loaded, skipping", key);
                }
            });
    }
}
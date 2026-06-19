package com.blackgear.vanillabackport.core.mixin.common.ender_pearl_persistance;

import com.blackgear.vanillabackport.common.api.modules.ender_pearl_persistance.EnderPearlAccess;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
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

import java.util.HashSet;
import java.util.Set;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player implements EnderPearlAccess {
    @Shadow @Final private static Logger LOGGER;
    @Unique private final Set<ThrownEnderpearl> enderPearls = new HashSet<>();
    
    public ServerPlayerMixin(Level level, BlockPos pos, float yRot, GameProfile gameProfile) {
        super(level, pos, yRot, gameProfile);
    }
    
    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void vb$savePlayerEnderPearls(CompoundTag compound, CallbackInfo ci) {
        if (!this.enderPearls.isEmpty()) {
            ListTag pearlsList = new ListTag();
            
            for (ThrownEnderpearl pearl : this.enderPearls) {
                if (pearl.isRemoved()) {
                    LOGGER.warn("Trying to save removed ender pearl, skipping");
                } else {
                    CompoundTag pearlTag = new CompoundTag();
                    pearl.save(pearlTag);
                    
                    Level.RESOURCE_KEY_CODEC.encodeStart(NbtOps.INSTANCE, pearl.level().dimension())
                        .resultOrPartial(LOGGER::error)
                        .ifPresent(dimensionTag -> pearlTag.put("ender_pearl_dimension", dimensionTag));
                    
                    pearlsList.add(pearlTag);
                }
            }
            
            if (!pearlsList.isEmpty()) {
                compound.put("ender_pearls", pearlsList);
            }
        }
    }
    
    @Override
    public void registerEnderPearl(ThrownEnderpearl pearl) {
        this.enderPearls.add(pearl);
    }
    
    @Override
    public void deregisterEnderPearl(ThrownEnderpearl pearl) {
        this.enderPearls.remove(pearl);
    }
    
    @Override
    public Set<ThrownEnderpearl> getEnderPearls() {
        return this.enderPearls;
    }
}
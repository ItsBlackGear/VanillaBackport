package com.blackgear.vanillabackport.core.mixin.common.grace_time_weapon;

import com.blackgear.vanillabackport.common.api.extensions.entity.GraceTimeWeaponHolder;
import com.blackgear.vanillabackport.common.level.entities.projectile.wind_charge.AffectedByExplosion;
import com.blackgear.vanillabackport.common.level.entities.projectile.wind_charge.IUpdateEntity;
import com.blackgear.vanillabackport.common.registries.entities.ModEntityTypes;
import com.mojang.authlib.GameProfile;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player implements AffectedByExplosion {
    @Shadow @Final private static Logger LOGGER;
    
    @Shadow
    @Nullable
    private Vec3 startingToFallPosition;
    @Unique @Nullable private Vec3 currentExplosionImpactPos;
    @Unique @Nullable private Entity currentExplosionCause;
    
    public ServerPlayerMixin(Level level, BlockPos pos, float yRot, GameProfile gameProfile) {
        super(level, pos, yRot, gameProfile);
    }
    
    @Inject(method = "readAdditionalSaveData", at = @At("TAIL"))
    private void vb$readAdditionalData(CompoundTag compound, CallbackInfo ci) {
        if (compound.contains("current_explosion_impact_pos", 9)) {
            Vec3.CODEC
                .parse(NbtOps.INSTANCE, compound.get("current_explosion_impact_pos"))
                .resultOrPartial(LOGGER::error)
                .ifPresent(pos -> this.currentExplosionImpactPos = pos);
        }
    }
    
    @Inject(method = "addAdditionalSaveData", at = @At("TAIL"))
    private void vb$addAdditionalData(CompoundTag compound, CallbackInfo ci) {
        if (this.currentExplosionImpactPos != null) {
            compound.put("current_explosion_impact_pos", Vec3.CODEC.encodeStart(NbtOps.INSTANCE, this.currentExplosionImpactPos).getOrThrow(false, IllegalStateException::new));
        }
    }
    
    @Inject(
        method = "trackStartFallingPosition",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/server/level/ServerPlayer;position()Lnet/minecraft/world/phys/Vec3;", shift = At.Shift.AFTER)
    )
    private void vb$onTrackStartFallingPosition(CallbackInfo ci) {
//        if (this.currentExplosionImpactPos != null && this.currentExplosionImpactPos.y <= this.startingToFallPosition.y) {
//            //TODO:
//        }
        
        this.currentExplosionImpactPos = null;
        this.currentExplosionCause = null;
    }
    
    @Override
    public void setCurrentExplosionImpactPos(@Nullable Vec3 currentExplosionImpactPos) {
        this.currentExplosionImpactPos = currentExplosionImpactPos;
    }
    
    @Override
    public void setCurrentExplosionCause(@Nullable Entity currentExplosionCause) {
        this.currentExplosionCause = currentExplosionCause;
    }
}
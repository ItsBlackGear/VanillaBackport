package com.blackgear.vanillabackport.core.mixin.client;

import com.blackgear.vanillabackport.client.level.sound.RidingHappyGhastSoundInstance;
import com.blackgear.vanillabackport.common.level.entities.happyghast.HappyGhast;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LocalPlayer.class)
public abstract class LocalPlayerMixin extends AbstractClientPlayer {
    @Shadow @Final protected Minecraft minecraft;

    public LocalPlayerMixin(ClientLevel clientLevel, GameProfile gameProfile) {
        super(clientLevel, gameProfile);
    }

    @Inject(
        method = "startRiding",
        at = @At("HEAD"),
        cancellable = true
    )
    private void onStartRiding(Entity vehicle, boolean force, CallbackInfoReturnable<Boolean> cir) {
        if (super.startRiding(vehicle, force)) {
            if (vehicle instanceof HappyGhast ghast) {
                this.minecraft.getSoundManager().play(new RidingHappyGhastSoundInstance((LocalPlayer)(Object)this, ghast));
                cir.setReturnValue(true);
            }
        }
    }
}
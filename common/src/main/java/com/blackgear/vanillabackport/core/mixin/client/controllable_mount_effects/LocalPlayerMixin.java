package com.blackgear.vanillabackport.core.mixin.client.controllable_mount_effects;

import com.blackgear.vanillabackport.client.level.sound.RidingEntitySoundInstance;
import com.blackgear.vanillabackport.client.registries.ModSoundEvents;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.happy_ghast.HappyGhast;
import com.blackgear.vanillabackport.common.level.entities.mob.animal.nautilus.AbstractNautilus;
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
                this.minecraft.getSoundManager().play(new RidingEntitySoundInstance(this,
                    ghast,
                    false,
                    ModSoundEvents.HAPPY_GHAST_RIDING.get(),
                    0.0F,
                    1.0F,
                    5.0F
                ));
                cir.setReturnValue(true);
            }
            
            if (vehicle instanceof AbstractNautilus nautilus) {
                this.minecraft.getSoundManager().play(new RidingEntitySoundInstance(this,
                    nautilus,
                    true,
                    ModSoundEvents.NAUTILUS_RIDING.get(),
                    0.0F,
                    1.0F,
                    5.0F
                ));
                cir.setReturnValue(true);
            }
        }
    }
}
package com.blackgear.vanillabackport.core.mixin.client.music_toast;

import com.blackgear.vanillabackport.client.api.modules.music_toast.MusicToastAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.sounds.Music;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MusicManager.class)
public abstract class MusicManagerMixin {
    @Shadow @Final private Minecraft minecraft;
    
    @Inject(
        method = "startPlaying",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/sounds/SoundManager;play(Lnet/minecraft/client/resources/sounds/SoundInstance;)V"
        )
    )
    private void vb$updateVolume(Music selector, CallbackInfo ci) {
        ((MusicToastAccess) this.minecraft.getToasts()).showNowPlayingToast();
    }
    
    @Inject(method = "stopPlaying()V", at = @At("TAIL"))
    private void vb$onStopPlaying(CallbackInfo ci) {
        ((MusicToastAccess) this.minecraft.getToasts()).hideNowPlayingToast();
    }
}
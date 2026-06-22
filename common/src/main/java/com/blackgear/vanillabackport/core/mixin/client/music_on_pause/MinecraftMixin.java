package com.blackgear.vanillabackport.core.mixin.client.music_on_pause;

import com.blackgear.vanillabackport.client.api.modules.music_on_pause.MusicTickAccess;
import net.minecraft.client.Minecraft;
import net.minecraft.client.sounds.MusicManager;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.sounds.SoundSource;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Shadow @Final private MusicManager musicManager;
    @Shadow private boolean pause;
    
    @Redirect(method = "pauseGame", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sounds/SoundManager;pause()V"))
    private void vb$dontPauseMusic(SoundManager instance) {
        ((MusicTickAccess) instance).pauseAllExcept(SoundSource.MUSIC);
    }
    
    @Inject(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/sounds/SoundManager;tick(Z)V"))
    private void onSoundManagerTick(CallbackInfo ci) {
        if (this.pause) {
            this.musicManager.tick();
        }
    }
}